# 🌐 Spring Cloud Config - Centralized Configuration Management

## 📌 What is Spring Cloud Config?

Spring Cloud Config is a tool used to **externalize and centralize configuration** for multiple applications or microservices.

Instead of keeping configuration inside the application code, configurations are stored externally and fetched at runtime. This allows applications to remain flexible and environment-independent.

---

## ❓ Why was it needed?

In real-world systems, applications run in multiple environments:

- Development (Dev)
- Testing (QA)
- Staging
- Production (Prod)

Each environment requires different configurations such as:

- Database URLs
- API endpoints
- Credentials
- Feature flags

If these configurations are hardcoded inside the application, managing them becomes difficult and risky.
We can load different profiles before running the application file but the main issue is we have to hardcode or keep the config files
inside our application which is not a good practice as you might have seen in the previous V1 folder.

---

## ⚠️ Problems with Hardcoded Configuration

Hardcoding configs inside code is a bad practice because:

- ❌ Requires code changes for every environment
- ❌ Requires rebuilding and redeploying application
- ❌ Increases risk of human error
- ❌ Breaks flexibility of deployment

---

## 🔐 Security Risks (Very Important)

Hardcoding sensitive data like:

- Database passwords
- API keys
- Tokens

can lead to serious issues:

- If pushed to Git, anyone with repo access can see them
- Secrets can accidentally leak in version history
- Even if removed later, they remain in Git commits

This makes the system vulnerable and insecure.

---

## 🔄 Version Control & Leakage Problem

When configs are stored in code:

- Every change gets tracked in Git
- Sensitive data may be exposed in commit history
- Anyone with repository access can view those configs

This is one of the biggest reasons why **external configuration management is required**

---

## 🚀 Benefits of Externalized Configuration

- No need to rebuild application for config changes
- Easy environment switching using profiles
- Centralized control of configuration
- Better security practices
- Scalable for microservices architecture

---

## 📦 Ways to Store & Retrieve Configuration
Incase of Spring we use the spring cloud config which basically is a project of the spring framework which helps us in creating a
external server or an app which is responsible to fetch the different config details and provide it to its cleint (other applications)
so that they can run as per the profile requirement .
We will see more ......

### Lets learn how to create a config server using spring cloud config:

With the above specs we can create a config server.
![img.png](images/img.png)
Now we need to make some changes in the code :
![img_1.png](images/img_1.png)
First we will have to add the @EnableConfigServer annotation and then just simply give this application a port.
what this annoation does is it makes this project to act like a config server.
![img_2.png](images/img_2.png)

### We need to also learn how we can link our microservices with the spring cloud config server so that they can get their configs:



There are multiple ways to manage configuration in Spring-based applications:

### 1️⃣ Classpath (Default)
- Configuration stored inside the application (`application.yml`)
- You can have as many files for different env as you want , classpath means having the config files inside the resources folder that's it.
- Suitable for simple applications
- Not flexible for real-world systems
- lets see how we can perform this:
- First we need to create a config folder inside the resources folder where we will store the different config files of all the applications that we want to register here.
![img_3.png](images/img_3.png)
- ![img_4.png](images/img_4.png)
- We have added the config files of the accounts microservice but we need to change their name as we have 2 more microservices and their name is also like this , so it might create confusion.
- ![img_7.png](images/img_7.png)
- As you can see we have attached all the necessary files with their own name.
- Now we have to do some more changes to the application.yml of the configserver application that is we have to give it details of where all our config files are present.
- ![img_6.png](images/img_6.png)
- Here in the above image profiles.active native means the files are present inside the same system it can be inside the project folder or anyother folder.
- Now cloud.config.server.native.search-locations "classpath:/config" This means the config files are present inside the class path and inside config folder that's it.
- Now inorder to check whether the app can find and load the files or not just simply RUN the app and hit the port on the browser at which the app is running and then followed by the first name of the config file and then the second name for example see below:
- once our app is live then ![img_8.png](images/img_8.png)
- when i am hitting the above URL which is basically :![img_9.png](images/img_9.png) this one then 
- The output that i am getting in the browser is going to be:
- ![img_10.png](images/img_10.png) I am getting both accounts-prod as well as accounts.yml this is normal expected behaviour but while loading the config files into their respecitve app the proper naming is followed and as per their name files gets inserted.
- You can check for other URL's as well if you are getting the configs or not.![img.png](img.png)
- ![img_11.png](images/img_11.png) we have this for cards qa , if you are having any issues with the default one then simply make its name as -dev.
- Now lets see the changes that we have to do at the client side , we have developed our config server properly now we need to make changes in the apps who's configs we have registered here.
- ![img_12.png](images/img_12.png) In the application.yml of accounts microservice we have to just keep the basic info like
- server port , name etc but remove the config settings that you have given to the config server.
- ![img_13.png](images/img_13.png) So this is how it looks now , make sure the name of the application you are giving here should match with the config file name that you have given to the config server.
- ![img_14.png](images/img_14.png) like we have used accounts , accounts-prod , accounts-qa etc so accordingly just give accounts no need to mention prod or qa etc.
- Another important thing is delete these from the accounts microservice as we will be injecting them : ![img_15.png](images/img_15.png)
- ![img_16.png](images/img_16.png) Add this dependency in your accounts microservice pom.xml.
- Make sure you attach and load these things inside the pom.xml ![img_19.png](images/img_19.png)
![img_18.png](images/img_18.png)
![img_17.png](images/img_17.png)
- Now we will have to provide links to the application.yml of accounts microservice so that it can find where our config server is hosted so for that we have:
- ![img_21.png](images/img_21.png) If you look at line no 22 then we have given the property as spring.config.import "configserver:http://localhost:8071/"
- so here we know that the name of our config server app is also configserver and here also we have given configserver but you can set the name of your app as anything ex accountconfigserver etc but in case of setting the property you have to use configserver only so that spring will understand ok this is the config server.
- ![img_22.png](images/img_22.png) Now we have done a small change , optional here means even if you config server is not available still your app will run with it's own properties and files , but if your configserver is very necesssary then just remove this optional and also let us do one thing , lets set a default profile for this account micrservice app.
- ![img_23.png](images/img_23.png) see here qa is the default property , rest we can change while running our app with env variables or maybe set the profiles via cmd.
- lets run our app:
- first run the configserver.
- Now start the accounts microservice .
- ![img_24.png](images/img_24.png) As you can see here our default profile is qa and also it is fetching the config details from the provided URL.
- lets check in postman:
- ![img_25.png](images/img_25.png) we are getting the qa related data.
- lets run the accounts microservice again with the inbuilt CLI arguments of intellij to check whether we are able to change the config or not from default qa to prod.
- ![img_26.png](images/img_26.png) we have set the cli as --spring.profiles.active=prod.
- In the apps log we have prod as active profile now: ![img_27.png](images/img_27.png)
- ![img_28.png](images/img_28.png) In case of postman also we are getting the correct configs.
- In case if you have created any Jar then simply while running your image you can simply set the profiles , you check that with any online sources thats simple.
- There might be a question in your mind that is what if you dont set any default profile and run the app then what will happen .
- Lets check that.
- ![img_29.png](images/img_29.png) we have commented the active line.
- ![img_30.png](images/img_30.png) we got the default profile activated.
- ![img_31.png](images/img_31.png) we got this configs comming in the postman , this is the default accounts.yml which had given to the config server so this one is getting loaded. we have also removed the CLI from the edit configs before running also .
- So even if you dont give the default profile inside your apps application.yml still it will load the config with the name same as your app name.
- ![img_14.png](images/img_14.png) as you can see.
- *** one bonus tip is in Real world industry level applications we dont hardcode the URL also rather we set it during run time using env variables or CLI arguments.
- ![img_32.png](images/img_32.png)
- As can be seen you keep it like this during run time give the details.
---

