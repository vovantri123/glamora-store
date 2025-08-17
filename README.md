# GlamoraStore - Website Bán Phụ Kiện Thời Trang

## 🚀 Khởi tạo Database

### Chạy lệnh:

**Windows CMD:**

```cmd
init_db.bat
```

**PowerShell:**

```cmd
./init_db.bat
```

## ⚠️ Lưu ý

- Đảm bảo bạn đã **cài đặt PostgreSQL**

## Tech stack

* Build tool: maven >= 3.9.5
* Java: 17
* Framework: Spring boot 3.2.x
* DBMS: PostgreSQL

## Start application

### Run with application.yml:

`mvn spring-boot:run`

### Run with application-prod.yml

`mvn spring-boot:run -D spring-boot.run.profiles=prod`

## Build application

`mvn clean package`

## Docker guideline

### Create network:

`docker network create glamora-network`

### Start PostgreSQL in glamora-network

`docker run --network glamora-network --name postgres -p 3306:3306 -e MYSQL_ROOT_PASSWORD=root -d mysql:8.0.36-debian`

### Run your application in devteria-network

`docker run --name identity-service --network devteria-network -p 8080:8080 -e DBMS_CONNECTION=jdbc:mysql://mysql:3306/identity_service identity-service:0.9.0`

### Build docker image (Thêm cái account là vovantri123 vào chỗ image: vovantri123/glamora-store:0.1 trong docker-compose.yml cũng được)

`docker build -t <account>/glamora-store:0.1 .`

### Push docker image to Docker Hub

`docker image push <account>/glamora-store:0.1`