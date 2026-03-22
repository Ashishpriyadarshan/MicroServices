#Base Image containing the JDK which can compile and run our java app
FROM eclipse-temurin:21-jre-noble

#information about who maintains the app
MAINTAINER AshishPriyadarshan

#Copy the jar to the image
#COPY source name
#first we copy the file from the source which is target/ .jar file
#then the other name which is same as jar name on the RHS is the name with which you want to save the file inside he image
COPY target/accounts-0.0.1-SNAPSHOT.jar accounts-0.0.1-SNAPSHOT.jar

#commands to execute the application and every , below means a space
ENTRYPOINT ["java","-jar","accounts-0.0.1-SNAPSHOT.jar"]