### 2️⃣ File System
- Config files stored outside the application (local or server path)
- Application reads config from external file location
- More flexible than classpath
- lets start with examples:
- first copy the configs to any folder inside your local system .
- ![img_33.png](images/img_33.png) suppose i have copied all the configs to this folder now do one thing , delete the configs inside you configserver application
- ![img_34.png](images/img_34.png) remove this config folder entirely.
- Now we need to make some changes inside the application.yml of the configserver.
- ![img_35.png](images/img_35.png) see how the serach location looks now first use file then use 3 slashes /// then the location on your pc and instead of one slash use 2 slashes that's it .
- Now lets check whether the config server is able to fetch the details or not by simply hitting the ulr of the configserver only:
- ![img_36.png](images/img_36.png) see we are getting the details now lets just randomly start our accounts app and check it's output in the postman.
- ![img_37.png](images/img_37.png) so it's working fine .
- So finally demonstrated the working of file sytem approach.

---

### 3️⃣ Git Repository (Most Popular 🔥)
- Centralized config stored in Git (GitHub, GitLab, Bitbucket)
- Used with Spring Cloud Config Server
- Supports versioning, rollback, and team collaboration
- Lets see how it's done:
- first we have to make changes in the application.yml of configserver app:
- ![img_38.png](images/img_38.png) Here we will have to set active as git and under that git section we will have to mention all the details of our 
- github config files repo like i have mentioned and the password field that you are seeing is a env variable inside which i have set the PAT value of my own account , likely use your own username and PAT value but make sure the uri is linked to the correct repo
- default lable main means it will take all the info from the main branch of the config files repo.
- timeout 5 means at max our configserver app will take 5 secs to connect to the config files repo after that it will throw error.
- clone-on-start:true means the config files repo will be cloned on our system the very first time it is called and it will be stored in temp folder.
- force-pull is true means
- Now lets start our microservices and check whether we are getting the configs or not:
- Loans microservice where we had set the default active profile as prod:
- ![img_39.png](images/img_39.png)
- cards microservice:
- ![img_40.png](images/img_40.png)
- Finally it is working fine.
- BTW i had to give username and password for the github repo because it is a private repo that's why.
- If the config files repo is public then you dont need any username or password.
- ![img_41.png](images/img_41.png) see how during the startup our configserver is connecting to the git repo although it is not showing it's url but as soon as you switch on any application which needs to connect to the config server to get its config it starts pulling all the configs of that application to it's temp.
- ### How to encrypt the info kept inside the github:
- you need to add a encryption key first to your configserver application.yml .
- Open your gitbash in local and you can create one or you can create one online too.
- ![img_42.png](images/img_42.png) so we have generated a key now copy this key and use it under encrypt.key in application.yml.
- ![img_43.png](images/img_43.png) as can be seen we have used it here now when we run the configserver application it is going to expose some api's like encrypt and decrypt which we can use to encrypt whatever we want.
- ![img_44.png](images/img_44.png) let us encrypt the name and email id field so let's first encrypt the name.
- ![img_45.png](images/img_45.png) in your postman hit the api encrypt of configserver and then pass the name as text in body .
- ![img_46.png](images/img_46.png) so here we got a encrypted text now copy this text into the github repo 
- ![img_47.png](images/img_47.png) we have copied it here now lets change encrypt the email id too with the same mechanism .
- ![img_48.png](images/img_48.png) we have done this too but we need to make some changes here in email that is add {cipher} inside the double quotes before the encrypted value of email.
- ![img_49.png](images/img_49.png) as can be seen now let's simply check what values will we get when we run the accounts app and hit one of its api.
- ![img_50.png](images/img_50.png) if you observer here then we are getting the email in a correct format where as the name is coming as a encrypted value it is because inorder to differentiate between the normal texts and encrypted value we have to add {cipher} before the encrypted text that's it .
- so we you know know where to make the change in the github.
- Now lets check another api decrypt exposed by our config server which can decrypt the encrypted text.
- ![img_51.png](images/img_51.png) see here .

---

### 4️⃣ Environment Variables
- Config passed at runtime (Docker, CI/CD, OS level)
- Commonly used for secrets and environment-specific values
- simply create a env variable in your os and fetch it's detail during run time either through CLI or set defaults inside a application.yml

---

### 5️⃣ Secret Management Tools
- Used for storing sensitive data securely
- Examples:
    - Vault
    - AWS Secrets Manager
    - Azure Key Vault

---

### Refreshing configs during runtime :
Think of a scenario where there is a change in the configs suppose in the username or email or any other field and that is done in the main branch of the config files , and for the new changes to be reflected we have to restart the cleint app.
cofigserver doesn't need any restart as it always fetches the details from the github main branch and not some local repo.
Configserver always fetches the new or latest files from the config files repo whenever the files are required by any application.
But for the microservices like accounts , loans or etc these services need to be restarted because they take the properties only once and that is while they are requesting the configs to the configserver after that no changes are reflected here.

So inorder for the new changes to be reflected while the app is running there is a way by which we dont need to restart the app.
And that is using the actuator .
Adding actuator dependency enables some end points of the app using which we can make so many changes during runtime or refresh the properties also without shutting donw the running instance of the app.
Lets see how we can do that.

So what we need to do is enable the actuator dependency inside all of our microservices because our config server due to a property that is the force-pull: true will always 
pull the latest changes be it in github , or file system or anyother place so we dont need to do anything but our microservice always start with the whatever was initialized during the first startup 
so they need the actuator so that we can refresh their configs without restarting the app.
So make sure that in the pom.xml of all the microservies we have the below.
![img_52.png](images/img_52.png)

*** Another very important change we have to do in one of the DTO classes is we have to change the record class from record to CLASS as in our case we have used the Record type of class inorder to take the info from the application.yml during run time 
and Record class are used to store info only and they have getters but no setters so it is not possible to set values during runtime so we need to change the class type.
### Changes below:
| Name(microservice) | Before                           | After                                |
|--------------------|----------------------------------|--------------------------------------|
| Accounts           | ![img_53.png](images/img_53.png) | ![img_54.png](images/img_54.png)     |
| Cards              | ![img_55.png](images/img_55.png) | ![img_56.png](images/img_56.png)     |
| Loans              | ![img_57.png](images/img_57.png) | High![img_58.png](images/img_58.png) |

