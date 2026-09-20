FROM bellsoft/liberica-runtime-container:jdk-25-stream-musl AS builder
WORKDIR /builder
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} application.jar
RUN java -Djarmode=tools -jar application.jar extract --layers --destination extracted
FROM bellsoft/liberica-runtime-container:jre-25-musl
WORKDIR /application
COPY --from=builder /builder/extracted/dependencies/ ./
COPY --from=builder /builder/extracted/spring-boot-loader/ ./
COPY --from=builder /builder/extracted/snapshot-dependencies/ ./
COPY --from=builder /builder/extracted/application/ ./
ENV SERVER_PORT="10000"
ENTRYPOINT ["java", "-jar", "application.jar"]
EXPOSE 10000