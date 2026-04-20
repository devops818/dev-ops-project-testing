#!/usr/bin/env bash

export IMAGE=$1
docker-compose -f /home/ec2-user/docker-compose.yaml up --detach
echo "success"
