# API GATEWAY , ROUTING AND CROSS CUTTING CONCERNS 
#### Why do we need a single entry point , why cant we just provide the URL of our services directly to the clients like the FE.
* It is possible but this will be a tightly coupled situation .
* FE will be having the URL of the microservices it wants to talk to , but what if tomorrow the service changes its port or IP .
* In that case we will have to push new codes to the FE as well .
* What if tomorrow we have more instances of the same microservice then what will we do in that case.
  * We will have to give the details of all the instances of the microservice's to the client and also it will be the task of the client to balance the load or evenly distribute the load which is not a good thing.

#### How will we handle cross-cutting concerns like logging , auditing , tracing and most importantly security.
* Well you may say we will introduce all of this for all the apps like we will code all of this for different apps.
* Ok like logging is ok at app level , auditing is also ok but what about tracing it will very diffcult to track which user or which client is sending requests where and where etc.
* Most importantly are you going to introduce different security features for different microservices .
* Suppose the user hits the api for the accounts he/she needs to provide his/her creds to login and do their work.
* Now they hit some other api which responds to loans now again since we have app level security so it will again ask for creds.
* Asking for creds again and again is a very very bad practice , so what are we supposed to do then.
* Imagine you have 1000's of microservices are you going to make cross cutting concerns for all of these 1000's of ms 1000 times .
* We have to enforce all of this things to all of our apps but only at a single place.
* We need a gate keeper who will redirect every thing to the right place.
* Think of a apartment security guard , who has eyes on every incoming and outgoing guest .
* He knows who came in when and went out when .
* He knows whom to allow inside the society whom to not.
* He knows who has access till the parking and who has access inside the apartments .
* He knows who lives where just need to tell him the name and he will send you to the right house no , which can be thought of as IP Address for block No and house no for port.

#### How do we route based on custom requirements ?
* How to provide dynamic routing capabilities which allows to define routing rules based on various criteria's such as HTTP headers, request , parameters etc , inside microservices network .


#### Well all of the above challenges can be solved using EDGE Server or API GATEWAY a common entry point.

___

#### In the Early traditional applications clients used to hit the microservices api directly which is not a good thing in todays era.
* Because if we keep hitting the api's of different services directly again and again then all of our microservices will have to implement so many things like some repeated set of codes.
* They will have to individually perform security checks again and again.
* Suppose you have a website where there are many clickable icons ,
  * Suppose you clicked icon A which will directly redirect you to the microservice A Api but now it will ask you for authentication and authorization.
  * Suppose you gave the creds and now you have entered into the api and u r happily doing your task.
  * Now once your task is done , you came back to the main page and now you clicked on icon B which takes you to microservice B.
  * Now B will ask you for authentication and authorization since B will have it's own set of security context .
  * You will again provide login creds here .
  * So this is the issue you didnt had any common point where you could just give details once and then the same details will be used across all the services.


#### Traditional:
* ![img.png](images/img.png)
* Directly hitting the microservice api's.

#### Modern Architect:
* ![img_1.png](images/img_1.png)
* In this approach all the clients they have to go through one single point or you can say a gate rather than direclty hitting the microservices.
* Advantage of this approach:
  * We can handle so many cross cutting concerns at this stage.
  * We can make our downstream services resilient and fault-tolerant.
  * At this stage we can manage the no of retries , timeout etc of calling an api.
  * Authorization and Authentication can be handled here at this single point.
  * We can enforce some quota policies here like the no of times a particular client can make request within some time frame  or the api's which or contents which they have access to and which have no access to.
  * We can have custom exception handling at this layer.
  * We can perform logging and monitoring and send the info to other services using this layer.
  * We can use REDIS Cache here.
* You may ask why cant we use our eureka server here.
* It is because the purpose of eureka server is to discover services and help services to find each other .
* It cannot perform cross-cutting functionalities like logging , auditing , tracing , security checks etc.
* From 6 mins lect 122

#### Spring Cloud Gateway:
* All the functionalities that i have explained above can be acheived by using the Spring Cloud Gateway .
* It is very easy to set up and it can handle the workload seamlessly while maintaining optimal scalability .
* We can use it to create production grade gateway server.
* Since the gateway sits at the edge of the servers so very heavy traffic is expected here .
* And the spring team has developed this in such a way that it uses very less memory and works very efficiently even in heavy traffic conditions.

