# build maven projects containing spring-boot application

FROM maven:3.6.0-jdk-11-slim AS build

WORKDIR /usr/share/website

VOLUME /usr/share/website/

ENTRYPOINT ["/usr/share/website/scripts/wait-for-it.sh", "cassandra:9042", "--timeout=0", "--", "mvn", "clean", "install"]