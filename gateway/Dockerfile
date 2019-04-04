FROM openjdk:11-jre-slim

WORKDIR /usr/share/website/gateway

ARG JAR_FILE

# spring boot executable jar
COPY target/${JAR_FILE} /usr/share/website/gateway/gateway-exec.jar

# Make port 80, 443 available to the world outside this container
EXPOSE 80
EXPOSE 443

ENTRYPOINT ["/usr/bin/java", "-jar", "/usr/share/website/gateway/gateway-exec.jar"]
