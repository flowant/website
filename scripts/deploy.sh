#!/bin/bash
set -x

SITE=~/site
echo Site Directory:$SITE


# Cassandra data storage.
mkdir -p $SITE/cassandra_data

# Backend File Storage.
mkdir -p $SITE/storage

if [ -d "$SITE/website" ]; then
  cd $SITE/website
  git pull
else
  cd $SITE
  git clone https://github.com/flowant/website.git
  cd $SITE/website
fi

echo Current Directory:`pwd`
docker-compose -f docker-compose.yml down
docker-compose -f docker-compose-maven.yml build
docker-compose -f docker-compose-maven.yml up --exit-code-from maven
docker-compose -f docker-compose.yml build
docker-compose -f docker-compose.yml up -d