### Key Aspects of Spring CLoud Gateway:
* The service gateway sits like the gatekeeper for all the incoming traffic to microservices calls.
* With a service gateway in place our service clients would never directly call the URL of the individual microservices but instead make a call to the service gateway.
* Spring cloud gateway is a library for building an API Gateway so it looks like any other spring boot application .
* Any person familiar with spring boot coding can easily start a gateway server with just few lines of codes and some configs.
* Spring cloud gateway supports dynamic routing .
* Routing based on context.
* Suppose in the header of a request if there is some value indicating to a particular version of an api then we can redirect the request there.
* The gateway can keep a track of the session of each and every user (Tracing).
* It can also help in creating sticky sessions which means a particular users request will always be forwarded to a particular instance of a microservice.
* Well there are other API Gateway available in the market like ZUUL and others too .
* But Spring Cloud Gateway is mostly preffered because spring cloud gateway build on spring reactor and spring webflux provides circuit breaker integration , service discovery with eureka , non-blocking in nature has superior performance compared to that of ZUUL

#### The service gateway sits between all calls from the client to the individual services and acts as a central Policy Enforcement Point (PEP) .
* Routing (Both static and Dynamic)
* Security ( Authentication and Authorization )
* Logging , Auditing and Metrics collection .
* ![img_2.png](images/img_2.png)
* TO learn more : https://spring.io/projects/spring-cloud-gateway


### Internal Architecture of API Gateway:
* ![ChatGPT Image May 23, 2026, 11_17_52 AM.png](images/ChatGPT%20Image%20May%2023%2C%202026%2C%2011_17_52%20AM.png)
* Well the above images shows us how the HTTP Requests flow inside a gateway server.
* ![GATEWAY Architect.png](images/GATEWAY%20Architect.png)
* Now the above image tells us about the internal architect of a Spring CLoud Gateway server.
* Let me explain each part:
  * While creating a gateway it is the developer who has to mention certain things in the configs like ,
  * The dev has the mention some predicates and filters .
  * What are these predicates and filters ?
  * Well the task of a gateway is to route the incoming http request to the proper instance of a microservice right.
  * So we have to mention routes inside the gateway server .
  * like for example suppose we have 5-10 microservices hosted at different servers lets just focus on microservices and not instances.
  * So we have to mention the details of all of these microservices right , inside gateway we never mention any hardcoded IP Address.
  * It is the task of the gateway to retrive the IP Address from the Service Registry for any of the particular service on the basis of the URL received.
  * Whenever any client sends a reqeust to the gateway server they will definitely mention the path .
  * like in their URL they definitely going to have the path of the API.
  * Suppose our gateway is hosted at localhost:8076 , Now suppose a client wants to hit the /api/fetch of the accounts microservice so for them they will use the below URL.
  * URL : localhost:8076/accounts/api/fetch.
  * Now what it does is the request will go to the gateway at localhost:8076 now from there, there are gateway handlers who are going to process this incoming http requests.
  * Now they will try to match the request which is /accounts/api/fetch with the pre-written lists of URL's which are coded by the developer inside the gateway.
  * Now this matching where the url of the http request is being handled by the gateway handlers and these gateway handlers are comparing the url with the list that they have this is know as conditional checking in gateway language.
  * This conditional checking that is being done is knows as ``predicates matching``.
  * And Predicates are basically the conditions which are written by the developer inside the gateway.
  * It is a set of conditions for a incoming HTTP request , on the basis of which a incoming http request is identifed and furthur processing is done and finally the request is sent to the right microservice address.
  * ### Predicates:
    * A list of conditions written by the developer for every specific route that they know.
    * A HTTP request will only be processed furthur by for a specific route only if it satisfies all the conditions for that route , if it doesnt satisfy then the http request will get compared to other routes mentioned.
    * Suppose we have 3 microservices accounts , loans and cards.
    * so inside the gateway configurations you will see that the developer has written 3 different predicates for 3 microservices.
    * Have a look at the picture:
    * ![Configs.png](images/Configs.png)
    * Here if you see to the left top then this is how a developer writes the routing information.
    * id: which basically means the internal name given to a route.
    * uri: It's task is to get the actual IP address for a microservice.
    * predicates: Conditions.
    * filters: Acutually filters are of two types 
      * pre and post filters.
      * well here the filters that you are seeing in the image their task is to modify the http request before sending it to the actual IP.
    * See at point 6 how the http request flows
    * Inside predicates first the path is matched then remaning predicates are matched.
    * There can be many predicates for a particular request in our image you might be seeing only 2 for every route id.
    * See at point 7 there are a list of common predicates which are used.
    * Now you have little bite idea about predicates , lets move ahead and understand more about filters.
  *  ### Filters:
    * Filters are of two types one is pre-filter and other one is post-filter.
    * Pre-filter modifies the http request before sending the request to the correct microservice instance.
    * Post-filter modifies the response that it has got from the microservice , before sending the response to the client.
    * But we write all the filters inside the filter tag only it is by the name of the filter that they get differentiated like which one is post and which one os pre.
    * ![filters.png](images/filters.png)
    * This is how the filters look .
    * ![A Pre and Post filter.png](images/A%20Pre%20and%20Post%20filter.png)
    * In the above image we have mentioned both pre and post filter , they are not written explicitly.
  * ### URI:
    * Once the request passes successfully through all the pre-filters now the gatway handlers will go to the URI Part and get the information about all the instances of the microservice.
    * Like suppose URI says lb://ACCOUNTS , it means now the Gateway server will contact the service discovery and seek information about all the instances of ACCOUNTS and when the service discovery returns all the instances of the ACCOUNTS.
    * It will now implement the loadBalancing using some algorithm from its end and finally send the request to some instance of that microservice .
    * This is how load balancing is also done by the gateway server.
  * ### POST Filters:
    * As you have seen above under the filters section we have mentioned how the post filters look.
    * Now once we get the response back from the microservice it will be modified on the basis of the post filters that we are using .
    * Then finally after modification the response will be send by the gateway server to the client.
  * ``This is the overall flow of the requests inside the gateway server.``
