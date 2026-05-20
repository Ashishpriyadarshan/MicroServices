# Service Discovery and Service Registry:

* In real world production grade applications there is the FE , BE , DB and many other things.
* Suppose there are multiple clients c1 , c2 , c3 etc . These clients never directly hit the BE API's or the location where the BE App is hosted .
* Their request always goes through a common entry point that is the API GATEWAY .
* API GATEWAY is a common app or gate whose responsibility is to redirect the traffic , enter the logs , perfom security checks , performs auditing etc .
* So we never directly hit the BE Apps.
* Now suppose we have 3 microservices do you know how are they going to talk to each other internally .
* Suppose let all our microservices are hosted in the same network or maybe not in the same network and they are inter dependent on each other like suppose , accounts microservice has a api which sends some info to the cards as well as loans microservice .
* So how do you think they are going to communicate .
* Any one would say they will communicate by hardcoding each others IP and port and send the request directly there .
* That is fine but what if for some reason their IP's and ports change , then in that case we have to make changes at so many levels.
* or suppose we need to scale our app suppose we scale loans to 5 instances so we have to now provided all the 5 instances details to accounts microservice and set some load balancing algo so that the traffic is eqaully distributed , but do you observe the amount of manual work we have to do here.
* So in this section we will learn how can we avoid this manual task and help microservices talk to each other without hardcoding each other's details.

___

## How microservices discover other microservices and how they register themselves too so that they are also recognized by others:
* 1:
  * So each microservice will be having their own host and port info and they will be exposing some remote API .
  * How will other microservices know about this dynamic endpoint URLs to invoke them.
  * Since our microservices might be scaling , downgrading or maybe destryoing their containers , restarting them etc.
* 2:
  * Suppose if a microservice instance fails , new instances will be brought online to ensure constant availability .
  * This means that the IP addresses of the instances can be constantly changing.
  * So how does this new instances start serving to their respective cleints .
  * Suppose accounts needs to talk to the loans microservice but 1 instance of loans was shut down and 5 new instances are now live so how will these instances start serving to accounts since accounts only knew details of 1 instance and that IP is now not in use by any.
* 3:
  * How do we make sure that there is proper load balancing between multiple instances of the microservice .
  * Suppose accounts microservice invokes loans microservice and loans has like 5 instances and accounts keeps sending all the load or request to only 1 instance then that is not proper load balancing right.
  * I mean 1 is heavily occuiped while rest 4 are enjoying free time and thats not a very ideal condition.

### To address the above challenges we have some solutions :
* Service Discovery.
* Service Registration.
* Load Balancing.

## How service communication happens in Traditional apps:
* In a web network when a service wants to communicate with another service/app, it must be given the necessary information to locate the app such as it's IP address or a DNS Name .
* Lets think of scenario where Accounts microservice wants to communicate with the Loans microservice and there is just one instance of Loans.
  * ![img.png](images/img.png)
  * Here Accounts needs the information of Loans like IP/DNS etc and that too it will hardcode those details .
  * Here Accounts is a upstream service meaning a service which is dependent on another service for processing some of it's api.
  * Loans is a downstream service with respect to accounts as it is not dependent on accounts.
  * Loans is a backing service wrt to accounts as without loans accounts cannot process some of it's own request.
* When there is only one instance of the loans microservice so managing the DNS name and its corresponding IP address mapping is straight forward very easy just hardcode it.
* But in cloud env we deploy multiple instances of a service and each instance has it's own unique IP.
* To address this challenge there is one approach that is to update the DNS Records with multiple IP Addresses and rely on some round robin name resolution .
* This method directs the request to one of the IP addresses assigned to the service replicas in a rotating manner . 
* However, this approach may not be suitable for microservices as containers or services frequently change.
* The rapid change makes it difficult to maintain the accurate DNS records manually and ensure efficient communication between microservices.
* This is not some monolithic application where they will get hosted on some physical machine's and have a long life span . They are deployed in some random cloud infrastructure .
* These instances are designed to be disposable and can be terminated or replaced for various reasons .
* Furthermore, auto-scaling capabilites can be enabled to automatically adjust the number of application instances based on the workload.