We have turned the record into class and added the necessary annotation of setter and getter .

* Now lets expose the actuator end-points because by default all it's end points are not exposed:
* ![img_59.png](images/img_59.png) Do this inside all of your application.properties to expose all the api end points of actuator otherwise you can also specify which all api's you want instead of having all.
* Now lets see how will it help us.


### How the changes reflect during run-time:
* First build all your app's and then start the cofig server first then start all the microservices.
* In our case by default the prod profile is active for all the microservices so lets have a look at the prod configs and what we are getting in the postman.
* Lets have a look at accounts/get-contact-info api:
* ![img_60.png](images/img_60.png) This is what we are getting and lets also see what's there in the github too:
* ![img_61.png](images/img_61.png) This is in github configs so let me try to do a change in the github and see whether it is reflecting in our app or not.
* ![img_62.png](images/img_62.png) Suppose here i have change the name field and commit the changes now let's see if the same change is reflecting in our app or not.
* ![img_63.png](images/img_63.png) The changed name has not yet reflected in our accounts app why and how , before that just check the configserver app.
* ![img_64.png](images/img_64.png) if we observe here then the new changed field is getting reflected here but not in our accounts app why , it is because our configserver always pulls the latest changes even without us manually doing anything to the configserver app but our microservice application is taking all the values it needs during the startup only that's why now even after chaning the value in github and configserver reflecting it still our microservice is not taking it as it only contacts the configserver during startup that's it.
* So how what should we do for our app to reflect the new changes.
* We will use the actuator of our accounts app .
* ![img_65.png](images/img_65.png) if you use the /actuator after the server location of our accounts app then you will get a list of api's which the actuator offers.
* From this we need the refresh api.
* ![img_66.png](images/img_66.png) we need this refresh api , what it will do is without restarting our app it will take all the configs again during run-time.
* ![img_67.png](images/img_67.png) But here is a problem if we try to invoke this api then it will not work as it doesn't support GET Method so we have to use the POST method in Postman to make it work.
* ![img_68.png](images/img_68.png) As soon as we hit the actuator api of the accounts app we get the respone , now call the get-contact-info api and see the magic.
* Btw what does the above response mean it means config version of the app was changed and accounts.contactDetails.name was the changed config.
* ![img_69.png](images/img_69.png) See this time we finally got the updated values reflected here .
* Lets repeate all the above steps for all the microservices.

### Lets change the configs of all the microservice and oberve the challenges:
* Lets just change the message from prod to PRODUCTION 
* | Name(microservice) | Before                           | After                            |
  |--------------------|----------------------------------|----------------------------------|
  | Accounts           | ![img_70.png](images/img_70.png) | ![img_71.png](images/img_71.png) |
  | Cards              | ![img_72.png](images/img_72.png) | ![img_73.png](images/img_73.png) |
  | Loans              | ![img_74.png](images/img_74.png) | ![img_75.png](images/img_75.png) |

* SO as can be seen we have just changed the message field and we know that configserver will pull all the new changes but for individual microservices the changes will only be reflected upon calling their respective actuator in POST Method.
* ![img_76.png](images/img_76.png) Accounts is still showing the old info.
* ![img_77.png](images/img_77.png) Cards is still showing the old info.
* ![img_78.png](images/img_78.png) Loans is still showing the old info.
* Lets call the individual actuators:
* Accounts: ![img_79.png](images/img_79.png) ![img_80.png](images/img_80.png)
* Lets do a check just by calling accounts actuator will other apps also reflect the changes?
* ![img_81.png](images/img_81.png) Loans is still showing the old value.
* So we will have to call individual actuators only.
* Loans: ![img_82.png](images/img_82.png) ![img_83.png](images/img_83.png)
* Cards: ![img_84.png](images/img_84.png) ![img_85.png](images/img_85.png)
* 

### We understood a way to refresh configs of our apps but there is a big problem here.

> The issue is we have to individually call the actuators of all the respective app's.
> In production scenario we have 100's and 1000's maybe more microservices running and most importantly multiple instances of same app are running.
> Do you think you can manually do the task of individually calling the actuator for all ?
> In that case there will be abnormal activity across your app's like one instance will be returing x while other be returing Y so that is not a good scenario .
> So let's look for a way by which we dont have to refresh everything manually.


### Spring-cloud-bus to refresh the configs across multiple apps or instances just by hitting one api:
* You can read more about spring cloud bus at spring.io/projects/spring-cloud-bus.
* Bascially spring cloud bus is a project by spring which helps applications in production and consuming a global event like change of configurations or other management instructions .
* Suppose there is a change in configs of one of the application so when you hit the api /actuator/busrefresh what it does is , it refreshes the app as well as broadcasts a message that the current app has triggered a busrefresh event and other apps should also get their configurations refreshed .
* Well bus is the producer as well as consumer so when the global event is received by other apps they have the spring cloud bus dependency too so they also consume the message and get themselves refreshed.
* But the bus can only produce and consume the event it cannot transfer the event so we now need a message broker like kafka or rabbitmq which will act as a message broker and will carry the event/message to other services or app's which are registered to the rabbitmq.
* Adding the bus dependency is necessary to all the services althought the buses of different microservices are independent and dont talk directly to each other but still it is important as bus produces and consumes info.
* let understand step by step: first add the spring cloud bus dependency to all the app's then either install and start message broker (RabbitMq) or run it in a docker container and expose it.
* Then add the rabbitmq details to the applications.yml of all the apps so that they are registerd with the same message broker otherwise how will they get the message.
* once the above is done , now after adding bus dependency the actuator api gets extended to two more api's of the bus , so we need to trigger the /actuator/busrefresh of any one of the microservice's .
* So what the above step will do is it will first refresh the configs of the current app that invoked the busrefresh then the bus dependency of the current app will publish this message that "the current app triggered a busrefresh api" to the rabbitmq and from there all other apps who are registered with the same message broker server will consume the message via their own bus dependency and they will also refresh themselves.
* This is how just by trigger in only 1 app the configs across all the registered app's get reflected.
* You can also hit the bus refresh of the cofigserver for that add the dependency and also register the configserver to the same messagebroker it will publish a event.
* But configserver even if you dont refresh still it always pulls the latest changes , it is the apps that dont fetch the latest changes unless poked.

