#FROM openjdk:21
#COPY ErtebatFardaBoarding.jar ErtebatFardaBoarding.jar
#ENTRYPOINT ["java","-jar","ErtebatFardaBoarding.jar"]


## Use an official OpenJDK runtime as a parent image
##FROM openjdk:17-jdk-slim
#FROM openjdk:21
## Add a volume pointing to /tmp
#VOLUME /tmp
## Make port 8080 available to the world outside this container
#EXPOSE 8888
## The application's jar file
#ARG JAR_FILE=target/ErtebatFardaBoarding.jar
## Add the application's jar to the container
##ADD ${JAR_FILE} app.jar
#COPY ${JAR_FILE} app.jar
#COPY wait-for-it.sh /wait-for-it.sh
#RUN chmod +x /wait-for-it.sh
## Run the jar file
##ENTRYPOINT ["java", "-jar", "/app.jar"]
#CMD ["/wait-for-it.sh", "oracle-db:8090", "--", "java", "-jar", "/app.jar"]

FROM openjdk:21
VOLUME /tmp
EXPOSE 8888
ARG JAR_FILE=target/ErtebatFardaBoarding.jar
COPY ${JAR_FILE} app.jar
COPY wait-for-it.sh /wait-for-it.sh
RUN chmod +x /wait-for-it.sh
CMD ["/wait-for-it.sh", "oracle-db:1521", "--timeout=50", "--", "java", "-jar", "/app.jar", "--spring.profiles.active=prod"]
#CMD ["/wait-for-it.sh", "oracle-db:1521", "--timeout=100", "--", "java", "-jar", "/app.jar", "--spring.profiles.active=prod"]

#JFS
#FROM openjdk:21
#ARG JAR_FILE=target/*.jar
##ARG WAR_FILE=target/*.war
#COPY ${JAR_FILE} app.jar
#ENTRYPOINT ["java","-jar","/app.jar"]