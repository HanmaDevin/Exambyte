FROM openjdk:17-jdk


COPY build/libs/Exambyte-0.0.1-SNAPSHOT.jar exambyte.jar
COPY .env .env
COPY src/main/resources /app/config/roles.yaml


EXPOSE 8080
EXPOSE 5432

CMD ["java", "-jar", "exambyte.jar"]
