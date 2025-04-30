#!/usr/bin/env bash
mvn install:install-file -Dfile=./bpmnutil.jar -DgroupId=com.bpmn -DartifactId=bpmnutil -Dversion=1.0 -Dpackaging=jar