___

### Overall Architect :
* ![GATEWAY Architect.png](images/GATEWAY%20Architect.png)
* Here you can see we have a NETTY Server running inside the GATEWAY .
* In normal springboot apps we have tomcat server running , most of the springboot apps have spring mvc which has the embedded tomcat.
* But there is a issue with tomcat server that is it's threads are blocking in nature they will not do anything else unless they get a reponse or you can say they keep waiting till they get a reponse whenever they execute some requests.
* And this is blocking in nature and it makes them slow.
* In API GATEWAY we need fast processing because it is the single entry point for millions of requests per second maybe and here if the threads are getting blocked then our requests will be resolved very very slowly .
* It will take a lot of time .
* So for this reason we use the NETTY server inside the spring gateway which has very threads and these threads are know as eventloop threads , but they are very fast compared to traditional threads.
* These threads are non blocking in nature , like they handle all the requests inside the gateway and once they send the request to the microservice they dont wait of the response in return they tell the request that once it is processed and the respone is sent by the microservice they will again start working on it but in the mean while they focus on other incoming requests .
* They work like a waiter of a restaurant like they take the order/request and process it and then they give it to the kitchen/microservice and then they say hey once the order is ready just call me (callback) and in the meanwhile they go and take other orders/requests.
* NETTY server is build inside the Spring WebFlux , it is reactive async and non blocking in nature.
  * ### NETTY Server VS Tomcat Server
    * ![A NETTY vs TOmact.png](images/A%20NETTY%20vs%20TOmact.png)
* TO understand more about predicates , filters etc go to the official documentation of spring cloud gateway.


### Lets start creating the Gateway Server:
* First lets copy all the projects and contents of the section 8 folder to this section 9 folder.
* Now lets create a project of the gateway server at start.spring.io .
* ![img_8.png](images/img_8.png)
* The above are the dependencies that we need to add.
* We added eureka discovery client so that our gateway sever has idea of where the service discovery is registered .
* Config client is added as we need to fetch the configs for the gateway using the configserver.
* Lets also add the google jib plugin into the pom.xml of gateway server so that it would be easy for us to generate the image.
* Lets start editing the application.yml of the gateway server.
* ![img_4.png](images/img_4.png)
* These are some of the basic properties for our api gateway , the main one here is the config.import as we will be importing rest of the properties from the github.
* We have enabled all the management related endpoints as well as some gateway related endpoints.
* We have not written here profiles:active default/qa/prod as for our project we will be having only one yml file for the configserver in the github.
* But in real projects teams are going to have different different profiles.
* ![img_5.png](images/img_5.png)
* As can be seen we have given it the port 8072 and then we have also given it instructions to register itself with the service discovery .
* So that it can fetch all the information from the service discovery.
* Now it will act just like any other microservice like it will get registered in the service discovery but will it forward the incoming external clients requests to the desired microservices ?
* No it will not.
* We still need to provide some more information and for that we need to provide some information in the gatewayserver's applcation.yml.
* With the above statements we are telling to the gateway to connect with the discovery server/service and locate all the details about the individual microservices , Once it locates all the details now it will forward all the external requests from the client to the individual microservices.
* ![img_9.png](images/img_9.png)
* This property actually takes up all the services registered with eureka and automatically creates routes to them without us manually routing.
  * Suppose we have accounts ms instances in eureka server so it will take all the instances and create routes on its own.
  * Now if a user hits : http://localhost:8072/ACCOUNTS/**
  * It will directly forward the request to the IP of the accounts service , so we dont have to manually do the routing .
  * We will see all the routes , when we start the gateway server and the service discovery and register some services with the eureka server.
  * Then upon hitting actuator/gateway/routes we will see all the routes that it has automatically routed.
  * As we have exposed the management:endpoint:gateway:enabled true .
  * ![Auto Routing.png](images/Auto%20Routing.png)

