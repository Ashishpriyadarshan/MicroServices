``In this lecture we will be implementing Logging Using Grafana , Loki and Alloy``.
``Since promtail is no more used so we will be using the Grafana Alloy Instead``

## Steps:
* First go to the official site of Grafana .
* There under OpenSources select Grafana Loki.
* ``Link``: https://grafana.com/docs/loki/latest/setup/install/docker/
* No on the above link scroll below, and you can see the details like how you can do the setup.
* ![img_9.png](images/img_9.png)
* You will see the above part and then simply copy the given links inside them and simply check whats inside them.
* There are lots and lots of docker config's written there which might not be easy to understand by a dev or first timer person so you can simply copy them and ask chatgpt what exactly each line means.
* You just follow the steps given by the official documentation thats all that you need to do.
* Inside the ``loki-config.yaml`` whatever you see is done for the local system tomorrow in prod env if you have your logs storage in some other system of DB like AWS or Azure or GCP or etc then in the official documentation you can see how you can do the setup so dont worry about that.

* So lets just create a directory with the name Loki.
* No lets create a folder with the name Observability and inside that lets create two folders Loki and Alloy.
* ![img_10.png](images/img_10.png)
* Now follow the below steps:
* ``Step 1``: Open Terminal and change the directory to some temporary director where you would like to store the downloaded yaml files for sometime.
  * ![img_11.png](images/img_11.png)
  * ![img_12.png](images/img_12.png)
  * See in the above image we have executed the commands which were mentioned in the Grafana Loki official documentation.
  * We also got them downloaded
* ``Step 2``: Copy the downloaded files to the Observability folder as per their name's.
  * ![img_13.png](images/img_13.png)
* `` Step 3``: Now we need to make some changes inside the docker-compose file of the loki .
  * ![img_14.png](images/img_14.png)
  * Remove the networks part.
  * ![img_15.png](images/img_15.png)
  * Remove the flog specs , flog is a demo app.
* IF you observer in the docker compose file then you will see that every service is registered to a network named loki .
* ``But we have to make sure that this name loki is changed to the name of the network where our microservices are running inside docker env``
* ``Thats is microdemo``
* ``Step 3``: ![img_16.png](images/img_16.png)
  * ![img_17.png](images/img_17.png)
  * ![img_18.png](images/img_18.png)
  * In the above image let the network name be like this , other than this whereever you see network mentioned as loki change it to microdemo.

* ``Step 4``: Since we have kept the loki and alloy config in different folders so we need to make changes inside the docker compose as well.
* Before: ![img_19.png](images/img_19.png)
* After: ![img_20.png](images/img_20.png)
* ![img_21.png](images/img_21.png)
* Before: ![img_22.png](images/img_22.png)
* After: ![img_23.png](images/img_23.png)
* Before: ![img_24.png](images/img_24.png)
* After: ![img_25.png](images/img_25.png)

* We were done with everything but then i compared the docker compose file in chatgpt and asked whether we are correct or not .
* I got a problem there that it says if we have two docker compose files and inside them both the networks are written as microdemo , then there can be a issue because the docker compose for the spring applications will start a network with the name microdemo and again when we run docker compose for the loki docker compose file then the names will clash saying network already exists .
* So it gave me two solutions.
* ``Solution 1 : Either externalize the docker network and create it even before running any of the docker compose file``
* ``Solution 2: Copy the service's of loki docker compose file into the same docker compose file which has the springboot services defined inside it``

* ``In this case we will go with solution 2 but again we will have to make some changes inside the volumes ``
* ![img_26.png](images/img_26.png)
* I copied the contents inside the docker compose which was inside Observability folder to the docker compose which is inside prod , now let's just make some changes to the volumes so that they can find their config yaml files.
* ![img_27.png](images/img_27.png)
* See here how i am changing the directory details to ../Observability etc .
* Now we are ready with everything we just need to do the tests now.

## Testing:
* Start the docker compose up -d command in the prod directory.
* We were facing a lot of problems while starting the compose file so we simply removed the rabbitmq and redis from the compose file as well as removed rabbitmq related configs and dependencies from multiple apps as well as the configserver after that it is working fine.
* Maybe my PC is not able to handle that amount of load .
* Lets get started now:
* One other workaround that you can do is change the timeout and interval durations to a bigger number so that your apps get enough time to start properly otherwise they will always fail.
* First lets start the compose file:
* ``docker compose up -d `` do this to the docker compose file present inside the prod folder.
* ![img_28.png](images/img_28.png)
* Once all the apps are live now lets create some records so that they generate some logs.
* ![img_29.png](images/img_29.png)
* ![img_30.png](images/img_30.png)
* ![img_31.png](images/img_31.png)
* ![img_32.png](images/img_32.png)
* Now after doing some processing lets go and check whats there in the grafana , as in the docker compose we had mentioned that grafana to start at port 3000 so lets hit the localhost:3000.
* ![img_33.png](images/img_33.png)
* This is how it is going to look , it may ask u for login or may not still: Username and Password both are admin.
* Now as we enter into grafana how are we going to see the logs.
* For this we need to make a connection to the datasource where all the logs are present.
* Click on connections from there click on add new connection.
* Otherwise, you can simply click on configured datasources , which creates a datasource by itself.
* And the details of this dataSource is provided in docker compose file itself within the grafana service.
* ![img_34.png](images/img_34.png)
* ![img_35.png](images/img_35.png)
* ![img_36.png](images/img_36.png)
* Now inorder to check the logs :
* ![img_38.png](images/img_38.png)
* Click on explore here you will have to give some info .
* Well if you goto alloy-local-config then you can see : 
* ![img_39.png](images/img_39.png)
* It is extracting all the telemetry details at the container level .
* So select container in that explore :
* ![img_40.png](images/img_40.png)
* See how it shows all the running containers details .
* Suppose i want to see the logs of accounts-microservice then just select it and click on the run query.
* ![img_41.png](images/img_41.png)
* See below :
* ![img_42.png](images/img_42.png)
* This is the information that we are getting.
* Now suppose i want to see logs where a specific line is present:
* ![img_43.png](images/img_43.png)
* ![img_44.png](images/img_44.png)
* You can also select multiple containers from which you want to see logs .
* Another thing:
* ![img_45.png](images/img_45.png)
* If you see the .data folder then it has multiple folders which store information in some format which is leveraged by the grafana , this minio is actually a part of the Loki which is the log aggregation system.
* In Dev env we have given the storage as this minio but in higher env we can give it as some cloud storage too.
* And accordingly we will have to do the setup.
* ![img_46.png](images/img_46.png)
* Check this drilldown part too .
* If you click on Metrics then it will ask you that there is no prometheus dataSource configured .
* 
