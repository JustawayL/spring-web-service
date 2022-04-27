FROM eclipse-temurin:17.0.3_7-jre
RUN mkdir /opt/app
COPY build/libs/service-0.0.1-SNAPSHOT.jar /opt/app
CMD ["java", "-jar", "/opt/app/service-0.0.1-SNAPSHOT.jar"]