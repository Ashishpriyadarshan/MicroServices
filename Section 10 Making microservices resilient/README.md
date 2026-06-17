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