### How traditional Load Balancers work:
* Suppose i have 3 apps and there are 3 cleints trying to send request to my 3 apps like accounts , loans and cards .
* So inorder to hit my api they will use DNS name of the server where my load balancer is hosted along with that they will be sending the api path too.
* ex : services.microdemo.com/accounts :
* Here the DNS is services.microdemo.com it will send the request directly to my server where my load balancer is present.
* Now in that server we will be having a primary load balancer which will pick up the request and then from there it will pickup accounts and try to look for the IP Address of accounts in it's routing table which is manually managed and then it will send the request to the correct IP.
* Also there will be a secondary load balancer that keeps checking the health of the primary load balancer.
* If for some reason the primary one fails then it will act as the primary one.
* But this type of traditional load balancers have got so many limitations as well.


### Limitations of traditional Load Balancers:
* With traditional approach each instance of a service used to be deployed in one or more machines.
* The number of these application servers was often static and even in case of restoration it would be restored to the same state with the same IP and other configurations.
* While this tye of model works well with monolithic and SOA based applications with a relatively small number of services running on a group of static servers , it doesnt work well for cloud based microservice applications for the below reasons:
  * Limited horizontal scaling . Upon heavy traffic our apps will not get scaled up automatically , it will be manual and a long task.
  * Single point of failure . If the load balancer fails for some reason and suppose the secondary load balancer also fails then it will create a lot of issues.
  * Manully managing to update any IP's or configurations.
  * Complex in nature and they are not container friendly as they cannot automatically take our images and deploy them into random servers.

### The biggest challenge with traditional load balancers is that someone has to manually maintain the routing tables which is an imposible task inside the microservices eco-system because containers/services are ephemeral in nature , thats means they are short lived.

___

## How to solve the problem for cloud native applications:
### Service Discovery and Service Registry:
* Service Discovery is a pattern which involve the tracking and storing information about all the running service instances in a registry.
* Whenever a new instance of any microservice is created it should be registered in the registry , and when the service or instance of the service is terminated then it should automatically get de-registered too.
* The register acknowledges that mulitple instances of the same application can be active simulataneously. 
* When an application needs to communicate with a backing service it performs the lookup in the registry to determine the IP address to connect to.
* If multiple instances are available a load balancing strategy is employed to evenly distribute the work load among them.

* Well there are two ways in which the service discovery problem is addressed:
  * Cleint side service discovery .
  * Server side service discovery.

* In modern microservice architecture , knowing the right network location of an application is a much more complex problem for the cleints as the service instances might have dynamically assigned IP Addresses .
* Moreover, the number of instances may vary due to auto-scaling and failures.
* Microservices service discovery & registration is a way for applications and microservices to locate each other on a network . 
* How it works:
  * A central server (or servers) that maintains a global view of address's . Means there is going to be a central server that acts as a registry and holds the address of many microservices and their instances.
  * Microservices/clients that connect to the central server to register their addresses when they start & ready. 
  * Basically the microservices are coded in a way that upon their startup they register themselves in the registry .
  * Like they register their IP and name in the central server.
  * Microservices/cleints needs to send their hearbeats at regular intervals to the central server about their health.
  * Microservices/cleints that got themselves registered to the central server de-register themselves when they are about to shut down.
  * If 5 instances of loans microservice is registered and running then if one of the instances gets terminated then it also de-registers that particular IP from the registry.
  * Service discovery and registry deals with the problems about how microservices talk to each other : Make api calls.

## Client side service discovery and Load Balancing:
* In Client side service discovery the services or instances of the services are responsible to register themselves to the central repository which is the service registry.
* Now suppose there is a service that needs to communicate with one of the service so it will first go to the central server the service repository.
* There it will look for the service with which it wants to communicate now from there the service registry will give it a list of IP's at which the instances of the service that it is looking for are hosted.
* Then finally the client application will select one of the IP addresses based on some type of load balancing algo .


