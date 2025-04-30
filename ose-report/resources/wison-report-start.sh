#!/usr/bin/env bash
java -Dspring.profiles.active=production -jar ./ose-report-0.0.1-SNAPSHOT.jar > /var/www/ose/ose-report.log &
