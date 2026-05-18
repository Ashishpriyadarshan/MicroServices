# Understanding how to use a REAL DB like a mysql DB in a microservice:
### We copied the contents of the V3 folder which is inside the section 6 here. There will be two versions of this section 7 one version would be where we will carry everything from the last section .
### One version is without dependency like the spring cloud monitor and spring rabbitmq dependency , because unecessarily it will take up the space.
### Another version will have all the things from the section 6.

___

### Now first of all even before migrating from H2 to the mysql DB we will create some DB's locally i mean using docker .
### In real projects the developer is given the information of different DB enviroments like the dev , qa and prod.
### In real projects the DB's are already pre created by some team and you dont need to create DB fields manually or by code.
### In real projects there is no schema.sql file inside your resources folder like we have while using the H2 DB.
### Lets create 3 DB's for our 3 microservices , each microservice will be having its own DB.

### Command that we used to create a DB for accounts microservice:
* If you ever need any help like whats the command for starting a docker container for mysql with a unique name and own port definations then just simply ask that to any LLM's present over the internet.
* ![img.png](images/img.png)
* In the above image -p 3306:3306 means a container will run at port 3306 of our local and 3306 inside the container .
* --name accountsdb means the name of the container .
* -e means environment variable MYSQL_ROOT_PASSWORD root , means the password for the user root is root.
* -e MYSQL_DATABASE means the name of our Database as by default initially the DB will be created but it will be empty .
* -d mysql means start the container in detached mode and at last we have given the name of the image which it has to pull that is the mysql.
* ![img_1.png](images/img_1.png)
* So finally we have the mysql image and the container is also running now inorder for us to properly use the DB we need a client or an interface .
* For that download sqlelectron .
* Once you have downloaded the sqlelectron .
* ![img_2.png](images/img_2.png) Fill all the details and then click on the test button to check if the client can connect to the DB or not.
* If it is working then simply save and then connect to it and try to see whats there inside the DB.
* ![img_3.png](images/img_3.png) 
* See here it looks like this , in the similiar manner lets setup the DB for other microservices as well.
* LoansDb: 
  * ![img_4.png](images/img_4.png) 
  * Here let me explain each word -p 3307:3306 means in our local system we will run a container at the port 3307 but inside the container we will run our image or whatever app at port 3306.
  * Thats it hostport: containerport , As we have exposed our hostport 3306 earlier to the accountsdb container so we need another hostport.
  * ![img_8.png](images/img_8.png)
  * Added the LoansDB to the client.
* CardsDb:
  * ![img_5.png](images/img_5.png)
  * 3308 is the hostport of our local system and 3306 is the port inside the container.
  * ![img_9.png](images/img_9.png)
* ![img_6.png](images/img_6.png)
* ![img_7.png](images/img_7.png) 
* Same image getting used again and again in different containers with different configs.
* Like if you open and see any of the containers DB then you will see that they have no tables inside them yes they do have a name given by us but no table why because we are using the same image to create multiple containers we are just changing the name and some configs.

## Important : In real projects we dont install or use mysql in our system or maybe docker . It is the project infra team that deploys the DB in different servers or env and they just give us the URL and the credentials thats it and thats how we connect to a DB hosted in some cloud env.

___

