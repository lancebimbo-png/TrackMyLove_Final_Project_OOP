# TrackMyLove (TML)
### Family Medication Tracking Information System

TrackMyLove is a Java Swing desktop application built for Filipino families where a caregiver at home needs to track and record daily medication for an elderly or sick relative, while a family member abroad — like an OFW parent — can monitor everything in real time through a shared database.

---

## The Problem

Many OFW families have elderly relatives who need daily medication at home. The family member abroad has no way to verify if medicine was given, what time it was taken, or if there were side effects. Text messages and calls leave no permanent record. TrackMyLove solves this by giving the whole family one shared database where every medication session is logged permanently.

---

## Features

- Login authentication per household
- Family member management with roles — patient, caregiver, viewer
- Medication master list with dosage, unit, and purpose
- Daily medication logging — who gave medicine, to whom, and when
- Per-log medication detail recording — time, taken or skipped, side effects
- Search across members, medications, and logs
- Real-time shared access for all family members on the same server

---

## How the Family Sharing Works

All family members connect to the same MySQL server. When the caregiver in the Philippines logs a medication session, the family member abroad opens TML on their device, connects to the same server, and sees the exact same data. No sync needed. One database, one family truth.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java JDK 8+ |
| GUI | Java Swing — NetBeans GUI Builder |
| Database | MySQL |
| DB Access | JDBC — PreparedStatement |
| IDE | Apache NetBeans |
| Version Control | GitHub |

---

## Database Structure

| Table | Type | Description |
|---|---|---|
| households | Master | One family group |
| members | Master | All family members and their roles |
| medications | Master | Master list of all medicines |
| medication_logs | Transaction Header | One medication session |
| log_details | Transaction Detail | Each medicine in one session |

```
households
├── id (PK)
├── family_name
├── address
└── created_at
       │
       │ 1
       │
       ▼ many
members
├── id (PK)
├── household_id (FK → households.id)
├── full_name
├── role (patient / caregiver / viewer)
├── location
├── password_hash
└── created_at
       │                    │
       │ 1 (as patient)     │ 1 (as given_by)
       │                    │
       ▼ many               ▼ many
medication_logs ◄───────────┘
├── id (PK)
├── patient_id (FK → members.id)
├── given_by_id (FK → members.id)
├── log_date
├── notes
└── logged_at
       │
       │ 1
       │
       ▼ many
log_details
├── id (PK)
├── log_id (FK → medication_logs.id)
├── medication_id (FK → medications.id)
├── time_given
├── was_taken (0 or 1)
└── side_effects
       ▲
       │ many
       │
       │ 1
medications
├── id (PK)
├── name
├── dosage
├── unit (mg / ml / tablet ...)
├── purpose
└── notes
```

### Entity-Relationship Diagram
<img width="946" height="657" alt="eer_tml" src="https://github.com/user-attachments/assets/9c5f3f9e-b6b4-4120-b4f9-07ff26f2ff59" />
---

## Project Structure

```
TrackMyLove/
├── src/
│   ├── trackmylove/          — GUI frames (JFrames)
│   │   ├── LoginFrame.java
│   │   ├── DashboardFrame.java
│   │   ├── MembersFrame.java
│   │   ├── MedicationsFrame.java
│   │   ├── LogFrame.java
│   │   └── LogDetailsFrame.java
│   ├── tml_model/            — Entity/model classes
│   │   ├── BaseEntity.java
│   │   ├── Household.java
│   │   ├── Member.java
│   │   ├── Medication.java
│   │   ├── MedicationLog.java
│   │   └── LogDetail.java
│   ├── tml_dao/              — CRUD and query logic
│   │   ├── MemberDAO.java
│   │   ├── MedicationDAO.java
│   │   ├── MedicationLogDAO.java
│   │   ├── LogDetailDAO.java
│   │   └── InputValidator.java
│   └── tml_db/               — Database connection
│       └── DatabaseConnection.java
└── database/
    └── tml_schema.sql        — Full SQL script
```

---

## OOP Principles Applied

