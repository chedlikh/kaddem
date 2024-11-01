
# Utilise une image Maven pour construire l'application
FROM maven:3.8.5-openjdk-11 AS build

# Définit le répertoire de travail dans le conteneur
WORKDIR /app

# Copie le code source dans le conteneur
COPY . .

# Exécute le build Maven et déploie vers Nexus
RUN mvn clean install -DskipTests && mvn deploy -DskipTests

# Utilise une image JDK allégée pour l'exécution
FROM openjdk:11-jre-slim

# Définit le répertoire de travail dans le conteneur pour l'application finie
WORKDIR /app

# Copie l'artefact généré depuis l'étape de build
COPY --from=build /app/target/*.jar app.jar

# Expose le port sur lequel l'application va tourner
EXPOSE 8080

# Commande d'exécution de l'application
ENTRYPOINT ["java", "-jar", "app.jar"]
