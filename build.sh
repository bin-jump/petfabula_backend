#!/bin/bash

mvn clean package -DskipTests=true

docker image rm -f app/petfabula
docker build -t app/petfabula .