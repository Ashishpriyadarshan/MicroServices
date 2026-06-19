# Making Microservices Resilient:
* In a microservices environment there can be many challenges that are faced by the microservices .
  * Be it network or internal factors like slow processing or server overload etc.
* In monolithic application the entire product was a single app and any issues caused inside the same app could hamper the entire app .
* That is the reason why microservices were introduced so that individual responsibilities of a big app can be broken down to simpler smaller apps.
* But that doesn't mean microservices architect will be perfect , offcourse there will be issue's.
* ![Resiliency.png](images/Resiliency.png)
* If you look at the image above then you can understand better.

### Scenario:
* Suppose we have a product and inside that product there are going to be multiple microservices running.
* Suppose we have Users service ,orders Service and payments Service .
* We know that each service has its own set of threads , and these threads are host system independent.
* Suppose the flow of control goes like : User places some order but before placing the order the payments service is called to make the payment.
* Now order is only placed and successful only when payment is done and a positive response is sent back by the payments service to the orders service.
* Suppose for some xyz reasons the payment service is working very slowly:
  * Maybe due to some network issue like orders is calling payment service hosted on some other machine but the machine on which payment service is hosted has a internet or network which has got slowed.
  * Maye due to some large chunk of code getting executed inside the payment service.
  * Maybe the payment service threads are all occupied and are working.
  * Maybe the machine on which payment service is hosted has all of its resources occupied by other services or apps due to which payment service threads are not getting the required allocation.
  * There can be multiple reasons.
* Now what will happen is orders service will keep sending new new orders request to the payment service which is already slow or is fully occupied .
* Due to this what will happen is slowly with time threads of orders service will also start getting occupied .
* Well it is not made using some NETTY Server like gateway that its threads will get freed easily.
* Obviously its threads will remain occupied due to which after some time due to payments service working slowly and sending a response back to the orders service either very slowly or not at all , this will cause the orders service to also become slow and loaded .
* Due to which at last the server might just crash .
* See how because of one microservice other services are also getting affected .
* This event is known as cascading failures.
* So we need to find and learn ways in which we can counter this issues so that our services work properly.


### Questions:
* How do we avoid cascading failures ?
  * -> One failed or slow service should not have a ripple effect on the other microservices.
  * -> Like in the scenario of multiple microservices are communicating , we need to make sure that the entire chain of microservices does not fail with the failure of a single microservice.

* How do we handle failures gracefully with fallbacks ?
  * -> In a chain of multiple microservices, how do we build a fallback mechanism like if one of the service is not working as expected , then what should we do so that the incoming requests start getting back a default response , like be it calling some other service or fetching some other record.

* How to make our services self-healing capable ?
  * -> In the cases of slow performing services , how do we configure timeouts , retries and give time for a failed service to recover itself.


### Methods in which we can implement Resiliency:
* ![Methods to implement Resiliency.png](images/Methods%20to%20implement%20Resiliency.png)
* We will be using a popular library Resilience4j to implement all of the above.
* We will learn more about these methods in the upcoming lecture.
* Link: ``https://resilience4j.readme.io/docs/getting-started``
* You can go to the above link and study more about the different methods.


### 1st Commit: "Introduction to Resiliency | Ways to implement it | Importance of Resiliency in a microservices based architect"
___

### Circuit Breaker Pattern:
* It is a very famous pattern followed by most of the microservices across the world.
* It is similar to a electric circuit.
* Suppose there are several devices connected in a circuit and at the beginning of the circuit sits a fuse/breaker .
* The task of the fuse/breaker is to detect if there is any excess current flowing .
* If excess current flows so the fuse breakers causing the current to stop flowing.
* It has 2 states : closed when the circuit is ok , open when the circuit is faulty and needs time to recover.
* Well now let's understand how circuit breaker pattern works in the microservices environment.
  * Suppose 2 services are there Service A and Service B .
  * Suppose the task of service A is to send requests to service B .
  * Now due to some reason be it due to network reasons or internal reasons of service B , the service A is getting affected .
  * Slow response by service B due to which threads of service A are waiting or are getting occupied for longer period of time due to which the load on service A is also increasing.
  * So if the service B doesnt get back to its normal working state then it will cause the failure of service A also.
  * So we need to do something .
  * Solution:
    * We have to monitor the response time and other factors of service B , And we will do all this by sitting at service A.
    * Service A will keep monitoring service B and it will decide whether to send any furthur requests to B or not.
    * And also if Service A is not sending any requests to service B then definitely it has to handle them either by rejecting the requests or by calling some other fallback methods.
    * And also After some time duration when Service A feels that maybe service B would have recoverd by now so it starts sending the incoming requests to service B but in a specific defined and lesser numbers to test if service B can properly process and send back the response or not.
    * If again the service B is failing to send back responses properly then Service A again will stop sending requests to service B.
    * All the above things which i mentioned under the Solution: This is know as the circuit breaker pattern.