* Suppose we have accounts service running at some IP and accounts has registered itself to the service registry.
* Now there are 3 instances of loans service running at 3 different IP's but all the instances have registered themselves to the service registry.
* Now suppose accounts needs to communicate with loans then first it will go to the service registry and ask for loans address and service registry will return it 3 different IP Addresses.
* Now accounts has all the needed IP's now based on some load balancing algorithm it will send its request to any one of the IP's .
* Since here the client is doing the searching/discovering of the service and also balancing the load on its end thats why it is known as client side discovery.


### Client side service discovery:
* It is a architectural pattern where client application are responsible for locating and connecting to the services they depend on.
* In this approach the client application communicates directly with a service registry to discover available service instances and obtain the necessary information to establish connections.

### Key Aspects of client-side service discovery:

* Service Registration : 
  * Client applications register themselves with the service registry upon startup.
  * They provide their essential information about their location , such as IP address , port and metadata which helps identify and categorize the service.
* Service Discovery:
  * When a client application needs to communicate with a specific service , it queries the service registry for all the available instances of that service.
  * The registry responds with the necessary information such as IP addresses and connection details.
* Load Balancing:
  * Client side service discovery often involves load balancing to distribute the workload across multiple service instances.
  * The client application can implement a load balancing strategy to select a specific instance based on factors like round-robin , weighted distribution or latency.

* The major advantage of client side service discovery is load balancing .
* Like we can implement various types of load balancing algorithms.
* Round-robin , weighted round-robin , least connections , or even custom algorithms.
* The only drawback is that client service discovery assigns more responsibilty to developers .
* Also we have to create a central server (Service Registry).
* But Server side discovery solve's these issues which we will learn about in some other sections.

### There can be multiple instances of the service registry too.
* Suppose you have created a service registry app , now while running it or starting it give it the info of the port at which it will run .
* And also provide the information of other ports or URL where the instances of the same service registry could be running or may run.
* Like this they are connected to each other and yes each service registry will share the info it has with other service registry instances too.
* S1 , S2 and S3 . Suppose instances of accounts are only getting registered at S1 doesnt mean only S1 will know it's details .
* S1 will share all the details to S2 and S3 as well . This is known as gossip mechanism.
* Suppose accounts wants the IP of loan microservice.
* It will ask the service registry and the service registry will return it a list of IP's .
* The accounts app instance will keep all those IP's in its cache or load balancing so that it doesnt have to ask for addresses again and again from the service registry.
* Another thing is their cache gets refreshed at some regular intervals so that they dont miss on the new list of IP's , if there are any additions or deletions.


___

## Implementing Client Side Discovery:
* We have some spring cloud projects that will help us in achieving this.
  * Spring Cloud Netflix Eureka : which will act as the service discovery agent (Service Registry)
  * Spring Cloud Load Balancer: Library for client-side load balancing.
  * Netflix Feign Client: Installed on teh client side which helps in looking up for the IP's of any microservice.
  * There are other service registries in the market like etcd , consul and apache zookeeper which are also good.

* Advantages of service discovery approach:
  * No limitation on availability.
  * Peer to peer communication b/w services discovery agents.
  * Dynamically managed IP's , configurations and Load Balanced.
  * Fault tolerant & Resilient in nature.

* Current Netflix Backend Tech:
  * https://netflixtechblog.com/netflix-oss-and-spring-boot-coming-full-circle-4855947713a0

* We can have multiple service registry running and all of them will know about each other , because in their application.yml they will be coded in that way.
* This coding is hardcoding stuffs but at infrastructure level not business requirements level.

