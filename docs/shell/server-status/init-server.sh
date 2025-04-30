#!/usr/bin/env bash
################################################################################
# 服务器初始化脚本。                                                           #
# Date:   2019-03-11                                                           #
# Author: JIN Hai Yan                                                          #
################################################################################

source ~/.bash_profile

mail_to="jinhy@livebridge.com,karlkim@163.com,fengtieji@hotmail.com,pangwu@hotmail.com,zhen.wang2012@hotmail.com,meily@livebridge.com.cn"
newline=$'\r\n'
mail_content="服务器【$HOSTNAME】刚刚执行了以下操作："
send_mail=false

# 启动 MooseFS 主控服务
if [[ -f "/opt/mfs/mfsmaster.pid" ]]; then
    if [[ -f "/var/lib/mfs/.mfsmaster.lock" ]] && ps -p `cat /var/lib/mfs/.mfsmaster.lock` > /dev/null; then
        echo "[running]  mfsmaster";
    else
        echo "[starting] mfsmaster";
        mail_content="${mail_content}${newline} • 启动 MooseFS 主控服务"
        send_mail=true
        mfsmaster start;
    fi
fi

# 启动 MooseFS 备选服务
if [[ -f "/opt/mfs/mfsmetalogger.pid" ]]; then
    if [[ -f "/var/lib/mfs/.mfsmetalogger.lock" ]] && ps -p `cat /var/lib/mfs/.mfsmetalogger.lock` > /dev/null; then
        echo "[running]  mfsmetalogger";
    else
        echo "[starting] mfsmetalogger";
        mail_content="${mail_content}${newline} • 启动 MooseFS 备选服务"
        send_mail=true
        mfsmetalogger start;
    fi
fi

# 启动 MooseFS CGI 服务
if [[ -f "/opt/mfs/mfscgiserv.pid" ]]; then
    if [[ -f "/var/lib/mfs/.mfscgiserv.lock" ]] && ps -p `cat /var/lib/mfs/.mfscgiserv.lock` > /dev/null; then
        echo "[running]  mfscgiserv";
    else
        echo "[starting] mfscgiserv";
        mail_content="${mail_content}${newline} • 启动 MooseFS CGI 服务"
        send_mail=true
        mfscgiserv start;
    fi
fi

# 启动 MooseFS 存储服务
if [[ -f "/opt/mfs/mfschunkserver.pid" ]]; then
    if [[ -f "/var/lib/mfs/.mfschunckserver.lock" ]] && ps -p `cat /var/lib/mfs/.mfschunckserver.lock` > /dev/null; then
        echo "[running]  mfschunckserver";
    else
        echo "[starting] mfschunckserver";
        mail_content="${mail_content}${newline} • 启动 MooseFS 存储服务"
        send_mail=true
        mfschunckserver start;
    fi
fi

# 挂载分布式文件系统
if [[ -f "/mnt/mfs/.mounted" ]]; then
    echo "[mounted]  mfsmount";
else
    echo "[mounting] mfsmount";
    mail_content="${mail_content}${newline} • 挂载分布式文件系统"
    send_mail=true
    mfsmount /mnt/mfs -H mfsmaster;
fi

# 启动 Redis 哨兵服务（6300）
if [[ -f "/opt/redis/redis_6300_sentinel.conf" ]]; then
    if [[ -f "/opt/redis/redis_6300_sentinel.pid" ]] && ps -p `cat /opt/redis/redis_6300_sentinel.pid` > /dev/null; then
        echo "[running]  redis-sentinel:6300";
    else
        echo "[starting] redis-sentinel:6300";
        mail_content="${mail_content}${newline} • 启动 Redis 哨兵服务（6300）"
        send_mail=true
        redis-sentinel /opt/redis/redis_6300_sentinel.conf;
    fi
fi

# 启动 Redis 服务（6301）
if [[ -f "/opt/redis/redis_6301.conf" ]]; then
    if [[ -f "/opt/redis/redis_6301.pid" ]] && ps -p `cat /opt/redis/redis_6301.pid` > /dev/null; then
        echo "[running]  redis-server:6301";
    else
        echo "[starting] redis-server:6301";
        mail_content="${mail_content}${newline} • 启动 Redis 服务（6301）"
        send_mail=true
        redis-server /opt/redis/redis_6301.conf;
    fi
fi

# 启动 Redis 服务（6302）
if [[ -f "/opt/redis/redis_6302.conf" ]]; then
    if [[ -f "/opt/redis/redis_6302.pid" ]] && ps -p `cat /opt/redis/redis_6302.pid` > /dev/null; then
        echo "[running]  redis-server:6302";
    else
        echo "[starting] redis-server:6302";
        mail_content="${mail_content}${newline} • 启动 Redis 服务（6302）"
        send_mail=true
        redis-server /opt/redis/redis_6302.conf;
    fi
fi

# 启动 Elasticsearch
if [[ -f "/opt/elasticsearch/config/elasticsearch.yml" ]]; then
    if [[ -f "/opt/elasticsearch/bin/elasticsearch.pid" ]] && ps -p `cat /opt/elasticsearch/bin/elasticsearch.pid` > /dev/null; then
        echo "[running]  elasticsearch";
    else
        echo "[starting] elasticsearch";
        mail_content="${mail_content}${newline} • 启动 Elasticsearch"
        send_mail=true
        sudo -u ose -H sh -c "/opt/elasticsearch/bin/elasticsearch -d -p /opt/elasticsearch/bin/elasticsearch.pid";
    fi
fi

# 启动 NginX
if [[ -f "/etc/nginx/nginx.conf" ]]; then
    if [[ -f "/var/run/nginx.pid" ]] && ps -p `cat /var/run/nginx.pid` > /dev/null; then
        echo "[running]  nginx";
    else
        echo "[starting] nginx";
        mail_content="${mail_content}${newline} • 启动 NginX"
        send_mail=true
        nginx;
    fi
fi

# 启动 OSE 前端服务
if [[ -f "/var/ose/www/operation/server.js" ]] && which pm2 > /dev/null; then
    if pm2 show server > /dev/null; then
        echo "[running]  server.js";
    else
        echo "[starting] server.js";
        mail_content="${mail_content}${newline} • 启动前端服务（/var/ose/www/operation/server.js）"
        send_mail=true
        pm2 start /var/ose/www/operation/server.js;
    fi
fi

# 发送邮件通知
if ${send_mail}; then
    echo "${mail_content}" | mutt -s "[WARNING] OSE OSE Server Initializing" ${mail_to}
fi
