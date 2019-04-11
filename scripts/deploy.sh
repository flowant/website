#!/bin/bash

check_fail() {
  exit_code="$?"
  if [["$exit_code" != "0" ]]; then
      exit "$exit_code";
  fi
}

SITE=~/site
echo Site Directory:$SITE

source $SITE/env_file.txt

set -x

# Cassandra data storage.
mkdir -p $SITE/cassandra_data

# Backend File Storage.
mkdir -p $SITE/storage

# Keystore has decripted private key. The path must be in a safe place.
mkdir -p $SITE/keystore

if [ -d "$SITE/website" ]; then
  cd $SITE/website
  git pull
else
  cd $SITE
  git clone https://github.com/flowant/website.git
  cd $SITE/website
fi

echo Current Directory:`pwd`
# export SSL_KEYSTORE_STOREPASS env variable before run scripts
set +x
set -v
openssl pkcs12 -nokeys -in keystore/ssl_certificate.p12 -out $SITE/keystore/certificate.crt -password pass:$SSL_KEYSTORE_STOREPASS
openssl pkcs12 -nocerts -nodes -in keystore/ssl_certificate.p12 -out $SITE/keystore/private.key -password pass:$SSL_KEYSTORE_STOREPASS

docker-compose -f docker-compose.yml down

docker-compose -f docker-compose-maven.yml build

docker-compose -f docker-compose-maven.yml up --exit-code-from maven
check_fail

docker-compose -f docker-compose.yml build
check_fail

docker-compose -f docker-compose.yml up -d
