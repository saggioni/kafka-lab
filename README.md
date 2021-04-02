# kafka-lab

#SUBIR AMBIENTE
docker-compose up -d

#DERRUBAR AMBIENTE
docker-compose stop

#LISTAR CONTAINERS
docker ps

#LISTAR TÓPICOS 
docker exec -ti kafka-lab_kafka_1 /opt/kafka/bin/kafka-topics.sh --list --bootstrap-server localhost:9092

#CRIAR TÓPICOS 
docker exec -ti kafka-lab_kafka_1 /opt/kafka/bin/kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 9 --topic topic2

#EXCLUIR TÓPICOS 
docker exec -ti kafka-lab_kafka_1 /opt/kafka/bin/kafka-topics.sh --delete --bootstrap-server localhost:9092 --topic topic2

#ENVIAR MENSAGEM TÓPICOS 
docker exec -ti kafka-lab_kafka_1 /opt/kafka/bin/kafka-console-producer.sh --topic topic1 --bootstrap-server localhost:9092