### Lets add the dependency and setup the rabbitmq and see how it works:
* If you go to the spring initializer then you will get this ![img_86.png](images/img_86.png).
* But dont use it because it will only give you the bus and no rabbitmq or kafka implementation so , simply go to google and ask for spring cloud bus rabbitmq ![img_87.png](images/img_87.png) it will give you this and this is what you need to add .
* It has both spring cloud bus and rabbitmq integrated.
* Now we need to add this dependency to all our microservices including configserver too.
* ![img_88.png](images/img_88.png) Make sure this is enabled in all of the services even in your configserver too because this will expose all the api's of actuator and ahead.
* Even in google you will get all the steps of how to do the setup.
* Now we need to either download rabbitmq or start it in a docker container.
* goto https://www.rabbitmq.com/docs/download From here you will get all the info ![img_89.png](images/img_89.png).
* just copy the docker commands and then run that in your local.
* it will start the docker image , to check just see the running containers in your system you will have a idea.
* It is running on port 5672:5672 you can also check it's ui by going to http://localhost:15672 here provide the details which is guest guest.
* Now we will register our services to this rabbitmq.
* ![img_90.png](images/img_90.png) mention this in all of your microservices as well as configserver and yes this comes under spring.
* spring:rabbitmq and then so on.
* Once you have mentioned that under all of your services , now when you will start all the services they will get themselves registerd to that rabbitmq.
* ![img_91.png](images/img_91.png) This is how the rabbitmq management looks before we started our apps .
* ![img_92.png](images/img_92.png) Now if you look at the connections it shows 4 lets just got to the connections tab.
* ![img_93.png](images/img_93.png) Here we have 4 connections which includes our 3 microservices and 1 configserver.
* Now lets change the configs in the github and then we will hit the busrefresh api for any one of the service.
* Before: Accounts-prod: ![img_94.png](images/img_94.png) After: Accounts-prod: ![img_95.png](images/img_95.png)
* Before: Cards-prod: ![img_96.png](images/img_96.png) After: Cards-prod: ![img_97.png](images/img_97.png)
* Before: Loans-prod: ![img_98.png](images/img_98.png) After: Loans-prod: ![img_99.png](images/img_99.png)
* so we have done the changes now lets check what our configserver is giving before we hit the api: ![img_100.png](images/img_100.png) well it is pulling the new data.
* Lets check what our get-contact-info api's are returning: 
* Cards: ![img_101.png](images/img_101.png) 
* Accounts: ![img_102.png](images/img_102.png)
* Loans: ![img_103.png](images/img_103.png) 
* So we are still getting the old data .
* just hit actuator to see the new apis which are exposed: ![img_104.png](images/img_104.png).
* We have the bus api too.
* Lets just hit the actuator/busrefresh of anyone of the services lets do that for accounts.
* before hitting: ![img_105.png](images/img_105.png) Response: ![img_106.png](images/img_106.png) we will get this 204 success reponse .
* Now hit the get-contact-info api for all the services and check if they refreshed their config or not.
* Accounts: ![img_107.png](images/img_107.png)
* Cards:![img_108.png](images/img_108.png)
* Loans: ![img_109.png](images/img_109.png)
* See with just one api of any one app all got refreshed , you can hit the busrefresh for your config server too.

### There is a minor issue here that is everytime we have to manually busrefresh anyone app connected then only others will reflect the changes , What if we could trigger a event from github only even without disturbing or manually calling the busrefresh , i mean right after we have done any changes in the repo it will automatically refresh the configs of all the apps.
### For that there is a solution and that is using github webhook's.
* What is github webhook actually ?
* A webhook is event driven HTTP callback mechanism in which one application automatically sends an HTTP Request (Usually a POST Request) to another applications URL when a specific event occurs.
* Ex: Suppose a new code is pushed then the system sends a POST request to the target system.
* In our case the Github webhook is a webhook service provided by github that automatically sends a HTTP Request to a configured URL whenever a specific(selected) repository event occurs.
* ![img_110.png](images/img_110.png)
* In our case we said if someone changes the configs maybe in their local system and pushes it to the main github repo or pushes a change in github repo itself then a POST request will be triggered by that repo and it will be sent to a given URL , like notifying that a action took place.
* In our case we will add the spring cloud config monitor dependency to the configserver application what it will do is it is responsible to process the incoming webhook request and will broadcast a busrefresh event to the message broker it is connected to so that all the services that were connected to the same message broker would implement it without restarting.
* Lets understand step by step.
* First add the spring cloud config monitor dependency to the configserver app , you will get the dependency details from mvnrepository.
* ![img_111.png](images/img_111.png) or simply ask google to give it to you.
* Now once you add this dependency to the configserver now check if you have made your configserver registered at the desired messagebroker rabbitmq or not ![img_112.png](images/img_112.png).
* Now the next thing to do is goto your config files repo and setup the webhook -> go to settings -> left side you will see webhook -> click on it -> Add webhook.
* 






## 💡 Key Takeaway

> Configuration should never be tightly coupled with code.  
> It should be external, secure, and manageable across environments.
# 🌐 Spring Cloud Config - Centralized Configuration Management

## 📌 What is Spring Cloud Config?

Spring Cloud Config is a tool used to **externalize and centralize configuration** for multiple applications or microservices.

Instead of keeping configuration inside the application code, configurations are stored externally and fetched at runtime. This allows applications to remain flexible and environment-independent.

---

## ❓ Why was it needed?

In real-world systems, applications run in multiple environments:

- Development (Dev)
- Testing (QA)
- Staging
- Production (Prod)

Each environment requires different configurations such as:

- Database URLs
- API endpoints
- Credentials
- Feature flags

If these configurations are hardcoded inside the application, managing them becomes difficult and risky.
We can load different profiles before running the application file but the main issue is we have to hardcode or keep the config files
inside our application which is not a good practice as you might have seen in the previous V1 folder.

---

## ⚠️ Problems with Hardcoded Configuration

Hardcoding configs inside code is a bad practice because:

- ❌ Requires code changes for every environment
- ❌ Requires rebuilding and redeploying application
- ❌ Increases risk of human error
- ❌ Breaks flexibility of deployment

---

## 🔐 Security Risks (Very Important)

Hardcoding sensitive data like:

- Database passwords
- API keys
- Tokens

can lead to serious issues:

- If pushed to Git, anyone with repo access can see them
- Secrets can accidentally leak in version history
- Even if removed later, they remain in Git commits

This makes the system vulnerable and insecure.

---

## 🔄 Version Control & Leakage Problem

When configs are stored in code:

- Every change gets tracked in Git
- Sensitive data may be exposed in commit history
- Anyone with repository access can view those configs

This is one of the biggest reasons why **external configuration management is required**

---

## 🚀 Benefits of Externalized Configuration

- No need to rebuild application for config changes
- Easy environment switching using profiles
- Centralized control of configuration
- Better security practices
- Scalable for microservices architecture

---

## 📦 Ways to Store & Retrieve Configuration
Incase of Spring we use the spring cloud config which basically is a project of the spring framework which helps us in creating a
external server or an app which is responsible to fetch the different config details and provide it to its cleint (other applications)
so that they can run as per the profile requirement .
We will see more ......

### Lets learn how to create a config server using spring cloud config:

