# coding: utf-8

import re

ENABLED_LINE_FLAG = "   Loaded:"


def is_enable_line(status_line: str) -> bool:
    if status_line.startswith(ENABLED_LINE_FLAG):
        return True
    return False


def is_enabled(status_line: str, service_name: str) -> bool:
    # regex = r"^   Loaded: .*(.+%s.+; enabled; vendor preset: .+)$" % service_name
    regex = r"^%s .*%s; enabled; .*: .*$" % (ENABLED_LINE_FLAG, service_name)
    if re.match(regex, status_line):
        return True
    return False

