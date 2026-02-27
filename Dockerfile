FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /build
COPY pom.xml .
COPY src ./src
RUN mvn clean package

FROM eclipse-temurin:17-jre
WORKDIR /code
COPY --from=build /build/target/java-app-1.0-SNAPSHOT-jar-with-dependencies.jar ./java-app.jar
CMD ["java", "-jar", "java-app.jar"]


