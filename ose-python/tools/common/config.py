# -*- coding: utf-8 -*-
import os
import six.moves.configparser

logger = logging.getLogger(__name__)

class QueryConfig(object):
    def __init__(self, config_file: str = "/var/www/saint-whale/backend/ose_global.ini") -> None:
        self.config_file = config_file
        self.cp = six.moves.configparser.RawConfigParser()
        self._load_config(self.config_file)

    def get_ip_hostname_list(self) -> List[List[str]]:
        """

        Returns: [['192.168.190.181','ose1'],['192.168.190.182','ose2'],['192.168.190.183','ose3']]

        """
        ip_hostname_list = []
        for ip in self._parse_server_list_info():
            if self.cp.has_option(ip, "hostname"):
                #if self.cp.get(ip, "ipv6") != "":
                #    ip_hostname_list.append([self.cp.get(ip, "ipv6"), self.cp.get(ip, "hostname")])
                if self.cp.get(ip, "ipv4") != "":
                    ip_hostname_list.append([self.cp.get(ip, "ipv4"), self.cp.get(ip, "hostname")])
        return ip_hostname_list

    def _load_config(self, cpath: str) -> None:
        logger.info("Loading Config")
        if not os.path.exists(cpath):
            logger.error("Config File Not Exists")
            raise ValueError("Config File Not Exists")

        self.cp.read(self.config_file)
