1. Tech stack
   Java 21
   Spring Boot (Web, Security, Data)
   PostgreSQL
   Lombok
2. You should create DB (CREATE DATABASE userrating;)
3. In properties file application.yml set your user for DB. Default user is "postgres" with password "root"
2. When first time start application in config class DatabaseLoader, will be created users with roles ADMIN and USER
   login - U1 ; password - 1
   login - U2 ; password - 2
   login - U3 ; password - 3
   login - U4 ; password - 4
   login - U5 ; password - 5
   login - A1 ; password - 1
   login - A2 ; password - 2
4. API Documentation for task in Swagger https://app.swaggerhub.com/apis/VSTARCHEVSKYI/UserRating/1.0.0
