# bank Account Management  (cqrs using kafka)
POC Project using CQRS Pattern with Kafka

# Tools Installation

Create Docker Network - techbankNet

docker network create --attachable -d bridge techbankNet

Create docker-compose.yml file with contents:

version: "3.4"

services:
zookeeper:
image: bitnami/zookeeper
restart: always
ports:
- "2181:2181"
volumes:
- "zookeeper_data:/bitnami"
environment:
- ALLOW_ANONYMOUS_LOGIN=yes
kafka:
image: bitnami/kafka
ports:
- "9092:9092"
restart: always
volumes:
- "kafka_data:/bitnami"
environment:
- KAFKA_ZOOKEEPER_CONNECT=zookeeper:2181
- ALLOW_PLAINTEXT_LISTENER=yes
- KAFKA_LISTENERS=PLAINTEXT://:9092
- KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://localhost:9092
depends_on:
- zookeeper

volumes:
zookeeper_data:
driver: local
kafka_data:
driver: local


The run by executing the following command:

> docker-compose up -d

# MongoDB

Run in Docker:
docker run -it -d --name mongo-container \
-p 27017:27017 --network techbankNet \
--restart always \
-v mongodb_data_container:/data/db \
mongo:latest

# Robo 3T:
https://robomongo.org/download

# MySQL

Run in Docker:
docker run -it -d --name mysql-container \
-p 3306:3306 --network techbankNet \
-e MYSQL_ROOT_PASSWORD=techbankRootPsw \
--restart always \
-v mysql_data_container:/var/lib/mysql  \
mysql:latest

# Adminer:
docker run -it -d --name adminer \
-p 8080:8080 --network techbankNet \
-e ADMINER_DEFAULT_SERVER=mysql-container \
--restart always adminer:latest
