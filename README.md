# Website Project

## A Goal

Studying and operating modern web technologies and sharing results.

[Website Demo link](http://www.flowant.org)

---

## Architecture

### Frontend
- Angular, Bootstrap, SCSS, OAuth2 JWT authentication.

### API Gateway
- Spring Boot Cloud Gateway. Facade of Backend and Author Servers.

### Backend
- API Server: Spring Boot Webflux, requires JWT issued by Auth server.

### Auth server
- Spring Boot Oauth2.

### Database
- Cassandra, used by Backend and Auth Server

### Log Management
- Angular based servers use ngx logger.
- Spring based servers use log4j2 async logger.
- Capacity for log directories should be small (< 30M): file logs will be used for debugging server booting issues.

---
## Install

Install Docker and Docker-compose.

Make **env_file.txt** file containing as following contents.

~~~~
# Visit Goole's OpenID Connect Site and make your one.
GOOGLE_CLIENT_ID=Yours
GOOGLE_CLIENT_SECRET=Yours

# Visit Facebook's OpenID Connect Site and make your one.
FACEBOOK_CLIENT_ID=Yours
FACEBOOK_CLIENT_SECRET=Yours

# Using Keytool program, make your new keystore.jks
# Place athserver/keystore directory then write your password here.
OAUTH2_KEYSTORE_STOREPASS=Yours

# Make your new ones, they are used in authserver's application.yml.
OAUTH2_CLIENT_ID=Yours
OAUTH2_CLIENT_PASSWORD=Yours
~~~~
Modify your **env_file.txt** path in docker-compose*.yml files.

Add website server addresses to **/etc/hosts** file in your host.

~~~~
# For local build.
127.0.0.1    cassandra

# For local build, local browser should use www.flowant.org.
# Instead of 127.0.0.1 because of CORS setting.
127.0.0.1    www.flowant.org

# The same address of www.flowant.org.
127.0.0.1    gateway.flowant.org
~~~~


Clone Website project git.
~~~~
cd ~/work
git clone https://github.com/flowant/website.git
~~~~


Make directories to be used as docker volumes, they are used in docker-compose*.yml files.

```
# Cassandra data storage.
mkdir -p /home/flowant/cassandra_data

# Backend File Storage.
mkdir -p /home/flowant/storage
```

Create build container for maven based project.
~~~~
cd ~/work/website
docker-compose -f docker-compose-maven.yml build
~~~~

Cassandra will run and start Maven building. Targets will be created in ~/work/website directory on your host by virtue of Docker Volume.
~~~~
docker-compose -f docker-compose-maven.yml up
~~~~
> If your development environment has Java, Maven, Cassandra then you can build by your self.
> ```
> mvn clean install
> ```


You can make sure the build has done successfully by following messages.

~~~~
maven-build_1  | [INFO] Basic Website ..... SUCCESS [ 37.065 s]
maven-build_1  | [INFO] common ............ SUCCESS [01:23 min]
maven-build_1  | [INFO] authserver ........ SUCCESS [01:19 min]
maven-build_1  | [INFO] backend ........... SUCCESS [01:22 min]
maven-build_1  | [INFO] gateway ........... SUCCESS [ 10.272 s]
maven-build_1  | [INFO] -------
maven-build_1  | [INFO] BUILD SUCCESS
maven-build_1  | [INFO] -------
maven-build_1  | [INFO] Total time:  05:08 min
maven-build_1  | [INFO] Finished at: 2019-04-05T10:18:05Z
~~~~
Press ctrl-c to quit.

Clean up.
```
sudo docker-compose -f docker-compose-maven.yml down
```

Create docker images from build results.
```
sudo docker-compose -f docker-compose.yml build
```

Run servers.
~~~~
sudo docker-compose -f docker-compose.yml up -d
~~~~

### To check, open your browser and go www.flowant.org

---

## TODO List
- Apply HTTPS.
- Apply product mode.
- Apply social login using OAuth2.
- CI/CD.
- Study and refine frontend with testcases.
