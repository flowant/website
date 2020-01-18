FROM openjdk:11-jre-slim

WORKDIR /usr/share/website/backend

COPY ./wait-for-it.sh /usr/share/website/wait-for-it.sh

RUN chmod +x /usr/share/website/wait-for-it.sh

COPY target/*-exec.jar /usr/share/website/backend/backend-exec.jar

# SSL keystore
COPY keystore /usr/share/website/backend/keystore

RUN mkdir -p /usr/share/website/backend/storage

VOLUME /usr/share/website/backend/storage

# Make port 80 available to the world outside this container
EXPOSE 80

ENTRYPOINT ["/usr/share/website/wait-for-it.sh", "auth.flowant.org:80", "--timeout=0", "--", "java", "-jar", "/usr/share/website/backend/backend-exec.jar"]