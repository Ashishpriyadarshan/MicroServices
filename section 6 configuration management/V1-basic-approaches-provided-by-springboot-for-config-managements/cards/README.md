# 💳 Cards Microservice

This is the **Cards Microservice**, responsible for handling all card-related operations in the system.

It provides REST APIs for:

* ✅ Creating a card
* 🔍 Fetching card details
* ✏️ Updating card information
* ❌ Deleting a card

---

# 🐳 Build Docker Image using Google Jib

To create a Docker image using **Google Jib**, follow the steps below:

---

## ⚙️ Step 1: Add Jib Plugin in `pom.xml`

Copy the following plugin inside the `<plugins>` section:

```xml
<plugin>
  <groupId>com.google.cloud.tools</groupId>
  <artifactId>jib-maven-plugin</artifactId>
  <version>3.4.0</version>
  <configuration>
    <to>
      <image>your-image-name</image>
    </to>
  </configuration>
</plugin>
```
---

## 🏷️ Step 2: Set Image Name

Inside the `<image>` tag, set the name of your application:

```
<image>cards-app</image>
```
your image name format should be like DockerHubUserName/App Name:Version

---

## 📦 Step 3: Ensure Packaging Type

Make sure your `pom.xml` has:

```xml
<packaging>jar</packaging>
```

---

## 🧹 Step 4: Clean Target Folder

Ensure the `target/` folder is empty or cleaned before building.

---

## 📂 Step 5: Navigate to Project Directory

Open terminal and navigate to the cards microservice directory (where `pom.xml` is present):

```bash
cd path/to/cards-microservice
```

---

## 🚀 Step 6: Build Docker Image

Run the following command:

```bash
mvn compile jib:dockerBuild
```

---

## 🔍 Step 7: Verify Docker Image

To check the created image:

```bash
docker images
```

---

## ▶️ Step 8: Run Docker Container

```bash
docker run -d -p 9000:9000 cards-app
```

---

# 📘 API Documentation (Swagger)

This Swagger documentation provides details about all the CRUD operations available in the Cards Microservice.

📍 Base URL:

```
http://localhost:9000
```

---

## 🔄 Update Card Details

**PUT** `/api/update`

Updates card details using full card information.

### Request Body

```json
{
  "mobileNumber": "1234567890",
  "cardNumber": "100646930341",
  "cardType": "Credit Card",
  "totalLimit": 100000,
  "amountUsed": 1000,
  "availableAmount": 90000
}
```

### Responses

* ✅ 200 OK
* ⚠️ 417 Expectation Failed
* ❌ 500 Internal Server Error

---

## ➕ Create Card

**POST** `/api/create`

Creates a new card record using a mobile number.

### Query Parameter

* `mobileNumber` (String)

### Responses

* ✅ 201 Created
* ❌ 500 Internal Server Error

---

## 🔍 Fetch Card Details

**GET** `/api/fetch`

Fetch card details based on mobile number.

### Query Parameter

* `mobileNumber` (String)

### Response Example

```json
{
  "mobileNumber": "1234567890",
  "cardNumber": "100646930341",
  "cardType": "Credit Card",
  "totalLimit": 100000,
  "amountUsed": 1000,
  "availableAmount": 90000
}
```

### Responses

* ✅ 200 OK
* ❌ 500 Internal Server Error

---

## ❌ Delete Card

**DELETE** `/api/delete`

Deletes card details based on mobile number.

### Query Parameter

* `mobileNumber` (String)

### Responses

* ✅ 200 OK
* ⚠️ 417 Expectation Failed
* ❌ 500 Internal Server Error

---

# ⚙️ Tech Stack

* Java 21
* Spring Boot
* Maven
* Docker
* Google Jib

---

# 📌 Notes

* Ensure Docker is installed and running 🐳
* Application runs on port **9000**
* Use proper image naming while building with Jib

---
