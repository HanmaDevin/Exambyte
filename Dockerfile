from openjdk:23-jdk

copy build/libs/Exambyte-0.0.1-SNAPSHOT.jar exambyte.jar

expose 8080

entrypoint ["java", "-jar", "exambyte.jar"]
