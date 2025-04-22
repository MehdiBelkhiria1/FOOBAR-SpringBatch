# Nom du Projet - FOO BAR QUIX

Ce projet illustre un kata nommé FOO BAR QUIX, implémenté avec Spring Boot et Spring Batch.
Son objectif est de transformer un nombre (compris entre 0 et l’infini) en une chaîne de caractères selon les règles suivantes :
- Si le nombre est divisible par 3 ou s'il contient 3, la chaîne de caractères doit contenir "FOO"
- Si le nombre est divisible par 5 ou s'il contient 5, la chaîne de caractères doit contenir "BAR"
- Si le nombre contient 7, la chaîne de caractères doit contenir "QUIX"
- La règle "divisible par" est plus prioritaire que la règle "contient"
- Le contenu est analysé de gauche à droite
- Si aucune des règles n'est vérifiée, retourner le nombre en entrée sous forme de chaîne de caractères

Le projet propose:

Un batch Spring qui s’exécute automatiquement au démarrage de l’application a partir d`un fichier input.txt vers output.txt

Un endpoint REST permettant de lancer le même batch.

un endpoint REST permettant de transformer un seul nombre vers une chaine de caractères.

---

## Sommaire

- [Prérequis](#prérequis)
- [Installation et Exécution en Local](#installation-et-exécution-en-local)
- [Utilisation via REST](#utilisation-via-rest)
- [Exécution via Docker](#exécution-via-docker)

---

## Prérequis

- **Java 17** ou version ultérieure
- **Maven** pour compiler et empaqueter l'application
- **Docker** (optionnel, uniquement pour exécuter l'application en conteneur)

---

## Installation et Exécution en Local

1. Cloner le dépôt:
   git clone https://github.com/MehdiBelkhiria1/FOOBAR-SpringBatch.git
   cd FOOBAR-SpringBatch

2. Compiler le projet et générer le package:
   mvn clean package

3. Lancer l'application Spring Boot (le job s'exécute automatiquement au démarrage):
  Via Maven:
  mvn spring-boot:run
  Ou via le JAR généré:
  java -jar target/FOOBAR-SpringBatch-0.0.1-SNAPSHOT.jar

---

## Utilisation via REST

En complément du démarrage automatique, vous pouvez lancer le job via REST:
curl http://localhost:9090/api/start

retourner une chaine de caractères avec un paramètre numérique (par exemple, 7):
curl http://localhost:9090/api/convert/7

---

## Exécution via Docker

Construire l'image Docker:
docker build -t FOOBAR-SpringBatch 

Lancer le conteneur:
docker run -d -p 9090:9090 --name conteneur-nom-du-projet FOOBAR-SpringBatch

## Exécution via Docker Compose(essentiel pour l'exemple Kafka)

Construire et lancer les conteneurs(application FOOBAR-SpringBatch+Apache Kafka):
docker-compose up -d 

lancer le webservice:
curl -X POST http://localhost:9090/kafka/launch -H "Content-Type: application/json" -d "{\"jobName\":\"fooBarJob\",\"params\":{}}"

Verifier l'execution du message par Apache Kafka:
docker exec -it kafka1  /opt/kafka/bin/kafka-consumer-groups.sh  --bootstrap-server localhost:9092 --group batch-group --describe
