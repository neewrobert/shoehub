FROM eclipse-temurin:17-jdk
EXPOSE 8090
ARG JAR_FILE=ShoeHub-*.jar
WORKDIR /opt/app
ADD /target/${JAR_FILE} app.jar
CMD ["java", "-jar", "app.jar" ]