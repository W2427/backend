#!/usr/bin/env bash

# 删除 1 小时前创建的上传文件缓冲数据
find /mnt/mfs/private/multipart/ -type f -mmin  +60 | xargs -n 100 rm -f
echo `date +"%Y-%m-%d %H:%M:%S"` > /mnt/mfs/private/multipart/.timestamp

# 删除 8 小时前创建的上传临时文件
find /mnt/mfs/private/upload/    -type f -mmin +480 | xargs -n 100 rm -f
echo `date +"%Y-%m-%d %H:%M:%S"` > /mnt/mfs/private/upload/.timestamp