With the above specs we can create a config server.
![img.png](images/img.png)
Now we need to make some changes in the code :
![img_1.png](images/img_1.png)
First we will have to add the @EnableConfigServer annotation and then just simply give this application a port.
what this annoation does is it makes this project to act like a config server.
![img_2.png](images/img_2.png)

### We need to also learn how we can link our microservices with the spring cloud config server so that they can get their configs:



There are multiple ways to manage configuration in Spring-based applications:

### 1️⃣ Classpath (Default)
- Configuration stored inside the application (`application.yml`)
- You can have as many files for different env as you want , classpath means having the config files inside the resources folder that's it.
- Suitable for simple applications
- Not flexible for real-world systems
- lets see how we can perform this:
- First we need to create a config folder inside the resources folder where we will store the different config files of all the applications that we want to register here.
![img_3.png](images/img_3.png)
- ![img_4.png](images/img_4.png)
- We have added the config files of the accounts microservice but we need to change their name as we have 2 more microservices and their name is also like this , so it might create confusion.
- ![img_7.png](images/img_7.png)
- As you can see we have attached all the necessary files with their own name.
- Now we have to do some more changes to the application.yml of the configserver application that is we have to give it details of where all our config files are present.
- ![img_6.png](images/img_6.png)
- Here in the above image profiles.active native means the files are present inside the same system it can be inside the project folder or anyother folder.
- Now cloud.config.server.native.search-locations "classpath:/config" This means the config files are present inside the class path and inside config folder that's it.
- Now inorder to check whether the app can find and load the files or not just simply RUN the app and hit the port on the browser at which the app is running and then followed by the first name of the config file and then the second name for example see below:
- once our app is live then ![img_8.png](images/img_8.png)
- when i am hitting the above URL which is basically :![img_9.png](images/img_9.png) this one then 
- The output that i am getting in the browser is going to be:
- ![img_10.png](images/img_10.png) I am getting both accounts-prod as well as accounts.yml this is normal expected behaviour but while loading the config files into their respecitve app the proper naming is followed and as per their name files gets inserted.
- You can check for other URL's as well if you are getting the configs or not.![img.png](img.png)
- ![img_11.png](images/img_11.png) we have this for cards qa , if you are having any issues with the default one then simply make its name as -dev.
- Now lets see the changes that we have to do at the client side , we have developed our config server properly now we need to make changes in the apps who's configs we have registered here.
- ![img_12.png](images/img_12.png) In the application.yml of accounts microservice we have to just keep the basic info like
- server port , name etc but remove the config settings that you have given to the config server.
- ![img_13.png](images/img_13.png) So this is how it looks now , make sure the name of the application you are giving here should match with the config file name that you have given to the config server.
- ![img_14.png](images/img_14.png) like we have used accounts , accounts-prod , accounts-qa etc so accordingly just give accounts no need to mention prod or qa etc.
- Another important thing is delete these from the accounts microservice as we will be injecting them : ![img_15.png](images/img_15.png)
- ![img_16.png](images/img_16.png) Add this dependency in your accounts microservice pom.xml.
- Make sure you attach and load these things inside the pom.xml ![img_19.png](images/img_19.png)
![img_18.png](images/img_18.png)
![img_17.png](images/img_17.png)
- Now we will have to provide links to the application.yml of accounts microservice so that it can find where our config server is hosted so for that we have:
- ![img_21.png](images/img_21.png) If you look at line no 22 then we have given the property as spring.config.import "configserver:http://localhost:8071/"
- so here we know that the name of our config server app is also configserver and here also we have given configserver but you can set the name of your app as anything ex accountconfigserver etc but in case of setting the property you have to use configserver only so that spring will understand ok this is the config server.
- ![img_22.png](images/img_22.png) Now we have done a small change , optional here means even if you config server is not available still your app will run with it's own properties and files , but if your configserver is very necesssary then just remove this optional and also let us do one thing , lets set a default profile for this account micrservice app.
- ![img_23.png](images/img_23.png) see here qa is the default property , rest we can change while running our app with env variables or maybe set the profiles via cmd.
- lets run our app:
- first run the configserver.
- Now start the accounts microservice .
- ![img_24.png](images/img_24.png) As you can see here our default profile is qa and also it is fetching the config details from the provided URL.
- lets check in postman:
- ![img_25.png](images/img_25.png) we are getting the qa related data.
- lets run the accounts microservice again with the inbuilt CLI arguments of intellij to check whether we are able to change the config or not from default qa to prod.
- ![img_26.png](images/img_26.png) we have set the cli as --spring.profiles.active=prod.
- In the apps log we have prod as active profile now: ![img_27.png](images/img_27.png)
- ![img_28.png](images/img_28.png) In case of postman also we are getting the correct configs.
- In case if you have created any Jar then simply while running your image you can simply set the profiles , you check that with any online sources thats simple.
- There might be a question in your mind that is what if you dont set any default profile and run the app then what will happen .
- Lets check that.
- ![img_29.png](images/img_29.png) we have commented the active line.
- ![img_30.png](images/img_30.png) we got the default profile activated.
- ![img_31.png](images/img_31.png) we got this configs comming in the postman , this is the default accounts.yml which had given to the config server so this one is getting loaded. we have also removed the CLI from the edit configs before running also .
- So even if you dont give the default profile inside your apps application.yml still it will load the config with the name same as your app name.
- ![img_14.png](images/img_14.png) as you can see.
- *** one bonus tip is in Real world industry level applications we dont hardcode the URL also rather we set it during run time using env variables or CLI arguments.
- ![img_32.png](images/img_32.png)
- As can be seen you keep it like this during run time give the details.
---

### 2️⃣ File System
- Config files stored outside the application (local or server path)
- Application reads config from external file location
- More flexible than classpath
- lets start with examples:
- first copy the configs to any folder inside your local system .
- ![img_33.png](images/img_33.png) suppose i have copied all the configs to this folder now do one thing , delete the configs inside you configserver application
- ![img_34.png](images/img_34.png) remove this config folder entirely.
- Now we need to make some changes inside the application.yml of the configserver.
- ![img_35.png](images/img_35.png) see how the serach location looks now first use file then use 3 slashes /// then the location on your pc and instead of one slash use 2 slashes that's it .
- Now lets check whether the config server is able to fetch the details or not by simply hitting the ulr of the configserver only:
- ![img_36.png](images/img_36.png) see we are getting the details now lets just randomly start our accounts app and check it's output in the postman.
- ![img_37.png](images/img_37.png) so it's working fine .
- So finally demonstrated the working of file sytem approach.

---

