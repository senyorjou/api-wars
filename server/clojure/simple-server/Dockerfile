FROM clojure:temurin-17-lein-bullseye-slim AS builder

WORKDIR /app

COPY project.clj .

RUN lein deps

COPY src/ resources/ ./

RUN lein ring uberjar


FROM eclipse-temurin:17 AS runner

WORKDIR /app

COPY --from=builder /app/target/simple-server-0.1.0-SNAPSHOT-standalone.jar ./app.jar

CMD ["java", "-jar", "/app/app.jar"]
