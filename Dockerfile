FROM azul/zulu-openjdk-alpine:17

WORKDIR /app

RUN addgroup --system javauser && adduser -S -s /usr/sbin/nologin -G javauser javauser

COPY out/artifacts/Parquimetro_jar/Parquimetro.jar app.jar

RUN chown -R javauser:javauser .

USER javauser

ENTRYPOINT ["java", "-jar", "app.jar"]