FROM senyorjou/api-wars-clojure-simple-server-base AS builder

WORKDIR /app

COPY . .

RUN lein deps

RUN lein test

RUN lein ring uberjar


FROM eclipse-temurin:17 AS runner

WORKDIR /app

COPY --from=builder /app/target/simple-server-0.1.0-SNAPSHOT-standalone.jar ./app.jar

CMD ["java", "-jar", "/app/app.jar"]
