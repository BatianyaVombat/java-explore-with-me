# ── Базовый образ с JRE 21 ───────────────────────────
FROM amazoncorretto:21-alpine

# ── Метаданные ───────────────────────────────────────
LABEL maintainer="your-email@example.com"
LABEL description="Stats Server for EWM project"

# ── Рабочая директория ───────────────────────────────
WORKDIR /app

# ── Копируем собранный jar ───────────────────────────
# Имя jar-файла зависит от artifactId в pom.xml и версии.
# Например: stats-server-0.0.1-SNAPSHOT.jar
COPY target/*.jar app.jar

# ── Открываем порт ───────────────────────────────────
EXPOSE 9090

# ── Запуск ───────────────────────────────────────────
ENTRYPOINT ["java", "-jar", "/app/app.jar"]