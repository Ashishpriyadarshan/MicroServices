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
  * It will do this both in half_open state and OPEN state as well as Closed state for any failed requests.
  * ![img_32.png](images/img_32.png)
  * See here after failed attempts exceeded threshold the other requests were not permitted and also the state changed.

* Finally, we have implemented the ciruitbreaker pattern inside the gateway . Next we will learn how to implement the circuitbreaker inside the service to service call.

### 3rd Commit: " Implemented Circuit Breaker inside the Gateway Server | Implemented a custom Fallback Method"


___

## Implementing Circuit Breaker pattern inside service to service call:
* Well if you go and ask Chatgpt like : How to implement circuit breaker pattern inside a service that is calling another service via OpenFeign client and ask what are the dependencies and configs needed along with fallback methods so it will give you all the details.
* Or else you can go to the official documentation of ``https://docs.spring.io/spring-cloud-openfeign/reference/spring-cloud-openfeign.html#spring-cloud-feign-circuitbreaker``
* Read the documentation, and you will find the ways in which you can implement the Circuit Breaker Pattern.
* Instead of asking chatgpt first go to the above link and see how the circuit breaker is implemented so that you can get a better idea.
  * Steps: 
    * First we have to include some dependencies in the pom.xml of the service that is using the feignclient .
    * In our case Accounts usese feignclient to make calls to loans and cards microservices.
    * ![img_33.png](images/img_33.png)
    * Now if you go to the above link then you can see how we can implement circuit breaker along with fallback.
    * ![img_34.png](images/img_34.png)
    * As per the above image you have to either return a NULL or return exactly what was being returned by the feignclient function or you can have custom exceptions as per your wish.
    * And also it says that you have to create a component class where there will be the fallback functions and these fallback functions are nothing but same overridden functions of the feignclient class.
    * That is reason why you would be seeing implementing the feignclient interface.
    * So lets start creating the Fallback class and other things.
    * ![img_35.png](images/img_35.png)
    * Enable the above in the application.yml . If you dont enable the above then circuit breaker integration and fallback will not work inside your app.
    * And it internally starts creating the circuitbreakers at method level for each of the feignclients function and you will see their naming ahead.
    * ![img_36.png](images/img_36.png)
    * Now we need to create some Fallback configs for our FeignClients in the application.yml .
      * CardsFeignClient:
        * ![img_37.png](images/img_37.png)
        * CircuitBreaker in application.yml:
          * ![img_38.png](images/img_38.png)
          * The naming convention here for the instances looks like CardsFeignClientfetchCard where CardsFeignClient is the name of the feignclient interface and fetchCard is the function name if you look at the above images of the CardsFeignCleint that i have attached.
          * We can also use the name value which is cards in the resilience4j configs but for that we have to change some other configs too.
          * ![img_39.png](images/img_39.png)
          * Because we have set the above property true that is the reason why we have to define method level circuitbreaker configs.
      * LoansFeignClient:
        * ![img_40.png](images/img_40.png)
        * Circuit Breaker in application.yml:
          * ![img_41.png](images/img_41.png)
          * See the name of the instance it is LoansFeignClient followed by the api name: fetchLoandetails.
          * We cannot use some other name because it is the name which is internally generated by the CircuitBreaker and that is the reason why we are able to give configs here under this name so that it can get overridden.
    * ### Creating Fallback classes for the above two feignclients:
      * CardsFallback:
        * ![img_42.png](images/img_42.png)
        * As explained earlier here we are just implementing the same feignclient interface.
        * Now inside this we are returning NULL value . In real projects it is upto the dev and business requirement what to return but generally a RUNTIME exception is throwed here to tell that this service is unavailable .
        * We will develope some RUNTIME Exceptions ahead.
      * LoansFallback:
        * ![img_43.png](images/img_43.png)
        * Just like the CardsFallback this one is also implementing the LoansFeignClient interface and overriding its functions and returning NULL.
      * While testing the fallback mechanism , we know inside our accounts there is a API which consolidates the info from all the other microservices now if cards and loans are unavailable then when we hit the API which brings the consolidated info then we will get only Accounts and Customer details . Loans and Cards details will come as NULL which can prove that our Fallback is working fine.
      * Now we need to do some changes inside our FeignClient interfaces that is adding the fallback class there .
        * CardsFeignClient:
          * Before: ![img_44.png](images/img_44.png)
          * After: ![img_45.png](images/img_45.png)
          * See inside the FeignClient annotation.
        * LoansFeignClient:
          * Before: ![img_46.png](images/img_46.png)
          * After: ![img_47.png](images/img_47.png)
        * Now we are ready to test our apps. But before that since our feignClients are returrning NULL due to OPEN Circuit so we have to do some NULL checks like handle them.
        * ![img_48.png](images/img_48.png)
        * Inside the CustomerServiceImpl class we are calling the feignClients and also here we are using the mappers to Map the incoming Data too so here we have to do the NULL checks.
        * ![img_49.png](images/img_49.png)
        * Now see here how we handled the NULL checks .
      * Now finally we are done with setting the CircuitBreaker and Fallbacks , so we can move ahead to the testing phase.

