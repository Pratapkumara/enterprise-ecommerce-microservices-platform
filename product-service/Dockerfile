FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY target/product-service-1.0.0.jar app.jar

EXPOSE 8084

ENTRYPOINT ["java","-jar","app.jar"]