## Validating the Gateway:
* Now as we have created our gateway now it is time for us to check whether the gateway is working or not.
* So for this lets starts our app in the given sequence: ``configserver->EurekaServer->All 3 microservices -> Gateway Server``
* ![img_7.png](images/img_7.png)
* See as soon as we started the apps in the proper sequence this is what is being shown inside the Eureka server .
* Now lets check the ``/actuator`` url of the gateway server. And observer what are the management endpoints it is exposing .
* But before that let me show you one very important thing:
  * Whenever you start any springboot application it says Tomcat started on port and then port info.
  * for example: ![img_10.png](images/img_10.png)
  * But Now when we have started the gateway server which has the reactive gateway dependency .
  * ![img_11.png](images/img_11.png)
  * See here we have the Netty server that is working here instead apache tomcat server.
* So as soon as we hit the gatewayIP/actuator so we are getting a list of URL endpoints exposed by the actuator.
* ![img_12.png](images/img_12.png)
* Now if you go to actuator/gateway then again we see lots of urls.
* ![img_13.png](images/img_13.png)
* Now lets go to ``actuator/gateway/routes``
* ![img_14.png](images/img_14.png)
* ![img_15.png](images/img_15.png)
* See how it is generating routes for all the services that it found inside the eureka service registry.
* We are not even manually routing anything still it making routes for all of them.
* It is performing some default predicates and filtering too for each of the routes.
* In predicates it is trying to match the incoming http request .
* filters: it is removing the first word of every http which is after localhost:8072.
* for example for us to send requests via gateway server we have to use.
* ``http://localhost:8072/ACCOUNTS/api/fetch?mobileNumber=9876543210``
* When this http request is recieved by the gateway it matches the predicates , like the actual matching starts from,
* /ACCOUNTS/api/fetch?mobileNumber=9876543210 .
* So the filter before sending the request to the ACCOUNTS will remove this ACCOUNTS from the URL and then send the api/fetch?mobileNumber=9876543210 to the IP of Actual ACCOUNTS microservice.
* uri: "lb://ACCOUNTS" this is what the gateway uses to get the info of all the instances of ACCOUNTS from the service registry.
* ### Now lets validate if the microservices can be accessed using gateway or not:
  * Lets try with the ACCOUNTS microservice.
  * Open postman and hit the URL ``http://localhost:8072/ACCOUNTS/api/create``
    * with a json body : ![img_17.png](images/img_17.png)
    * Method should be POST:
    * ![img_16.png](images/img_16.png)
    * See we got a success response .
    * Lets validate this using the fetch api of the ACCOUNTS using gateway and then we will use the fetch via accounts URL only and also check in the H2.
      * ``localhost:8072/ACCOUNTS/api/fetch?mobileNumber=9876543210``: ![img_18.png](images/img_18.png)
      * ``localhost:8080/api/fetch?mobileNumber=9876543210``: ![img_19.png](images/img_19.png)
      * H2 of Accounts: ![img_20.png](images/img_20.png)
    * SO it is now validated that everything is working as expected .

### 1st Commit: In the upcoming lectures we will learn more about manual routing .
### Commit Message: "Gateway Server | Explained the Internal Architect of Spring CLoud Gateway | Netty Server| Demonstrated creation and working of Gateway Server"

### Accepting Service names with lowercase letters:
* Till now whatever URL we are trying to access we are using block letters for the service name like:
* ``localhost:8072/ACCOUNTS/api/fetch?mobileNumber=9876543210``
*  ``localhost:8072/LOANS/api/fetch?mobileNumber=9876543210``
*  ``localhost:8072/CARDS/api/fetch?mobileNumber=9876543210``
* This looks really bad and do you want your client applications to send the request to a URL like this .
* I mean this is the ideal behaviour of the gateway server but we can overcome this .
* By using a property inside the application.yml that is : lower-case-service-id
* ![img_21.png](images/img_21.png)
* Now try to hit the URL with both Uppercase as well as lower case .
  * UpperCase: ![img_22.png](images/img_22.png)
  * lowerCase: ![img_23.png](images/img_23.png)
  * See how the lowercase is working fine.
* Lets check the /actuator/gateway/routes to see how it forms the URL for incoming lowercase http requests:
* ![img_24.png](images/img_24.png) 
* See how the lowercase letter service name are showing up here.

### 2nd Commit: In the Upcoming lectures we will learn more about manual routing.
### Commit Message: "Made changes in the application.yml to accept lowercase letters in the URL"

