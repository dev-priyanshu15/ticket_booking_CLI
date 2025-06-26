# 🚆 Train Ticket Booking System (Java + JSON-Based CLI)

A **robust, extensible, and production-grade Java-based CLI application** designed for managing train ticket reservations. This system simulates core functionalities of real-world railway booking systems like IRCTC—providing secure authentication, live seat availability, and a smooth ticket lifecycle—from booking to cancellation. Data is managed using structured JSON files, ensuring portability and flexibility for future API or database integrations.

---

## 📌 Table of Contents

* [📖 Project Overview](#-project-overview)
* [🚀 Features](#-features)
* [🏗 Architecture & Structure](#-architecture--structure)
* [⚙️ Setup & Installation](#-setup--installation)
* [💻 Running the Application](#-running-the-application)
* [🧾 Sample JSON Schema](#-sample-json-schema)
* [🧠 Booking Workflow](#-booking-workflow)
* [🧪 Interactive CLI Demo](#-interactive-cli-demo)
* [📈 Planned Enhancements](#-planned-enhancements)
* [🛡 License](#-license)
* [👨‍💻 Author](#-author)

---

## 📖 Project Overview

This project is a **simulation of a real-time train booking system** tailored for command-line usage. It supports complete booking functionality, including:

* 🔐 Account creation and login with encrypted passwords
* 🔎 Train searches between arbitrary station pairs
* 🧭 Route and schedule tracking with accurate time maps
* 🪑 Seat visualization and matrix-based selection
* 🎫 Ticket confirmation and cancellation using IDs

> Designed as a backend-first, modular system ready to be integrated into enterprise-grade applications.

---

## 🚀 Features

* ✅ **User Authentication**: Secure login with password hashing (`bcrypt`)
* 🧭 **Multi-Station Routing**: Search trains across multi-hop station routes
* 📊 **Matrix Seat Map**: 2D array for real-time seat status
* 🎟 **Booking Engine**: Handles validations, rebooking, and cancellation
* 🧾 **Ticket Tracking**: Each booking has a traceable unique ID
* 🗃 **Data Persistence**: Fast JSON-based storage via Jackson
* 📦 **Domain-Driven Codebase**: Clean structure for models, services, utils

---

## 🏗 Architecture & Structure

```
📁 ticket_booking_CLI/
├── app/
│   ├── App.java                  # 🚪 Main CLI application entry
│   ├── entities/
│   │   ├── Train.java            # 🛤️ Train route & seat structure
│   │   ├── Ticket.java           # 🎫 Ticket object
│   │   └── User.java             # 👤 User credentials
│   ├── localDb/
│   │   ├── trains.json           # 🚉 Train schedules & seats
│   │   └── users.json            # 👥 Registered users
│   ├── services/
│   │   ├── TrainService.java     # 🔍 Train search & routing
│   │   └── UserBookingService.java # ✅ Booking & cancellation
│   └── util/
│       └── UserServiceUtil.java # 🔧 User helper methods
├── test/                         # ✅ Unit tests for core modules
│   └── ticket.booking/          
└── build.gradle                 # ⚙ Gradle build file
```

---

## ⚙️ Setup & Installation

### 🔧 Prerequisites

* Java 21 (as per Gradle toolchain config)
* Gradle installed
* Terminal or Java IDE (VS Code, IntelliJ, etc.)

### 📦 Dependencies

```groovy
dependencies {
  implementation 'com.fasterxml.jackson.core:jackson-databind:2.12.6'
  implementation 'org.mindrot:jbcrypt:0.4'
  testImplementation 'junit:junit:4.13.2'
}
```

---

## 💻 Running the Application

### 1️⃣ Clone the Repository

```bash
git clone https://github.com/dev-priyanshu15/ticket_booking_CLI.git
cd ticket_booking_CLI
```

### 2️⃣ Run the CLI

```bash
./gradlew run       # On macOS/Linux
gradlew.bat run     # On Windows
```

---

## 🧾 Sample JSON Schema

```json
{
  "train_id": "bacs123",
  "train_no": "12345",
  "stations": ["bangalore", "jaipur", "delhi"],
  "station_times": {
    "bangalore": "13:50:00",
    "jaipur": "15:10:00",
    "delhi": "18:30:00"
  },
  "seats": [
    [0, 0, 0, 0, 0, 0],
    [1, 1, 1, 0, 0, 0],
    [0, 0, 0, 0, 1, 0],
    [0, 0, 0, 0, 0, 1]
  ]
}
```

Legend:

* `0` = available
* `1` = booked

---

## 🧠 Booking Workflow

1. 🔐 User logs in securely (password check via bcrypt)
2. 📍 Source and destination stations are entered
3. 🚉 Available trains are listed with real-time schedule
4. 🪑 A 4x6 seat matrix is rendered for selected train
5. 🎫 User chooses a row-column pair to book a seat
6. ✅ A Ticket ID is issued and stored in memory (JSON)
7. ❌ Ticket can be cancelled → seat becomes available again

---

## 🧪 Interactive CLI Demo

```
Welcome to Train Booking System

1. Sign Up
2. Login
3. Fetch Bookings
4. Search Trains
5. Book a Seat
6. Cancel a Booking
7. Exit

> 2
Enter username: gourav
Password: ******
Login successful.

> 4
Source: bangalore
Destination: delhi
Train Found: bacs123

> 5
Seat Matrix:
0 0 0 1 0 0
0 0 0 0 0 0
0 0 0 1 0 0
0 0 0 0 0 1

Enter row: 2
Enter col: 4
🎫 Seat booked! Ticket ID: TCK12345

> 3
🧾 Booking Summary:
Train: bacs123
Seat: Row 2, Col 4
Route: bangalore → delhi

> 6
Cancel Ticket ID: TCK12345
✔️ Ticket successfully cancelled.
```

---

## 📈 Planned Enhancements

* 📅 Date-based train scheduling
* ✉️ Email ticket confirmation with SMTP
* 🧾 PDF ticket export
* 🌐 REST API with Spring Boot
* 🔒 JWT-based authentication for APIs
* 📊 Admin dashboard for managing routes and trains
* 📁 Migration to MongoDB or PostgreSQL
* 🧩 Angular/React frontend for web interface

---

## 🛡 License

Licensed under the **MIT License**. You are free to use, distribute, or modify this project commercially or personally.

---

## 👨‍💻 Author

Maintained by [Priyanshu Singh](https://github.com/dev-priyanshu15) —
🚀 Full Stack Developer | Backend Engineer | Open Source Contributor

📬 Questions, suggestions, or contributions? Feel free to open an issue or pull request.
