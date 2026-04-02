# 📘 Configuration Management in Spring Boot and Different ways of taking up values

## 🚀 What is Configuration Management?

Configuration management in Spring Boot refers to the way an application handles external settings such as:

* Database credentials (URL, username, password)
* API keys
* Server ports
* Feature flags
* Environment-specific values

Instead of hardcoding these values into the code, Spring Boot allows you to **externalize configurations**, making your application more flexible and production-ready.

---

## ❓ Why is Configuration Management Needed?

Without proper configuration management:

* ❌ You would need to change code for every environment
* ❌ Risk of exposing sensitive data (like passwords)
* ❌ Difficult to maintain and scale

With configuration management:

* ✅ Clean separation of code and configuration
* ✅ Easy environment switching
* ✅ Secure handling of secrets
* ✅ Supports **“Build Once, Deploy Anywhere”**

---

## 🌍 Different Environments

In real-world applications, we usually have multiple environments:

| Environment           | Purpose                       |
| --------------------- | ----------------------------- |
| **Development (dev)** | Local development and testing |
| **Testing (test)**    | QA testing                    |
| **Staging (stage)**   | Pre-production environment    |
| **Production (prod)** | Live system used by users     |

Each environment has **different configurations**:

* Different databases
* Different API endpoints
* Different logging levels

---

## 🛠️ Ways to Access Configuration in Spring Boot

### 1. 🔹 Using `@Value`

```java
I have demonstrated below how to use the value annotation.
```

In your application.proprties create some random variables:
![img_2.png](images/img_2.png)
Now in your controller layer:
![img_3.png](images/img_3.png)
![img_5.png](images/img_5.png)
![img_4.png](images/img_4.png)

### ✅ Pros:

* Simple and quick
* Good for small use cases

### ❌ Cons:

* Not type-safe
* Hard to manage large configs
* Scattered across code

---

### 2. 🔹 Using `Environment` Interface

```java
@Autowired
private Environment env;

String port = env.getProperty("JAVA_HOME");

For this check your system environment variables or create a new variable .
Suppose you have java installed in your local then .
system variable with name JAVA_HOME will correspond to the path where the java app is present.
        
```
example:
![img_1.png](images/img_1.png)
do the above code in your controller layer or wherever you want to use it.
Then do the below code.
![img.png](images/img.png)

### ✅ Pros:

* Dynamic access
* Useful in conditional logic

### ❌ Cons:

* Still not type-safe
* Not clean for large configs

---

### 3. 🔹 Using `@ConfigurationProperties` (🔥 Recommended)

```java
@ConfigurationProperties(prefix = "app")
@Component
public class AppConfig {
    private String name;
    private String version;
}
```
The below is not a class it is a Record used just to store values during its obj creation.
It comes with inbuild getters only no setters.
The annotaion that we are using is to create a bean of this type and register the values in it 
from the application.properties where the property starts with accounts.
![img_7.png](images/img_7.png)
Write the below into your YML now the Record class will try to Map all the props into the AccountsContactInfoDto
make sure the record argument list is in sequence with whatever you have mentioned in the application.yml

![img_8.png](images/img_8.png)
![img_6.png](images/img_6.png)
At last on top of your main class write the below code:
![img_9.png](images/img_9.png)
The above will be responsible to create your bean of AccountsContactInfoDto

### Required Setup:

