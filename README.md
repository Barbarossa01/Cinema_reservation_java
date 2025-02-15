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
```

### 2️⃣ Configure the Database
Ensure you have PostgreSQL installed and create a database:
```sql
CREATE DATABASE cinema_reservation;
```
Update `application.properties` with your database credentials:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/cinema_reservation
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
```

### 3️⃣ Run the Application
```bash
mvn spring-boot:run
```

The application will be accessible at:  
**[http://localhost:8080](http://localhost:8080)**

---

## 🔑 Default User Credentials (For Testing)

| Role         | Email                  | Password  |
|---------------|-----------------------|-----------|
| **Admin**     | admin@cinema.com       | admin123  |
| **Admin**     | admin@admin.com        | admin12345|
| **User**      | user@cinema.com        | user123   |
| **User**      | maja@maja.maja         | maja123   |

*(You can change these credentials in the database.)*

---

## 🛡️ Security Measures

- **Password Encryption**: User passwords are stored securely using BCrypt.
- **Role-Based Access Control**: Access to sensitive actions is restricted.
- **Input Validation**: Forms are validated on both client and server sides to prevent injection attacks.

---

## 🔮 Future Improvements (Roadmap)

- 🎯 Add payment integration (e.g., Stripe/PayPal) for ticket payments.
- 🎯 Implement seat selection during reservation.
- 🎯 Improve UI/UX with React or Vue.js.
- 🎯 Add user activity logs for better monitoring.

---

## 🧑‍💻 Contributors

- **Mohammed** *(Project Owner/Developer)*  
  GitHub: [Barbarossa01](https://github.com/Barbarossa01)

---

## 📄 License

This project is licensed under the **MIT License**.

---

🎬 **Enjoy using the Cinema Reservation System!** 🍿  
Feel free to fork, improve, and contribute! 🤝

