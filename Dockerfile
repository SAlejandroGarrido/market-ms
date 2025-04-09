FROM openjdk:17-slim AS builder

WORKDIR /app

COPY  ./target/*.jar market.jar

EXPOSE 8089

ENTRYPOINT ["java", "-jar", "market.jar"]