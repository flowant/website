FROM openjdk:11-jre-slim

WORKDIR /usr/share/website/registry

# spring boot executable jar
COPY target/*-exec.jar /usr/share/website/registry/registry-exec.jar

# Make port 8761  available to the world outside this container
EXPOSE 8761

ENTRYPOINT ["java", "-jar", "/usr/share/website/registry/registry-exec.jar"]
