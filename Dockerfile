# 1. Stufe: Den Build-Prozess benennen (AS build)
FROM gradle:8-jdk17 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon

# 2. Stufe: Das eigentliche Laufzeit-Image
FROM amazoncorretto:17
WORKDIR /app
# Hier wird nun auf die Stufe "build" von oben verwiesen
COPY --from=build /home/gradle/src/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]