import logging

logger = logging.getLogger(__name__)


class ServiceStatus(object):
    def __init__(self):
        self.hostname = ""
        self.name = ""
        self.is_enabled = True
        self.desc = ""
        self.status = ""
        self.pid = ""
        self.uptime = ""
        self.id = ""
        self.image = ""

    def __str__(self):
        return "hostname:%s, servicename:%s, enabled:%s, status:%s, pid:%s, uptime:%s, desc:%s, id:%s, image:%s" % \
               (self.hostname, self.name, self.is_enabled, self.status, self.pid, self.uptime, self.desc, self.id,
                self.image)