### Lets Build a Service Registry:
* First we will copy all the folders from the section 6 not section 7 it is because we will be using the H2 DB not the section 7 MYSQL DB.
* As it will take a lot of space in our system.
* ![img_1.png](images/img_1.png)
* Copying all of the above.
* Now first go to the start.spring.io
* Create a simple project there with the below settings:
* ![img_2.png](images/img_2.png)
* Now generate it and copy it into your section 8.
* We need the config client as it will extract all the details about itself from the configserver.
* Eureka server is the base dependency that it needs.
* We need actuator dependency too inorder to monitor its health.
* ![img_3.png](images/img_3.png)
  * Now open the main class of the eurekaserver app:
  * Add the annotation of EnableEurekaServer.
  * It will make the app work like a service registry and also it will expose a API Which might look like a dashboard
    * Before: ![img_4.png](images/img_4.png)
    * After: ![img_5.png](images/img_5.png)
  * Now lets make changes in the application.yml:
    * ![img_6.png](images/img_6.png)
    * We added the configserver details to it so that it can fetch information about itself from the configserver.
    * Let us also create a eurekaserver.yml file and add some instructions and then push it to the github repo of the microservices config.
      * eurekaserver.yml: ![img_7.png](images/img_7.png)
      * application.yml: ![img_8.png](images/img_8.png)
      * Let me just add teh eurekasever.yml to the configserver repo:
        * ![img_9.png](images/img_9.png)
  * Also we need to add some more properties to the application.yml or maybe eurekaserver.yml about the health check details.
  * Because certain probes are needed to us for using them proplery inside the docker.
  * Just simply go to chatgpt and ask what are the healthprobes that i need to be enabled in my springboot app so that i can use them in docker and also tell me what and how to do.
  * And this will give you the answer.
    * Add the actuator dependency then:
    * ![img_10.png](images/img_10.png)
    * copy and past the above instructions in the application.yml.
    * ![img_11.png](images/img_11.png)
  * SO now we are done with all the basic setup , but before starting both the configserver app and the eurekaserver app , let me explain what i have written inside the application configs:
    * ![img_12.png](images/img_12.png) 
    * This is the eurekaserver.yml .
    * Whenever it will start it will start at 8070 port be it in local or inside some container.
    * eureka.instance.hostname means what we want the hostname to be we can set it as localhos or eureka whatever you want.
    * client.register-with-eureka: false , it means dont register yourself at any of the other instances of yourself which are running and who's info you have.
    * Because default behaviour of any eureka server app is also like a cleint they can register themselves on any of their other running instances.
    * fetch-registry: false .
    * It means dont fetch the data from any other instances or any other registry who's information you have like their URL.
    * serviceUrl.defaultZone here we provide the links of all the probable instances of the eurekaserver.
    * You might be asking how will 1 instance know about other instance . Well it is not automatic .
    * Before starting any instance we can either configure the application.properties and tell it that either presently or maybe in the future at this link you can get a instance of yourself running so that's how they know.
    * This is not told to them during runtime , it is all predefined .
    * And multiple instances of the eurekaserver comes in when we want a cluster of service registry .
    * We will learn more about that later .
    * If you are planning for cluster then the two properties which we have set as false make them true , so that they can fetch each others details and data and can also register themselves .
    * Thats how they stay in sync.
* Enough talking lets run the app:
  * First start the configserver:
    * Now lets check if our configserver is fetching the details of eureakserver.yml or not:
    * ![img_13.png](images/img_13.png)
    * See we can fetch the details by going at 8071/eurekaserver/default.
  * Now start the eurekaserver:
    * ![img_14.png](images/img_14.png)
    * Well we got this dashboard by default.
    * There's nothing here because we need to start other microservice's and register them as clients here.

### 1st Commit: In the Next lectures we will configure our microservice's and register them as client to this eurekaserver.
### Commit Message: "Created EurekaServer | Explained some of it's properties | made a config file for the eurekaserver"

