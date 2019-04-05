FROM openjdk:11-jre-slim

WORKDIR /usr/share/website/gateway

# spring boot executable jar
COPY target/*-exec.jar /usr/share/website/gateway/gateway-exec.jar

# Make port 80, 443 available to the world outside this container
EXPOSE 80
EXPOSE 443

ENTRYPOINT ["/usr/bin/java", "-jar", "/usr/share/website/gateway/gateway-exec.jar"]
