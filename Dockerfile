FROM maven:4.0.0-rc-7-eclipse-temurin-21-alpine AS build

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -DskipTests


FROM alpine/java:21-jre

WORKDIR /app
COPY --from=build /app/target/*.jar /app

EXPOSE 8080

HEALTHCHECK --interval=5m --timeout=3s --start-period=120s\
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/ || exit 1



ENTRYPOINT ["java", "-jar"]
CMD ["/app/libraryapp.jar"]