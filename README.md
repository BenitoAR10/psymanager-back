# 🧠 Psymanager Backend

Este repositorio contiene el backend del sistema integral **Psymanager**, una plataforma web y móvil con gamificación para la gestión y monitoreo del servicio de atención psicológica universitaria.

## 🎓 Proyecto de Grado

**Universidad Católica Boliviana “San Pablo”**  
Facultad de Ingeniería – Departamento de Ingeniería de Sistemas  
**Autor:** Amir Adolfo Rojas Bellido  
**Tutor:** M.Sc. Ángel Ávila Maceda  
**Relator:** M.Sc. Jorge Tancara Aguilar

---

## 📌 Descripción General

Psymanager está diseñado para asistir a estudiantes y terapeutas en el manejo de sesiones psicológicas, ofreciendo además un **kit de ayuda inmediata** gamificado para situaciones de ansiedad o estrés.

- **Estudiantes (App móvil):**
  - Autenticación vía JWT (OAuth2)
  - Visualización y reserva de horarios
  - Acceso a ejercicios guiados en momentos de crisis
  - Sistema de recompensas y logros

- **Terapeutas (App web):**
  - Autenticación vía Google OAuth2
  - Gestión de citas, historial clínico y tratamientos
  - Administración de disponibilidad
  - Visualización de sesiones

---

## 🧱 Estructura del Proyecto

```plaintext
src/
├── bo.com.ucb.psymanager/
│   ├── bl/                 # Lógica de negocio por dominio (Schedule, Treatment, etc.)
│   ├── config/             # Configuración: CORS, Seguridad, MinIO, RabbitMQ, Twilio
│   ├── controller/         # Controladores REST
│   ├── dao/                # DAOs (equivalente a repositorios)
│   ├── dto/                # DTOs para transferencia de datos
│   ├── entities/           # Entidades JPA
│   ├── enums/              # Enumeraciones (SessionState)
│   ├── events/             # Publisher/Consumer de eventos para notificaciones
│   ├── exceptions/         # Manejo centralizado de errores
│   ├── notification/       # Servicios de notificación (Observer Pattern)
│   ├── service/            # Servicios auxiliares (e.g. Minio)
│   └── util/               # Utilidades generales
resources/
├── application.yml         # Configuración principal
├── log4j.properties        # Logging
```

---

## 🔐 Autenticación

- **Web:** Google OAuth2 (cuenta institucional)
- **Móvil:** JWT emitido desde backend con login propio
- Autorización por roles: `ADMIN`, `THERAPIST`, `PATIENT`
- Seguridad gestionada con **Spring Security**

---

## 🔔 Sistema de Notificaciones

- Implementado con **RabbitMQ** y patrón **Observer**
- Canales disponibles:
  - WhatsApp (Twilio)
  - Email (soporte futuro)

---

## 🧩 Clases destacadas

- `AmqpEventPublisher` — emisor de eventos
- `AppointmentBookedConsumer`, `AppointmentRejectedConsumer`, `AppointmentReminderConsumer`
- `TwilioNotificationService` — suscriptor concreto
- `NotificationServiceImpl` — orquestador de notificaciones

---

## ⚙️ Tecnologías Utilizadas

| Tecnología       | Versión         | Descripción                              |
|------------------|------------------|------------------------------------------|
| Java             | 17               | Lenguaje de programación principal       |
| Spring Boot      | 3.4.2            | Backend y APIs REST                      |
| PostgreSQL       | 16               | Base de datos relacional                 |
| MinIO            | Última estable   | Almacenamiento de archivos               |
| RabbitMQ         | Última estable   | Sistema de colas para eventos            |
| Twilio API       | Actual           | Envío de notificaciones (WhatsApp)       |

---

## 🚀 Cómo ejecutar el proyecto

1. Clona el repositorio:

```bash
git clone https://github.com/amirrojasdev/psymanager-backend.git
cd psymanager-backend
```

2. Configura las variables necesarias en `application.yml` o mediante `.env`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/psymanager
    username: postgres
    password: tu_contraseña

jwt:
  secret: tu_clave_secreta

minio:
  url: http://localhost:9000
  accessKey: minioadmin
  secretKey: minioadmin

twilio:
  accountSid: TU_SID
  authToken: TU_TOKEN
  fromNumber: "+1415XXXXXXX"
```

3. Ejecuta el proyecto con Maven o desde tu IDE:

```bash
./mvnw spring-boot:run
```
