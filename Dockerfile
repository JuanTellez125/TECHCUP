FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

RUN ./mvnw dependency:go-offline -B

COPY src ./src

RUN ./mvnw clean package -DskipTests -B

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "target/TECHCUP-0.0.1-SNAPSHOT.jar"]