### 3️⃣ Git Repository (Most Popular 🔥)
- Centralized config stored in Git (GitHub, GitLab, Bitbucket)
- Used with Spring Cloud Config Server
- Supports versioning, rollback, and team collaboration
- Lets see how it's done:
- first we have to make changes in the application.yml of configserver app:
- ![img_38.png](images/img_38.png) Here we will have to set active as git and under that git section we will have to mention all the details of our 
- github config files repo like i have mentioned and the password field that you are seeing is a env variable inside which i have set the PAT value of my own account , likely use your own username and PAT value but make sure the uri is linked to the correct repo
- default lable main means it will take all the info from the main branch of the config files repo.
- timeout 5 means at max our configserver app will take 5 secs to connect to the config files repo after that it will throw error.
- clone-on-start:true means the config files repo will be cloned on our system the very first time it is called and it will be stored in temp folder.
- force-pull is true means
- Now lets start our microservices and check whether we are getting the configs or not:
- Loans microservice where we had set the default active profile as prod:
- ![img_39.png](images/img_39.png)
- cards microservice:
- ![img_40.png](images/img_40.png)
- Finally it is working fine.
- BTW i had to give username and password for the github repo because it is a private repo that's why.
- If the config files repo is public then you dont need any username or password.
- ![img_41.png](images/img_41.png) see how during the startup our configserver is connecting to the git repo although it is not showing it's url but as soon as you switch on any application which needs to connect to the config server to get its config it starts pulling all the configs of that application to it's temp.
- ### How to encrypt the info kept inside the github:
- you need to add a encryption key first to your configserver application.yml .
- Open your gitbash in local and you can create one or you can create one online too.
- ![img_42.png](images/img_42.png) so we have generated a key now copy this key and use it under encrypt.key in application.yml.
- ![img_43.png](images/img_43.png) as can be seen we have used it here now when we run the configserver application it is going to expose some api's like encrypt and decrypt which we can use to encrypt whatever we want.
- ![img_44.png](images/img_44.png) let us encrypt the name and email id field so let's first encrypt the name.
- ![img_45.png](images/img_45.png) in your postman hit the api encrypt of configserver and then pass the name as text in body .
- ![img_46.png](images/img_46.png) so here we got a encrypted text now copy this text into the github repo 
- ![img_47.png](images/img_47.png) we have copied it here now lets change encrypt the email id too with the same mechanism .
- ![img_48.png](images/img_48.png) we have done this too but we need to make some changes here in email that is add {cipher} inside the double quotes before the encrypted value of email.
- ![img_49.png](images/img_49.png) as can be seen now let's simply check what values will we get when we run the accounts app and hit one of its api.
- ![img_50.png](images/img_50.png) if you observer here then we are getting the email in a correct format where as the name is coming as a encrypted value it is because inorder to differentiate between the normal texts and encrypted value we have to add {cipher} before the encrypted text that's it .
- so we you know know where to make the change in the github.
- Now lets check another api decrypt exposed by our config server which can decrypt the encrypted text.
- ![img_51.png](images/img_51.png) see here .

---

### 4️⃣ Environment Variables
- Config passed at runtime (Docker, CI/CD, OS level)
- Commonly used for secrets and environment-specific values
- simply create a env variable in your os and fetch it's detail during run time either through CLI or set defaults inside a application.yml

---

### 5️⃣ Secret Management Tools
- Used for storing sensitive data securely
- Examples:
    - Vault
    - AWS Secrets Manager
    - Azure Key Vault

---

### Refreshing configs during runtime :
Think of a scenario where there is a change in the configs suppose in the username or email or any other field and that is done in the main branch of the config files , and for the new changes to be reflected we have to restart the cleint app.
cofigserver doesn't need any restart as it always fetches the details from the github main branch and not some local repo.
Configserver always fetches the new or latest files from the config files repo whenever the files are required by any application.
But for the microservices like accounts , loans or etc these services need to be restarted because they take the properties only once and that is while they are requesting the configs to the configserver after that no changes are reflected here.

So inorder for the new changes to be reflected while the app is running there is a way by which we dont need to restart the app.
And that is using the actuator .
Adding actuator dependency enables some end points of the app using which we can make so many changes during runtime or refresh the properties also without shutting donw the running instance of the app.
Lets see how we can do that.

So what we need to do is enable the actuator dependency inside all of our microservices because our config server due to a property that is the force-pull: true will always 
pull the latest changes be it in github , or file system or anyother place so we dont need to do anything but our microservice always start with the whatever was initialized during the first startup 
so they need the actuator so that we can refresh their configs without restarting the app.
So make sure that in the pom.xml of all the microservies we have the below.
![img_52.png](images/img_52.png)

*** Another very important change we have to do in one of the DTO classes is we have to change the record class from record to CLASS as in our case we have used the Record type of class inorder to take the info from the application.yml during run time 
and Record class are used to store info only and they have getters but no setters so it is not possible to set values during runtime so we need to change the class type.
### Changes below:
| Name(microservice) | Before                           | After                                |
|--------------------|----------------------------------|--------------------------------------|
| Accounts           | ![img_53.png](images/img_53.png) | ![img_54.png](images/img_54.png)     |
| Cards              | ![img_55.png](images/img_55.png) | ![img_56.png](images/img_56.png)     |
| Loans              | ![img_57.png](images/img_57.png) | High![img_58.png](images/img_58.png) |

We have turned the record into class and added the necessary annotation of setter and getter .

* Now lets expose the actuator end-points because by default all it's end points are not exposed:
* ![img_59.png](images/img_59.png) Do this inside all of your application.properties to expose all the api end points of actuator otherwise you can also specify which all api's you want instead of having all.
* Now lets see how will it help us.


### How the changes reflect during run-time:
* First build all your app's and then start the cofig server first then start all the microservices.
* In our case by default the prod profile is active for all the microservices so lets have a look at the prod configs and what we are getting in the postman.
* Lets have a look at accounts/get-contact-info api:
* ![img_60.png](images/img_60.png) This is what we are getting and lets also see what's there in the github too:
* ![img_61.png](images/img_61.png) This is in github configs so let me try to do a change in the github and see whether it is reflecting in our app or not.
* ![img_62.png](images/img_62.png) Suppose here i have change the name field and commit the changes now let's see if the same change is reflecting in our app or not.
* ![img_63.png](images/img_63.png) The changed name has not yet reflected in our accounts app why and how , before that just check the configserver app.
* ![img_64.png](images/img_64.png) if we observe here then the new changed field is getting reflected here but not in our accounts app why , it is because our configserver always pulls the latest changes even without us manually doing anything to the configserver app but our microservice application is taking all the values it needs during the startup only that's why now even after chaning the value in github and configserver reflecting it still our microservice is not taking it as it only contacts the configserver during startup that's it.
* So how what should we do for our app to reflect the new changes.
* We will use the actuator of our accounts app .
* ![img_65.png](images/img_65.png) if you use the /actuator after the server location of our accounts app then you will get a list of api's which the actuator offers.
* From this we need the refresh api.
* ![img_66.png](images/img_66.png) we need this refresh api , what it will do is without restarting our app it will take all the configs again during run-time.
* ![img_67.png](images/img_67.png) But here is a problem if we try to invoke this api then it will not work as it doesn't support GET Method so we have to use the POST method in Postman to make it work.
* ![img_68.png](images/img_68.png) As soon as we hit the actuator api of the accounts app we get the respone , now call the get-contact-info api and see the magic.
* Btw what does the above response mean it means config version of the app was changed and accounts.contactDetails.name was the changed config.
* ![img_69.png](images/img_69.png) See this time we finally got the updated values reflected here .
* Lets repeate all the above steps for all the microservices.