___

### Testing the Circuit Breaker and Fallbacks of FeignClients: 
* First start all your mircoservices: configserver->eurekaserver->gatewayserver->accounts->loans->cards.
* Well while starting the accounts in debug mode we encountered a error:
* ![img_50.png](images/img_50.png)
* So upon deep diving we go to know that we never created a bean of LoansFallback nor CardsFallback:
* We forgot to add the @Component annotation .
* That is the reason why our accounts microservice beans were not getting created as they are interdependent on each other.
* LoansFallback:
  * Before: ![img_51.png](images/img_51.png)
  * After: ![img_53.png](images/img_53.png)
* CardsFallback:
  * Before: ![img_52.png](images/img_52.png)
  * After: ![img_54.png](images/img_54.png)
* Now just normally create some raw data inside all the services.
  * postman:
    * Accounts: ![img_55.png](images/img_55.png)
    * Cards: ![img_56.png](images/img_56.png)
    * Loans: ![img_57.png](images/img_57.png)
* Now try to hit the consolidated fetch API.
  *     * Consolidated Fetch API:
      * ![img_58.png](images/img_58.png)
      * ![img_59.png](images/img_59.png)
* Now stop the Loans microservice which is responsible for sending the info via feignclient to the accounts service.
* 
* Now just stop the loans microservice and then hit the same consolidated api again:
  * consolidated API: ![img_61.png](images/img_61.png)
  * See the above response we are getting the response but at the same time loansDto is null means the fallback mechanism was exectuted.
* Now lets again hit the same consolidated API but before that lets stop the Cards app:
  * ![img_62.png](images/img_62.png) 
  * See here CardsDto is null that means fallback successfully executed . This might not immediately take place as de-registration from eureka takes some time.
* Now lets see the events of the circuitbreakers of accounts:
  * ``http://localhost:8080/actuator/circuitbreakerevents``
  * If you go to the above link during runtime then you can see all the events taking place.
  * But here we observe a very strange thing even after more than 50% failed requests still the state of circuitbreaker is not changing from closed to open:
  * Evidence:
    * ![img_63.png](images/img_63.png)
    * If you look at the LoansFeignClientfetchLoanDetails then this is the actual name that we had given to our circuit breaker instance then why instead of this circuit breaker LoansFeignClientfetchLoanDetailsStringString is picking up all the signals and also it is not defined by us.
    * If you go to the documentation of OpenFeignClient support for circuit breaker 
    * ![img_64.png](images/img_64.png)
    * You will see this that the pattern of the name of circuitbreaker which are auto generated by open feign is FeignClientClassName then CalledMethod then ParameterTypes.
    * That is the reason why we are getting LoansFeignClientfetchLoansDetailsStringString.
    * ![img_65.png](images/img_65.png)
    * See here the name of the interface class followed by the function/api name followed by the parameter types .
    * But then you may ask then how is the fallback function working created by us and who's name we had given inside the FeignClient annotation.
    * The thing is fallback is not a part of circuitbreaker it is just another function which will execute when something is not working as expected.
    * circuit breaker just monitors and allows traffic accordingly.
    * And If you remember we had set one property which is going to wrap all the feignclients and create default circuit breakers using the default naming : 
    * ![img_66.png](images/img_66.png)
    * The above property.
    * In the next part we will learn how we can resolve this automatic name generated conflict.

