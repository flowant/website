FROM openjdk:11-jre-slim

WORKDIR /usr/share/website/backend

COPY ./wait-for-it.sh /usr/share/website/wait-for-it.sh

# spring boot executable jar
COPY target/*-exec.jar /usr/share/website/backend/backend-exec.jar

RUN mkdir -p /usr/share/website/backend/storage

VOLUME /usr/share/website/backend/storage

# Make port 80, 443 available to the world outside this container
EXPOSE 80
EXPOSE 443

ENTRYPOINT ["/usr/share/website/wait-for-it.sh", "auth:80", "--timeout=0", "--", "java", "-jar", "/usr/share/website/backend/backend-exec.jar"]