### Lets change the configs of all the microservice and oberve the challenges:
* Lets just change the message from prod to PRODUCTION 
* | Name(microservice) | Before                           | After                            |
  |--------------------|----------------------------------|----------------------------------|
  | Accounts           | ![img_70.png](images/img_70.png) | ![img_71.png](images/img_71.png) |
  | Cards              | ![img_72.png](images/img_72.png) | ![img_73.png](images/img_73.png) |
  | Loans              | ![img_74.png](images/img_74.png) | ![img_75.png](images/img_75.png) |

* SO as can be seen we have just changed the message field and we know that configserver will pull all the new changes but for individual microservices the changes will only be reflected upon calling their respective actuator in POST Method.
* ![img_76.png](images/img_76.png) Accounts is still showing the old info.
* ![img_77.png](images/img_77.png) Cards is still showing the old info.
* ![img_78.png](images/img_78.png) Loans is still showing the old info.
* Lets call the individual actuators:
* Accounts: ![img_79.png](images/img_79.png) ![img_80.png](images/img_80.png)
* Lets do a check just by calling accounts actuator will other apps also reflect the changes?
* ![img_81.png](images/img_81.png) Loans is still showing the old value.
* So we will have to call individual actuators only.
* Loans: ![img_82.png](images/img_82.png) ![img_83.png](images/img_83.png)
* Cards: ![img_84.png](images/img_84.png) ![img_85.png](images/img_85.png)
* 

### We understood a way to refresh configs of our apps but there is a big problem here.

> The issue is we have to individually call the actuators of all the respective app's.
> In production scenario we have 100's and 1000's maybe more microservices running and most importantly multiple instances of same app are running.
> Do you think you can manually do the task of individually calling the actuator for all ?
> In that case there will be abnormal activity across your app's like one instance will be returing x while other be returing Y so that is not a good scenario .
> So let's look for a way by which we dont have to refresh everything manually.


### Spring-cloud-bus to refresh the configs across multiple apps or instances just by hitting one api:
* You can read more about spring cloud bus at spring.io/projects/spring-cloud-bus.
* Bascially spring cloud bus is a project by spring which helps applications in production and consuming a global event like change of configurations or other management instructions .
* Suppose there is a change in configs of one of the application so when you hit the api /actuator/busrefresh what it does is , it refreshes the app as well as broadcasts a message that the current app has triggered a busrefresh event and other apps should also get their configurations refreshed .
* Well bus is the producer as well as consumer so when the global event is received by other apps they have the spring cloud bus dependency too so they also consume the message and get themselves refreshed.
* But the bus can only produce and consume the event it cannot transfer the event so we now need a message broker like kafka or rabbitmq which will act as a message broker and will carry the event/message to other services or app's which are registered to the rabbitmq.
* Adding the bus dependency is necessary to all the services althought the buses of different microservices are independent and dont talk directly to each other but still it is important as bus produces and consumes info.
* let understand step by step: first add the spring cloud bus dependency to all the app's then either install and start message broker (RabbitMq) or run it in a docker container and expose it.
* Then add the rabbitmq details to the applications.yml of all the apps so that they are registerd with the same message broker otherwise how will they get the message.
* once the above is done , now after adding bus dependency the actuator api gets extended to two more api's of the bus , so we need to trigger the /actuator/busrefresh of any one of the microservice's .
* So what the above step will do is it will first refresh the configs of the current app that invoked the busrefresh then the bus dependency of the current app will publish this message that "the current app triggered a busrefresh api" to the rabbitmq and from there all other apps who are registered with the same message broker server will consume the message via their own bus dependency and they will also refresh themselves.
* This is how just by trigger in only 1 app the configs across all the registered app's get reflected.
* You can also hit the bus refresh of the cofigserver for that add the dependency and also register the configserver to the same messagebroker it will publish a event.
* But configserver even if you dont refresh still it always pulls the latest changes , it is the apps that dont fetch the latest changes unless poked.

### Lets add the dependency and setup the rabbitmq and see how it works:
* If you go to the spring initializer then you will get this ![img_86.png](images/img_86.png).
* But dont use it because it will only give you the bus and no rabbitmq or kafka implementation so , simply go to google and ask for spring cloud bus rabbitmq ![img_87.png](images/img_87.png) it will give you this and this is what you need to add .
* It has both spring cloud bus and rabbitmq integrated.
* Now we need to add this dependency to all our microservices including configserver too.
* ![img_88.png](images/img_88.png) Make sure this is enabled in all of the services even in your configserver too because this will expose all the api's of actuator and ahead.
* Even in google you will get all the steps of how to do the setup.
* Now we need to either download rabbitmq or start it in a docker container.
* goto https://www.rabbitmq.com/docs/download From here you will get all the info ![img_89.png](images/img_89.png).
* just copy the docker commands and then run that in your local.
* it will start the docker image , to check just see the running containers in your system you will have a idea.
* It is running on port 5672:5672 you can also check it's ui by going to http://localhost:15672 here provide the details which is guest guest.
* Now we will register our services to this rabbitmq.
* ![img_90.png](images/img_90.png) mention this in all of your microservices as well as configserver and yes this comes under spring.
* spring:rabbitmq and then so on.
* Once you have mentioned that under all of your services , now when you will start all the services they will get themselves registerd to that rabbitmq.
* ![img_91.png](images/img_91.png) This is how the rabbitmq management looks before we started our apps .
* ![img_92.png](images/img_92.png) Now if you look at the connections it shows 4 lets just got to the connections tab.
* ![img_93.png](images/img_93.png) Here we have 4 connections which includes our 3 microservices and 1 configserver.
* Now lets change the configs in the github and then we will hit the busrefresh api for any one of the service.
* Before: Accounts-prod: ![img_94.png](images/img_94.png) After: Accounts-prod: ![img_95.png](images/img_95.png)
* Before: Cards-prod: ![img_96.png](images/img_96.png) After: Cards-prod: ![img_97.png](images/img_97.png)
* Before: Loans-prod: ![img_98.png](images/img_98.png) After: Loans-prod: ![img_99.png](images/img_99.png)
* so we have done the changes now lets check what our configserver is giving before we hit the api: ![img_100.png](images/img_100.png) well it is pulling the new data.
* Lets check what our get-contact-info api's are returning: 
* Cards: ![img_101.png](images/img_101.png) 
* Accounts: ![img_102.png](images/img_102.png)
* Loans: ![img_103.png](images/img_103.png) 
* So we are still getting the old data .
* just hit actuator to see the new apis which are exposed: ![img_104.png](images/img_104.png).
* We have the bus api too.
* Lets just hit the actuator/busrefresh of anyone of the services lets do that for accounts.
* before hitting: ![img_105.png](images/img_105.png) Response: ![img_106.png](images/img_106.png) we will get this 204 success reponse .
* Now hit the get-contact-info api for all the services and check if they refreshed their config or not.
* Accounts: ![img_107.png](images/img_107.png)
* Cards:![img_108.png](images/img_108.png)
* Loans: ![img_109.png](images/img_109.png)
* See with just one api of any one app all got refreshed , you can hit the busrefresh for your config server too.