## Manually Routing the requests:
* There are two ways in which we can manually route the incoming http requests.
* One of them is writing the configs inside the application.yml which you might have seen in most of the images attached above in this lecture.
* The 2nd one is creating bean of RouteLocator and giving all the required information inside it.
* The 2nd one is used widely by developers as it gives more flexibility to introduce more custom predicates and filters.
  * ### Steps to create Routing inside the bean of RouteLocator:
    * First create a folder config.
    * Inside the config folder create a class GatewayConfig: or whatever name you want.
    * Now give it the annotation of @Configuration and then create a function whose return type is going to be RouterLocator.
    * ![img_25.png](images/img_25.png)
    * Now lets create some routes inside the function:
    * ![img_26.png](images/img_26.png)
    * See the above image this is how we create different routes , the fashion in which we are writing the code here looks pretty similiar to create custom security configs.
    * Now let me explain each and every line :
      * ![A Custom Routing.png](images/A%20Custom%20Routing.png)
      * The details about each and every line has been explained clearly.
      * ![img_27.png](images/img_27.png)
      * Here the route->route.path is not same as the .route() above because inside we are using a lambda function and we can use r or anything else instead of that route->route.path.
      * Lets see how are our URL's getting exposed in the actuator/gateway/routes .
      * ![img_28.png](images/img_28.png)
      * ![img_29.png](images/img_29.png)
      * This is how the /gateway/routes looks now but if you observer one thing then even after mentioning our custom route still we are getting the auto configured routes by gateway server.
      * for example:
        * ![img_30.png](images/img_30.png)
        * Even if a client wants then they can still hit the localhost:8072/accounts/** and still they would get redirected to the actual instance of ACCOUNTS.
      * It is happening because we have set a property true that is :
        * ![img_31.png](images/img_31.png)
        * Change the value here from true to false then again run the edge server.
        * ![img_32.png](images/img_32.png)
      * Now lets hit localhost:8072/gateway/routes :
        * ![img_33.png](images/img_33.png)
        * See no more autoconfigured routes.



### 3rd Commit: In the Upcoming lectures we will see how we can have some custom filters added.
### Commit Message: "Created Custom Routes using RouteLocator"

### Adding Response Header filter to the route:
* For this just go through the official documentation and look for adding response header filter.
* ![img_34.png](images/img_34.png)
* Well this is the yml config one .
* But we need the java dsl one right .
* So for that lets work on our current code.
* ![img_35.png](images/img_35.png)
* Well this is how you add multiple filters like 
* ``.filters(f->f.firstfilter().secondfilter().thirdfilter().andso on)``
* We have used the AddResponseHeader filter here to send a header value in the http response like we are going to send the Current LocalTime.
* Replicate the same to other routes too.
* Now start the edge server and use postman to send some request and then observe the incoming request .
  * ![img_36.png](images/img_36.png)
  * ![img_37.png](images/img_37.png)
  * In response select headers and see the added filter that we are getting as a response.

### 4th Commit: In the Upcoming lectures we will see how we can have some custom filters added.
### Commit Message: "Demonstrated how to add a response header filter"


### Implement some cross cutting concerns :
* Lets try to implement some cross cutting concers which are very useful.
* If you go to chatgpt or any other AI and ask what are the custom cross cutting concerns which we can add to our server gateway then it will give you a list along with why you need to do that.
* One of the cross cutting concerns is logging and tracing .
* It is very important because we can analyze how the requests are getting transfered from one service to another service and we can also see the time like how long is a server taking to send the request ahead or maybe forward.
* And inorder to trace any http request let us use the CorrelationID , bascially it is defined by us only but the term we use to call it is correlationID.
* It is either the task of the FrontEnd client to add the CorrelationID which will be carried along with request to all the places wherever the request goes or simply it can be added by the BE Gateway also.
* It's simple task is to trace the request only like suppose Client sends a request it goes to the gateway now at the gateway we check whether this request has a header correlationID or not if not then we simply add it to the request and then the gateway forwards it to wherever it want the request to go ,
* Now suppose gateway forwards the request to accounts-ms we can see a request recieved by the accounts-ms in its log with the same correlationID then suppose accounts-ms sends it to the loans-ms there also we can see the correlationID and then finally after much processing the request gets back to gateway and from there it goes to the client again there at client we can see the same correlationID so this is how we do tracing and this is how we can do the analysis.
* If you want to know how to add this global filter , it is a global filter as we are going to add this filter to all the incoming and outgoing requests.
* Go to chatgpt and ask how can i manually add global Filter like correlationID to all the incoming requests and outgoing response's in my spring cloud reactive gateway server.
* One more important thing is using this correlationID we can actually debug the code like where the exception is happening like who is recieving the request and who is not.
* Lets start creating the filters:
  * First create package by the name filters.
  * ![img_38.png](images/img_38.png)
  * We will see today 3 ways in which we can have global filters.
  * Lets first create a utility class , this class object will be used by our custom defined globalfilter , I mean it will act as a helper class only.
  * FilterUtility:
    * ![img_39.png](images/img_39.png)
    * As you can see the attached image above , It has 3 funtions which are publicly accessible.
    * The first function get coorelation_ID:
      * ![img_40.png](images/img_40.png)
      * The purpose of this function is to take a parameter of type HttpHeaders and using that it will look for all the headers present in that list.
      * It will search for a header who's name is microdemo-correlation-id if it is present then it will put all it's values into a list of strings.
      * Like the microdemo-correlation-id Header can have mutliple values so it will do that and it will only consider the first value of the list.
      * And then it will return the first value to the function caller.
      * Incase there is no such Header then it will return NULL to the caller.
      * Here we are using the string variable CORRELATION_ID inorder to carry the string microdemo-correlation-id.
      * 
    * Now we have two other functions which are dependent on each other.
      * ![img_41.png](images/img_41.png)
      * If you see here then bottom function setCorrelationId is called first by any other class function and the function carries some values like the correlationID and the exchange.
      * Basically who ever calls this function has to pass 2 things:
        * ServerWebExchange object : This object is responsible to carry the details of any incoming requests to the gateway server.
        * String object: It will be carrying a random system generated code which will used to initialize the value of the correlation_id.
        * Now once this function is called it will return a value which is returned by the function that it is calling and the function it is calling is the setRequestHeader.
      * ![img_41.png](images/img_41.png)
      * Now the setCorrelationID calls the setRequestHeader function by passing 3 things first one is the ServerWebExchange object .
      * 2nd is the String variable CORRELATION_ID which carries value "microdemo-correlation-id".
      * 3rd one is the random generated code.
      * Now in the setRequestHeader function the ServerWebExchange object is mutated which means modified , 
      * ![img_42.png](images/img_42.png) it is mutated and a new object of type ServerWebExchange is generated and returned back to the caller.
      * The caller is the setCorrelationID and then the function setCorrelationID returns the newly generated object to its caller.
      * Remeber one thing here we are not at all manipulating the actual incoming http request we are passing its copy and working on it and then mutating it without affecting the original one.
      * Once the new object is created it will replace the original one , which we will see ahead.
      * ![Mutating_ServerWebExchangeRequests.png](images/Mutating_ServerWebExchangeRequests.png)
      * See the above image for more understanding.
    * We have created the utility class now lets actually create the GlobalFilter for adding the correlation_ID.
    * RequestTraceFilter class: 
      * This class is reponsible to create a bean that implements the GlobalFilter which will add a correlationID to the incoming http requests based on some conditions.
      * ![img_43.png](images/img_43.png)
      * Like if you see here in the above image then, it is injecting the utility class bean as it is dependent on it.
      * Now the function public Mono<Void> Filter it's declaration is there in the GlobalFilter interface.
      * It uses two parameters one is the ServerWebExchange which captures the incoming web request and other one is the GatewayFilterChain .
      * ServerWebExchange handles the incoming http request while GatewayFilterChain object actually helps in passing the control or execution to the next filter.
      * Bascially here in the RequestTraceFilter class inside that Mono<Void> filter function we are performing some filterations on the incoming request now after all the tasks are performed, Now it is time for the same function to send the control or execution to the next filter.
      * That is why we are using return chain.filter(exchange) . If you dont use this at the end then our request wont move ahead.
      * We are not using the GlobalFilterChain object to add a filter rather it is used to move to the next default/user-defined filter.
      * When we are implementing the GlobalFilter interface that is the moment we are telling the gateway server that this class has some filters and the Order(1) annotation says that in the list of GlobalFilters please make this class as the first filter through which the code will travel.
      * Explaining the code: 
        * First the http request comes to the exchange and from there we are taking out all the http headers using : HttpHeaders requestHeaders = exchange.getRequest().getHeaders();
        * exchange.getRequest() : This gets the current http request.
        * exchange.getRequest().getHeaders() : This brings out all the headers and assigns them to the HttpHeaders requestHeaders object.
        * ![img_44.png](images/img_44.png)
        * Now the if part is executed to check whether it has the correlation_ID in it already or not.
        * ![img_45.png](images/img_45.png)
        * For that the above function is invoked.
        * And the above function calls the getCorrelationID function of the FilterUtility class.
        * ![img_46.png](images/img_46.png)
        * The above one.
        * If there is not correlationID then it returns NULL otherwise it logs the correlationID if present.
        * Then the below part of the RequestTraceFilter class is executed if it gets NULL in response:
        * ![img_47.png](images/img_47.png)
        * The above part , like creation of CORRELATIONID and assigning it to the request.
        * First a random string is generated then the same random string is passed as a argument to the setCorrelationId function of the filterUtility.
        * And as we have already mentioned what the function does still let me show it again.
        * ![img_48.png](images/img_48.png)
        * The setCorrelationID function calls the setRequestHeader function where first a new ServerWebExchange object is created with the new header that is microdemo-correlation-id along with the existing details of the exchange.
        * Once the new ServerWebExhange object is created it is assigned to the original ServerWebExchange object and then it simply returns to the caller.
        * Since the ServerWebExchange object is immutable in nature so we cannot directly change it but we can copy it and create new manipulated copies of it and then assign it to the original one.
        * Now once the request is mutated.
        * ![img_49.png](images/img_49.png) 
        * See here exchange = filterUtility.setCorrelationId(exchange,correlationID) , how the original ServerWebExchange object is getting assinged with the new one.
        * Then the logger is getting executed.
        * finally return chain.filter(exchange) : This tells the gatewayserver that now i have done all the work inside this filter now you can take the request ahead and forward it to other filters thats it.
    * ### Image Demostration of the Mono<Void> filter function:
      * ![MonoVoid.png](images/MonoVoid.png)
      * The Gateway uses this kind of coding since it is build using reactive framework and not the usual spring framework that's why you will see a lot of Mono<Void> , Mono<String> , Mono<T> etc.
      * Mono bascially means not now but maybe later in the future you are expected to hv a return type of <>,
      * Mono<String> name = Mono.just("Ashish").
      * ![img_50.png](images/img_50.png)
  * ### Adding the CorrelationID in the ReponseHeader:
    * Well you may ask me that we just added the correlationID in the requestHeader then why we need it in the ResponseHeader, Like can the client not see the correlationID from the requestHeader ?
    * Well RequestHeader and ResponseHeader are two different things.
    * When the client sends a http in that request we add the request header.
    * And when the same request is processed and the client is about to recieve the response so we add the responseHeader.
    * Lets create a class that will add the ReponseHeader.
    * ![img_51.png](images/img_51.png)
    * See the above image this is how we can crate a responseHeader filter , there are other easier ways too to create the same.
    * Code Explanation:
      * Configuration Annotation means this class has beans .
      * Then we have the bean annotation on top of a function which returns a bean of type GlobalFilter .
      * In the return statement we are using the lambda expression .
      * where just like RequestTraceFilter class here also we are taking two parameters .
      * ServerWebExchange obj and GlobalFilterChain object.
      * ![img_52.png](images/img_52.png)
      * return chain.filter(exchange).then this is important as we had also seen chain.filter(exchange) in the return statement of the RequestTraceFilter class too.
      * .then bascially tells the gateway server to only execute the current statements after all the processing is done and after all the response is finally recieved and the response is ready to be directed back to the client.
      * if you dont use .then , then instead of properly adding the response header it might actually add it like a normal reponseHeader and expect that right now this is not a response but a request which will be forwarded to the next filter .
      * So please add .then .
      * Now after this what we are doing is from the current exhange request we are looking for all the headers , specifically the requestheaders and looking for the correlationID header.
      * Once we get that from there we are going to use the same correlationID value in the reponseHeader too and then finally the response will be sent back to the client.
      * Since all the requests go through our order(1) RequestTraceFilter so all of them have the CorrelationID so from requestHeaders we can get the correlationId and use it in the ResponseHeader that's it.
      * exchange.getResponse().getHeaders().add(FilterUtility.CORRELATION_ID, correlationId) : This part is actually adding the response header.
      * Code Explanation in better way:
        * ![Better way of ResponseTraceFilter.png](images/Better%20way%20of%20ResponseTraceFilter.png)
      
### Why do we need both the filters :
* ![Both RequestFilter and ResponseFilter.png](images/Both%20RequestFilter%20and%20ResponseFilter.png)

___


### 5th Commit: Making Code Changes inside the Microservices :
### Commit Message: "Created two custom GlobalFilter | RequestTraceFilter| ResponseTraceFilter | Explained their codes and their working"

### Making Changes in at the microservice level to print the CORRELATIONID Header:
* If you remember we had a Controller/API in the Accounts microservice which returned us the consolidated information of all the other microservices.
* ![img_53.png](images/img_53.png)
* The above API in the CustomerController.java class .
* Now in the same API lets include a parameter in the parameter list which will capture the Header from the http request.
* ![img_54.png](images/img_54.png)
* See above we have added the new parameter . 
* We added that because when gateway server sends the request to the microservice it sends a http request only and inside spring web mvc to capture the Header we have to use the @RequestHeader annotation.
* Now lets track which are the functions it calls and accordingly we will make changes.
* ![img_55.png](images/img_55.png)
* It brings us to the CustomerServiceImpl.java class where the actual processing takes place.
* Here we will do multiple things like change the parameter list of the fetchCustomerDetails function and add the header received at the controller layer so that we can print that here.
* ![img_56.png](images/img_56.png) 
* First i made changes at the caller function or controller function it is showing error as in the fetchCustomerDetails function's declaration only 1 parameter is there so we need to modify the parameter of this function too.
* ![img_57.png](images/img_57.png)
* I added it here in the parameter list still it is showing error as in the interface we dont have the function looking like this.
* ![img_58.png](images/img_58.png)
* See here we only have 1 parameter so lets add one more.
* ![img_59.png](images/img_59.png)
* Now the error is no more there.
* Let me first add the Logger to the Controller class CustomerController.java.
* ![img_61.png](images/img_61.png)
* Now lets print the debug statement:
* ![img_62.png](images/img_62.png)
* Now since we wanted the same correlationId to be there in other microservices so for that we had also changed the method signature of the fetchCustomerDetails function because this is the function inside which we are making the calls to other microservices.
* ![img_63.png](images/img_63.png)
* Here along with the mobile Number we have to send the correlationId too.
* ![img_64.png](images/img_64.png)
* See as soon as we added the correlationID string it shows error , so lets make changes in the feignclient interface.
* ![img_65.png](images/img_65.png)
* At this point we are just sending the mobile Number to the fetchLoansDetails function of the Loans Microservice .
* But now we will send the correlationID not as a normal String variable but as a RequestHeader so for this.
* ![img_66.png](images/img_66.png)
* Do the same thing in the other feignclient too , the cards microservice one.
* ![img_67.png](images/img_67.png)
* Now inorder for the respective api's of the Loans and Cards microservice to extract the Header details and print it , we have to make changes in the actual microservices too.
* ### You may ask me why do we have to send the correlationId again to the other microservices that too as RequestHeader.
  * It is because from gateway the correlationId is set and is sent in the http request to the first microservice and the first microservice extracts it .
  * Now when the first microservice makes calls to other microservices i uses new http requests which are created via the feign client and the feign client uses the service discovery not the gateway server.
  * So new http request won't be having the same Exchange request correlationId so it is us who has to set the correlationId and the ID passess to other microservices like that.
  * And when we are using @RequestHeader even while calling the function of other microservice's so in the outgoing http request a new header is added with name given by us and value sent by us.
* Now lets make changes in the Loans microservice:
* ![img_68.png](images/img_68.png)
* Made changes in the fetchLoanDetails function of the LoansController as well as add the logger in the class too.
* ![img_69.png](images/img_69.png)
* Now make the same type of changes to the fetchCard function of the CardsController class of the Cards microservice.
* ![img_70.png](images/img_70.png)
* See we have made the similiar changes here too.
* Also make sure you have enabled the logging in the application.yml of the different microservices.
* Now lets start the apps one by one in sequence : configserver -> Service Discovery -> Loans -> Cards -> Accounts -> GatewayServer :
* Now once all of the apps are live :
  * Try to create some records using the api's and then finally we will hit the consolidated api which is the fetchCustomerDetails api and read the logs of the respective apps to check the correlationId.
  * 1st : /api/create of the AccountsController of the Accounts microservice using the Ip of the gateway or we can use the normal IP of the accounts app , but while fetching hit the microservice via gateway otherwise no correlationId.
    * ![img_71.png](images/img_71.png)
    * See the URL properly .
    * Now using the same mobile Number create records inside the cards and loans microservice : 
  * 2nd: microdemo/loans/create :
    * ![img_72.png](images/img_72.png)
  * 3rd: microdemo/cards/create:
    * ![img_73.png](images/img_73.png)
  * Now since all the records are created , Now hit the microdemo/accounts/api/fetchCustomerDetails:
    * ![img_74.png](images/img_74.png)
    * Hit the above api and then check the logs of all the microservices: 
        * GatewayServer Logs: ![img_78.png](images/img_78.png)
        * Accounts Logs: ![img_75.png](images/img_75.png)
        * Loans Logs: ![img_76.png](images/img_76.png)
        * Cards Logs: ![img_77.png](images/img_77.png)
        * Postman Response Headers: ![img_79.png](images/img_79.png)
      * See all the correlationId values are same and matching .
      * The gateway server first adds the correlationId and then in the response also it adds it back .

### 6th Commit: In the Next lecture we will learn about different types of Gateway Server Design Patterns :
### Commit Message: "Made code changes in the microservices | Demonstrated how correlationID is generated and how it is carried forward in request header as well as response Header | Made changes to the functions parameter lists | Made changes at feignclient level too"
