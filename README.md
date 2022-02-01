# mobile-app-ws
The course that is followed is
https://confizlimited.udemy.com/course/restful-web-service-with-spring-boot-jpa-and-mysql/

Follow the following link to deploy app on tomcat server
https://mkyong.com/spring-boot/spring-boot-deploy-war-file-to-tomcat/

To find tomcat and other dependencies version use this maven command:-
_mvn dependency:tree_

Use following maven commands to run application from command line:
1)mvn install
2)mvn spring-boot:run
and also we can run our deployable jarfile as a stand-alone application using:-
_java -jar mobile-app-ws-0.0.1-SNAPSHOT.jar_

We need to add liquibase scripts as well