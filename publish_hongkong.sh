#!/usr/bin/env bash
scp -r ./target/* root@8.210.66.29:/mnt/mfs
# ssh root@prddata01.chinaeast2.cloudapp.chinacloudapi.cn "cd /mnt/mfs/temporary/ && mv *.jar ../ose-jars/ -f && mv reports.tar.gz ../resources/templates/ && cd ../resources/templates/ && tar -xf reports.tar.gz"
ssh root@8.210.66.29
