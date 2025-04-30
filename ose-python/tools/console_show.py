# -*- coding: UTF-8 -*-
from prettytable import PrettyTable


class Show(object):
    def __init__(self):
        pass

    @staticmethod
    def show_empty_info(path):
        """显示结果为空的提示信息"""
        tab = PrettyTable([path])
        tab.align = 'l'
        tab.add_row([" "])
        print(tab)

    @staticmethod
    def add_empty_column(src_list, add_to_count):
        """补充空的列直到列数达到add_to_count指定的数量"""
        new_list = src_list
        if len(src_list) < add_to_count:
            for i in range(len(src_list), add_to_count):
                new_list.append(" ")
        return new_list

    @staticmethod
    def show_tables(files_array, path=list()):
        """表格显示文件信息"""
        tab = PrettyTable()

        f_counts = list()
        for f in files_array:
            f_counts.append(len(f))

        max_count = max(f_counts)
        for i in range(0, len(files_array)):
            files_array[i] = Show.add_empty_column(files_array[i], max_count)

        i = 0
        for p in path:
            tab.add_column(p, files_array[i])
            i += 1
        tab.align = 'l'
        print(tab)

    @staticmethod
    def red_pen(data=""):
        return "\033[1;31m%s\033[0m" % data

    @staticmethod
    def green_pen(data=""):
        return "\033[1;32m%s\033[0m" % data

    @staticmethod
    def yellow_pen(data=""):
        return "\033[1;33m%s\033[0m" % data

    @staticmethod
    def blue_pen(data=""):
        return "\033[1;34m%s\033[0m" % data

    @staticmethod
    def purple_pen(data=""):
        return "\033[1;35m%s\033[0m" % data

    @staticmethod
    def cyan_pen(data=""):
        return "\033[1;31m%s\033[0m" % data

