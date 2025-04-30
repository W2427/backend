#!/usr/bin/env bash
rm -rf ./**/*.tar.gz ./target ./*/target
tar -czf ./ose-ose.tar.gz \
    ./docs \
    ./resources \
    ./templates \
    ./ose-* \
    ./.editorconfig \
    ./.eslintignore \
    ./.eslintrc \
    ./.gitignore \
    ./checks.xml \
    ./package.sh \
    ./package-all.sh \
    ./pom.xml \
    ./publish.sh \
    ./release.sh
