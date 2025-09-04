# EcoScore

## Configuração do mySQL com Docker

1. `docker-compose.yml`
    ```yaml
    version: "3.9"
    
    services:
      mysql:
        image: mysql:8.0
        container_name: ecoscore-mysql
        restart: always
        environment:
          MYSQL_ROOT_PASSWORD: root
          MYSQL_DATABASE: ecoscore_db
          MYSQL_USER: ecoscore_user
          MYSQL_PASSWORD: ecoscore_pass
        ports:
          - "3306:3306"
        volumes:
          - mysql_data:/var/lib/mysql
    
    volumes:
      mysql_data:
    ```

2. Suba o container
    ```bash
    docker compose -f ./docker-compose.yml up -d
    ```