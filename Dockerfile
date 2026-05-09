# ប្រើ Java 21 JRE សម្រាប់ការរត់ (ឬប្តូរទៅ 26 បើអ្នកមាន Image ផ្លូវការ)
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# ចម្លងហ្វាយ .jar ដែលយើង Build រួចពីម៉ាស៊ីន Mac ចូលមកក្នុង Docker
COPY target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]