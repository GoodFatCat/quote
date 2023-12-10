FROM maven:3.9.4-eclipse-temurin-17-alpine as build
ENV HOME=/usr/app
RUN mkdir -p $HOME
WORKDIR $HOME
ADD . $HOME
RUN mvn clean package

FROM amazoncorretto:17-alpine3.16-jdk
COPY --from=build /usr/app/target/*.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]