FROM openjdk:11-jre-slim

WORKDIR /usr/share/website/gateway

# spring boot executable jar
COPY target/*-exec.jar /usr/share/website/gateway/gateway-exec.jar

# SSL keystore
COPY keystore /usr/share/website/gateway/keystore

# Make port 443 available to the world outside this container
EXPOSE 443

ENTRYPOINT ["java", "-jar", "/usr/share/website/gateway/gateway-exec.jar"]
