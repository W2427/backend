#!/usr/bin/env bash
tar -czf ../resources/stress-test.tar.gz \
    --exclude=./tstress-testing/.idea \
    --exclude=./stress-testing/node_modules \
    ./stress-testing