## Registering the microservices at the eurekaserver:
* Lets start with our Accounts-microservice:
  * First copy the mvn dependency for the eureka client and paste it in the pom.xml of accounts
    * ![img_15.png](images/img_15.png)
    * ![img_16.png](images/img_16.png)
    * ![img_17.png](images/img_17.png)
  * Now lets make changes in the code of accounts microservice:
  * ![img_18.png](images/img_18.png)
  * In the application.yml we did this.
  * Well eureka.cleint.fetch-registry: true means fetch the data from the eureka server.
  * register-with-eureka means register yourself with eureka server.
  * We have also given it the serverURL so that it has idea with the eureka server or servers are running.
  * Now what about the last property:
    * ![img_19.png](images/img_19.png)
    * prefer-ip-address true means prefer to register the microservice with it's ip address .
    * Because by default it will register the app with the app name which it will get from spring.app.name .
    * Think tomorrow if there is a app which needs to talk to accounts then it will try to fetch it's details from the service registry but it will not get the IP , it will only get the app name.
    * And it is of no use .
    * We can use the app-name instead of IP too only if the DNS mapping is done then only .
    * Think of google.com do u write it's IP no right you simply try the DNS name and somehow internally it hits the google IP thats it.
    * I will explain more about this IP Address and DNS mapping thing with context to eureka server ahead.
    * Now lets add some more details like info about our microservice which will be shown in the eureka dashaboard:
    * ![img_20.png](images/img_20.png) 
    * Well this info related property is actually a part of the actuator dependency , which needs to be exposed as it is not exposed by default.
    * ![img_21.png](images/img_21.png) 
    * See here we added the property  , this will expose the info api which will be consumed by the eureka server.
    * Another thing lets just add some properties which will help using during de-registration of the app from the eureka server.
      * ![img_22.png](images/img_22.png) 
      * This endpoint.shutdown.access unrestricted means if there is any shutdown by the app then the eureka will de register the microservice instance from the eureka server.
      * Well we will add the graceful shutdown at later stages where we will learn more about it so for now lets just leave it here.
    * Let's start the configserver and eureka and then accounts micro:
      * As soon as you start the accounts' app:
        * ![img_24.png](images/img_24.png)
        * You will see it pulling the configs from configserver.
        * ![img_25.png](images/img_25.png)
        * See here it is sending heartbeats to the eureka server and also registering itself and also trying to fetch info about all the apps instances from the eureka server.
        * The microservice by default will keep sending heartbeat signals to the eureka server every 30 seconds.
        * ![img_26.png](images/img_26.png)
        * See here this time in the dashboard of eureka we can see Accounts app instance And if you click on the link below the status part:
        * ![img_28.png](images/img_28.png)
        * Observer above that it is using my local IP Address along with the port and then the /actuator/info .
        * ![img_27.png](images/img_27.png) We are getting the info that we had given in the applications.yml .
        * BTW the application name is taken from spring.application.name .
        * And the link under that status is randomly created by eureka server using my PC name etc. i mean the host name is taken from my PC name only.


### 2nd Commit: In the Next lectures we will configure our loans and cards microservice and register them as client to this eurekaserver.
### Commit Message: "Installed Eureka Client dependency to our Accounts microservice | Configured the accounts microservice app | Provided the eureka cleint info and eureka server info | Exposed management end points like info and shutdown"


### Lets make changes in the Loans and Cards microservice:
* Loans:
  * Add the mvn eureka client dependency.
  * Make changes in the application.yml:
    * ![img_29.png](images/img_29.png)
    * ![img_30.png](images/img_30.png)
* Cards:
  * Add the mvn client dependency.
  * Make changes in the application.yml:
    * ![img_31.png](images/img_31.png)
    * ![img_32.png](images/img_32.png)

* Now just simply start the applications:
  * configserver:->eurekaserver:->All 3 microservices:
  * ![img_33.png](images/img_33.png)
  * See here this time we are getting all the app instances in the eureka server.
