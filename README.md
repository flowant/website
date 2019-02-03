# Website Project

## A Goal

Studying and operating modern web technologies and sharing results.

## Architecture

Frontend
- Angular, Bootstrap, SCSS.

API Gateway
- Spring Boot Cloud Gateway.

Backend
- Auth server: Spring Boot Oauth2.
- API Server: Spring Boot Webflux.

Database
- Cassandra

Log Management
- Angular based servers use ngx logger.
- Spring-based servers use log4j2 async logger.
- Capacity for log directories should be small (< 30M): file logs will be used for debugging server booting issues.
- All logs are aggregated by Fluentd via http.

## TODO
Log Management.
Deployment to Cloud.
Continuous integration.
