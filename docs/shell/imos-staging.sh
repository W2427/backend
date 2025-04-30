#!/usr/bin/env bash
################################################################################
# OSE OSE service management shell script.                                  #
# Command syntax:                                                              #
#   $ ./ose.sh [start|stop] [profile_name] [service_name ...]                #
# Date: 2018-10-31                                                             #
# Author: JIN Hai Yan                                                          #
################################################################################

# 颜色
c_normal="\033[0;36m"
c_bold="\033[0;32m"
c_error="\033[1;31m"
c_end="\033[0m"

# 所有服务名称
all_service_names=(config auth docs notifications report bpm materialspm issues tasks)

# 启动配置名
profile_name="staging"

# 服务名称前缀
service_name_prefix="ose-"

# 发布版本号
release_version="0.0.1-SNAPSHOT"

# 服务启动超时时长（秒）
timeout_seconds=30

# 取得参数数组
arguments=("$@")
argument_count=${#arguments[@]}

# 必须设置第一个参数，且必须为 start 或 stop
if [[ ${argument_count} == 0 ]] || [[ "$1" != "start" ]] && [[ "$1" != "stop" ]]; then
    printf "\n使用方法：\n\n\t${c_normal}./ose.sh${c_end} ${c_bold}操作${c_end} [${c_bold}服务名称${c_end} ...]\n\n"
    printf "操作：\n\t${c_bold}start${c_end}, ${c_bold}stop${c_end}\n\n"
    printf "服务名称：\n\t${c_bold}config${c_end}, ${c_bold}auth${c_end}, ${c_bold}docs${c_end}, ${c_bold}notifications${c_end}, ${c_bold}report${c_end}, ${c_bold}bpm${c_end}, ${c_bold}materialspm${c_end}, ${c_bold}issues${c_end}, ${c_bold}tasks${c_end}\n\n"
    printf "当服务包含 ${c_bold}config${c_end} 时，其他服务将被忽略。\n\n"
    exit
fi

# 设置配置启动参数
if [[ "${profile_name}" != "" ]]; then
    printf "\n${c_normal}profile: ${c_bold}${profile_name}${c_end}\n"
    profile_name="-Dspring.profiles.active=${profile_name}"
else
    printf "\n${c_normal}profile: ${c_bold}default${c_end}\n"
fi

# 从参数取得服务名称
service_names=("${arguments[@]:1}")
service_count=${#service_names[@]}

# 若未设置服务则默认为所有服务
if [[ ${service_count} == 0 ]]; then
    service_names=("${all_service_names[@]:1}")
fi

printf "\n${c_normal}services: ${c_bold}${service_names[*]}${c_end}\n"

# 取得元素在数组中的索引
function index_of() {

    arguments=("$@")
    item=$1
    items=("${arguments[@]:1}")

    for i in "${!items[@]}"; do
        if [[ "${items[${i}]}" == "${item}" ]]; then
            return ${i}
        fi
    done

    return 255
}

# 获取当前绝对路径
current_path=`pwd`

# 停止/启动每一个服务
for service_name in ${all_service_names[@]}; do

    index_of ${service_name} ${service_names[@]}

    # 若未指定当前服务，则略过该服务
    if (( $? == 255 )); then
        continue
    fi

    service_name="${service_name_prefix}${service_name}"
    service_pid_file="${current_path}/${service_name}.pid"
    service_jar_file="${current_path}/${service_name}-${release_version}.jar"
    service_log_file="${current_path}/${service_name}.log"
    time_elapsed=0

    # 若服务尚未启动且不为启动处理，则略过该服务
    if [[ ! -f "${service_pid_file}" ]] && [[ "$1" != "start" ]]; then
        continue;
    fi

    printf "\n-------- ${service_name} --------\n"

    # 停止服务
    if [[ -f "${service_pid_file}" ]]; then
        printf "${c_normal}stopping ${c_bold}${service_name}${c_normal} ...${c_end}\n"
        kill -9 `cat ${service_pid_file}`
        rm -f ${service_pid_file}
        rm -f ${service_log_file}
        printf "${c_normal}${c_bold}${service_name}${c_normal} stopped${c_end}\n"
    fi

    # 启动服务
    if [[ "$1" == "start" ]]; then

        # 查找 JAR 文件
        if [[ ! -f "${service_jar_file}" ]]; then

            if [[ "${profile_name}" != "" ]]; then
                continue
            fi

            service_jar_file="${current_path}/${service_name}/target/${service_name}-${release_version}.jar"

            if [[ ! -f "${service_jar_file}" ]]; then
                printf "${c_error}file '${service_name}-${release_version}.jar' not found!${c_end}"
                continue
            fi

        fi

        # 启动服务
        printf "${c_normal}starting ${c_bold}${service_name}${c_normal} ...${c_end}\n"
        java ${profile_name} -Xms256m -Xmx256m -jar ${service_jar_file} > ${service_log_file} &

        # 等待启动完成
        while true; do

            sleep 1s

            time_elapsed=$(( time_elapsed + 1 ))

            if (( time_elapsed > timeout_seconds )); then
                printf "${c_error}timeout!${c_end}\n"
                break
            fi

            if [[ -f "${service_pid_file}" ]]; then
                printf "${c_normal}${c_bold}${service_name}${c_normal} started in ${time_elapsed} seconds${c_end}\n"
                break
            fi

        done

    fi

    # 若为 config 服务延迟 10 秒
    if [[ "${service_name}" == "${service_name_prefix}config" ]]; then
        break
    fi

done

echo ""
echo "finished."
echo ""