* There is a endpoint exposed by the eureka where you will get all the info about the registered apps:
  * localhost:8070/eureka/apps
    * ![img_34.png](images/img_34.png)
    * You will get so much information from here like no of instances their IP their name , their health , port etc , everything.
    * If you want this in the json format then :
      * Open postman: 
        * ![img_35.png](images/img_35.png)
        * Send info with the Accept header.
        * If you want info related to a specific app like accounts then use:
          * ``localhost:8070/eureka/apps/accounts``

### 3rd Commit: In the Next lectures we will see how to de-registration works and also how the heartbeats mechanism works.
### Commit Message: "Installed the eureka client dependency in cards and loans microservice | learned more about the api's exposed by eureka | eureka/apps"

### Service De-Registration and HeartBeats Mechanism:
* In real prod applications we wont be running the apps in intellij right .
* I want to say do you think in prod apps you will simply go and press the stop all button .
* ![img_36.png](images/img_36.png) 
* The above , no right.
* So for that reason we have exposed the actuator end-point that is the actuator/shutdown , when you hit this api it will gracefully shutdown your app and then our app will de-register from the eureka.
* ![img_37.png](images/img_37.png)
* Lets do see how the apps De-register from the eureka server:
  * Lets start all the related apps in seq:
    * configserver->eureka->All apps:
    * So as usual when our apps start they fetch configs from config server and then they register themselves on the eureka server as per the logs .
    * Now lets hit the shutdown api for any one of the service instance.
    * ![img_38.png](images/img_38.png)
    * Lets hit the actuator/shutdown api for the accounts app.
    * ![img_39.png](images/img_39.png)
    * GET is not supported so lets open postman and perform a POST Request at ``localhost:8080/actuator/shutdown``.
    * ![img_40.png](images/img_40.png)
    * As soon as we execute this we will see the response and also read the logs and also the dashboard.
      * Logs:
        * Accounts: ![img_41.png](images/img_41.png)
        * Accounts: ![img_42.png](images/img_42.png)
        * See above it is unregistering itself and then gets completely shutdown
        * EurekaServer: ![img_43.png](images/img_43.png)
        * See here the status is DOwn so it is also removing the service instance from it's registry.
      * Postman: ![img_44.png](images/img_44.png)
      * Dashboard: ![img_45.png](images/img_45.png)
* So this is how the de-registration works .

#### Hearbeats Mechanism:
* By-default every eureka cleint keeps sending heartbeats to the eurekaserver at regular intervals of 30s.
* So in-order to see that lets just stop the eurekaserver and then you can see in the logs of the client app how it keeps sending heartbeats.
  * First stop the eureka server then look at the logs of any service that was registered at that eureka server.
  * Cards:
    * ![img_46.png](images/img_46.png)
  * This is the heartbeats mechanism , they need to send to the eurekaserver.



### Graceful Shutdown:
* We forgot to mention about graceful shutdown.
* Well it is not an api-endpoint but a config for the app , that makes the app shutdown gracefully.
* Which means suppose some processing is going on for some request but all of a sudden the ops team decides to close the app so they will hit some api to close the app.
* What it does is it will completely stop the app which can cause data inconsistency.
* So inorder to avoid that if we mention server.shutdown:graceful , it will shutdown only after all the current ongoing requests are processed and meanwhile if a new request comes in then it will not take the request .
* It's like we have closed taking orders and right now who ever is inside the restaurant and who's order is being prepared once they are served the food the restaurant will close.
  * This is very important in situations where :
    * The app is performing some money transfer.
    * Payment.
    * Order Booking
    * File Upload.
    * DB transaction.
* Donot get confused between actuator endpoint shutdown and graceful shutdown.
* graceful shutdown is a way to handle requests while shutting down , where as actuator endpoint is a api to initiate shutdown process.


### 4th Commit: In the Next lectures we will understand inter-services communication | FeignClient
### Commit Message: "Demonstrated De-Registration | Hearbeat Mechanism | Graceful Shutdown "  