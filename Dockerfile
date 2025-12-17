FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

COPY mvnw pom.xml ./
COPY .mvn .mvn
RUN chmod +x mvnw
COPY src src
RUN ./mvnw -DskipTests package

FROM eclipse-temurin:21-jre
WORKDIR /app

ARG JAR_FILE=/app/target/*.jar
COPY --from=build ${JAR_FILE} app.jar
EXPOSE 8080

ENTRYPOINT ["java","-jar","/app/app.jar"]
