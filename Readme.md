# News Aggregator API - Spring Boot Project

## Overview

This project is a **News Aggregator API** built using **Spring Boot** that allows users to register, log in, and fetch news articles based on their preferences. The project uses **JWT** (JSON Web Tokens) for authentication, an **database** for storing user information, and integrates with external news APIs to fetch articles.

## Features

- **User Registration**: New users can register via the `/register` endpoint.
- **User Login**: Registered users can log in and receive a JWT token via the `/signin` endpoint.
- **User Preferences**:
    - Retrieve news preferences using `/preferences`.
    - Update preferences via `/preferences`.
- **Fetch News**: Get personalized news articles based on user preferences via `/news`.
- **Authentication & Authorization**: Secure endpoints using Spring Security and JWT.
- **External API Integration**: Fetch news from third-party APIs of  **NewsAPI**.

## Requirements

- Java 17+
- Spring Boot 3.0+
- Gradle 7+
- Postman or Curl (for testing the API)

## API Endpoints

### 1. **User Registration**
Registers a new user.

- **Endpoint**: `POST /register`
- **Request Body**:
  ```json
  {
    "username": "john_doe",
    "password": "password123",
    "email": "john@example.com"
  }
  ```
- **Response**:
    - `201 Created` on success
    - `400 Bad Request` if validation fails

---

### 2. **User Login**
Logs in a user and returns a JWT token.

- **Endpoint**: `POST /signin`
- **Request Body**:
  ```json
  {
    "username": "john_doe",
    "password": "password123"
  }
  ```
- **Response**:
    - `200 OK` with JWT token
    - `401 Unauthorized` if authentication fails

---

### 3. **Get Preferences**
Retrieves the logged-in user's news preferences.

- **Endpoint**: `GET /preferences`
- **Headers**:  
  `Authorization: Bearer <JWT_TOKEN>`
- **Response**:
  ```json
  ["technology", "business"]
  ```

---

### 4. **Update Preferences**
Updates the logged-in user's news preferences.

- **Endpoint**: `PUT /preferences`
- **Headers**:  
  `Authorization: Bearer <JWT_TOKEN>`
- **Request Body**:
  ```json
  {
    "categories": ["health", "science"]
  }
  ```
- **Response**:
    - `200 OK` on success
    - `400 Bad Request` if validation fails

---

### 5. **Fetch News**
Fetches news articles based on the user's preferences.

- **Endpoint**: `GET /news`
- **Headers**:  
  `Authorization: Bearer <JWT_TOKEN>`
- **Response**:
  ```json
  [
    {
      "title": "Latest in Technology",
      "description": "The latest advancements in technology...",
      "url": "https://newsapi.org/article/123"
    },
    {
      "title": "Business Trends",
      "description": "Top business trends for 2024...",
      "url": "https://newsapi.org/article/456"
    }
  ]
  ```

---

## External API Integration

The project integrates with external APIs of [NewsAPI](https://newsapi.org).

---

## Input Validation

User input is validated using **Spring Validation** annotations. Common validations include:

- Non-null and non-empty fields for registration and preference updates
- Email format validation for user registration
- List size and content type validation for news preferences

---

## Testing

Use **Postman** or **Curl** to test the API endpoints:

### Example Curl Commands

**Register a User**:
```bash
curl -X POST http://localhost:8088/register -H "Content-Type: application/json" -d '{"username":"john_doe","password":"password123","email":"john@example.com"}'
```

**Login**:
```bash
curl -X POST http://localhost:8088/signin -H "Content-Type: application/json" -d '{"username":"john_doe","password":"password123"}'
```

**Get Preferences**:
```bash
curl -X GET http://localhost:8088/preferences -H "Authorization: Bearer <JWT_TOKEN>"
```

---