* Circuit Breaker Pattern:
  * In a distributed environment , calls to remote resources and service can fail due to transient faults , such as slow network connections , timeouts or the resources being overcommitted or temporarily unavailable.
  * These faults typically correct themselves after a short period of time and a robust cloud application should be prepared to handle them.
  * The circuit breaker pattern which is inspired from electrical circuit breaker will monitor the remote calls . If the calls take too long and the no of failed call attempts cross a certain threshold then the circuit breaker will intercede and kill the call.
  * Also, the circuit breaker will monitor all calls to a remote resource , and if enough calls fail , the circuit break implementation will pop , failing fast and preventing future calls to the failing remote resource.
  * The circuit breaker pattern also enables a application to detect whether the fault has been resolved . If the problem appears to have been fixed, the application can try to invoke the operation.
  * Advantages of circuit breaker pattern:
    * Fail fast.
    * Fail gracefully.
    * Recover seamlessly.
* There are 3 states : 
    * Closed when everything is fine and working.
    * Open when the other service is healing or handling its prebooked or other exisiting requests and in this stage calls are not made to the healing service so that it first solves its existing requests and after that let it heal.
    * Half-Open state when the service is tested by sending a limited no or actual requests rather than sending so many.

### Circuit breaker can be implmented both in service to service call and Gateway Server too.
### Difference is incase of gateway server we will have to write the Reactive coding syntax's .

### Diagram of a Circuit Breaker in terms of Electric Circuit:
* ![Electric Circuit.png](images/Electric%20Circuit.png)

### Diagramatic Flow of Circuit Breaker in terms of microservices Calls and Gateway Server:
* ![Circuit Breaker pattern.png](images/Circuit%20Breaker%20pattern.png)


### Circuit Breaker Flow and Sliding window:
* Image 1:
    * ![Circuit Breaker and Sliding window.png](images/Circuit%20Breaker%20and%20Sliding%20window.png)
* Image 2:
    * ![Circuit Breaker and sliding window 2.png](images/Circuit%20Breaker%20and%20sliding%20window%202.png)


### 2nd Commit: " Circuit Breaker Pattern Introduction and Cases"

___

