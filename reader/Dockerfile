FROM openjdk:12-alpine
RUN mkdir /opt/app
COPY build/libs/reader-1.0.jar /opt/app/app.jar
COPY build/output/libRuntime /opt/app/

CMD java -jar /opt/app/app.jar
ENTRYPOINT ["java", "-cp", "/opt/app/*", "io.github.andyswe.elva.data.reader.Application"]