## Now as we have completed creation of our DB's so now it is time to make the code changes in our microservices so that they connect to our DB:
### Accounts Microservice:
* First thing that we need to do is delete the H2 related dependencies from the pom.xml , because if we dont remove it then during run time even if we mention our mysql db still our microservice will get connected to the H2 DB.
* ![img_10.png](images/img_10.png)
* ![img_11.png](images/img_11.png)
* Remove the above two dependencies .
* Now add the mysql dependency to our pom.xml.
* ![img_12.png](images/img_12.png) We need this dependency.
* ![img_13.png](images/img_13.png) Copy and paste it in the pom.xml file.
* This dependency will help the app in connecting to a mysql DB as well as act as an interpreter between the app and the DB.
* Now as we have pasted the dependency successfully so the next task is to make changes to the configs like the application.yml.
  * Before : ![img_14.png](images/img_14.png) JVM , CLI , ENV Variables configmaps or secrets to use the DB creds as they are not directly mapped.
  * After: ![img_15.png](images/img_15.png)
  * Let me explain what i have changed and what i have removed and why.
  * I have changed the URL which was earlier ``jdbc:h2:mem:testdb`` to ``jdbc:mysql://localhost:3306/accountsdb`` which says that my DB is in localhost IP running at port 3306 and the name of the DB is accountsDB.
  * Changed the username and password.
  * remove that h2 console part as it is no more needed , we dont need to hit the h2 console url.
  * removed the JPA database platform and the hibernate part but we need the show-sql true as it will help us in looking at the logs.
  * Thats it thats all we have done .
  * Again we dont use the hardcoded values for the creds in any env rather we use : JVM , CLI , ENV Variables configmaps or secrets to use the DB creds as they are not directly mapped.
  * Another very important change that we need to do is , dealing with the schema.sql because for us we dont have the tables inside our DB.
  * In real projects the tables are already created we just need to connect to the DB but since here we need to create tables in the DB.
  * There is a way by which we can execute the schema.sql to populate our DB.
  * ![img_16.png](images/img_16.png)
  * ![img_17.png](images/img_17.png) Added this SPRING_SQL_INIT_MODE : ALWAYS 
  * What it does is it will always run the schema.sql present inside our resource folder so that the DB is populated with the tables.
  * Another thing our DB tables will not be created again and again because we have used IF EXISTS that means if they exist in the DB then dont create any new table.
  * So now we are done with setting up all the properties we could have done this thing in the docker compose file too but we will see that ahead.
  * Now we need to replicate all of the changes to the cards and loans microservice as well.
  * Cards:
    * Before: ![img_18.png](images/img_18.png)
    * After: ![img_19.png](images/img_19.png)
    * Also we need to remove the dependency of the H2 DB and add the mysql connector dependency.
    * We have the cardsdb container running at the port 3308.
  * Loans:
  * Before: ![img_20.png](images/img_20.png)
  * After: ![img_21.png](images/img_21.png)
  * Also remove the dependency of the H2 DB and add the mysql connector dependency.
  * We have the loansdb container running at the port 3307.
* Finally we have done the setup for all of our microservices.
* Now we will test our microservices and their connectivity like are they working properly or not.

___
## Testing the connectivity and working:
### First make sure you have started all of your databases and then start your apps one by one.
* ![img_22.png](images/img_22.png)
* Well all are in running status.
* ![img_23.png](images/img_23.png)
* As soon as we started our accounts app it failed with this error . It says it cant find build.version .
* It is because that config is present in our config files and to load the config files we need the configserver to be running.
* So first start the config server.
* Well now there is another issue that is we need the rabbitmq server to run first other wise our configsever will keep giving wrong configs.
* But still we can start our microservices as we are starting them manually not via docker compose so it wont cause any issue to dependency.
* In their logs you will see that they are trying to connect to the rabbitmq again and again thats it no issue.
* ![img_24.png](images/img_24.png) Talking about this thing in the logs.
* Thats why at the beginning of the course i said we need to have two version one without that rabbitmq and another with rabbitmq.
* Anyways so our accounts has started :
  * In the sql client: ![img_25.png](images/img_25.png) 
  * See this time it is showing us all the tables and fields.
  * lets check whether it is taking any input or not by using postman.
  * ![img_26.png](images/img_26.png) 
  * We are getting a error upon hitting the api .
  * Upon looking deeper into the issue we found that it is not able to find a table with the name customer_seq under the accountsdb.
  * But why do we even need a table like that when we just have two tables one is customer and another is accounts.
  * The answer is because there are some codes which we have done while creating the entity that is causing this issue.
  * ![img_27.png](images/img_27.png)
  * ![img_28.png](images/img_28.png)
  * Well the issue lies with the customer entity where for creating a new entry we have told it to generate a ID in auto which means Hibernate will select a method of creating ID because of which it is trying to insert records into the accounts.customer_seq table which will have the id and those id's will be used in the customer table too and since there is no such table like customer_seq that is why it is failing so just change the Generation Type to Identity.
  * ![img_29.png](images/img_29.png)
  * So we have changed it here now lets run the accounts again.
  * ![img_30.png](images/img_30.png) See this time it worked.
  * Lets check the DB client: ![img_31.png](images/img_31.png)
  * ![img_32.png](images/img_32.png)
  * Finally it is working .
  * Now we need to check with other microservices as well but before that lets just make sure the ID Generation Strategy is set to Identity.
  * Cards is ok but in loans we have to change : ![img_33.png](images/img_33.png)
  * Change it to identity.
  * Cards:
    * Postman: ![img_34.png](images/img_34.png)
    * DB Client: ![img_35.png](images/img_35.png)
  * Loans: 
    * Postman: ![img_36.png](images/img_36.png)
    * DB Client:  ![img_37.png](images/img_37.png)
* See everything is working fine. For now stop all your applications just dont delete the running mysql containers or image otherwise you will loose all the data .

