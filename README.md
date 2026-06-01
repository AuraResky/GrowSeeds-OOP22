# GrowSeeds

GrowSeeds is a farm management desktop application built using JavaFX.

This application helps farmers manage agricultural data with stock tracking, harvest recording, profile management, and practical farming advice.

GrowSeeds was created as a Final Project for the Object Oriented Programming course.

---

# Features

## Authentication

* Login
* Register
* Per-user farm data storage

## Farm Management

* Stock tracking for fruits, vegetables, and staple crops
* Harvest logging by field, crop, date, and weather
* Farm profile management, including contact email and farm status
* Farming advice with fertilizer recommendations, planting calendar, and pest detection

---


# Dashboard

The home dashboard shows:

* Total active fields
* Total harvest yield
* Low stock alerts
* Quick navigation to stock, harvest, farming advice, and profile sections

The dashboard provides a quick overview of the farm status and access to the main modules.

---

# Stock Management

Stock can be tracked in categories:

* Fruit
* Vegetable
* Staple Crop

Stock features:

* Add new stock
* Sell available stock
* Search commodities
* View stock status (available, running low, out of stock)
* Track quantity sold

---

# Harvest Management

Harvest features:

* Add harvest entries
* Delete selected entries
* View harvest history by field, crop, date, and weather

---

# Farming Advice

The farming advice module provides:

* Fertilizer recommendation calculator
* Planting calendar
* Pest detection database and handling suggestions

These features help farmers choose fertilizer doses and identify pest issues more quickly.

---

# UI Layout

The application uses:

* Left sidebar navigation
* Clean green-and-white theme
* Card-based panels
* Easy-to-use input forms

Sidebar menu:

* Home
* Harvest Management
* Stock Tracking
* Farming Advice
* Profile

---

# Technologies

* Java 17+
* JavaFX
* SQLite
* JDBC SQLite Driver
* Gradle

---

# Requirements

Before running this project, make sure you have installed:

## 1. Java JDK 17 or higher

Download:
https://www.oracle.com/java/technologies/downloads/

Verify installation:

```bash
java --version
```

## 2. IDE

Recommended IDEs:

* IntelliJ IDEA
* VS Code

---

# Running the Application

Open the `ProjectGrow` folder in a terminal or IDE, then run:

```bash
./gradlew run
```

For Windows:

```bash
gradlew.bat run
```

---

# Project Structure

```bash
ProjectGrow/
└── app/
    └── src/main/java/
        ├── database/
        ├── model/
        ├── projectgrow/
        ├── service/
        ├── session/
        └── view/
```

This structure separates data, business logic, user session handling, and user interface components.

---

# Database

This application uses SQLite for local storage.

The database stores:

* users
* profiles
* stock
* harvest
* pest_detection
* planting_calendar
* fertilizer_recommendations

---

# Screens

* Login
* Register
* Dashboard
* Harvest Management
* Stock Tracking
* Farming Advice
* Profile

---

# Future Improvements

Planned features:

* Export data to CSV or Excel
* Low stock reminder notifications
* More farming recommendation data
* Harvest statistics and visual trends

---

# Team

* UI & JavaFX
* Database & Backend
* Logic & Agriculture

---

# License

This project is created for educational purposes.
