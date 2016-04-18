# Ticket-Service


Simple ticket service that facilitates the discovery, temporary hold, and final reservation of seats within a high-demand performance
venue.

The service is done as a Rest Service. All data is stored in memory.

**System requirements:** java 1.8, maven 3.x

**Assumptions:**
 - The lower level id the better seats
 - Seats have the same priority within the same level
 - MinLevel and MaxLevel should be both null or both have values
  
**Technologies:**

 - Java 1.8
 - Maven 3.x
 - Spring
 - Spring boot
 - Spring MVC Rest
 - Mockito
 - Rest assure
 - jacoco
 
 
 **Design Solutions:**

 - The Service has classical architecture: controller->service-dao
 - Venue data is stored in memory and will be erased after the redeploy
 - Concurrency problem is solved with pessimistic locks (for write operations). All the write operations synchronized.  All the read operations are not synchronized so they do not wait for the end of write operation. This will work only if the application is running on a single node (only one jvm). For a cluster mode distributed lock mechanism should be used (for example database select for update)
 -  Seat holds are cleaned by scheduler that runs every 60(possible to change value) seconds.
 -  Exceptions are handled by handler that uses AOP with @ControllerAdvice and @ExceptionHandler
 -  Id Generator is done with AtomicInteger (works only if running on one jvm)
 -  Configuration (venue data and expiration time) is stored in VenueConfig.java for simplicity.

    

 **How to build**:
 
 
**unit tests:**
   

     mvn clean test

 code coverage folder: {project}/target/site/index.html
 currently code coverage = 92%, branch coverage= 97%
 
 
**deploy application:**
   

     mvn clean package && java -jar target/ticket-service-1.0.jar 

 
 **acceptance test:**

     mvn clean verify -P acceptance

 (You will see an exception trace in console but it is ok, some validation failed and rest exception handler handled this)
 
**End Points:**
 
 **1. Find the number of seats available within the venue, optionally by seating level:**         

      curl -X GET http://localhost:8080/rest/v1/seats?level={level}

 
  **2. Find and hold the best available seats on behalf of a customer, potentially limited to specific levels. MinLevel and MaxLevel are optional:**
  
    curl -H "Content-Type: application/json" -X POST -d '{"numSeats" : "10", "minLevel" : "2", "maxLevel" : "2","customerEmail" :"my@gmail.com"}' http://localhost:8080/rest/v1/holds

 
  **3. Reserve and commit a specific group of held seats for a customer:**
 

     curl -H "Content-Type: application/json" -X POST -d '{"seatHoldId" : "1", "customerEmail" : "my@gmail.com"}' http://localhost:8080/rest/v1/reservations
 

