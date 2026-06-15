# Generating Docker Images for all the microservices and Making Changes to the Docker Compose file:
* First we will expose some liveness and readiness probes for our apps like accounts , cards and loans because we are going to start our gateway server only after all the 3 microservices start successfully.
* So for this let's add the below configs to the application.yml of all the 3 microservices.
* ![img.png](images/img.png)
* See what we have added here under the management .
* Do this in all the 3 microservices accounts , loans and cards .
* Now once the above is done , Now check the google jib plugin in the pom.xml of all the apps and make sure the tag is s9.
* ![img_1.png](images/img_1.png)
* Make sure the above plugin is in all the apps like accounts , cards , loans , eurekaserver, configserver , gatewayServer .
* The Reason why we are creating images of all the services with s9 tag is to help you understand how the docker compose file works.

### Now lets make generate the images and push them into the docker hub:
* Open terminal and navigate to individual microservice's work directory:
  * Run the command ``mvn compile jib:dockerBuild``
  * Make sure the command is run in the same directory where pom.xml is present.
* ![img_2.png](images/img_2.png)
* Now once the images are created you need to just push them to you docker hub.
* Open terminal anywhere and simply execute the cmd ``docker push dockerhubname/imagename:tag``
* Ex: ``docker push captainashish/accounts:s9``
* Like this push all the images to the docker hub.
* Now since all of your images are pushed to your docker hub , Now we will see what are the changes that we need to make in the docker compose file.


___

### Updating the docker compose file:
* First create a service with then name gatewayserver .
* Give the service the image and the container name also give the port mapping.
* ![img_3.png](images/img_3.png)
* Do this inside the docker-compose file.
* Now we need to extend some basic other services .
* Open the common-config.yml and see which are the services that need to be used by gateway:
* ![img_4.png](images/img_4.png)
* We need to extend this service so that our gateway server can extend it and use the configs of the configserver and eurekaServer URL otherwise we will have to do that manually.
  * Option 1: ![img_5.png](images/img_5.png)
  * Option 2: ![img_6.png](images/img_6.png)
* Now since our gateway server depends on services like accounts , cards and loans so only after they are healthy our gateway server container will run .
* For this lets first expose some health checks by this services:
  * accounts: 
    * before: ![img_11.png](images/img_11.png)
    * after: ![img_12.png](images/img_12.png)
  * loans:
    * before: ![img_9.png](images/img_9.png)
    * after: ![img_10.png](images/img_10.png)
  * cards:
    * before: ![img_7.png](images/img_7.png)
    * after: ![img_8.png](images/img_8.png)
*  Now once we have done all the changes like including the health checks , now we need to add the depends on tag inside the gatewayserver service and include all the services that it is dependent on.
  * ![img_13.png](images/img_13.png)
  * We can also include here that it depends on configserver and eurekaserver too , but accounts , cards and loans are already dependent on them so they will start only when configserver and eureka is live after that their health check will be done.
  * And once the healthcheck is done then only gateway will start.

##### Make sure to change the tags of the image names from s8 to s9.

___

### Now as we are done with the changes lets start the docker compose file and run some manual checks:
* open the terminal at the location where the docker compose file is present.
* ``docker compose up -d``
* ![img_14.png](images/img_14.png)
* But upon checking the logs we go this issue:
  * ![img_15.png](images/img_15.png)
* ![img_16.png](images/img_16.png)
* Here also the gateway never started .
* So upon debugging and looking for errors:
  * We found out that in the health check part we have given wrong ports of each service because of which the health check is not running properly.
  * ![img_17.png](images/img_17.png)
  * See here cards run at 9000:9000 but we have given 8071 in healthcheck:
  * So let us change it to 9000 and also for accounts change it and loans also.
* Now run the same cmd again ``docker compose up or docker compose up -d``
* ![img_18.png](images/img_18.png)
* See finally this time it started .
* ![img_19.png](images/img_19.png)
* All the containers are also running .
* Now lets just test whether the gateway is working or not:
  * accounts: ![img_20.png](images/img_20.png)
  * accounts-header: ![img_21.png](images/img_21.png)
  * See here we have the microdemo-correlation-id.
  * cards: ![img_22.png](images/img_22.png)
  * loans: ![img_23.png](images/img_23.png)
* Lets hit the api for the consolidated information:
  * ![img_24.png](images/img_24.png)
  * microdemo-correlation-id: ![img_25.png](images/img_25.png)
  * accounts-microservice logs: ![img_26.png](images/img_26.png)
  * cards-microservice logs: ![img_27.png](images/img_27.png)
  * loans-microservice logs: ![img_28.png](images/img_28.png)
  * See how the same correlationId is carried everywhere .

### Conclusion: Our docker file is working properly and the underlying microservices are also working fine.
* Now just copy the settings of the docker-compose.yml and common-config.yml to other folders too like the qa and prod and change the SPRING_PROFILES_ACTIVE to qa and prod respectively.



### Commit Message: "Generated Docker Images and Updated the docker compose file | Tested the API's via gateway server"