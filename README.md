## Project Summary:
This is a sample project for implementing  useful subjects and technologies in Java enterprise applications.
Some features and technologies used in this project are:
- Rate limiter to prevent DDOS attack (Bucket4j)
- Logging system and user actions using ELK stack
- Spring Security (Implemented roles and permission for different access level)
- Redis for user OTP authentication and caching
- Docker for project deployment
- Hibernate and JPA for handling database transactions.

## Building and Starting the Project:
Open terminal and run the following commands in order: 
``` 
docker-compose down
```
``` 
docker-compose build
```
``` 
docker-compose up
```