### 1st commit: Next we will learn what changes we need to make inside docker compose file.
### Commit Message: "Demonstrated the mysql in docker | Multiple mysql containers for microservices | Made config changes in all the microservices as per their DB configs"
___

## Updating the Docker Compose file:
### Start creating the docker images for all the microservices with their own tag like s7:

* First go to their pom.xml and change the name of the image.
* Accounts:
  * Before: ![img_38.png](images/img_38.png)
  * After: ![img_39.png](images/img_39.png)
  * Command: Open the terminal in the accounts microservice project folder and then run ``mvn compile jib:dockerBuild``
* Loans:
  * Before: ![img_41.png](images/img_41.png)
  * After: ![img_42.png](images/img_42.png)
  * Command: Open the terminal at the Loans microservice project folder and then run ``mvn compile jib:dockerBuild``
* Cards:
  * Before: ![img_40.png](images/img_40.png)
  * After: ![img_41.png](images/img_41.png)
  * Command: Open the terminal at the Cards microservice project folder and then run ``mvn compile jib:dockerBuild``
* Also, we will do this for our configserver , its not needed but we should do this:
  * ![img_43.png](images/img_43.png)
  * Open the terminal at the configsever folder location and then run ``mvn compile jib:dockerBuild``

### Now once all of our docker images are ready we need to work on the docker compose file now:
* ![img_44.png](images/img_44.png)
* Open the docker compose file which we had copied from the last previous section 6.
* There we will be creating 3 new services which will be for the DB.
* accountsdb service:
  * ![img_45.png](images/img_45.png)
  * Like this we will create the rest of the service's like the loansdb and cardsdb.
* loansdb service:
  * ![img_46.png](images/img_46.png)
* cardsdb service:
  * ![img_47.png](images/img_47.png)
* Now since we have all the services now we need to make changes to their individual microservices like add that depends_on tag:
* Because we don't want our microservices to start unless our DB containers have started successfully.
* Another important thing is we need to provide the credentials of our DB's to the microservices otherwise how will our microservices read or write from the DB.
* ![img_48.png](images/img_48.png)
* Accounts:
  * ![img_49.png](images/img_49.png)
* Cards:
  * ![img_50.png](images/img_50.png) See how we are using s7 tag .
* Loans:
  * ![img_52.png](images/img_52.png)
* Another very important thing that we need to do is include the depend_on because our microservices depends on their own DB's.
* Accounts:
  * ![img_53.png](images/img_53.png)
  * depends_on accountsdb
* Loans:
  * ![img_54.png](images/img_54.png)
  * depends_on loansdb
* Cards:
  * ![img_55.png](images/img_55.png)
  * depends_on cardsdb

### 2nd commit: We will optimize the compose file in the next lecture and also push the docker images to the docker hub.
### Commit Message: " Updated the docker compose file | Created the docker images for all the microservice | Gave info of which docker image to be pulled for creation of the DB"

___

### There are certain things which are getting duplicated in our compose file like the :
* ![img_56.png](images/img_56.png) Like if you see here then the image name , health check and environment details are getting repeated again and again in all of the 3 db services.
* So let us create a new service inside the common-config file and then we will extend it and use it in the docker compose file.
* ![img_57.png](images/img_57.png) This new service will be common for all the services like accountsdb, loansdb , cardsdb .
* accountsdb:
  * Before: ![img_58.png](images/img_58.png)
  * After: ![img_59.png](images/img_59.png)
* loansdb:
  * Before: ![img_60.png](images/img_60.png)
  * After: ![img_61.png](images/img_61.png)
* cardsdb:
  * Before: ![img_62.png](images/img_62.png)
  * After: ![img_63.png](images/img_63.png)
* So finally we have optimized the compose file , if we want we can have a common service for the rabbitmq specs too but lets just leave it as it is.


## Important details:
* ![img_64.png](images/img_64.png) Here
* In the datasource URL : jdbc:mysql we are giving the information of the type of DB we are supposed to connect and we are connecting it using mysql connector.
* loansdb:3307 means loansdb is the name of the service that we have inside the compose file and since all of them are connected to the same network so finding the service woudl be easy and then we need to connect to the port 3307 of that service which is the external port no for the process running at 3306 port inside the container .
* /loansdb is the name of the database , so dont get confused between loansdb written two times.
* hit loansdb service -> port 3307 -> which hits the 3306 port which is inside the container then hit loansdb which is the name of the db with the given creds like username and password.

### 3rd commit: We will run the compose file in the next lecture .
### Commit Message: " Optimized the docker compose file by creating a common service which is getting extended by all the db services"

___

## Pushing into the docker hub:
* Dont push your mysql image to your docker hub.
* accounts:s7 :
  * ![img_65.png](images/img_65.png)
  * ![img_66.png](images/img_66.png)
