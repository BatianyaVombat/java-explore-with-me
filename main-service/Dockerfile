# ── Базовый образ с JRE 21 ───────────────────────────
FROM amazoncorretto:21-alpine

# ── Метаданные ───────────────────────────────────────
LABEL maintainer="your-email@example.com"
LABEL description="Main Service for EWM project"

# ── Рабочая директория ───────────────────────────────
WORKDIR /app

# ── Копируем собранный jar ───────────────────────────
COPY target/*.jar app.jar

# ── Открываем порт ───────────────────────────────────
EXPOSE 8080

# ── Запуск ───────────────────────────────────────────
ENTRYPOINT ["java", "-jar", "/app/app.jar"]