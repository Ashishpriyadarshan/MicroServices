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




## 💡 Key Takeaway

> Configuration should never be tightly coupled with code.  
> It should be external, secure, and manageable across environments.
