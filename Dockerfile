FROM adoptopenjdk:11-jre-hotspot
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} package-finder.jar
ENTRYPOINT ["java", "-jar", "package-finder.jar"]