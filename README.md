# TrackMyLove (TML)
### Family Medication Tracking Information System

TrackMyLove is a Java Swing desktop application built for Filipino families where a caregiver at home needs to track and record daily medication for an elderly or sick relative, while a family member abroad — like an OFW parent — can monitor everything in real time through a shared database.


## The Problem

Many OFW families have elderly relatives who need daily medication at home. The family member abroad has no way to verify if medicine was given, what time it was taken, or if there were side effects. Text messages and calls leave no permanent record. TrackMyLove solves this by giving the whole family one shared database where every medication session is logged permanently.


## Features

- Login authentication per household
- Family member management with roles — patient, caregiver, viewer
- Medication master list with dosage, unit, and purpose
- Daily medication logging — who gave medicine, to whom, and when
- Per-log medication detail recording — time, taken or skipped, side effects
- Search across members, medications, and logs
- Real-time shared access for all family members on the same server


## How the Family Sharing Works

All family members connect to the same MySQL server. When the caregiver in the Philippines logs a medication session, the family member abroad opens TML on their device, connects to the same server, and sees the exact same data. No sync needed. One database, one family truth.


## Tech Stack

| Layer | Technology |
|---|---|
| **Language** | Java (JDK 8+) |
| **GUI** | Java Swing — NetBeans GUI Builder |
| **Database** | MySQL |
| **DB Access** | JDBC — PreparedStatement |
| **IDE** | Apache NetBeans |
| **Version Control** | GitHub |

## Database Structure

| Table | Type | Description |
|---|---|---|
| `households` | Master | One family group |
| `members` | Master | All family members and their roles |
| `medications` | Master | Master list of all medicines |
| `medication_logs` | Transaction Header | One medication session |
| `log_details` | Transaction Detail | Each medicine in one session |

<img width="1919" height="1077" alt="image" src="https://github.com/user-attachments/assets/abae5907-b930-492c-a8f9-ae0bbd825ee8" />

## Project Structure

```
TrackMyLove/
├── src/
│   ├── trackmylove/          — GUI frames (JFrames)
│   │   ├── Loginframe.java
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

### Sample login credentials
| Field | Value |
|---|---|
| Household ID | 1 |
| Full Name | Lance Bimbo |
| Password | lance123 |


## Screenshots

<img width="1919" height="1079" alt="image" src="https://github.com/user-attachments/assets/b612edc5-ba32-4049-8c10-ba5d98c91f5c" />
<img width="1919" height="1077" alt="image" src="https://github.com/user-attachments/assets/ac845238-bf4c-478c-8053-e3fa696c5488" />
<img width="1906" height="1068" alt="image" src="https://github.com/user-attachments/assets/d61e8db8-4fd5-4cf1-bd24-96437a21375b" />
<img width="1919" height="1069" alt="image" src="https://github.com/user-attachments/assets/c415c726-4210-4c2e-befa-b31fe03dc1e8" />
<img width="1919" height="1078" alt="image" src="https://github.com/user-attachments/assets/133e4348-8918-4040-924e-09ea49ea03a6" />



| Screen | Description |
|---|---|
| Login | Household ID, name, and password authentication |
| Dashboard | Recent logs table and family overview |
| Members | Add, edit, delete, and search family members |
| Medications | Manage the medicine master list |
| Give Meds | Create a new medication log session |
| Log Details | Add individual medicines to a log session |


## Sample Data Included

The SQL script includes one household — the Bimbo Family — with three members:

- Lola Lydia Bimbo — patient
- Lance Bimbo — caregiver
- Liza Bimbo - viewer

And three medications — Metformin, Losartan, Amlodipine — with two sample log sessions.


## Author

**Lance Xyzherlee A. Bimbo**
Final Project — Object-Oriented Programming
TrackMyLove — Built for Filipino OFW families


*Every log entry in TrackMyLove is a record of care. The name says it all — Track My Love.*
