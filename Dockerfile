FROM openjdk:21
VOLUME /tmp
EXPOSE 8888
ARG JAR_FILE=target/ErtebatFardaBoarding-1.jar
COPY ${JAR_FILE} app.jar
COPY wait-for-it.sh /wait-for-it.sh
RUN chmod +x /wait-for-it.sh
CMD ["/wait-for-it.sh", "oracle-db:1521", "--timeout=50", "--", "java", "-jar", "/app.jar", "--spring.profiles.active=prod"]