* cards:s7:
  * ![img_67.png](images/img_67.png)
* loans:s7:
  * ![img_68.png](images/img_68.png)
* configserver:s7:
  * ![img_69.png](images/img_69.png)

## Lets run the docker compose file:
* Navigate the terminal to the docker compose file location and then start.
* ![img_70.png](images/img_70.png)
* We're getting this error lets look for what went wrong.
* ![img_71.png](images/img_71.png)
* ![img_72.png](images/img_72.png)
* Well the problem is in the common config file we have metioned everything but we forgot to mention the name of the image that our container has to use as the base image.
* ![img_73.png](images/img_73.png)
* Now run it.
* ![img_74.png](images/img_74.png)
* Well this time it worked.
* ![img_75.png](images/img_75.png)
* ![img_76.png](images/img_76.png)
* There is one thing which is strange here that is the cards and loans microservice are paused or they could not start lets check their logs like what is the issue.
* Upon going through my logs i see two major issue one says cannot connect to the configserver because it is 
* ![img_77.png](images/img_77.png) but as far as we can remeber we have set the environment variables for the configserver too in the docker compose file which is supposed to override the default application.yml of the cards or any other microservice and my microservices are depentent on configserver so unless configserver starts they will also not start thats why you can see my app is trying to connect to the configserver again and again.
* But then how with the same common config accounts microservice started , this means there is no issue with configservr.
* Even in the accounts microservice logs it is trying to connect to the localhost configserver but after some time later on it is connecting to the configserver docker container.
* Upon analyzing my docker logs we go to know that problem is happening because my cards and loans microservice are unable to connect to the db.
* ![img_78.png](images/img_78.png)
* ![img_79.png](images/img_79.png)
* Observer here lets try to see what we have done wrong in the docker compose file.
* ![img_80.png](images/img_80.png)
* Well the problem here is very simple.
* If our loans microservice which is hosted inside the same docker network microdemo and it is trying to access the loansdb so it should hit it's 3306 port and not 3307.
* Because 3307 is to be used for external communication suppose if am trying to access the port 3306 of the docker container from my local machine or maybe some other cloud machine then i should hit the IP followed by the port 3307.
* So since all the services are under the same network so they are supposed to be using the name of the service and the port which they are trying to access .
* For example : if i am hosting multiple apps on my pc and i want to access a app in my pc then i will use localhost:port_no right , so the same thing is happening inside the microservice network as well service_name:port no because unders same network they will recognize each other by their service name.
* ![img_80.png](images/img_80.png)
* Same thing happens with cards as well change the port to 3306:
  * Before: ![img_81.png](images/img_81.png)
  * 3308 is for the external communication not for inter network communication.
  * After: ![img_82.png](images/img_82.png)
* Now it will work fine another thing is let me try to adjust some scripts to see if this helps our app to wait for configserver first:
  * Before: ![img_83.png](images/img_83.png)
  * After: ![img_84.png](images/img_84.png)
  * depends_on first configserver then rest of the services.
  * Do this to all the services.
* Now run the docker compose again.

### Check the containers , Status and their DB:
* ![img_85.png](images/img_85.png)
* ![img_86.png](images/img_86.png)
* See this time all of our containers are running.
* Postman : 
  * Accounts: ![img_87.png](images/img_87.png)
  * Loans: ![img_88.png](images/img_88.png)
  * Cards: ![img_89.png](images/img_89.png)
## DB :
  * Accounts: 
    * Postman: ![img_90.png](images/img_90.png)
    * DB: ![img_91.png](images/img_91.png)
  * Cards:
    * Postman: ![img_92.png](images/img_92.png)
    * DB:  ![img_93.png](images/img_93.png)
  * Loans:
    * Postman: ![img_94.png](images/img_94.png)
    * DB: ![img_95.png](images/img_95.png)

* Just do one thing now since we have 3 docker compose files for dev , qa and prod so just make the changes.

### So now everything is working fine . In the upcoming lecture i will demonstrate about docker networks and host port and container port and inter service communication.


### 3rd commit: In the upcoming lecture i will demonstrate about docker networks and host port and container port and inter service communication..
### Commit Message: " Ran the docker composed | Issues regarding DB Connectivity sorted "

