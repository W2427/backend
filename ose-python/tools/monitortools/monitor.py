# coding:utf-8

import json
import logging
import os
import time
import requests
import sys

from typing import Tuple, List
from yujiantool.common.config import QueryConfig
from yujiantool.common.soc_path import SocPath
from yujiantool.tools.monitortool import timeit
from yujiantool.tools.monitortool.result.list_result import ListResult, GitLogListResult
from yujiantool.tools.monitortool.target_host import TargetHost
from yujiantool.tools.soc_console_show import Show
from yujiantool.tools.monitortool.ansible_util import AnsibleUtil
from .update.yujian_log import yujian_log
from .update.soc_update_base import SocUpdateBase
from yujiantool.common.soc_ansible import AnsibleRunner

class Monitor(Show, TargetHost):
    def __init__(self, args):
        self.soc_path = SocPath()
        self.config = QueryConfig(self.soc_path.isa_global_path)
        self.ansible = AnsibleRunner()
        self.output = args.output
        self.ose_systemd: OSESystemdService = OSESystemdService(args.host, self.ansible)

    @timeit
    def show_services_info(self, name_list_str: str = 'all'):
        service_list = self.soc_services.normalize_service_str(name_list_str)

        systemd_service_status = self.soc_systemd.get_service_status(service_list)
        docker_service_status = self.soc_docker_service.get_service_status(service_list)
        docker_container_status = self.soc_docker_container.get_service_status(service_list)
        need_color = sys.stdout.isatty()
        list_result = ListResult(need_color)
        list_result.systemd_service_infos = systemd_service_status
        list_result.docker_service_infos = docker_service_status
        list_result.docker_container_infos = docker_container_status
        if self.output == "console":
            print(list_result.console_output())
        else:
            print(list_result.json_output())

    def cat_service_file(self, name_list_str: List[str]):
        ret, not_systemd_service = self.soc_services.is_all_systemd_services(name_list_str)
        if not ret:
            print("service: %s is not systemd service" % Show.red_pen(not_systemd_service))
            return
        service_name_list = self.soc_systemd.get_service_name_list(name_list_str)
        fnames = list()
        fcontent = list()
        for service_name in service_name_list:
            host_index = 0
            cmd = 'cat /etc/systemd/system/ose-%s.*' % service_name.strip()
            ansible_ret, data = self.ansible.run(hosts=self.soc_host.get_valid_hostnames_str(), cmd=cmd)
            if not ansible_ret:
                raise Exception("ansible command failed!\n%s" % self.ansible.last_command_error_message_repr())
            for hostname in self.soc_host.valid_hostnames:
                host_index += 1
                if hostname in data:
                    fnames.append("%s(%s)" % (service_name, hostname))
                    fcontent.append(data[hostname]['stdout'])
                    if host_index != len(self.soc_host.valid_hostnames):
                        fnames.append(' ')
                        fcontent.append('----------------------------------------------'
                                        '----------------------------------------------')
        self.show_tables([fnames, fcontent], ["配置文件", "内容"])

    @timeit
    def service_operation(self, oper: str, service_names: str) -> Tuple[bool, str]:
        """
        oper: 支持 start/stop/restart
        """
        services = self.normalize_service_str(service_names)
        if ALL_SERVICE in services:
            ret, output = self.soc_systemd_service.service_operation(oper, services)
            if not ret:
                logger.info("ret:%s, output;%s", ret, output)
                return ret, output
            self.soc_docker_service.service_operation(oper, services)
        else:
            systemd_service_list = []
            for svr in services:
                if self.soc_systemd_service.is_service_exist(svr):
                    systemd_service_list.append(svr)
            if len(systemd_service_list) > 0:
                ret, output = self.soc_systemd_service.service_operation(oper, systemd_service_list)
                if not ret:
                    logger.info("ret:%s, output;%s", ret, output)
                    return ret, output
            dockerd_service_list = []
            for svr in services:
                if self.soc_docker_service.is_service_exist(svr):
                    dockerd_service_list.append(svr)
            self.soc_docker_service.service_operation(oper, dockerd_service_list)
            dockerd_container_list = []
            for svr in services:
                if self.soc_docker_container.is_service_exist(svr):
                    dockerd_container_list.append(svr)
            self.soc_docker_container.service_operation(oper, dockerd_container_list)
        logger.info("service_names:%s, oper:%s success", oper, service_names)
        return True, ''

    def normalize_service_str(self, service: str) -> List[str]:
        if service == ALL_SERVICE:
            service_list = [ALL_SERVICE]
        else:
            service_list = service.split(",")
        return service_list