### Naming Pattern of CircuitBreaker:
* Here we will learn how to give custom names for our CircuitBreakers.
* If your app doesnt need any custom names for multiple circuit breakers then you can simply create a default instance under the application.yml and it will be followed by all the FeignCleint.
* Anyways lets create custom names:
* For this we will have to create a bean of type CircuitBreakerNameResolver.
* First go to the accounts.config package .
* Create a configuration class with name FeignCircuitBreakerConfig .
* ![img_67.png](images/img_67.png)
* Now every FeignClient interface that we have inside our accounts app will create circuit breakers as per the above naming convention .
* FeignClientName_functionName .
* Here the FeignClientName would be picked from the name value of the FeignClient annotation.
* Now we will have to make changes in the application.yml:
  * Before: ![img_68.png](images/img_68.png)
  * After: ![img_77.png](images/img_77.png)
  * Remeber whatever is the name inside the FeignClient(name="value") use that to create the circuitbreaker otherwise there might be issues.
  * Ex:
    * ![img_78.png](images/img_78.png)
    * Suppose here inside the FeignClient the name is loans so in application.yml use loans_fetchLoanDetails dont write Loans_fetchLoansDetails otherwise it will create Loans_fetchLoansDetails circutibreaker for the accounts app but it will never handle or montior any requests.
    * The name resolver is the one that creates the name of the circuit breakers and as per that we have to create configs in the application.yml then only it will override the default configs otherwise if name is different event Block letters or captial then also it will create random behaviour.
* Now we will again perform the testing .
  * #### Testing the Circuit Breakers:
    * Again restart all the apps in the debug mode.
    * Postman:
      * Accounts: ![img_74.png](images/img_74.png)
      * Cards: ![img_71.png](images/img_71.png)
      * Loans: ![img_72.png](images/img_72.png)
    * Consolidated API: 
      * ![img_75.png](images/img_75.png)
    * Lets check the actuator of the accounts and see all the circuitbreakers and events too:
    * ``8080/actuator/ciruitbreakers``
      * ![img_76.png](images/img_76.png)
      * See here we have two circuitbreakers as mentioned by us in the application.yml .
    * ``8080/actuator/circuitbreakerevents``
      * ![img_79.png](images/img_79.png)
    * Now lets do one thing stop the loans service and then we will keep hitting the consolidated api and see what happens.
      * ![img_80.png](images/img_80.png)
      * See the fallback worked , it returned null.
      * Lets see the circuitbreaker events:
        * ![img_81.png](images/img_81.png)
      * Now let us keep hitting the api again and again and see the state transition also.
      * ![img_82.png](images/img_82.png)
      * See the transition of the loans_fetchLoanDetails circuitbreaker .
      * After 30 seconds as mentioned by us : ![img_83.png](images/img_83.png)
      * It goes into the half_open state .
      * Well if you stop cards app too then the below is the response:
        * ![img_84.png](images/img_84.png)


### What if instead of stopping the carsd and loans app we add some breakpoints to them:
* ![img_85.png](images/img_85.png)
* Suppose we added a breakpoint here inside this api of loans app and then sent the api all to the consolidated api via gateway.
* Then we get this response:
  * ![img_86.png](images/img_86.png)
* But why we got this fallback message which is actually the fallback message for the accounts route via gateway.
* And if you look at the circuitbreakers of the gateway app then you can see this:
  * ![img_87.png](images/img_87.png)