* Add dependency (if needed):

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-configuration-processor</artifactId>
</dependency>
```

* Enable it:

```java
@EnableConfigurationProperties
```

### ✅ Pros:

* Type-safe
* Clean and structured
* Best for large configs
* Easy to maintain

### ❌ Cons:

* Slightly more setup

---

## 🏆 Which Approach is Used in Real World?

👉 **`@ConfigurationProperties` is the industry standard**

Because:

* Clean code structure
* Scalable
* Easy to manage complex configurations

👉 `@Value` is used only for **small or quick values**

---

## 🔄 Configuration Priority Order

When Spring Boot loads configurations, it follows this priority (highest → lowest):

1. 🥇 Command Line Arguments (CLI)
2. 🥈 JVM System Properties
3. 🥉 Environment Variables
4. 📄 `application.properties` / `application.yml`
5. 📦 Default values in code

👉 Example:

```bash
java -jar app.jar --server.port=9090
```

This will override everything else.

---

## 🧠 Why “Build Once, Configure per Environment”?

Instead of rebuilding your app for every environment:

* Build **once**
* Deploy same JAR everywhere
* Change only configurations

### Benefits:

* ✅ Faster deployments
* ✅ Consistency
* ✅ Less risk of bugs

---

## ⚙️ Spring Profiles

Spring Boot allows you to define environment-specific configs using **profiles**.

### Example:

```
application-dev.properties
application-prod.properties
```

---

## 🛑 Setting Profile in `application.properties` (Not Recommended)

```properties
spring.profiles.active=dev
```

### ❌ Drawbacks:

* Hardcoded environment
* Not flexible
* Requires rebuild to change

---

## ✅ Setting Profile at Runtime (Best Practice)

### 🔹 Using CLI

```bash
java -jar app.jar --spring.profiles.active=prod
```

### 🔹 Using JVM Argument

```bash
java -Dspring.profiles.active=prod -jar app.jar
```

### 🔹 Using Environment Variable

```bash
export SPRING_PROFILES_ACTIVE=prod
```

---

## 💡 Best Practices

* ✅ Never hardcode credentials
* ✅ Use `@ConfigurationProperties` for structured configs
* ✅ Use environment variables for secrets
* ✅ Always use runtime profile switching
* ✅ Follow “Build Once, Deploy Anywhere”

---

### Lets see how we can create different profiles in the app and use them.

First we will learn how to use them manually then we will see what are the demerits of manually setting the profile.

* 1st Step: 
  create 2 files with the name application_prod.yml and application_qa.yml
as per our requirment we are having 3 env one is dev then qa then prod.
The dev env configs are inside the application.yml so we are not creating any extra config file.
But ideally each env has seperate files.
![img_10.png](images/img_10.png)
* Now let us first set our default dev env configs:
* application.yml file
![img_15.png](images/img_15.png)
!![img_12.png](images/img_12.png)
* There are some props which will be same accross all the env in our case those are in blue color in the above config .
* application_qa.properties:
![img_13.png](images/img_13.png)
* application_prod.yml
![img_14.png](images/img_14.png)
* Now we have set all the configs of different env for demonstration.

### By default springboot will always load the default application.yml/properties file unless we mention the environment to be loaded.

### Let me show yu how it is done manually:

The biggest disadvantage of manually setting the profiles inside the application.yml is that 
it gets hardcoded and when you will create the docker image or jar file of it then at that time it will only work in one 
specific env who's profile is active means suppose your active profile is dev then when you create a image or jar
and deploy it to some other env suppose qa there it might cause problem.

* lets see below
![img_16.png](images/img_16.png)
here i have not mentioned any active profile so lets run 
![img_17.png](images/img_17.png)
see the above op that i am getting.
* suppose let me activate a profile:
![img_18.png](images/img_18.png)
* See the below OP this time i got this
![img_19.png](images/img_19.png)



### Lets look at the priority of ways in which we can set arguments:
* For this first let me make some more yml files for prod , dev , stage and qa env .
* ![img_29.png](images/img_29.png)
* Now let me show you the details that are present inside each yml file.
* ## 📸 Environment Screenshots

| Dev                                                                | Stage                                  | QA                        | Prod                                 |
|--------------------------------------------------------------------|----------------------------------------|---------------------------|--------------------------------------|
| ![img_33.png](images/img_33.png) | ![img_32.png](images/img_32.png) | ![img_34.png](images/img_34.png) | ![img_31.png](images/img_31.png) |
* Now lets see some test cases according to different situations how will our app perform.
* ## 🧪 Test Cases( Running directly from IDE(IntelliJ) Not on Jar files)

| Case No | Case Definition                                                                                                                                                                                                                                                                 | Case Input / Image                                                                                                                                                                                                               | Expected Output                                                                                                                                                                                                                                                                                                                                                                                                   |
|---------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| TC-01   | when we have all the config files but we have not shared any details to the application.yml<br/>As you can see that it will load the default configs if we dont mention the active profile                                                                                      | ![img_35.png](images/img_35.png)                                                                                                                                                                                                        | ![img_36.png](images/img_36.png)                                                                                                                                                                                                                                                                                                                                                                                         |
| TC-02   | When we have provided the config files details to the application.yml but we have not activated any profile.<br/> You will observe here that still we are getting the default configs only.                                                                                     | ![img_37.png](images/img_37.png)                                                                                                                                                                                                        | * ![img_38.png](images/img_38.png)<br/>* ![img_39.png](images/img_39.png)                                                                                                                                                                                                                                                                                                                                                       |
| TC-03   | Lets activate a profile inside the application.yml file . We are hardcoding it which is not usually recommended . <br/> If you activate some other config then it will show the values of that config suppose you activate qa then it will show configs of application_qa.yml . | *![img_40.png](images/img_40.png)<br/> * This will be shown in the terminal when you run ur app ![img_41.png](images/img_41.png)                                                                                                               | ![img_42.png](images/img_42.png) See finally we got the configs of the application_prod.yml<br/>* ![img_43.png](images/img_43.png)                                                                                                                                                                                                                                                                                              |
| TC-04   | Lets see how to use the CLI and provide config details .<br/>We will use the embeded CLI of the IntelliJ .<br/> For this right click on your main app file and then select modify Run configuration then Run the app.                                                           | * First comment these lines in the application.yml <br/>![img_44.png](images/img_44.png)     <br/> * Now fill the details here in the edit config before run menu<br/>![img_45.png](images/img_45.png) <br/> ![img_49.png](images/img_49.png)         | * As soon as we give the configs in the CLI we can see this in the logs of the app :<br/> ![img_46.png](images/img_46.png) <br/>but when we are calling the API we are getting the configs of the default application.yml as can be seen. <br/> ![img_47.png](images/img_47.png)<br/> we could only change the build.version because it is common property accross all the .yml files thats why <br/> ![img_48.png](images/img_48.png) |
| TC-05   | Continuation of case 04 . We faced issues in case 04 because our application.yml has no idea about the other yml config files so lets uncomment the lines.                                                                                                                      | * First uncomment few lines from application.yml <br/>   ![img_52.png](images/img_52.png) <br/> Now set the config <br/> ![img_49.png](images/img_49.png) <br/> Now simply Run the main app file                                               | Finally we got the desired OP , the profile was finally used <br/> ![img_50.png](images/img_50.png) <br/> * The build version is    ![img_51.png](images/img_51.png)                                                                                                                                                                                                                                                            |
| TC-06   | Continue case 05 , Now instead of using CLI we will use the JVM Options which is again iside the edit configuration before RUN menu.                                                                                                                                            | First enable the Vm options inside the edit configurations then we have to fill the commands here ![img_53.png](images/img_53.png) <br/> * ![img_54.png](images/img_54.png)                                                                    | As soon as you Run you will see this in your logs You will the stage getting activted <br/>![img_58.png](images/img_58.png) <br/> Now when we hit the API we can see that our application_stage.yml configs were loaded ![img_56.png](images/img_56.png) <br/> * build.version is     <br/> ![img_57.png](images/img_57.png)                                                                                                           |
| TC-07   | Continue case 06 , Now we will use the inbuild ENV Variables option , we are not overRiding the sytem env variables we will simply inject some Variables during RUN Time again using the edit configuration                                                                     | We will have to fill the details here first <br/> ![img_59.png](images/img_59.png) <br/> * Lets fill the details <br/> * You can either do this ![img_61.png](images/img_61.png)  <br/> * Or you can expand the list and fill here one by one ![img_60.png](images/img_60.png) | As soon as you start your app you will see this in the logs <br/> * ![img_62.png](images/img_62.png) <br/> * Now when you hit the API's you will see <br/> *    ![img_63.png](images/img_63.png) <br/>*  ![img_64.png](images/img_64.png)                                                                                                                                                                                                                       |

[//]: # (| TC-08   | Description here                                                                                                                                                                                                                                                                | ![Input]&#40;images/tc8.png&#41;                                                                                                                                                                                                         | Output here                                                                                                                                                                                                                                                                                                                                                                                                       |)

[//]: # (| TC-09   | Description here                                                                                                                                                                                                                                                                | ![Input]&#40;images/tc9.png&#41;                                                                                                                                                                                                         | Output here                                                                                                                                                                                                                                                                                                                                                                                                       |)

[//]: # (| TC-10   | Description here                                                                                                                                                                                                                                                                | ![Input]&#40;images/tc10.png&#41;                                                                                                                                                                                                        | Output here                                                                                                                                                                                                                                                                                                                                                                                                       |)


* see we are getting the qa related configs as CLI has the highest priority
## 📊 Priority of all the possible ways to set values

| Case No | Description                                                                                                                                                                | Case Inputs                                                                                                                                                                                                                                             | Case Outputs                                                                                                                     | Conclusion                                                                                                                                                                                                                                                                                                                                                             |
|---------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| TC-01   | Suppose we have set some values in all the ways possible . Like we have set the values in CLI , JVM Options , ENV Variables , Hardcoded in the application.yml .           | * inside application.properties<br/>![img_65.png](images/img_65.png)<br/>* inside the application configuration<br/>![img_66.png](images/img_66.png) <br/>* Observe the values that i have given under different tabs now lets see which one gets highest preference. | * we get this in the logs <br/>![img_67.png](images/img_67.png) <br/>  ![img_68.png](images/img_68.png) <br/> * ![img_69.png](images/img_69.png)      | We can see that the CLI has the highest preference as compared to any other method.                                                                                                                                                                                                                                                                                    |
| TC-02   | Now lets remove the CLI and check the preference for the remaning 3 ways . application.yml then JVM Options and ENV Variables way.                                         | * inside application.properties<br/>![img_65.png](images/img_65.png)<br/>* inside the application configuration<br/>  ![img_70.png](images/img_70.png) Observer as we have removed the CLI argements now see who gets the highest priority.                           | * we get this in the logs <br/>![img_71.png](images/img_71.png) <br/>   ![img_72.png](images/img_72.png) <br/>     * ![img_73.png](images/img_73.png) | From the screenshots on the left side we can conclude that out of JVM Options , hardcoded active profile in application.yml and ENV Variables inside the run configs  JVM Options has the highest priority after CLI arguments                                                                                                                                         |
| TC-03   | Now lets remove the JVM Options and check the priority of the remaining two ways. application.yml active profile hardcoded vs ENV Variables inside the run configurations. | * inside application.properties<br/>![img_65.png](images/img_65.png)<br/> * inside the application configuration <br/> ![img_74.png](images/img_74.png) Observe we have removed the JVM Options lets see who has the highest priority now                             | * We get this in the logs <br/> ![img_75.png](images/img_75.png) <br/> *  ![img_76.png](images/img_76.png) <br/>  ![img_77.png](images/img_77.png)    | From the screenshots on the left side we can finally conclude that out of ENV variables and hardcoded active profile inside the application.yml we have ENV variables as the highest priority and hardcoded one has the least priority . So even if you hardcode any active profile then still you change the profile of the JAR file before running it in the servers |

### CLI > JVM OPTIONS > ENV VARIABLES > HARDCODED ACTIVE PROFILE


---

### We will see how the configs management work in case of JAR Files which is actually used a lot:



## 🔥 Final Takeaway

Configuration management is not just a concept — it’s **critical for real-world scalable systems**.

If you do it right:

* Your app becomes flexible
* Deployment becomes smooth
* Maintenance becomes easy

If you do it wrong:

* You’ll struggle with bugs, environment issues, and security risks

---