**Inheritance** — `BaseEntity` is an abstract class extended by all five model classes. It holds the shared `id` field so no class has to repeat it.

**Encapsulation** — Every field in every model class is `private` with public getters and setters.

**Abstraction** — `BaseEntity` cannot be instantiated directly. It forces every child class to implement `toString()` through an abstract method.

**Constructors** — Every model class has both a no-argument constructor and a parameterized constructor.

**Modular Design** — Code is split into four packages, each with one responsibility: model, db, dao, and gui.

---

## How to Run

### Requirements
- Java JDK 8 or higher
- MySQL Server
- Apache NetBeans IDE
- MySQL Connector/J JAR (JDBC driver)

### Step 1 — Set up the database
1. Open MySQL Workbench or HeidiSQL
2. Run the file `database/tml_schema.sql`
3. This creates the `tml_db` database with all tables and sample data

### Step 2 — Configure the connection
Open `tml_db/DatabaseConnection.java` and update:
```java
private static final String URL = "jdbc:mysql://localhost:3306/tml_db";
private static final String USER = "root";
private static final String PASSWORD = "your_password_here";
```

### Step 3 — Add JDBC driver
1. Right-click the project in NetBeans → Properties → Libraries
2. Click Add JAR/Folder
3. Select your `mysql-connector-j-x.x.x.jar` file

### Step 4 — Run the project
Press `F6` in NetBeans. The login screen opens first.

### Sample Login Credentials

| Field | Value |
|---|---|
| Household ID | 1 |
| Full Name | Lance Bimbo |
| Password | lance123 |

> 💡 **Security Note:** While users authenticate using plain-text strings in the GUI, passwords are fully hashed using SHA-256 via MySQL's `SHA2()` function before being stored in the database. This corresponds to the `password_hash` column in the `members` table. Plain-text passwords never touch the database layer.

---

## Screenshots

| Screen | Preview |
|---|---|
| Login | <img width="1919" height="1079" alt="Screenshot 2026-05-26 051728" src="https://github.com/user-attachments/assets/d89ff06a-787c-447a-839f-d0de8248f2d0" />
| Dashboard | <img width="1919" height="1077" alt="Screenshot 2026-05-26 051950" src="https://github.com/user-attachments/assets/e74dcd5e-a33b-45d2-8a2b-f7fa725e8584" />
| Members | <img width="1906" height="1068" alt="Screenshot 2026-05-26 052002" src="https://github.com/user-attachments/assets/d21cedbf-98da-44b5-baf0-192715d50f51" />
| Medications | <img width="1919" height="1069" alt="Screenshot 2026-05-26 052013" src="https://github.com/user-attachments/assets/4448f28f-4fa4-4fa2-a4b9-47ff98a086bb" />
| Give Meds | <img width="1919" height="1078" alt="Screenshot 2026-05-26 052024" src="https://github.com/user-attachments/assets/53212f78-f594-47c0-a323-5ba1d79305b9" />
| Log Details | <img width="1919" height="1079" alt="image" src="https://github.com/user-attachments/assets/4294ac10-81ab-4cd1-9cc8-e57169a4cd7d" />


---

## Demo Video

[https://docs.google.com/videos/d/1adHDHjRQB75ElS60rVY9QQeYB8vhWqVcnG8Jkw3KhrI/edit?usp=sharing](https://drive.google.com/file/d/1vvz-3nLO79dT6eT2K_2Jqtwl_2QP--SF/view?usp=sharing)
---

## 🚀 Future Roadmap

- **Medical Report Export:** Implement an "Export to CSV" feature allowing caregivers to download medication history logs to show physicians during check-ups.
- **Low-Stock Inventory Tracking:** Automatically decrement medication quantities upon successful logs and flag low-stock items on the Dashboard.
- **REST API Transition:** Migrate from a direct JDBC server connection to a secure Spring Boot or Node.js API layer to protect database credentials in production environments.

---

## Author

**Lance Bimbo**
Final Project — Object-Oriented Programming
TrackMyLove — Built for Filipino OFW families

---
