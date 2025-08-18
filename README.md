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

### With test

`mvn clean package`

### Without test

`mvn clean package -DskipTests`

## Docker guideline

### Build docker image

#### Thủ công

`docker build -t <account>/glamora-store:<tag> .`

#### Dùng docker-compose.yml (Bằng cách thêm cái account là vovantri123 vào chỗ image: vovantri123/glamora-store:0.1)

`docker compose up -d --build`

### Push docker image to Docker Hub

`docker image push vovantri123/glamora-store:0.1`

### Pull docker image from Docker Hub

`docker pull vovantri123/glamora-store:0.1`

### Run docker image by file docker-compose.yml (Cả tự build hoặc pull từ Docker Hub về)

`docker compose up -d`