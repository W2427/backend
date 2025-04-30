#!/bin/bash
rm -f /var/ose/ose-*.jar
ln -s /mnt/mfs/ose-jars/ose-config-0.0.1-SNAPSHOT.jar        /var/ose/ose-config-0.0.1-SNAPSHOT.jar
ln -s /mnt/mfs/ose-jars/ose-auth-0.0.1-SNAPSHOT.jar          /var/ose/ose-auth-0.0.1-SNAPSHOT.jar
ln -s /mnt/mfs/ose-jars/ose-docs-0.0.1-SNAPSHOT.jar          /var/ose/ose-docs-0.0.1-SNAPSHOT.jar
ln -s /mnt/mfs/ose-jars/ose-notifications-0.0.1-SNAPSHOT.jar /var/ose/ose-notifications-0.0.1-SNAPSHOT.jar
ln -s /mnt/mfs/ose-jars/ose-report-0.0.1-SNAPSHOT.jar        /var/ose/ose-report-0.0.1-SNAPSHOT.jar
ln -s /mnt/mfs/ose-jars/ose-materialspm-0.0.1-SNAPSHOT.jar   /var/ose/ose-materialspm-0.0.1-SNAPSHOT.jar
ln -s /mnt/mfs/ose-jars/ose-issues-0.0.1-SNAPSHOT.jar        /var/ose/ose-issues-0.0.1-SNAPSHOT.jar
ln -s /mnt/mfs/ose-jars/ose-bpm-0.0.1-SNAPSHOT.jar           /var/ose/ose-bpm-0.0.1-SNAPSHOT.jar
ln -s /mnt/mfs/ose-jars/ose-tasks-0.0.1-SNAPSHOT.jar         /var/ose/ose-tasks-0.0.1-SNAPSHOT.jar

