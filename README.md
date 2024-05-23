## Restaurant Voting REST API

### Technical requirements
> Design and implement a REST API using Hibernate/Spring/SpringMVC (Spring-Boot preferred!) without frontend.
Build a voting system for deciding where to have lunch.
2 types of users: admin and regular users
* Admin can input a restaurant and it's lunch menu of the day (2-5 items usually, just a dish name and price)
* Menu changes each day (admins do the updates)
* Users can vote for a restaurant they want to have lunch at today
* Only one vote counted per user
* If user votes again the same day:
  * If it is before 11:00 we assume that he changed his mind.
  * If it is after 11:00 then it is too late, vote can't be changed
* Each restaurant provides a new menu each day.

### Swagger local
Ensure that you're working at **localhost:8080**
> * [API Docs](http://localhost:8080/v3/api-docs)
> * [Swagger UI](http://localhost:8080/swagger-ui/index.html)

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
> * Admins have tools to manage users' profiles
> * User can vote only once a day & change their decision before deadline (11.00 am)
> * All summary & API methods with parameters info are available at Swagger UI
