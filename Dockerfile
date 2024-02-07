FROM maven:3.8.3-openjdk-17 AS builder

WORKDIR /app

COPY . .

RUN mvn clean install spring-boot:repackage

FROM openjdk:17-oracle

WORKDIR /app

COPY --from=builder /app/target/wallet-rest-service-0.0.1-SNAPSHOT.jar /app/wallet-application.jar

CMD ["java", "-jar", "wallet-application.jar"]