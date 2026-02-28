FROM eclipse-temurin:21-jdk-ubi9-minimal AS builder

WORKDIR /home/app
ADD . /home/app/spring-boot-app
RUN cd spring-boot-app && ./gradlew clean build

FROM eclipse-temurin:21-jdk-ubi9-minimal

WORKDIR /home/app
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/backend.jar"]
COPY --from=builder /home/app/spring-boot-app/build/libs/*.jar /backend.jar
