FROM openjdk:11-jre-slim

WORKDIR /usr/share/website/authserver

COPY ./wait-for-it.sh /usr/share/website/wait-for-it.sh

RUN chmod +x /usr/share/website/wait-for-it.sh

COPY target/*-exec.jar /usr/share/website/authserver/authserver-exec.jar

# Keystore for JWT authentication and SSL
COPY keystore /usr/share/website/authserver/keystore

# Make port 80 available to the world outside this container
EXPOSE 80

ENTRYPOINT ["/usr/share/website/wait-for-it.sh", "cassandra:9042", "--timeout=0", "--", "java", "-jar", "/usr/share/website/authserver/authserver-exec.jar"]
