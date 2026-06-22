# REST API Reference

Base URL: `http://localhost:8080/api/v1`

All protected endpoints require a JWT token in the `Authorization` header:

```
Authorization: Bearer <your-jwt-token>
```

---

## Table of Contents

- [Authentication](#authentication)
  - [Register](#register)
  - [Login](#login)
- [Employees](#employees)
  - [Get All / Search](#get-all--search-employees)
  - [Get Employee Count](#get-employee-count)
  - [Add Employee](#add-employee)
  - [Update Employee](#update-employee)
  - [Delete Employee](#delete-employee)
- [Departments](#departments)
  - [Get All Departments](#get-all-departments)
  - [Get Department by ID](#get-department-by-id)
  - [Add Department](#add-department)
  - [Update Department](#update-department)
  - [Delete Department](#delete-department)
- [Error Responses](#error-responses)

---

## Authentication

### Register

**`POST`** `/api/v1/auth/register`

> 🔓 Public — no token required.

**Request Body:**

| Field             | Type   | Required | Constraints              | Description                      |
|-------------------|--------|----------|--------------------------|----------------------------------|
| `username`        | String | ✅       | 5–50 chars, unique       | Login username                   |
| `email`           | String | ✅       | Valid email format, unique | User email                     |
| `password`        | String | ✅       | 6–255 chars              | Plain text (hashed on server)    |
| `confirmPassword` | String | ✅       | 6–255 chars              | Must match `password`            |
| `role`            | String | ✅       | `ROLE_USER` / `ROLE_ADMIN` | User role                      |

**Example Request:**

```json
{
  "username": "johndoe",
  "email": "john@example.com",
  "password": "secret123",
  "confirmPassword": "secret123",
  "role": "ROLE_USER"
}
```

**Response `201 Created`:**

```json
{
  "id": 4,
  "username": "johndoe",
  "email": "john@example.com",
  "role": "ROLE_USER",
  "createdAt": "2026-06-22T10:00:00",
  "updatedAt": "2026-06-22T10:00:00"
}
```

---

### Login

**`POST`** `/api/v1/auth/login`

> 🔓 Public — no token required.

**Request Body:**

| Field      | Type   | Required | Constraints   |
|------------|--------|----------|---------------|
| `username` | String | ✅       | 5–50 chars    |
| `password` | String | ✅       | 6–255 chars   |

**Example Request:**

```json
{
  "username": "admin",
  "password": "123456"
}
```

**Response `200 OK`:**

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInJvbGUiOiJST0xFX0FETUlOIiwiaWF0IjoxNzUwNTI2MDAwfQ.xxxx"
}
```

> Copy the `token` value and use it as `Authorization: Bearer <token>` in all subsequent requests.

---

## Employees

### Get All / Search Employees

**`GET`** `/api/v1/employees`

> 🔐 Requires: `ROLE_USER` or `ROLE_ADMIN`

**Query Parameters:**

| Parameter | Type   | Required | Default | Description                                       |
|-----------|--------|----------|---------|---------------------------------------------------|
| `keyword` | String | ❌       | —       | Search by employee name or department name (LIKE) |

**Examples:**

```
GET /api/v1/employees
GET /api/v1/employees?keyword=alice
GET /api/v1/employees?keyword=engineering
```

**Response `200 OK`:**

```json
[
  {
    "id": 1,
    "name": "Alice",
    "email": "alice@example.com",
    "departmentId": 1,
    "departmentName": "Human Resources",
    "createdAt": "2026-06-04T10:00:00",
    "updatedAt": "2026-06-04T10:00:00"
  }
]
```

---

### Get Employee Count

**`GET`** `/api/v1/employees/report/count`

> 🔐 Requires: `ROLE_USER` or `ROLE_ADMIN`
>
> Result is **cached** (Caffeine) and refreshed every 60 seconds.

**Response `200 OK`:**

```
Total current employees are: 6
```

---

### Add Employee

**`POST`** `/api/v1/employees`

> 🔐 Requires: `ROLE_ADMIN`

**Request Body:**

| Field          | Type   | Required | Constraints                | Description              |
|----------------|--------|----------|----------------------------|--------------------------|
| `name`         | String | ✅       | 2–100 chars                | Employee full name       |
| `email`        | String | ✅       | Valid email format, unique | Employee email           |
| `departmentId` | Long   | ✅       | Must reference existing ID | Department to assign to  |

**Example Request:**

```json
{
  "name": "Jane Doe",
  "email": "jane@example.com",
  "departmentId": 3
}
```

**Response `201 Created`:**

```json
{
  "id": 7,
  "name": "Jane Doe",
  "email": "jane@example.com",
  "departmentId": 3,
  "departmentName": "Engineering",
  "createdAt": "2026-06-22T10:00:00",
  "updatedAt": "2026-06-22T10:00:00"
}
```

---

### Update Employee

**`PUT`** `/api/v1/employees/{id}`

> 🔐 Requires: `ROLE_ADMIN`

**Path Parameter:**

| Parameter | Type | Description |
|-----------|------|-------------|
| `id`      | Long | Employee ID |

**Request Body:**

| Field          | Type   | Required | Constraints                         | Description              |
|----------------|--------|----------|-------------------------------------|--------------------------|
| `name`         | String | ✅       | 2–100 chars                         | Employee full name       |
| `email`        | String | ✅       | Valid email format, unique (excl. self) | Employee email       |
| `departmentId` | Long   | ✅       | Must reference existing ID          | Department to assign to  |

**Example Request:**

```json
{
  "name": "Jane Smith",
  "email": "jane.smith@example.com",
  "departmentId": 2
}
```

**Response `200 OK`:** Same structure as Add Employee response.

---

### Delete Employee

**`DELETE`** `/api/v1/employees/{id}`

> 🔐 Requires: `ROLE_ADMIN`

**Path Parameter:**

| Parameter | Type | Description |
|-----------|------|-------------|
| `id`      | Long | Employee ID |

**Response `204 No Content`**

---

## Departments

### Get All Departments

**`GET`** `/api/v1/departments`

> 🔐 Requires: `ROLE_USER` or `ROLE_ADMIN`

**Response `200 OK`:**

```json
[
  {
    "id": 1,
    "name": "Human Resources",
    "createdAt": "2026-06-04T10:00:00",
    "updatedAt": "2026-06-04T10:00:00"
  },
  {
    "id": 2,
    "name": "Finance",
    "createdAt": "2026-06-04T10:00:00",
    "updatedAt": "2026-06-04T10:00:00"
  }
]
```

---

### Get Department by ID

**`GET`** `/api/v1/departments/{id}`

> 🔐 Requires: `ROLE_USER` or `ROLE_ADMIN`

**Path Parameter:**

| Parameter | Type | Description   |
|-----------|------|---------------|
| `id`      | Long | Department ID |

**Response `200 OK`:** Single department object.

---

### Add Department

**`POST`** `/api/v1/departments`

> 🔐 Requires: `ROLE_ADMIN`

**Request Body:**

| Field  | Type   | Required | Constraints          | Description          |
|--------|--------|----------|----------------------|----------------------|
| `name` | String | ✅       | 2–100 chars, unique  | Department name      |

**Example Request:**

```json
{
  "name": "Research & Development"
}
```

**Response `201 Created`:**

```json
{
  "id": 7,
  "name": "Research & Development",
  "createdAt": "2026-06-22T10:00:00",
  "updatedAt": "2026-06-22T10:00:00"
}
```

---

### Update Department

**`PUT`** `/api/v1/departments/{id}`

> 🔐 Requires: `ROLE_ADMIN`

**Path Parameter:**

| Parameter | Type | Description   |
|-----------|------|---------------|
| `id`      | Long | Department ID |

**Request Body:** Same as Add Department.

**Response `200 OK`:** Updated department object.

---

### Delete Department

**`DELETE`** `/api/v1/departments/{id}`

> 🔐 Requires: `ROLE_ADMIN`

**Response `204 No Content`**

---

## Error Responses

All API errors return a consistent JSON structure:

```json
{
  "status": 404,
  "error": "Not Found",
  "message": "Employee not found with id: 99",
  "path": "/api/v1/employees/99"
}
```

### Validation Error (400)

When request body fails `@Valid` constraints, each invalid field is listed:

```json
{
  "status": 400,
  "error": "Bad Request",
  "message": {
    "name": "Employee name cannot be blank!",
    "email": "Email format is incorrect (eg.: example@gmail.com)!"
  }
}
```

### HTTP Status Code Reference

| Code | Meaning               | When                                          |
|------|-----------------------|-----------------------------------------------|
| `200` | OK                   | Successful GET / PUT                          |
| `201` | Created              | Successful POST                               |
| `204` | No Content           | Successful DELETE                             |
| `400` | Bad Request          | Validation failure (missing/invalid fields)   |
| `401` | Unauthorized         | Missing or invalid JWT token                  |
| `403` | Forbidden            | Authenticated but insufficient role           |
| `404` | Not Found            | Resource does not exist                       |
| `409` | Conflict             | Duplicate resource (email / department name)  |
| `500` | Internal Server Error| Unexpected server-side error                  |

