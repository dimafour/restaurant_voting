## Restaurant Voting REST API

### Technical specifications
> [Task to implement](https://github.com/JavaWebinar/topjava/blob/doc/doc/graduation.md)

### Swagger
Ensure that you're working at **localhost:8080**
> * [API Docs](http://localhost:8080/v3/api-docs)
> * [Swagger UI](http://localhost:8080/swagger-ui/v3)

### Tech Stack
> * Java 21
> * Lombok, JUnit, Jackson
> * Spring Boot, Data JPA, MVC, Security
> * H2 Database
> * Caffeine Cache

### Description
> * This API allow users to vote for the restaurant they want to dinner at. 
> * Two types of roles: regular users & admins
> * Admins create, update, delete restaurants, menus and meals for today 
> * Admins have access to stored vote history & tools to manage users' profiles
> * User can vote only once a day & change their decision before deadline (11.00 am)
> * All summary & API methods with parameters info are available at Swagger UI
