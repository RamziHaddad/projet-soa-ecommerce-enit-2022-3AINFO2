Running the application in dev mode
Starting a multi-node cluster with Docker Compose

cd src/main/docker/
docker-compose up
You can run your application in dev mode that enables live coding using:

./mvnw compile quarkus:dev
Fruit demo
Single page app to create and search for fruit

http://localhost:8080/product.html