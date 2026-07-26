# Tutorial to perform Authorization Code Grant Type Flow:
* Since we are using Keycloak so in that case first lets create a client.
* A client in our realm.
* ![img_102.png](images/img_102.png)
* Click on create client.
* ![img_103.png](images/img_103.png)
* ![img_104.png](images/img_104.png)
* We dont need PKCE now as we are not using any FE Application as we will be testing using postman.
* ![img_105.png](images/img_105.png)
* Here in ideal case scenario we need to mention the redirect_uri so that post successful login we are redirected to our app/ webpage otherwise if we dont mention then in that case any hacker can Redirect the code flow to their site where they can extract some valuable informations.
* We marked * here as we are ok to be redirected to anywhere since we are just testing.
* Mark the web origins as * too.
* ![img_106.png](images/img_106.png)
* Finally, we have created a client, and we also can see the client_id and the client_credentials.
* Now time for us to create a user.


## Creating Users:
* ![img_107.png](images/img_107.png)
* Click on create user.
* ![img_108.png](images/img_108.png)
* We have filled the info btw the mobile Number is not given by default .
* As i had previously added some more user attributes that's why its there and it is specific to the realm's not just user or client.
* But in response while sending tokens it is upto the client whether they want to send mobile Number and other attributes as response or not in the token.
* ![img_109.png](images/img_109.png)
* Once created now go to Credentials and give the password.
* ![img_110.png](images/img_110.png)
* Once the password is created now it is time for us to assign the realm_roles which are needed to access the protected resources as at our gateway we had security configs which had ROLES as ACCOUNTS, CARDS and LOANS.
* ![img_111.png](images/img_111.png)
* Now both the user and the client are ready.
* Another thing in real world small organizations may create users manually in the keycloak with admin login but in the majority of the cases the UI applications can hit some api's exposed by the keycloak to create new users etc .


### Now lets set Postman for simulating Authorization Code Grant Type Flow:
* ![img_112.png](images/img_112.png)
* goto Authorization tab and fill the details as shown.
* ![img_113.png](images/img_113.png)
* Make Sure you have ticked the Authorize using browser as the login page will open in the browser and make sure to close all the tabs otherwise if there is any keycloak page that you have opened with some creds then it will use that account.
* Select Grant Type as Authorization Code.
* Client ID and Client Secret if you remeber then we have used the one which we registered .
* Fill the scope as openid email and profile.
* Make sure the Client Credentials is sent using the body.
* Now in the state value fill some random string just to maintain the CSRF :
* ![img_114.png](images/img_114.png)
* If you look at the callback URI then it is prefigured by the postman as the request will be redirected here only after authentication .
* ![img_115.png](images/img_115.png)
* ![img_116.png](images/img_116.png)
* This Auth URL and Access Token URL Where did we get that from :
* ![img_117.png](images/img_117.png)
* Well we got it from here.


### Testing:
* Start all your apps : configserver -> eureka -> accounts -> loans -> cards -> gateway .
* ![img_118.png](images/img_118.png)
* Now as soon as you click on get tokens , it will redirect you to a page.
* ![img_119.png](images/img_119.png)
* Fill the user details here then ,
* ![img_120.png](images/img_120.png)
* Once the credentials are verified then .
* ![img_121.png](images/img_121.png)
* See we got back the access token in response now just proceed and finally 
* ![img_122.png](images/img_122.png)
* Click on send and you will get a 201 response.
* ![img_123.png](images/img_123.png)
* Now we will do the same thing for the cards and loans api as well .
* ![img_124.png](images/img_124.png)
* ![img_125.png](images/img_125.png)
* Now lets fetch all the details: But this let me just remove one character from the access token and lets see what happens.
* ![img_126.png](images/img_126.png)
* See i removed on character 'g' from the access token and this happened.
* Another thing is you dont need to ask for auth tokens again and again if you have got them once that means the auth tokens will be stored in your browser as well as in the cookies of the postman and they will be used again and again.
* ![img_127.png](images/img_127.png)
* See finally we got the response , that's it.

``In another repository i have demonstrated how to implement Authorization Code flow when you have a FE app and also Client Credentials when communication is happening between two backend services.``
`` We will also learn in that repository how to create a signup page and sign new users into the keycloak without using the keycloak console via admin creds``

