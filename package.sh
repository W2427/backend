#!/usr/bin/env bash
tar -czf resources/ose.tar.gz \
    --exclude=./.idea \
    --exclude=./*/target/* \
    --exclude=./*/*.iml \
    --exclude=./*.pid \
    ./ose-* \
    ./pom.xml