* Well it happened because we hit the api via gateway now it goes to accounts and inside accounts it calls the loans api too . 
* But since loans api has a break point so it is taking too much of time more than 1sec to respond so the circuit breaker sitting at the gateway for accounts instantly noticed that the accounts is not responding in time .
* So it called the fallback method thereby flaging the request as failed if you keep sending more requests then it will enter the OPEN state too lets do that then.
* ![img_88.png](images/img_88.png)
* See we are in the open State now .
* If you go and check events then you may get the transition info too.
* Another thing is you can also set the circuitbreaker time like how long do you expect the api to get a response , because by default it is 1 sec.

### 4th Commit: "Introduced Circuit Breaker in OpenFeignClients | Used CircuitBreakerNameResolver to create custom Names for CircuitBreakers | Demonstrated Stopping and pausing a Upstream Service"

___


### HTTP timeout Configurations:
* You can specify the time under which you want the response , and beyond which an http timeout error will be thrown and the threads will be released.
* Think of a scenario that you are trying to hit the loans app api's via gateway and for some reason the loans app is taking a long time to be it 1 min or 30 sec or maybe infinite time .
* So what happens in this case is the resources at the gateway like threads will wait infinitely as well as the resouces at the loans app will wait for infinite time , due to which resources will be occupied for a long period of time.
* So to avoid this we have to set a timeout configuration.
* Timeout configuration can be set both in normal api calls and api calls where they have circuit breaker implemented.
* Let's see some examples:
  * Suppose we put a breakpoint in one of the api's of loans microservice:
    * get-contact-info:
      * ![img_89.png](images/img_89.png)
      * ![img_91.png](images/img_91.png)
      * See the response time is just 1.08 seconds
    * Now let's add a breakpoint at:
      * ![img_90.png](images/img_90.png)
    * Now lets hit the same api again and watch what happens:
    * ![img_92.png](images/img_92.png)
    * Now it will be waiting like this unless i lift that breakpoint and this type of event is somewhat similiar to mimicing slow response time or other network issues.
    * ![img_93.png](images/img_93.png)
    * Now look at the response time which is more than 1 min .
    * Do you think users are going to wait this long.
  * Lets check another Api this time:
    * accounts/api/get-contact-info via gateway:
      * ![img_94.png](images/img_94.png)
      * Now lets put a breakpoint at this api inside the accounts app:
        * ![img_95.png](images/img_95.png)
      * Lets again hit the same api:
        * ![img_96.png](images/img_96.png)
        * We got this response immediately and if you look at the response time then it was just 1.05 secs.
        * But why two different behaviours like difference between loans and accounts.
    * It is because of the circuit breaker that we have used in the route of the accounts inside the gateway which immediately executes the fallback method if there is no response under 1 sec.
    * ![img_97.png](images/img_97.png)
    * See here the .
    * So circuit breaker also has a internal default timeout already configed for 1 sec which you can also.
    * But what about the api's where we are not using any ciruit breaker.
    * ![img_98.png](images/img_98.png)
    * If you observer here in the response then TCP handshake is 1ms which is the time taken to establish a connection between Postman and Gateway app then , it took almost 1.05 sec for the response time which is actually the time spent by gateway to send request to accounts and the response time spent by accounts .

* Now inorder to understand more about timeout configs at the gateway server we have to go to their official documentation .
* ``https://docs.spring.io/spring-cloud-gateway/reference/spring-cloud-gateway-server-webflux/http-timeouts-configuration.html``
* If you go to the above route then you will see two types of timeout's one is connect-timeout and another is response-timeout .
* connect-timeout is same as TCP handshake time : which means the time within which the connection between the client and server must be established.
* response-timeout means the time within which the client must receive a response only after connection is made , failing this time would result in response timeout.
* IF you go to the above link of spring cloud gateway timeouts configuration then there you will see many things which i will list below:
  * ![img_99.png](images/img_99.png)
  *     * If you follow the above then it will set a global timeout config for all of the routes that you have mentioned inside the gateway RouteLocator.
  * ![img_100.png](images/img_100.png)
  * ![img_101.png](images/img_101.png)
  *       * IF you follow the above configs then you can define timeout manually as per the route , and this will override the global timeout too.
  * ![img_102.png](images/img_102.png)
  *     * If you follow the above then even if you hv not given any route specific timeout config still it will not use the global timeout config.