## Docker Networks:
* In the last lecture we noticed that instead of using port no of the DB's like 3306 , 3307 ,3308 we were simply using their service name.
* It is because all of them were running under the same network.
* When a system install's docker by default if they run anything inside docker then it will run in the docker daemon but all of them will be running in the default network.
* default network means whatever is provided by docker , if you dont mention the name of the network for any docker image then it will run in default only.
* You can create your own network inside network like you can create as many as networks and possible.
* A group of computers connected within the same network and recognize each other easily but external of that network they can't recognize each other and in that case we need the proper ip and port.
* Let me give you a demonstration of why we were using the service name and port for providing the URL details to our microservices and why not we were using the hostport:
  * When you were trying to access the accounts microservice from your local system what was the URL that you were trying to hit:
  * ![img_96.png](images/img_96.png) You were hitting this URL right which says here localhost means the same machine and then the port no which is 8090.
  * While in docker while running the container of the accounts microservice we had given the hostport:containerport mapping.
  * So for the external world a world or network outside the docker eco-system for anyone to access the containerport of the docker container they have to use the hostport rather than the continerport.
  * ![img_97.png](images/img_97.png) see here we have all the container details like which port they are running at inside the container and to which port they are exposed outside.
  * Suppose look at cardsdb it runs at 3307:3306 means inside the docker env it has it's own container inside which the DB is running at 3306 and the same db is exposed to the host machine at port 3307.
  * ![img_98.png](images/img_98.png) See here while accessing the cardsdb from our localhost we are simply mentioning the localhost:3307 because it is exposed by the docker container to the outside world i mean for the host machine.
  * Cards:
    * ![img_99.png](images/img_99.png)
  * Cardsdb:
    * ![img_100.png](images/img_100.png)
  * Network:
    * ![img_101.png](images/img_101.png)
  * Now observer here we have the cardsdb details here and at the same time if you look at the URL which the cards microservice is using then you will see that it is using the name of the service rather than the hostport because they are all hosted inside the docker env and also inside the same network , because this is how they recognize each other and if in the URL instead of writing the name of the service like cardsdb you write 3307 then it will never be able to connect to the db because now it will think that in it's own local env there must be something at port 3307 which is wrong.
  * One simply analogy think docker networks like your class room all the students of class X D know each other and their names and they communicate with each other using just their name.
    * If Ashish of X(D) needs anything from siddhartha of X(D) then he can directly go and ask for it , but if  Ashish of X(D) needs anything from Ashirbad of X(C) then he has to first take permission of the class teacher then upon allowing him he will go to class X(C) a new network and there he will start looking for Ashirbad.
  * Lets perfom a experiment and check what if i remove the network configurations from the some services and then expect them to work properly:
  * ![img_102.png](images/img_102.png) 
  * IF you observer here then all of our DB's are starting inside the same network that goes by the name microdemo .
  * Now what if i remove this spec and then run the docker compose.
  * ![img_103.png](images/img_103.png) Well the service is no more extending the service.
  * Now let me run the compose file:
  * ![img_104.png](images/img_104.png) See here all the DB they have started but all the microservices got started then they stopped if you check the logs of all of this microservices then you will notice one thing very common.
  * ![img_105.png](images/img_105.png) They are not able to connect to their DB's , even when all of them are running in the docker env.
  * Let us execute a command in the terminal ``docker network ls``
  * ![img_106.png](images/img_106.png) 
  * So we got the details of all the docker networks that are there inside my docker env.
  * we have default_default we have default_microdemo etc .
  * Now let us check the network to which all of our services are connecting themselves.
  * ``docker ps `` to get all the running containers information.
  * ![img_107.png](images/img_107.png) 
  * Let us pick the container id of the 3rd docker container a mysql image that is 68c7c712d819
  * And try to find more info about it.
  * Run ``docker inspect container_id``
  * ![img_108.png](images/img_108.png) 
    * Well you will get this info here it says the NetworkID as well as the DNSNames and also it says default_default.
    * ![img_109.png](images/img_109.png) 
    * ![img_110.png](images/img_110.png)
    * Compare both the network ID's they are the same .
    * Now suppose configserver which is connected at the microdemo network lets check its network config.
    * ![img_111.png](images/img_111.png)
    * Run ``docker inspect container_id``
    * ![img_112.png](images/img_112.png)
    * Now look here at the network name default_microdemo .
    * Look at the NetworkID.
    * ![img_113.png](images/img_113.png) 
    * See here the network ID is same.
    * So there is a high level of isolation inside docker too if you dont mention the network to which your service will be registered.
    * IF you dont mention a seperate network then they will never get registred in the default network .
    * So as can be clearly seen default network and default_microdemo are two seperate networks so the services running inside them cannot communicate properly or as expected.
    * 

## Docker Network Diagram:
* ![docker_network.png](images/docker_network.png)


### 4th commit: .
### Commit Message: " Demonstrated the working of docker networks "
``docker network ls``
``docker inspect container_id``