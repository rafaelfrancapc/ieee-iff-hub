FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /build
COPY pom.xml .
RUN mvn -B -q dependency:go-offline
COPY src src
RUN mvn -B -q verify

FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=build /build/target/hub-0.0.1-SNAPSHOT.jar /app/hub.jar
ENV JAVA_TOOL_OPTIONS="-Xms64m -Xmx256m -XX:MaxMetaspaceSize=160m -XX:ReservedCodeCacheSize=48m -XX:MaxDirectMemorySize=16m -XX:+UseSerialGC"
USER 10001:10001
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/hub.jar"]
