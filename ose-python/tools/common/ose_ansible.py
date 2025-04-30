# -*- coding: UTF-8 -*-
import json
import logging
import subprocess
from json import JSONDecodeError

from yujiantool.tools.monitortool import timeit

logger = logging.getLogger(__name__)

ANSIBLE_JSON_OUTPUT = "ANSIBLE_LOAD_CALLBACK_PLUGINS=true ANSIBLE_STDOUT_CALLBACK=json"


class AnsibleRunner(object):
    def __init__(self):
        self.last_ansible_command = ""
        self.last_ansible_error_message = ""

    @timeit
    def run_playbook(self, playbook, playbook_result_idx=0, timeout=300, **extra_vars):
        str_extra_vars = ""
        if len(extra_vars) > 0:
            for k, v in extra_vars.items():
                one_var = """ -e "%s=%s" """ % (k, v)
                str_extra_vars += one_var
        ansible_cmd = """%s ansible-playbook %s %s""" % (ANSIBLE_JSON_OUTPUT, str_extra_vars, playbook)
        self.last_ansible_command = ansible_cmd
        logger.info("execute cmd:%s", ansible_cmd)
        ret = subprocess.run(ansible_cmd, shell=True,
                             stdout=subprocess.PIPE, stderr=subprocess.PIPE, timeout=timeout)

        return self.__get_playbook_result(ret, playbook_result_idx)

    @timeit
    def run(self, hosts: str, module='shell', cmd='', timeout=300):
        """
        return Tuple(rc, output)
            when rc=true, output=tasks response body
                "{
                    "isa1": {
                            "_ansible_no_log": false,
                            "_ansible_parsed": true,
                            "action": "command",
                            "changed": true,
                            "cmd": "asldjfasfree",
                            "delta": "0:00:00.008999",
                            "end": "2022-01-02 16:32:13.038956",
                            "failed": true,
                            "invocation": {
                                "module_args": {
                                    "_raw_params": "asldjfasfree",
                                    "_uses_shell": true,
                                    "argv": null,
                                    "chdir": null,
                                    "creates": null,
                                    "executable": null,
                                    "removes": null,
                                    "stdin": null,
                                    "warn": false
                                }
                            },
                            "msg": "non-zero return code",
                            "rc": 127,
                            "start": "2022-01-02 16:32:13.029957",
                            "stderr": "/bin/sh: asldjfasfree: \u672a\u627e\u5230\u547d\u4ee4",
                            "stderr_lines": [
                                "/bin/sh: asldjfasfree: \u672a\u627e\u5230\u547d\u4ee4"
                            ],
                            "stdout": "",
                            "stdout_lines": []
                        },
                }"
            when rc=false, ansible command failed, output = ansible command stderr
        """

        ansible_cmd = """%s ansible %s -m %s -a '%s' """ % (ANSIBLE_JSON_OUTPUT, hosts, module, cmd)
        self.last_ansible_command = ansible_cmd

        # ansible isa -m shell -a 'systemctl status isa-isa_console'命令的返回码 与 具体的命令存在相关性
        # 因此命令成功与否不能使用 if ret.returncode == 0 判定
        logger.info("execute cmd:%s", ansible_cmd)
        ret = subprocess.run(ansible_cmd, shell=True,
                             stdout=subprocess.PIPE, stderr=subprocess.PIPE, timeout=timeout)
        return self.__get_adhoc_task_result(ret)

    def __get_adhoc_task_result(self, ret):
        ansible_callback_json_data = ret.stdout
        try:
            json_data = json.loads(ansible_callback_json_data)
            tasks = json_data["plays"][0]["tasks"][0]["hosts"]
            self.last_ansible_error_message = None
            return True, tasks
        except JSONDecodeError as e:
            logger.exception(e)
            logger.debug(ansible_callback_json_data)
            self.last_ansible_error_message = ret.stderr
            return False, ret.stderr

    def __get_playbook_result(self, ret, playbook_index):
        ansible_callback_json_data = ret.stdout
        try:
            lines = ansible_callback_json_data.decode("utf-8").split("\n")
            if "to retry, use" in lines[0]:
                remove_retry_lines = "\n".join(lines[1:])
                json_data = json.loads(remove_retry_lines)
            else:
                json_data = json.loads(ansible_callback_json_data)
            tasks = json_data["plays"][playbook_index]
            self.last_ansible_error_message = None
            return True, tasks
        except JSONDecodeError as e:
            logger.exception(e)
            logger.debug(ansible_callback_json_data)
            self.last_ansible_error_message = ret.stderr
            return False, ret.stderr

    def last_command_error_message_repr(self):
        error_message = """last command is '%s':\nerror message is:\n%s """ % (
            self.last_ansible_command, self.last_ansible_error_message)
        return error_message

