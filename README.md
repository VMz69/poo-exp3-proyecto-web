# poo-exp3-proyecto-web

Proyecto web desarrollado en Java como parte de la Experiencia Práctica 3 (EXP3), enfocado en Programación Orientada a Objetos y arquitectura MVC usando tecnologías estándar de Java EE.

---

## 📌 Tecnologías utilizadas

- Java
- Servlets
- JSP
- JSTL
- Maven
- HTML / CSS
- JDBC
- MySQL (u otro motor configurable)
- Apache Tomcat

---

## 🧱 Arquitectura

El proyecto sigue el **patrón MVC (Model–View–Controller)**:

- **Model**: Clases Java (Beans / DAO) que representan las entidades y lógica de acceso a datos.
- **View**: Archivos JSP encargados de la presentación.
- **Controller**: Servlets que gestionan las peticiones HTTP y coordinan el flujo de la aplicación.

---

## 📂 Estructura del proyecto

```
poo-exp3-proyecto-web
├── src
│   └── main
│       ├── java
│       │   └── com/
│       │       └── biblioteca/
│       │           ├── controllers/
│       │           ├── models/
│       │           ├── filters/
│       │           └── utils/
│       ├── resources
│       └── webapp
│           ├── css/
│           ├── js/
│           ├── images/
│           ├── WEB-INF/
│           │   └── web.xml
│           └── *.jsp
├── pom.xml
└── README.md
```

---

## 🔐 Funcionalidades principales

- Autenticación de usuarios (login / logout)
- Manejo de sesiones HTTP
- Filtro de autenticación para proteger rutas
- CRUD de entidades del sistema
- Validación de datos
- Separación clara de capas (modelo, vista y controlador)

---

## ▶️ Ejecución del proyecto

1. Clonar el repositorio:
   ```bash
   git clone https://github.com/VMz69/poo-exp3-proyecto-web.git
   ```

2. Importar el proyecto en un IDE compatible con Maven.

3. Configurar la base de datos según los parámetros del proyecto.

4. Compilar con Maven:
   ```bash
   mvn clean package
   ```

5. Desplegar el archivo WAR en Apache Tomcat.

6. Acceder desde el navegador a:
   ```
   http://localhost:8080/poo-exp3-proyecto-web
   ```

---

## 🧪 Consideraciones

- El control de acceso se realiza mediante filtros.
- Las vistas JSP no son accesibles directamente sin pasar por el controlador.
- La lógica de negocio no está embebida en las vistas.
- La aplicación consume una **base de datos remota** alojada en un **servidor privado** perteneciente a un integrante del equipo.
- Los parámetros de conexión pueden variar según el entorno de ejecución.

---

## 👤 Autor

Proyecto académico desarrollado por **VMz69**.

---

## 📄 Licencia

Uso académico / educativo.

