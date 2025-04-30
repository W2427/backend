#!/usr/bin/env bash
mvn clean install
#cd ./ose-config
#cp ./src/main/resources/application-production01.properties ./src/main/resources/application-production.properties
#mvn clean install
#mv ./target/ose-config-0.0.1-SNAPSHOT.jar ./target/ose-config01-0.0.1-SNAPSHOT.jar
#cp ./src/main/resources/application-production02.properties ./src/main/resources/application-production.properties
#mvn install
#mv ./target/ose-config-0.0.1-SNAPSHOT.jar ./target/ose-config02-0.0.1-SNAPSHOT.jar
#cd ..
rm -rf ./target
mkdir ./target
cp ./ose-*/target/*.jar ./target/
cd ./target
rm -f *-api-*.jar *-base-*.jar *-prints-*.jar *-test-*.jar
cd ../resources/templates/
tar -czf reports.tar.gz reports
mv reports.tar.gz ../../target/
