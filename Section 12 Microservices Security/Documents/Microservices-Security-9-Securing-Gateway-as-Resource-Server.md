# Securing the Spring Reactive Gateway as Resource Server:
* By far till the previous lectures we have seen how the client credentials grant flow happens and we also saw how we can get the access token as well as the ID Token .
* In this lecture we will see how we can secure our gateway server as a Resource Server.
* Basically we are going to implement some security inside the gateway and then give it the configs of the Keycloak server so that after receiving a request it can validate the access token which it will receive in the request.


## Add the dependencies inside the pom.xml of the gateway server:
* Open the pom.xml of the gateway Server.
* ![img_35.png](images/img_35.png)
* Added the above 2 dependencies .
* There is another dependency which we can add but even if we don't add it will be pulled automatically.
* ![img_36.png](images/img_36.png)

## Creating the Security Bean:
* First create a config class .
* ![img_37.png](images/img_37.png)
* See below what's inside the config class.
* ![img_38.png](images/img_38.png)
* use the Configuration and EnableWebFluxSecurity annotation.
* Create a bean of type SecurityFilterChain .
* And then mention the paths that you want to be secured and the paths which you want to be not secured.
* In our case if any HTTP requests has GET Method then we dont need authentication.
* Rest all are authenticated .
* We have disabled the csrf protection as we dont have any FE till now.
* ![img_39.png](images/img_39.png)
* Make sure this line is present as it will enable the OAUTH2 flow in the incoming http requests.

## Giving the configs of the KeyCloak Server:
* First of all lets login into our custom realm ``company-A-realm``
* ``http://localhost:7080/admin/company-A-realm/console``
* Now from there goto the Realm Settings from there goto the Endpoints and open the OpenID Endpoint Configuration.
* ![img_40.png](images/img_40.png)
* Here we need to copy the jwks_uri , which ends with /certs .
* Basically this endpoint should be included inside the gateway config as during startup of our gateway it will download the public certificates belonging to this realm so that the resource sever can contact the keycloak server to validate if a token that it has received in the request is valid or not.
* So copy this and then we have to write down some configs in the application.yml of the gateway Server app.
* ![img_41.png](images/img_41.png)
* See the above config that is what we have to do.
* There are also private certificates which are used by the keycloak to issue new tokens.


## Testing the Resource Serving using postman:
* As we have done the security bean configs where if the http request has a GET METHOD then we will simply permit where as any other method then we will secure it.
* So for this lets start our apps : configServer->ServiceDiscovery->Accounts->Loans->Cards->GatewayServer .
* We know that the get-contact-info api of all the apps is a GET METHOD so lets first test that.
* Open Postman :
  * ![img_42.png](images/img_42.png)
  * ![img_43.png](images/img_43.png)
  * ![img_44.png](images/img_44.png)
  * As can be seen they are permitted , now lets hit some api's which have a different HTTP Method.
  * ![img_45.png](images/img_45.png)
  * See here we hit the create api of the accounts via gateway and it was blocked with 401 Unauthorized error as it lacks the access token .
* So what we can do for this is first we will have to get the access token from the Keycloak using the Client_Credentials , i have taught this in the last lecture .
* But we will now follow that lengthy process as first we wil have to hit the keycloak api to get the tokens then we will have to manually set the access token in the POST API Request thats a lengthy process.

## POSTMAN Configs:
* ![img_46.png](images/img_46.png)
* Goto Authorization and select AuthType as OAUTH2.0 and Request Headers , now fill the rest of the info.
* ![img_47.png](images/img_47.png)
* ![img_48.png](images/img_48.png)
* ![img_49.png](images/img_49.png)
* In the access token URL we are using the link where we can get the access tokens .
* client ID is whatever we have given in the Keycloak.
* client secret is whatever was generated in the keycloak.
* Now as soon as you click on the Get New access Token you will get the below screen.
* ![img_50.png](images/img_50.png)
* ![img_51.png](images/img_51.png)
* ![img_52.png](images/img_52.png)
* ![img_53.png](images/img_53.png)
* There will be so many things here , now as soon as you click on the use Token , it will populate the access token in the Headers of your API POST Request.
* ![img_54.png](images/img_54.png)
* ![img_55.png](images/img_55.png)
* See the response this time , the post method which was protected finally worked .
* What if i change the access token a little bit:
* ![img_56.png](images/img_56.png)
* See we got this 401 UnAuthorized .


___
