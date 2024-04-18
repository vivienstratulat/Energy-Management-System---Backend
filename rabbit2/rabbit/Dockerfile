FROM openjdk:17-jdk-alpine
COPY build/libs/rabbit-0.0.1-SNAPSHOT.jar app.jar
CMD ["java", "-jar", "app.jar", "--spring.profiles.active=dev"]
