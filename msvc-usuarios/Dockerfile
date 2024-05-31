FROM openjdk:17-jdk-alpine AS builder
ARG MSVC_NAME=msvc-usuarios
WORKDIR /app/$MSVC_NAME

COPY ./pom.xml/ /app

COPY ./$MSVC_NAME/.mvn ./.mvn
COPY ./$MSVC_NAME/mvnw .
COPY ./$MSVC_NAME/pom.xml .

RUN ./mvnw clean package -Dmaven.test.skip -Dmaven.main.skip -Dspring-boot.repackage.skip && rm -r ./target/
#RUN ./mvnw dependency:go-offline

COPY ./$MSVC_NAME/src ./src

RUN ./mvnw clean package -DskipTests

FROM openjdk:17-jdk-alpine

WORKDIR /app

RUN mkdir ./logs

COPY --from=builder /app/msvc-usuarios/target/msvc-usuarios-0.0.1-SNAPSHOT.jar .

ENV PORT 8001

EXPOSE $PORT

CMD [ "java", "-jar", "msvc-usuarios-0.0.1-SNAPSHOT.jar" ]