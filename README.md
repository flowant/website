# Website Project

## A Goal

Studying and operating modern web technologies and sharing results.

---
## Demo Site
https://www.flowant.org

For your convenience, you can use the following username/password to sign in.

    Username: user0
    Password: pass0


---

## Architecture

[![](https://mermaid.ink/img/eyJjb2RlIjoiZ3JhcGggVEJcbiAgIFxuICBzdWJncmFwaCBIb21lXG4gIFJvdXRlcihSb3V0ZXIgVmVyaXpvbilcbiAgUm91dGVyIC0tIGh0dHBzIDg0NDMgLS0-IEFQSUdhdGV3YXlcbiAgUm91dGVyIC0tIGh0dHBzIDQ0MyAtLT4gRnJvbnRlbmRcblxuICAgIHN1YmdyYXBoIExpbnV4IERlc2t0b3BcbiAgICBGcm9udGVuZFxuICAgIEFQSUdhdGV3YXkoQVBJR2F0ZXdheSlcbiAgICBBUElHYXRld2F5IC0tIDgwIC0tPiBBdXRoU2VydmVyXG4gICAgQVBJR2F0ZXdheSAtLSA4MCAtLT4gQmFja2VuZFxuICAgIEFQSUdhdGV3YXkgLS4tIEV1cmVrYVsoRXVyZWthKV1cbiAgICBBdXRoU2VydmVyIC0tPiBDYXNzYW5kcmFbKENhc3NhbmRyYSldXG4gICAgQXV0aFNlcnZlciAtLi0gRXVyZWthWyhFdXJla2EpXVxuICAgIEJhY2tlbmQgLS0-IENhc3NhbmRyYVsoQ2Fzc2FuZHJhKV1cbiAgICBCYWNrZW5kIC0uLSBFdXJla2FbKEV1cmVrYSldXG5cbiAgICBlbmRcblxuICBlbmRcblxuICBzdWJncmFwaCBCcm93c2VyXG4gIFdlYkFwcCAtLT4gUm91dGVyXG4gIGVuZCIsIm1lcm1haWQiOnsidGhlbWUiOiJkZWZhdWx0In0sInVwZGF0ZUVkaXRvciI6ZmFsc2V9)](https://mermaid-js.github.io/mermaid-live-editor/#/edit/eyJjb2RlIjoiZ3JhcGggVEJcbiAgIFxuICBzdWJncmFwaCBIb21lXG4gIFJvdXRlcihSb3V0ZXIgVmVyaXpvbilcbiAgUm91dGVyIC0tIGh0dHBzIDg0NDMgLS0-IEFQSUdhdGV3YXlcbiAgUm91dGVyIC0tIGh0dHBzIDQ0MyAtLT4gRnJvbnRlbmRcblxuICAgIHN1YmdyYXBoIExpbnV4IERlc2t0b3BcbiAgICBGcm9udGVuZFxuICAgIEFQSUdhdGV3YXkoQVBJR2F0ZXdheSlcbiAgICBBUElHYXRld2F5IC0tIDgwIC0tPiBBdXRoU2VydmVyXG4gICAgQVBJR2F0ZXdheSAtLSA4MCAtLT4gQmFja2VuZFxuICAgIEFQSUdhdGV3YXkgLS4tIEV1cmVrYVsoRXVyZWthKV1cbiAgICBBdXRoU2VydmVyIC0tPiBDYXNzYW5kcmFbKENhc3NhbmRyYSldXG4gICAgQXV0aFNlcnZlciAtLi0gRXVyZWthWyhFdXJla2EpXVxuICAgIEJhY2tlbmQgLS0-IENhc3NhbmRyYVsoQ2Fzc2FuZHJhKV1cbiAgICBCYWNrZW5kIC0uLSBFdXJla2FbKEV1cmVrYSldXG5cbiAgICBlbmRcblxuICBlbmRcblxuICBzdWJncmFwaCBCcm93c2VyXG4gIFdlYkFwcCAtLT4gUm91dGVyXG4gIGVuZCIsIm1lcm1haWQiOnsidGhlbWUiOiJkZWZhdWx0In0sInVwZGF0ZUVkaXRvciI6ZmFsc2V9)


### CI/CD

- CI: Jenkins builds, tests and pushs images to DockerHub
- CD: Kubernetes
  - Being Developed
  - I’m porting this site to Kubernetes on my desktop in my free time. Eureka Registry will be deprecated because Kubernetes’ Service manages the pods and provides load balancing, Spring Cloud API Gateway will be substituted with a Kubernetes’ Ingress controller which has API gateway functionalities.

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

## Sequence

### How DynamicDNS is updated

[![](https://mermaid.ink/img/eyJjb2RlIjoic2VxdWVuY2VEaWFncmFtXG4gICAgbG9vcCB1cGRhdGUgcGVyaW9kaWNhbGx5XG4gICAgRERDbGllbnQgSW4gRGVza3RvcCAtPj4gRERDbGllbnQgSW4gRGVza3RvcDogRmluZCBQdWJsaWMgSVAgKFJvdXRlcidzIElQKVxuICAgIEREQ2xpZW50IEluIERlc2t0b3AgLT4-IEdvb2dsZURvbWFpbjogVXBkYXRlIFB1YmxpYyBJUCBpbiBEeW5hbWljIEROUyBSZWNvcmRcbiAgICBlbmQiLCJtZXJtYWlkIjp7InRoZW1lIjoiZGVmYXVsdCJ9LCJ1cGRhdGVFZGl0b3IiOmZhbHNlfQ)](https://mermaid-js.github.io/mermaid-live-editor/#/edit/eyJjb2RlIjoic2VxdWVuY2VEaWFncmFtXG4gICAgbG9vcCB1cGRhdGUgcGVyaW9kaWNhbGx5XG4gICAgRERDbGllbnQgSW4gRGVza3RvcCAtPj4gRERDbGllbnQgSW4gRGVza3RvcDogRmluZCBQdWJsaWMgSVAgKFJvdXRlcidzIElQKVxuICAgIEREQ2xpZW50IEluIERlc2t0b3AgLT4-IEdvb2dsZURvbWFpbjogVXBkYXRlIFB1YmxpYyBJUCBpbiBEeW5hbWljIEROUyBSZWNvcmRcbiAgICBlbmQiLCJtZXJtYWlkIjp7InRoZW1lIjoiZGVmYXVsdCJ9LCJ1cGRhdGVFZGl0b3IiOmZhbHNlfQ)


### How this website works

[![](https://mermaid.ink/img/eyJjb2RlIjoic2VxdWVuY2VEaWFncmFtXG4gICAgcGFydGljaXBhbnQgVXNlclxuICAgIHBhcnRpY2lwYW50IEZyb250ZW5kXG4gICAgTm90ZSByaWdodCBvZiBGcm9udGVuZDogTkdJTlhcbiAgICBwYXJ0aWNpcGFudCBBdXRoU2VydmVyXG4gICAgTm90ZSByaWdodCBvZiBBdXRoU2VydmVyOiBTcHJpbmdcbiAgICBwYXJ0aWNpcGFudCBCYWNrZW5kXG4gICAgVXNlciAtPj4gRnJvbnRlbmQ6IGJyb3dzZSB3d3cuZmxvd2FudC5vcmdcbiAgICBGcm9udGVuZCAtLT4-IFVzZXI6IGRvd25sb2FkIEhUTUw1IEFwcC5cbiAgICBVc2VyIC0-PiBBdXRoU2VydmVyOiBTaWduLXVwXG4gICAgVXNlciAtPj4gQXV0aFNlcnZlcjogU2lnbi1pblxuICAgIEF1dGhTZXJ2ZXIgLS0-PiBVc2VyOiBBY2Nlc3MtdG9rZW5cbiAgICBcbiAgICBsb29wIGhhbmRsZSByZXNvdXJjZXMgd2l0aCBhY2Nlc3MgdG9rZW5cbiAgICBVc2VyIC0-PiBCYWNrZW5kOiBSRVNUIEFQSSBSZXF1ZXN0XG4gICAgQmFja2VuZCAtLT4-IFVzZXI6IFJFU1QgQVBJIFJlc3BvbnNlXG4gICAgZW5kIiwibWVybWFpZCI6eyJ0aGVtZSI6ImRlZmF1bHQifSwidXBkYXRlRWRpdG9yIjpmYWxzZX0)](https://mermaid-js.github.io/mermaid-live-editor/#/edit/eyJjb2RlIjoic2VxdWVuY2VEaWFncmFtXG4gICAgcGFydGljaXBhbnQgVXNlclxuICAgIHBhcnRpY2lwYW50IEZyb250ZW5kXG4gICAgTm90ZSByaWdodCBvZiBGcm9udGVuZDogTkdJTlhcbiAgICBwYXJ0aWNpcGFudCBBdXRoU2VydmVyXG4gICAgTm90ZSByaWdodCBvZiBBdXRoU2VydmVyOiBTcHJpbmdcbiAgICBwYXJ0aWNpcGFudCBCYWNrZW5kXG4gICAgVXNlciAtPj4gRnJvbnRlbmQ6IGJyb3dzZSB3d3cuZmxvd2FudC5vcmdcbiAgICBGcm9udGVuZCAtLT4-IFVzZXI6IGRvd25sb2FkIEhUTUw1IEFwcC5cbiAgICBVc2VyIC0-PiBBdXRoU2VydmVyOiBTaWduLXVwXG4gICAgVXNlciAtPj4gQXV0aFNlcnZlcjogU2lnbi1pblxuICAgIEF1dGhTZXJ2ZXIgLS0-PiBVc2VyOiBBY2Nlc3MtdG9rZW5cbiAgICBcbiAgICBsb29wIGhhbmRsZSByZXNvdXJjZXMgd2l0aCBhY2Nlc3MgdG9rZW5cbiAgICBVc2VyIC0-PiBCYWNrZW5kOiBSRVNUIEFQSSBSZXF1ZXN0XG4gICAgQmFja2VuZCAtLT4-IFVzZXI6IFJFU1QgQVBJIFJlc3BvbnNlXG4gICAgZW5kIiwibWVybWFpZCI6eyJ0aGVtZSI6ImRlZmF1bHQifSwidXBkYXRlRWRpdG9yIjpmYWxzZX0)

---

## Install

Install Docker and Docker-compose.

Make **env_file.txt** file containing as following contents.

```
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

# Registry and load balancer for services
EUREKA_ID=Yours
EUREKA_PASSWORD=Yours

```

Modify your **env_file.txt** path in docker-compose*.yml files. Default value is **~/site/env_file.txt**

Add website server addresses to **/etc/hosts** file in your host.

```
# For local build.
127.0.0.1    cassandra

# For local build, local browser should use www.flowant.org.
# Instead of 127.0.0.1 because of CORS setting.
127.0.0.1    www.flowant.org

# The same address of www.flowant.org.
127.0.0.1    gateway.flowant.org
```

Clone Website project git.

```
mkdir -p ~/site
cd ~/site
git clone https://github.com/flowant/website.git
```

Make free certification from "SSL for free", then make PKCS12 for HTTPS as follows.

```
openssl pkcs12 -export -name website_ssl -out ~/site/website/keystore/ssl_certificate.p12 -inkey private.key -in certificate.crt -certfile ca_bundle.crt
```

Make jwk cert for AuthServer

```
keytool -genkey -keyalg RSA -alias website_keystore -keypass Yourkey -keystore ~/site/website/keystore/keystore.jks -storepass Yourkey -validity 365
```

Make directories to be used as docker volumes, they are used in docker-compose*.yml files.

```
# Cassandra data storage.
mkdir -p ~/site/cassandra_data

# Backend File Storage.
mkdir -p ~/site/storage
```

Create build container for maven based project.

```
cd ~/site/website
docker-compose -f docker-compose-maven.yml build
```

Cassandra will run and start Maven building. Targets will be created in ~/site/website directory on your host by virtue of Docker Volume.

```
docker-compose -f docker-compose-maven.yml up --exit-code-from maven
```

> If your development environment has Java, Maven, Cassandra then you can build by your self.
> 
> ```
> mvn clean install
> ```

You can make sure the build has done successfully by following messages.

```
maven_1      | [INFO] Basic Website ...................................... SUCCESS [  1.042 s]
maven_1      | [INFO] common ............................................. SUCCESS [ 20.422 s]
maven_1      | [INFO] authserver ......................................... SUCCESS [ 24.546 s]
maven_1      | [INFO] backend ............................................ SUCCESS [ 24.208 s]
maven_1      | [INFO] gateway ............................................ SUCCESS [  1.545 s]
maven_1      | [INFO] registry ........................................... SUCCESS [  0.692 s]
maven_1      | [INFO] ------------------------------------------------------------------------
maven_1      | [INFO] BUILD SUCCESS

```

Create docker images from build results.

```
docker-compose -f docker-compose.yml build
```

Run servers.

```
docker-compose -f docker-compose.yml up -d
```

Or you can just run the deploy script just after deploying certificates to ~/site/website/keystore

```
cd website
./scripts/deploy.sh
```

### To check, open your browser and go www.flowant.org

---

## TODO List

- Apply social login using OAuth2.
- Porting on Kubernetes
- Study and refine frontend with testcases.