### There is a minor issue here that is everytime we have to manually busrefresh anyone app connected then only others will reflect the changes , What if we could trigger a event from github only even without disturbing or manually calling the busrefresh , i mean right after we have done any changes in the repo it will automatically refresh the configs of all the apps.
### For that there is a solution and that is using github webhook's.
* What is github webhook actually ?
* A webhook is event driven HTTP callback mechanism in which one application automatically sends an HTTP Request (Usually a POST Request) to another applications URL when a specific event occurs.
* Ex: Suppose a new code is pushed then the system sends a POST request to the target system.
* In our case the Github webhook is a webhook service provided by github that automatically sends a HTTP Request to a configured URL whenever a specific(selected) repository event occurs.
* ![img_110.png](images/img_110.png)
* In our case we said if someone changes the configs maybe in their local system and pushes it to the main github repo or pushes a change in github repo itself then a POST request will be triggered by that repo and it will be sent to a given URL , like notifying that a action took place.
* In our case we will add the spring cloud config monitor dependency to the configserver application what it will do is it is responsible to process the incoming webhook request and will broadcast a busrefresh event to the message broker it is connected to so that all the services that were connected to the same message broker would implement it without restarting.
* Lets understand step by step.
* First add the spring cloud config monitor dependency to the configserver app , you will get the dependency details from mvnrepository.
* ![img_111.png](images/img_111.png) or simply ask google to give it to you.
* Now once you add this dependency to the configserver now check if you have made your configserver registered at the desired messagebroker rabbitmq or not ![img_112.png](images/img_112.png).
* Now the next thing to do is goto your config files repo and setup the webhook -> go to settings -> left side you will see webhook -> click on it -> Add webhook.
* we actaully have to give it the url of the configserver/monitor .
* ![img_113.png](images/img_113.png) We have given the details but do you think it will work by any chance , like how on earth will github know what is that localhost.
* In production or real applications the configserver is hosted in cloud servers so we have a address but what willl you do here .
* So just for the testing purpose let us just use ngrok , this a platform which is used inorder to test the webhooks.
* First go to the ngrok official webpage.
* search for ngrok windows download then , ![img_117.png](images/img_117.png) select as per your OS then download then you will get a zip file simply extract that and inside that there will be a exe file just open it , you will be redirected to a cmd line.
* But most importantly you need the auth token given by ngrok so for that simply signin or signup ![img_118.png](images/img_118.png).
* Once you signin into your ngrok account ![img_119.png](images/img_119.png) in this page scroll below and you will find your ngrok auth token as well as the command that you need to run on your local system.
* copy the command and run it in the cmd either your local cmd or the cmd which opened after you clicked on the ngrok exe present inside the extracted folder.
* ![img_120.png](images/img_120.png) open this exe and the ngrok will run in your local but make sure you run the command to set the auth token otherwise it wont be able to connect to your online account neither it will create a public testing URL for your app.
* Now once you set the auth token now lets see how we can expose our configserver to the github webhook.
* First run the rabbitmq as usual otherwise all our apps will keep looking for the rabbitmq and may show abnormal behaviour.
* As we had already had rabbitmq image in our local docker repo so no need to copy that official cmd and run it , simply open cmd and run the below command.
* docker run -d --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:4-management
* Once the rabbitmq container is active now , run the configserver app .
* And yes i will just clone my config server to my local pc so that i can do changes to so many files all together and then push them all to the repo.
* ![img_121.png](images/img_121.png) Now lets change the details here : ![img_122.png](images/img_122.png) .
* Before: ![img_123.png](images/img_123.png)  after: ![img_124.png](images/img_124.png).
* Before: ![img_125.png](images/img_125.png)  After: ![img_126.png](images/img_126.png)
* So now we have done all the changes but before pushing to github repo this changes lets just wait.
* Now what we need to do is since our ngrok is also running so we will give it the port address of our configserver so that it will expose it to the public.
* type ngrok http 8071 or ngrok http <port of configserver> configserver must be running so that it will return us a URL.
* ![img_127.png](images/img_127.png) Now whatever link is there against Forwarding just copy it and give it to the webhook in github.
* Open your github config files repo then settings-> webhooks -> Add webhook.
* In that payload url paste the link which was provided by ngrok and give a /monitor at the end.
* select the content type as json , now select below just the push event.
* Now add that . Now your webhook is ready .
* Now turn on all your microservices.
* Now hit the get-contact-info api of all the microservices and compare what is there in github and what are you getting in postman:
* Accounts: postman-> ![img_128.png](images/img_128.png)
* Accounts: Github-> ![img_129.png](images/img_129.png)
* Cards: postman-> ![img_130.png](images/img_130.png)
* Cards: Github-> ![img_131.png](images/img_131.png)
* Loans: postman-> ![img_132.png](images/img_132.png)
* Loans: Github-> ![img_133.png](images/img_133.png)
* ### Now lets see what happens as soon as i push the local github config repo to the main github repo:
* ![img_134.png](images/img_134.png) we have changed this 3 files if you remeber now let me push it to the repo.
* ![img_135.png](images/img_135.png) we have added the 3 files.
* Now as soon as git push origin main is done now first check if the github is updated or not and also check the configserver app is pulling new data or not and after that check the individual apps in postman whether they are pulling or not.
* I have checked we got the latest in the github as well as configserver.
* ![img_136.png](images/img_136.png) see the loans config files as per configserver.
* Accounts postman: ![img_138.png](images/img_138.png)
* Loans postman: ![img_137.png](images/img_137.png)
* Cards postman: ![img_139.png](images/img_139.png)
* See how new configs as being pulled by our apps without us manually clicking on that busrefresh api , so this is how webhooks work .
* This is followed in real production evn .
* Another thing you can check ![img_140.png](images/img_140.png) here in the same cmd screen you can see the POST request sent to the URL given by ngrok as well as you can go to the Web Interface link and see all the POST requests and what data do they carry.
* http://localhost:15672/ You can go to this link and montior the queues of your rabbitmq.
* Now just close you connection of ngrok ctrl+c and turnoff all your apps.


### Finally we learned how webhook's work and how we can automate the config refresh in all of our app's without even using the busrefresh.
### Make sure to include the spring-cloud-config-monitor which exposes the /monitor api which will broadcast a refresh event and this api link must be given to the webhook URL.




## 💡 Key Takeaway

> Configuration should never be tightly coupled with code.  
> It should be external, secure, and manageable across environments.
