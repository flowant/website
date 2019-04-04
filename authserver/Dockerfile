FROM openjdk:11-jre-slim

WORKDIR /usr/share/website/authserver

ARG JAR_FILE

COPY ./wait-for-it.sh /usr/share/website/wait-for-it.sh

# spring boot executable jar
COPY target/${JAR_FILE} /usr/share/website/authserver/authserver-exec.jar

# Auth Server keystore for JWT authentication
COPY keystore/keystore.jks /usr/share/website/authserver/keystore/keystore.jks

# Make port 80, 443 available to the world outside this container
EXPOSE 80
EXPOSE 443

ENTRYPOINT ["/usr/share/website/wait-for-it.sh", "cassandra:9042", "--timeout=0", "--", "java", "-jar", "/usr/share/website/authserver/authserver-exec.jar"]