* Let's create a global timeout config and test the Loans API and let's check how it works:
  * Open the application.yml of the gatewayServer app.
  * ![img_103.png](images/img_103.png)
  * This the config that we have to use since we are using spring webflux reactive gateway so we just cannot use:
  * spring.cloud.gateway.httpclient.connect-timeout and response-timeout .
  * Rather we have to use the : spring.cloud.gateway.server.webflux.httpclient.connect-timeout and responsetimeout .
  * Now we have set the timeout for connection as 2000 which is 2sec and response timeout as 5s so.
  * Now lets add the break-pointer at the same spot :
  * ![img_104.png](images/img_104.png)
  * Now lets hit the same api again:
    * ``http://localhost:8072/microdemo/loans/api/get-contact-info``
    * Response: ![img_105.png](images/img_105.png)
    * We got the Gateway timeout and the Response time was 5.63 sec so our response-timeout worked it waited for 5 secs then Came with this error at us.
  * What about the connect-timeout:
    * Do one thing just stop the loans app then send the request again:
      * ![img_106.png](images/img_106.png)
      * See this time it waited for 2 secs and since it couldn't connect with the loans app so it came back with this error.
* Let's test the accounts get-info api , first lets put a break pointer there too:
  * ![img_107.png](images/img_107.png)
  * Now lets hit the api via gateway:
  * ![img_108.png](images/img_108.png)
  * Even after putting the global response-timeout as 5s still we got a response under 1.5 sec.
  * It is because we have implemented a circuitbreaker for the accounts route which has a internal default timeout running .
  * And it waits for just 1 sec and also it has a fallback that is the reason instead of error we got a good response.
  * We can create a custom timeout for the circuitbreaker too.
  * ![img_109.png](images/img_109.png)


### 5th Commit: "Http Timeout Configuration | Implementation of timeout "

### Retry Pattern:
* The retry pattern will make configured( settings done by developer ) multiple retry attempts when a service has temporarily failed.
* This pattern is very succesful in scenarios like network disruption or service overload where one service/client may send the request multiple times to a service which is not responding or maybe failing.
* ### Key components of Retry pattern:
  * ``Retry Logic``: Determine how many times to retry an operation . This can be based on factors such as error codes, exceptions, or response status.
  * ``Backoff Strategy``: Define a strategy for delaying retries to avoid overwhelming the system or increasing load on the system. This strategy involves gradually increasing the delay between each retry known as exponential backoff.
    * Ex: Suppose we want to retry 3 times : We will go for the first retry after 1 sec of the actual request , then the second request will be done after 4 sec or maybe some exponential time after the 1st retry and the 3rd retry maybe done after 8 secs or so after the 2nd retry.
    * The goal is to not keep a static time delay between retries.
  * ``Circuit Breaker Integration``: We can combine Circuit Breaker with the retry pattern , like if a certain number of retries fail for any particular request then we can Open the circuit to prevent furthur attempts and then give a Reset time to the other service.
    * Actually incase of using circuit breaker the sequence in which circuit breaker is added to the service call be it in the gateway or the in service call matters a lot.
    * If circuit breaker is used inside the retry pattern then the normal request will be registered inside the circuit breaker window , as well the retries too like 1st , 2nd and 3rd etc.
    * And suppose a new fresh request comes in the for this the normal request will be registerd inside the circuit breaker window and its retries too .
    * Like this the circuit might get OPEN easily.
    * But if the circuit breaker comes first and inside it we have the retry pattern then no matter how many retries is done by the retry pattern only the final response be it failed or successful will be registered inside the circuit breaker.
  * ``Idempotent Operation``: Make sure no matter the number of retries it always produces the same result.
    * Because if the http request contains some kind of operations which might disturb the DB and we are doing it again and again then brother peace out the DB records will get GG.
    * Ex: You can retry GET operations again and again no matter how many times you try it the result will always be the same.
* ``Notes of Retry``: ![Retry 1.png](images/Retry%201.png)

### 6th Commit: "Retry Pattern Introduction"