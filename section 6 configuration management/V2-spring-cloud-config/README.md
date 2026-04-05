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

---

### 2️⃣ File System
- Config files stored outside the application (local or server path)
- Application reads config from external file location
- More flexible than classpath

---

### 3️⃣ Git Repository (Most Popular 🔥)
- Centralized config stored in Git (GitHub, GitLab, Bitbucket)
- Used with Spring Cloud Config Server
- Supports versioning, rollback, and team collaboration

---

### 4️⃣ Environment Variables
- Config passed at runtime (Docker, CI/CD, OS level)
- Commonly used for secrets and environment-specific values

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
