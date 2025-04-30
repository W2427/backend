# coding:utf-8

import argparse
import logging
import logging.config
import os
import sys
import time

from monitor import Monitor
from ..soc_console_show import Show

def calc_and_print_elapsed_time(start_time):
    elapsed_time = time.time() - start_time
    # elapsed_time 保留2位小数
    print("耗时: %s 秒" % round(elapsed_time, 2))


def main():
    parser = argparse.ArgumentParser(prog="monitortool", description="\033[1;31m瀚辰服务监控管理工具\033[0m")
    parser.add_argument('-v', "--version", action="version", version='%(prog)s 1.0')
    parser.add_argument('-l', "--list", action="store", default="", dest="list", help="显示服务状态,默认值为：all; [all:显示全部; "
                                                                                      "xxx:具体的后台名称,多个服务名称则使用英文逗号分隔]")
    parser.add_argument('-host', "--host", action="store", default="local", dest="host", help="指定host,\
    默认值为：local(工具执行所在的本机); [all:显示全部; xxx:具体机器的hostname或者ip,多个时则使用英文逗号分隔，\
    例如: --host ose1,192.168.190.182,ose3; --host ose1,local,isa3")
    parser.add_argument('-restart', "--restart", action="store", default="", dest="restart", help="重启服务，\
        [all:重启所有服务; xxx:具体的服务称,多个服务名称则使用英文逗号分隔],使用'-l'可以查看所有服务名称")
    parser.add_argument('-start', "--start", action="store", default="", dest="start", help="启动服务，\
        [all:启动所有服务; xxx:具体的服务名称,多个服务则使用英文逗号分隔],使用'-l'可以查看所有服务名称")
    parser.add_argument('-stop', "--stop", action="store", default="", dest="stop", help="停止服务，\
        [all:停止所有服务; xxx:具体的服务名称,多个服务则使用英文逗号分隔],使用'-l'可以查看所有服务名称")
    parser.add_argument('-enable', "--enable", action="store", default="", dest="enable", help="设置服务开机自启动，\
        [all:所有服务开机自启动; xxx:具体的服务名称,多个服务则使用英文逗号分隔], 使用'-l'可以查看所有服务名称")
    parser.add_argument('-disable', "--disable", action="store", default="", dest="disable", help="禁止开启自启动，\
        [all:禁止所有服务开机自启动; xxx:具体的服务名称,多个服务则使用英文逗号分隔], 使用'-l'可以查看所有服务名称")
    parser.add_argument('-cat', "--cat", action="store", default="", dest="cat", help="查看服务配置文件内容，\
        [all:查看所有服务配置文件内容; xxx:具体的服务名称,多个服务则使用英文逗号分隔], 使用'-l'可以查看所有服务名称")

    args = parser.parse_args()
    begin_time = time.time()

    m = Monitor(args)
    if args.list:
        m.show_services_info(args.list)
        calc_and_print_elapsed_time(begin_time)

    if args.cat:
        m.cat_service_file(args.cat.split(","))
        calc_and_print_elapsed_time(begin_time)

    if args.disable:
        bret, err = m.soc_services.service_operation("disable", args.disable)
        if not bret:
            print(Show.red_pen(err))
            sys.exit(2)

    if args.enable:
        bret, err = m.soc_services.service_operation("enable", args.enable)
        if not bret:
            print(Show.red_pen(err))
            sys.exit(3)

    if args.start:
        bret, err = m.soc_services.service_operation("start", args.start)
        if not bret:
            print(Show.red_pen(err))
            sys.exit(4)
        calc_and_print_elapsed_time(begin_time)

    if args.stop:
        bret, err = m.soc_services.service_operation("stop", args.stop)
        if not bret:
            print(Show.red_pen(err))
            sys.exit(5)
        calc_and_print_elapsed_time(begin_time)

    if args.restart:
        bret, err = m.soc_services.service_operation("restart", args.restart)
        if not bret:
            print(Show.red_pen(err))
            sys.exit(6)
        calc_and_print_elapsed_time(begin_time)


if __name__ == "__main__":
    main()


