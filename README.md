# Cinema_reservation_java
# 🎬 Cinema Reservation System 🎟️

## 📖 Project Overview

The **Cinema Reservation System** is a Java Spring Boot web application that allows users to browse available films, reserve tickets, and manage cinema schedules. The system supports three distinct roles with varying permissions: **Anonymous Users**, **Authenticated Users**, and **Administrators**.

---

## 🚀 Features

### 🌐 For Anonymous Users (Unauthenticated)
- Browse available films 🎞️.
- View film details, including descriptions, schedules, and prices.
- Discover the latest cinema offers and promotions.

### 👤 For Authenticated Users (Logged-In Users)
- All the features of anonymous users.
- Reserve tickets for available screenings.
- View personal reservation history.
- Update personal information.

### 🛠️ For Administrators
- All the features of authenticated users.
- Add, edit, or delete films.
- Schedule screenings (set hall, date, time, and pricing).
- View a list of all reservations.
- Cancel any reservation if necessary.

---

## 🏗️ Tech Stack

- **Java Spring Boot** - Backend framework.
- **Thymeleaf** - Server-side template engine for views.
- **Spring Security** - User authentication and authorization.
- **Hibernate (JPA)** - ORM for database interactions.
- **PostgreSQL** - Database for storing system data.
- **Maven** - Dependency management.
- **HTML, CSS, JavaScript** - Frontend technologies.

---

## 🛠️ System Architecture

- **MVC Pattern**: Clear separation of concerns.
- **Role-Based Access Control**: Different permissions for anonymous, authenticated, and admin users.
- **Secure Authentication**: Passwords are securely hashed using Spring Security.
- **Modular Design**: Easy to extend and maintain.

---

## ⚙️ Installation Guide

### 1️⃣ Clone the Repository
```bash
git clone https://github.com/Barbarossa01/Cinema_reservation_java.git
cd Cinema_reservation_java
