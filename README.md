# Backend Marketplace

Backend para un marketplace de productos próximos a vencer. Permite gestionar negocios, usuarios, productos y reservas para reducir desperdicio y facilitar ofertas cercanas a la fecha de expiración.

## Requisitos
- JDK 17 o superior
- Maven 3.x
- MySQL 8.x

## Configuración de base de datos
1. Crear la base de datos y el usuario que utilizará la aplicación:
   ```sql
   CREATE DATABASE marketplace CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   CREATE USER 'market_user'@'%' IDENTIFIED BY 'contraseña_segura';
   GRANT ALL PRIVILEGES ON marketplace.* TO 'market_user'@'%';
   FLUSH PRIVILEGES;
   ```
2. Asegurarse de que MySQL esté en ejecución y accesible.

## Configuración de `application.properties`
Editar `src/main/resources/application.properties` con los datos de conexión y opciones básicas:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/marketplace?useSSL=false&serverTimezone=UTC
spring.datasource.username=market_user
spring.datasource.password=contraseña_segura
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
server.port=8080
```
Ajustar usuario, contraseña y puerto según tu entorno.

## Ejecutar el backend
Desde la raíz del proyecto:
```bash
mvn spring-boot:run
```

## Rutas principales de la API (puerto 8080)
- `GET/POST /api/productos`
- `GET/POST /api/negocios`
- `GET/POST /api/usuarios`
- `GET/POST /api/reservas`

El backend queda accesible en `http://localhost:8080`.
