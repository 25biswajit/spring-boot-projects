### Show running Docker Containers
docker container ps or docker container ls

### Stop Existing Docker Container
docker stop <Container ID>

### Remove Existing Docker Container
docker rm <Container ID>

### Build Project
mvn clean install

### Build Docker Image
docker build -t spring-boot-jpa-crud-image .

### Pull MySql Image
docker pull mysql:latest

### Executed Docker MySql Image 
#### Container Name mysql-standalone
#### Host Machine 192.168.99.100:6033 <--- Docker 3306
docker run --detach --publish 6033:3306 --name=mysql-standalone --env="MYSQL_ROOT_PASSWORD=root" --env="MYSQL_DATABASE=test" mysql:latest

### Verify using docker logs
docker logs mysql-standalone

### Run Spring Boot Image link with Mysql Image
#### Host Machine 192.168.99.100:8086 <--- Docker 8086
docker run --detach --publish 8086:8086 --name=spring-boot-jpa-crud-image --link mysql-standalone:mysql spring-boot-jpa-crud-image

### Verify using docker logs
docker logs --follow spring-boot-jpa-crud-image

#### GET http://192.168.99.100:8086/users
#### GET http://192.168.99.100:8086/users?id=1
#### GET http://192.168.99.100:8086/users?id=100
##### POST http://192.168.99.100:8086/users
{"name": "Tejas","address": "Baranashi","salary": 20000,"age": 30}
##### PUT http://192.168.99.100:8086/users
{"id": 1, "name": "Tejas","address": "Baranashi","salary": 20000,"age": 30}
##### DELETE http://192.168.99.100:8086/users/10
##### DELETE http://192.168.99.100:8086/users