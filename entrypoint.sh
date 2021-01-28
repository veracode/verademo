#!/bin/bash

/usr/local/bin/docker-entrypoint.sh mysqld &
mvn spring-boot:run