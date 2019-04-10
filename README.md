# Website Project

## A Goal

Studying and operating modern web technologies and sharing results.

[Website Demo link](https://www.flowant.org)

---

## Architecture

### CI/CD
- https://travis-ci.org/flowant/website

### Security
- The OAuth 2.0 Authorization Framework.
- HTTPS.

### Frontend
- Angular, Bootstrap, SCSS.
- OAuth2 role: client.
- With the authorization obtained from Auth Server, provides UX to users.

### API Gateway
- Spring Boot Cloud Gateway.
- Facade of Backend and Auth Servers.

### Backend
- Spring Boot Webflux.
- OAuth2 role: resource server.
- API Server

### Auth server
- Spring Boot OAuth2.
- OAuth2 role: authorization server.

### Database
- Cassandra
- Used by Backend and Auth Server

### Log Management
- Angular based servers use ngx logger.
- Spring based servers use log4j2 async logger.


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
# Place it to ${Project}/keystore/keystore.jks
# It's used for JWK
OAUTH2_KEYSTORE_STOREPASS=Yours

# Make your new ones, they are used in authserver's application.yml.
OAUTH2_CLIENT_ID=Yours
OAUTH2_CLIENT_PASSWORD=Yours

# Using Keytool program, Make your new ssl_certificate.p12
# Place it to ${Project}/keystore/ssl_certificate.p12
# It's used by Spring Boot Server SSL.
SSL_KEYSTORE_STOREPASS=Yours

~~~~
Modify your **env_file.txt** path in docker-compose*.yml files. Default value is **~/site/env_file.txt**

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
mkdir -p ~/site
cd ~/site
git clone https://github.com/flowant/website.git
~~~~


Make directories to be used as docker volumes, they are used in docker-compose*.yml files.

```
# Cassandra data storage.
mkdir -p ~/site/cassandra_data

# Backend File Storage.
mkdir -p ~/site/storage
```

Create build container for maven based project.
~~~~
cd ~/site/website
docker-compose -f docker-compose-maven.yml build
~~~~

Cassandra will run and start Maven building. Targets will be created in ~/site/website directory on your host by virtue of Docker Volume.
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

Clean up docker resources.
```
docker-compose -f docker-compose-maven.yml down
```

Create docker images from build results.
```
docker-compose -f docker-compose.yml build
```

Run servers.
~~~~
docker-compose -f docker-compose.yml up -d
~~~~

### To check, open your browser and go www.flowant.org

---

## TODO List
- Apply social login using OAuth2.
- CI/CD.
- Study and refine frontend with testcases.
