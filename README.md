# 🏦 Bank-App  
A complete banking web application built with **Spring Boot**, **Thymeleaf**, **MySQL**, and deployed on **Railway**.

---

## 🚀 Features

### 👤 User Management  
- Account registration  
- Secure login (Spring Security)  
- Password encryption  

### 💰 Bank Account Features  
- Display account balance  
- Deposit money  
- Withdraw money (with sufficient balance check)  
- Transfer money to another user  

### 📜 Transaction History  
- Every operation is recorded automatically  
- Transactions include: Deposits, Withdrawals, Transfers  
- Styled with green (income) and red (expense) indicators  

### 🎨 Modern UI  
- Responsive interface with **Bootstrap**  
- Dark Mode / Light Mode toggle  
- Custom bank logo and branding  

---

## 🛠️ Technologies Used

| Technology | Purpose |
|------------|----------|
| **Java 17+** | Main programming language |
| **Spring Boot 3** | Backend framework |
| **Spring MVC** | Web architecture |
| **Spring Security** | Authentication & security |
| **Spring Data JPA** | Database access |
| **Thymeleaf** | Server-side rendering |
| **MySQL** | Relational database |
| **Bootstrap 4** | Front-end design |
| **Railway** | Hosting & deployment |

---

## 📁 Project Structure

/src
├── main
│ ├── java/com/example/bankapp
│ │ ├── controller/
│ │ ├── service/
│ │ ├── repository/
│ │ ├── model/
│ │ └── BankappApplication.java
│ ├── resources
│ │ ├── templates/ (HTML files)
│ │ ├── static/ (CSS, images)
│ │ └── application.properties
└── test



---

## ⚙️ How to Run Locally

### 1️⃣ Clone the repository

```bash
git clone https://github.com/your-account/Bank-App.git
cd Bank-App


2️⃣ Set up MySQL

###Create a database:
```CREATE DATABASE bankapp;
```

###Configure application.properties:
```spring.datasource.url=jdbc:mysql://localhost:3306/bankapp
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

3️⃣ Run the project
```mvn spring-boot:run
```

###Then open:
```http://localhost:8080
```

🌍 Deployment on Railway
Steps to Deploy

1.Push the project to GitHub

2.Create a new Railway project

3.Add your GitHub repository

4.Add a new MySQL database on Railway

5.Railway automatically generates environment variables:

```MYSQL_URL
MYSQLUSER
MYSQLPASSWORD
MYSQLHOST
MYSQLPORT
MYSQLDATABASE
```

6.Update application.properties for Railway:
```spring.datasource.url=${MYSQL_URL}
spring.datasource.username=${MYSQLUSER}
spring.datasource.password=${MYSQLPASSWORD}
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
```

👩‍💻 Author

Chaymae AL Morchid
Java & Spring Boot Developer

