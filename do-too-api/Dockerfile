FROM gradle:8.3.0-jdk20 as builder
WORKDIR /etc/dotoo-api
COPY . .
USER root
# Create the shadowjar (chmod +x makes the gradlew script executable)
RUN chmod +x ./gradlew
RUN ./gradlew shadowJar --no-daemon

FROM eclipse-temurin:20
WORKDIR /opt/dotoo-api
# Copy the shadowjar in the current workdir
COPY --from=builder ./etc/dotoo-api/build/libs/ .
# Entrypoint is used instead of CMD because the image is not intended to run another executable instead of the jar
ENTRYPOINT java \
    # java -D tag --> set a system property
    -Dkotlin.script.classpath="/opt/dotoo-api/dotoo-api.jar" \
    -jar \
    ./dotoo-api.jar