### Implementing Circuit Breaker Pattern in Gateway/Edge Server:
* We know that the gateway server that we are using is build on top of NETTY Server .
* It uses Reactive Coding not the normal java spring syntax so we have to configure it accordingly.
* Lets first add the dependency to the pom.xml of gateway server .
* ![img.png](images/img.png)
* The above dependency we have to add .
* Now circuit breaker dependency is added .
* Lets first create a circuitbreaker and then add it to any one of the microservices that we have introduced inside the RouteLocator .
* ![img_1.png](images/img_1.png)
* From the above image suppose we want to create a circuitbreaker for the accounts route , so how will we do that.
  * Steps:
    * 1st: create a config inside the application.yml for the circuit breakers , You can either create a single default circuit breaker or you can have as many custom circuit breakers as you want.
      * Suppose we are creating a circuit breaker for the Router of the accounts.
      * ![img_2.png](images/img_2.png)
      * The above image is what are the configs for the custom circuit breaker which is ``accountsServiceCircuitBreaker``
    * 2nd: Now before adding the circuit breaker to the router of accounts we need to create a fallback method also which will be executed when the circuit is in OPEN state.
    * 3rd: But lets just observe if we add the circuit breaker directly to the route without fallback what happens.
      * ![img_3.png](images/img_3.png)
      * See here we have added the circuit breaker inside the route of the accounts and we added it inside the filters .
      * We added it inside the filters because we want the request to be modified before sending it directly , only when we add the filter then only the circuit breaker will work before forwarding the request to the desired microservice.
      * ### Now lets test how the circuit breaker will work:
        * Lets start the configserver -> eurekaserver -> accouts -> gatewayserver. Start all in debug mode.
        * Once all the apps are live , now just go and hit the actuator link of the gateway server.
        * ![img_4.png](images/img_4.png)
        * See we are getting so many links for the circuit breaker . Each one serves a different purpose.
        * If we go to /actuator/circuitbreakers then we will get the list and configs of all the circuit breakers that we have created along with their info of failed as well as accepted requests.
        * ![img_5.png](images/img_5.png)
        * See in the above image we can see our circuit breaker along with the metrics that we have mentioned and we are also getting its current state.
        * Now suppose on postman we hit this api: ``http://localhost:8072/microdemo/accounts/api/get-contact-info``
        * ![img_6.png](images/img_6.png)
        * Now if we got to ``actuator/circuitbreakerevents?name=accountsServiceCircuitBreaker`` then we can see the events or requests that were sent to the accounts microservice and their info like were they successfull or failed.
        * ![img_8.png](images/img_8.png)
        * See in the above image .
        * Now suppose we keep a break point intentionally inside the accounts and to mimic slow response by accounts service then lets see what happens.
        * ![img_9.png](images/img_9.png)
        * See here i have inserted a breakpoint here.
        * Now suppose let me hit the same URL in the postman again: ``http://localhost:8072/microdemo/accounts/api/get-contact-info``
          * ![img_10.png](images/img_10.png)
          * See it instantly failed with the above error response.
        * lets check the /actuator/circuitbreaker details.
        * ![img_11.png](images/img_11.png)
        * Observe the no of failedcalls has increased to 1 but the state is still CLOSED because we haven't hit the failureRateThreshold.
        * Also in ```` we can observe that a new event is registered here. Image attached below .
        * ![img_12.png](images/img_12.png)
        * If you observer the state transition is NULL which means no change of state of circuitbreaker has happened.
        * Another thing is by-default the response time is set to 1000ms which is 1s so if no response within 1 sec so it will be considered failed.
        * Now lets try to hit the threshold by trying multiple requests under a small time.
        * ![img_13.png](images/img_13.png)
        * See after again and again hitting the accounts service now this error comes with message that Upstream service is not available.
        * When you hit ``actuator/ciruitbreakers`` You see the below info
        * ![img_14.png](images/img_14.png)
        * Here observe the no of failed calls and state which is now OPEN and it will remain in OPEN state for 30sec as mentioned by us in the application.yml .
        * Now if you go to ``actuator/circuitbreakerevents?name=accountsServiceCircuitBreaker`` then you will see how the TYPE is changing.
        * ![img_15.png](images/img_15.png)
        * In the above image see after so many failed attemps we finally got the TYPE as FAILURE_RATE_EXCEEDED.
        * Then in another request it is saying the state is under transition , then in another request it doesnt allow any furthur requests unless the state gets back to half_open and then finally closed state.
        * ![img_16.png](images/img_16.png)
        * See in the above image finally after 30 seconds we are in the half open state.
        * See we also get the stateTransition info like from OPEN to half_OPEN
        * ![img_21.png](images/img_21.png)
        * Now this state is crucial as mentioned by us in the configs if 3 requests are made and 50% of the 3 requests is successful then only the state will change to CLOSED.
        * Now suppose let me remove the breakpoint just for 1 request and lets see what happens when i put it back for the 2nd and 3rd request:
        * ![img_17.png](images/img_17.png)
        * Break Point removed and also click on the resume button to resume the program.
        * Now lets hit the same get-app-info api in postman.
        * 1 success: ![img_18.png](images/img_18.png)
        * actuator/circuitbreakers: ![img_20.png](images/img_20.png)
        * We are still in the HALF_OPEN State.
        * Now again add the breakpoint at the same point. 
        * ![img_19.png](images/img_19.png)
        * Now lets go for the 2nd and 3rd requests:
          * ![img_22.png](images/img_22.png)
          * See the state became OPEN after 2 failed requests out of 3.
          * Had it been 1 failed request out of 3 then in that case the State would have changed to CLOSED.
          * ![img_23.png](images/img_23.png)
          * We get this notification after sending 4th request after the state became OPEN.
          * ![img_24.png](images/img_24.png)
          * The above image show the logs like state transition status and TYPE like not_permitted.
          * Suppose we intentionally fail only 1 request and rest 2 are successful in that case lets see how the state goes from half_open to closed.
          * For this again add the breakpoint and just fail 1 request after that remove the break point and then send 2 requests and then send another request and see the state change.
          * //////////////////////////////////////////////////////



### Adding a Fallback Method to our CiruitBreaker:
* Inorder to add a fallback method , we have to create a controller class which have the RESTCONTROLLER annotation and some methods insdie it.
* We need a fallback mechanism because in real applications we cannot just throw the error trace to the users or client so we need a better approach.
* First create a package inside the gatewayServer app with the name controller.
* Inside that create a class with the name FallbackController.
* And make it as it is shown in the below image:
  * ![img_25.png](images/img_25.png)
* It will be having RESTCONTROLLER annotation as well as GETMAPPING annotation for the method as it is going to return something to the user as a http response.
* Since we are writing it inside the gatewayServer so we are using the webflux syntaxs like mono etc.
* Now we just need to integrate this into the fallback mechanism for the accounts route .
* ![img_26.png](images/img_26.png)
* See here how we added it to the Route definition . we are just forwading it to the internal API that is accountsFallbackController.
* Now lets test it : same as before we will add breakpoint at the same place and then try to hit 10 requests then we will see what happens if we hit more requests in the OPEN State.
* Start configserver->eureka->gateway->accounts in the debug mode:
  * ![img_27.png](images/img_27.png)
  * Added breakpoint.
  * ![img_28.png](images/img_28.png)
  * Initial State is shown above now lets hit using postman.
  * ![img_29.png](images/img_29.png)
  * See this time instead of giving us a error message it just gave us the fallback method response.
  * It will do this both in half_open state as well as OPEN state as well as Closed state for any failed requests.
  * ![img_32.png](images/img_32.png)
  * See here after failed attempts exceeded threshold the other requests were not permitted and also the state changed.

* Finally we have implemented the ciruitbreaker pattern inside the gateway . Next we will learn how to implement the circuitbreaker inside the service to service call.

### 3rd Commit: " Implemented Circuit Breaker inside the Gateway Server | Implemented a custom Fallback Method"