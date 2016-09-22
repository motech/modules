#!/usr/bin/env bash

if [ "$TRAVIS_EVENT_TYPE" != "cron" ]; then
    docker network create --subnet=192.168.33.0/16 OpenMRS
    git clone https://github.com/motech/modules.git ../modules-master -b master --single-branch
    mkdir ~/.motech
    cp ../modules-master/testdata/config-locations.properties ~/.motech/
    cd ../modules-master/openmrs/
    if [ "$OPENMRS_VERSION" = "1.9" ]; then
        docker pull motech/openmrs
        docker run -itd --net OpenMRS --ip 192.168.33.9 motech/openmrs
        mvn clean install -Dopenmrs.host=192.168.33.9:8080 -P OPENMRSIT -U
    elif [ "$OPENMRS_VERSION" = "1.12" ]; then
        docker pull motech/openmrs:v1.12
        docker run -itd --net OpenMRS --ip 192.168.33.12 motech/openmrs:v1.12
        mvn clean install -Dopenmrs.host=192.168.33.12:8080 -Dopenmrs.version=1_12 -P OPENMRSIT -U
    fi
fi
