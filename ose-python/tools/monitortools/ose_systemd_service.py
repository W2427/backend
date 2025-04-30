# -*- coding: UTF-8 -*-
import logging
import os
import re
from typing import Dict, Tuple, List, Any, Set

from yujiantool.common.soc_ansible import AnsibleRunner
from yujiantool.tools.monitortool import timeit
from yujiantool.tools.monitortool.service_status import ServiceStatus
from yujiantool.tools.monitortool.soc_host import SocHost
from yujiantool.tools.monitortool.systemd.systemd_util import is_enable_line
from yujiantool.tools.monitortool.systemd.systemd_util import is_enabled
from yujiantool.tools.monitortool.service_gitlog import ServiceGitLog, SystemdServiceGitLog
from yujiantool.tools.monitortool.ansible_util import AnsibleUtil

ALL_SERVICE = "all"
ISA_SYSTEMD_PREFIX = "ose-"
ISA_SYSTEMD_SERVICE_SUFFIX = ".service"

logger = logging.getLogger(__name__)

ENABLED_REGEX_PATTERN = r"Loaded: .* \(.*; (\w); vendor preset: \w"


class SocSystemdService(object):

    @timeit
    def __init__(self, soc_host: SocHost, ansible: AnsibleRunner):
        self.soc_host = soc_host
        self.ansible = ansible
        systemd_service_playbook = os.path.dirname(__file__) + os.path.sep + "playbooks/systemd_status.yml"
        ansible_ret, playbook_output = self.ansible.run_playbook(systemd_service_playbook,
                                                                 NOT_ISA_PREFIX_SERVICES=" ".join(
                                                                     list(NOT_ISA_PREFIX_SERVICES)))
        if not ansible_ret:
            raise Exception("""ansible run failed!!!\n%s""" % self.ansible.last_command_error_message_repr())

        all_tasks_output = playbook_output["tasks"][0]["hosts"]
        self.isa_services = self.__parse_all_service_status(all_tasks_output)
        self.all_systemd_service = self.parser_all_systemd_service()
        self.all_service_name = self.parser_all_service_name()
        self.cluster_service_names = self.parser_cluster_service_name()

    # flake8: noqa: C901
    @timeit
    def __parse_all_service_status(self, data) -> Dict[str, Dict[str, Any]]:
        """
        获取集群内，ip对应systemd服务
        Returns: {
               '192.168.190.181':
               {
               'isa-api_dashboard_stat.service': {'is_enabled': 'enabled', 'status': '', 'desc': '', 'pid': '', 'keeptime': ''},
               'isa-api_dashboard_webapi.service': {'is_enabled': 'enabled', 'status': '', 'desc': '', 'pid': '', 'keeptime': ''},
               'isa-api_gateway.service': {'is_enabled': 'enabled', 'status': '', 'desc': '', 'pid': '', 'keeptime': ''},
               'isa-elasticsearch-1.service': {'is_enabled': 'enabled', 'status': '', 'desc': '', 'pid': '', 'keeptime': ''}
               }
              }
        """
        isa_services: Dict[str, Any] = dict()
        for hostname, all_service_system_output in data.items():
            ip = self.soc_host.cluster_hostname2ip[hostname]
            isa_services[ip] = dict()
            service_status_data: Dict[str, Any] = self.__parse_service_status(all_service_system_output)
            for name, service_status in service_status_data.items():
                pid = ""
                status = ""
                desc = ""
                keeptime = ""
                enable_status = "disabled"
                match_desc = re.findall(r"%s - (.*)" % name, service_status[0])
                if len(match_desc) > 0:
                    desc = match_desc[0]
                for line in service_status[1:]:
                    if "Main PID" in line:
                        pid = re.findall(r"\d+", line)[0]
                    elif is_enable_line(line):
                        enable_status = "enabled" if is_enabled(line, name) else "disabled"
                    elif "Active:" in line:
                        match_status = re.findall(r"Active: (.* \(.*\))", line)
                        if len(match_status) > 0:
                            status = match_status[0]
                        match_keeptime = re.findall(r"^.*; (.*) ago", line)
                        if len(match_keeptime) > 0:
                            keeptime = match_keeptime[0]
                if pid == "" and ".timer" in name:
                    service_status = service_status_data[name.split(".")[0] + ".service"]
                    for line in service_status[1:]:
                        if "Main PID" in line:
                            pid = re.findall(r"\d+", line)[0]
                    pid = "[timer][last pid is %s]" % pid
                isa_services[ip][name] = {"is_enabled": enable_status, "status": status, "desc": desc,
                                          "pid": pid, "keeptime": keeptime}
                logger.info("ip:%s, service:%s, info:%s", ip, name, isa_services[ip][name])
        return isa_services

    @timeit
    def parser_all_systemd_service(self) -> Set[str]:
        """
            获取集群内，所有systemd服务
        Returns: {
                    'isa-elasticsearch-1.service',
                    'isa-api_gateway.service',
                    'isa-api_dashboard_stat.service',
                    'isa-api_dashboard_webapi.service'
                 }
        """
        systemd_service = set()
        for _, service_infos in self.isa_services.items():
            for service, _ in service_infos.items():
                systemd_service.add(service)
        return systemd_service

    def parser_cluster_service_name(self) -> Dict[str, List[str]]:
        """
        获取集群内，按ip分的原始服务名
        Returns:
            {
              '192.168.190.181': ['elasticsearch-1', 'api_gateway']
            }

        """
        service_names = dict()
        for ip, service_info in self.isa_services.items():
            service_names[ip] = [re.findall(r'^(.*).(service|timer)$', x)[0][0].split("isa-")[-1] for x in
                                 list(service_info.keys())]
        return service_names

    def parser_all_service_name(self) -> List[str]:
        """
        获取集群内，所有服务原始服务名
        Returns:
          ['elasticsearch-1', 'api_gateway']
        """
        service_names = [re.findall(r'^(.*).(service|timer)$', x)[0][0].split("isa-")[-1] for x in
                         self.all_systemd_service]
        service_names.sort()
        return service_names

    def get_service_running_at(self, systemd_name: str) -> List[str]:
        """
        获取集群内, 服务运行的节点
        Args:
            systemd_name: "isa-api_gateway.service"

        Returns: ["isa1", "isa2"]

        """
        running_hosts = list()
        for ip in self.isa_services:
            services_of_this = self.isa_services[ip]
            if systemd_name in services_of_this:
                running_hosts.append(self.soc_host.cluster_ip2hostname[ip])
        return running_hosts

    def is_service_exist(self, svr: str) -> bool:
        """
        Args:
            svr: isa-api_gateway.service / mariadb.service / influxdb.service
                or api_gateway / mariadb / influxdb
        """
        exist = False
        for ip in self.isa_services:
            if self.__get_systemd_service_full_name(svr) in self.isa_services[ip]:
                exist = True
                break
        return exist

    def __get_systemd_service_full_name(self, svr: str):
        """
        Args:
            svr: isa-api_gateway.service / mariadb.service / influxdb.service
                or api_gateway / mariadb / influxdb
        """
        if not svr.endswith(ISA_SYSTEMD_SERVICE_SUFFIX):
            if svr not in NOT_ISA_PREFIX_SERVICES:
                svr = ISA_SYSTEMD_PREFIX + svr + ISA_SYSTEMD_SERVICE_SUFFIX
            else:
                svr = svr + ISA_SYSTEMD_SERVICE_SUFFIX
        return svr

    def get_service_name_list(self, service_names: List[str]) -> List[str]:
        """

        Args:
            service_names: all
                          api_gateway, elasticsearch-1

        Returns:
            ['api_gateway', 'elasticsearch-1']
        """
        name_list = list()
        for ip in self.soc_host.valid_ips:
            if ip in self.isa_services:
                if ALL_SERVICE in service_names:
                    name_list += self.cluster_service_names[ip]
                else:
                    for name in service_names:
                        name_list.append(name.strip())
        name_list = sorted(list(set(name_list)))
        return name_list

    def service_operation(self, oper: str, service_names: List[str]) -> Tuple[bool, str]:
        """
        启停集群内的相关服务
        Args:
            oper: start
                  restart
                  stop
            service_names: all
                           api_gateway, elasticsearch-1
        Returns:

        """
        service_names_list = self.get_service_name_list(service_names)

        return self._service_operation(oper, service_names_list)

    # flake8: noqa: C901
    @timeit
    def _service_operation(self, oper: str, service_names_list: List[str]) -> Tuple[bool, str]:
        logger.info("oper:%s, service_names_list:%s", oper, service_names_list)
        failed_service_info = dict()
        success_service_info = set()
        for service_name in service_names_list:
            cmd = ""
            timer = "isa-%s.timer" % (service_name.strip())
            service = "isa-%s.service" % (service_name.strip())
            if timer in self.all_systemd_service:
                cmd = "systemctl %s %s" % (oper, timer)
            elif service in self.all_systemd_service:
                cmd = "systemctl %s %s" % (oper, service)
            if cmd == "":
                logger.info("error, service_name:%s, oper:%s not supported", service_name, oper)
                continue
            ansible_ret, data = self.ansible.run(hosts=self.soc_host.get_valid_hostnames_str(), cmd=cmd)
            if not ansible_ret:
                failed_service_info[service_name] = "执行命令失败, %s" % self.ansible.last_command_error_message_repr()
                print("服务:'%s', 执行操作:%s 失败" % (service_name, oper))
                logger.warning("服务:'%s', 执行操作:%s 失败", service_name, oper)
            else:
                success_service_info.add(service_name)
                print("服务:'%s', 执行操作:%s 成功" % (service_name, oper))
                logger.info("服务:'%s' 执行操作:'%s' 成功", service_name, oper)

        if len(failed_service_info) > 0:
            return False, "重启失败"
        else:
            return True, ''

    @timeit
    def get_service_status(self, name_list: List[str]):
        name_list = self.get_service_name_list(name_list)
        service_status_list: List[ServiceStatus] = []
        not_exist_services = []
        for name in name_list:
            real_name = "isa-%s.service" % name
            real_name_timer = "isa-%s.timer" % name
            if name in NOT_ISA_PREFIX_SERVICES:
                real_name = "%s.service" % name
                real_name_timer = ""

            if not self.is_service_exist(real_name) and not self.is_service_exist(
                    real_name_timer):
                not_exist_services.append(real_name)
                continue

            for ip in self.soc_host.valid_ips:
                if ip not in self.isa_services or (
                        real_name not in self.isa_services[ip] and real_name_timer not in
                        self.isa_services[ip]):
                    continue

                service_timer_name = real_name
                if real_name_timer in self.isa_services[ip]:
                    service_timer_name = real_name_timer
                    logger.info("use timer name:%s", service_timer_name)
                else:
                    logger.info("use service name:%s", service_timer_name)

                service_status: ServiceStatus = ServiceStatus()
                service_status.name = name
                service_status.hostname = self.soc_host.cluster_ip2hostname[ip]
                service_status.desc = self.isa_services[ip][service_timer_name]['desc']
                service_status.is_enabled = self.isa_services[ip][service_timer_name]['is_enabled']
                service_status.status = self.isa_services[ip][service_timer_name]['status']
                service_status.pid = self.isa_services[ip][service_timer_name]['pid']
                service_status.uptime = self.isa_services[ip][service_timer_name]['keeptime']
                service_status_list.append(service_status)
        service_status_list = sorted(service_status_list, key=lambda x: (x.name, x.hostname))
        return service_status_list
