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




## 💡 Key Takeaway

> Configuration should never be tightly coupled with code.  
> It should be external, secure, and manageable across environments.
