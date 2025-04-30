# -*- coding: UTF-8 -*-
from typing import List, Dict, Tuple

from yujiantool.common.common import get_hostname_ip
from yujiantool.common.config import QueryConfig


class OSEHost(object):
    def __init__(self, hosts: str, config: QueryConfig):
        self.hosts = hosts
        self.cluster_ip2hostname: Dict[str, str] = dict()
        self.cluster_hostname2ip: Dict[str, str] = dict()

        self.cluster_hostnames: List = list()
        self.cluster_ips: List = list()

        self.valid_ips: List = list()
        self.valid_hostnames: List = list()
        self.local_hostname, self.local_ip = get_hostname_ip()
        self.config = config
        self.read_etc_hosts()
        self.host_trans(self.hosts)

    def trans_hostname(self, hosts) -> str:
        if hosts == 'local':
            return self.local_hostname
        elif hosts in self.cluster_ips:
            return self.cluster_ip2hostname[hosts]
        elif hosts in self.cluster_hostnames:
            return hosts
        return ''

    def host_trans(self, hosts='all') -> Tuple[List[str], List[str]]:
        if hosts == 'all':
            return self.cluster_hostnames, self.cluster_ips
        elif hosts == 'local':
            self.valid_hostnames = [self.local_hostname]
            self.valid_ips = [self.local_ip]
            return self.valid_hostnames, self.valid_ips

        if 'local' in hosts:
            hosts = hosts.replace('local', self.local_hostname)

        host_list = [x.strip() for x in hosts.split(',')]
        self.valid_hostnames = list()
        self.valid_ips = list()
        for item in host_list:
            if item in self.cluster_ips:
                self.valid_hostnames.append(self.cluster_ip2hostname[item])
                self.valid_ips.append(item)
            elif item in self.cluster_hostnames:
                self.valid_hostnames.append(item)
                self.valid_ips.append(self.cluster_hostname2ip[item])

        self.valid_hostnames.sort()
        self.valid_ips.sort()
        return self.valid_hostnames, self.valid_ips

    def read_etc_hosts(self):
        for ip_hostname in self.config.get_ip_hostname_list():
            ip_addr, hostname = ip_hostname
            self.cluster_hostname2ip[hostname] = ip_addr
            self.cluster_ip2hostname[ip_addr] = hostname
            self.cluster_hostnames.append(hostname)
            self.cluster_ips.append(ip_addr)
        self.cluster_hostnames.sort()
        self.cluster_ips.sort()
        self.valid_hostnames = self.cluster_hostnames
        self.valid_ips = self.cluster_ips

    def get_cluster_hostnames_str(self) -> str:
        """

        Returns: 'isa1,isa2,isa3'

        """
        return ','.join(self.cluster_hostnames)

    def get_cluster_ips_str(self) -> str:
        """

        Returns: '192.168.190.181,192.168.190.182,192.168.190.183'

        """
        return ','.join(self.cluster_ips)

    def get_valid_ips_str(self) -> str:
        """

        Returns:

        """
        return ','.join(self.valid_ips)

    def get_valid_hostnames_str(self) -> str:
        return ','.join(self.valid_hostnames)

