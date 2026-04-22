# 🏥 Sistema de Gestión Hospitalaria

API REST desarrollada en **Spring Boot** para la gestión de pacientes, turnos, consultas médicas y listas de espera.  
Incluye integración con un microservicio de IA y sistema de notificaciones automáticas por email.

---

## 🚀 Features

- 👤 Gestión de pacientes (CRUD)
- 📅 Gestión de turnos médicos
- 🩺 Registro de consultas
- ⏳ Sistema de listas de espera
- 📬 Emails automáticos (recordatorios)
- 🤖 Integración con microservicio de IA
- 🔐 Autenticación con JWT
- 📄 Documentación con Swagger

---

## 🧩 Módulos

### 👤 Pacientes
- Alta, baja y modificación
- Consulta de datos personales

### 📅 Turnos
- Creación y asignación de turnos
- Asociación con pacientes
- Recordatorios automáticos:
  - 24h antes
  - 2h antes

### 🩺 Consultas
- Registro de consultas médicas
- Historial por paciente

### ⏳ Lista de espera
- Gestión de pacientes en espera
- Ordenamiento por prioridad

### 🤖 IA (Microservicio)
- Chatbot para consultas
- Creación de turnos mediante interacción

---

## 🛠️ Tech Stack

- **Java + Spring Boot**
- **PostgreSQL**
- **JWT (Auth)**
- **Swagger**
- **Microservices architecture**
- **Email service**

---

## ⚙️ Configuración

Crear un archivo `.env` con:

```env
# Base de datos
DATABASE_URL=jdbc:postgresql://db.tuproyecto.supabase.co:5432/postgres
DATABASE_USERNAME=postgres.tuproyecto
DATABASE_PASSWORD=tu_password_de_supabase

# JWT
JWT_SECRET=clave_secreta_local_para_desarrollo

# CORS
CORS_ORIGINS=http://localhost:3000,http://localhost:5173

# Puerto
PORT=8080
```

---

## ▶️ Run local

```bash
git clone https://github.com/Santy-M1502/Sistema-de-Gestion-Hospitalaria.git
cd Sistema-de-Gestion-Hospitalaria
```

Configurar las variables de entorno y ejecutar la app.

---

## 📄 API Docs (Swagger)

Una vez levantado el proyecto:

```
http://localhost:8080/swagger-ui/index.html
```

---

## 🔐 Autenticación

El sistema utiliza JWT:

- Login → devuelve token
- Requests protegidas → requieren header:

```
Authorization: Bearer <token>
```

---

## 📡 Endpoints principales

| Endpoint     | Descripción             |
|--------------|-------------------------|
| `/api/pacientes` | Gestión de pacientes    |
| `/api/medicos` | Gestión de medicos    |
| `/api/turnos`    | Gestión de turnos       |
| `/api/consultas` | Consultas médicas       |
| `/api/lista-espera`    | Lista de espera         |
| `/api/auth`      | Autenticación           |
| `/api/chat`        | Integración con IA      |

---

## 📬 Notificaciones

Se envían emails automáticos:

- ⏰ 24 horas antes del turno
- ⏰ 2 horas antes del turno

---

## 🏗️ Arquitectura

```
[ Cliente ]
     ↓
[ API Spring Boot ]
     ↓
[ PostgreSQL ]

+ Microservicio IA
+ Servicio de Emails
```

---

## 📌 Estado del proyecto

- ✅ CRUD completo
- ✅ JWT implementado
- ✅ Integración con IA
- ✅ Emails automáticos
- ✅ Swagger
- ❌ Deploy (pendiente)

---

## 👨‍💻 Autor

Desarrollado por **Santi**
