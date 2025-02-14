FROM rockylinux:9

# Устанавливаем OpenJDK и Maven
RUN dnf install -y java-17-openjdk maven

# Создаем рабочую директорию
WORKDIR /app

# Копируем только файлы зависимостей для кэширования
COPY pom.xml .
RUN mvn dependency:go-offline

# Теперь копируем весь исходный код и собираем проект
COPY . .
RUN mvn clean package

# Запуск приложения (замени `your-app.jar` на реальное имя)
CMD ["java", "-jar", "target/LuxMart_BackEnd-0.0.1-SNAPSHOT.jar"]