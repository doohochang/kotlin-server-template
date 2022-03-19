FROM openjdk:17-jdk-alpine
EXPOSE ${KTSERVER_HTTP_PORT}
RUN mkdir /app
COPY build/install/kotlin-server-template /app
WORKDIR /app/bin
CMD ["./kotlin-server-template"]
