-- ============================================================
--  TrackMyLove (TML) — Database Schema
--  Course: Object-Oriented Programming
--  Description: Family medication tracker with household sharing
-- ============================================================

CREATE DATABASE IF NOT EXISTS tml_db;
USE tml_db;

-- ============================================================
--  TABLE 1: households
--  The family group. Everyone in one household shares the
--  same medication records — this is how OFW mom abroad
--  sees the same data as the caregiver in the Philippines.
-- ============================================================
CREATE TABLE IF NOT EXISTS households (
    id            INT           NOT NULL AUTO_INCREMENT,
    family_name   VARCHAR(100)  NOT NULL,
    address       VARCHAR(255)  NOT NULL,
    created_at    DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_households PRIMARY KEY (id)
);

-- ============================================================
--  TABLE 2: members
--  Every person in the family. Roles:
--    'patient'   — the one receiving medicine (e.g. Grandma)
--    'caregiver' — the one giving medicine (e.g. you, PH)
--    'viewer'    — read-only watcher (e.g. Mom, OFW abroad)
-- ============================================================
CREATE TABLE IF NOT EXISTS members (
    id             INT           NOT NULL AUTO_INCREMENT,
    household_id   INT           NOT NULL,
    full_name      VARCHAR(100)  NOT NULL,
    role           ENUM('patient','caregiver','viewer') NOT NULL DEFAULT 'caregiver',
    location       VARCHAR(100)  NOT NULL DEFAULT 'Philippines',
    password_hash  VARCHAR(255)  NOT NULL,
    created_at     DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_members      PRIMARY KEY (id),
    CONSTRAINT fk_member_house FOREIGN KEY (household_id)
        REFERENCES households(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

-- ============================================================
--  TABLE 3: medications
--  Master list of all medicines in the household.
--  Cannot be deleted if already used in a log_detail.
-- ============================================================
CREATE TABLE IF NOT EXISTS medications (
    id       INT           NOT NULL AUTO_INCREMENT,
    name     VARCHAR(100)  NOT NULL,
    dosage   DECIMAL(8,2)  NOT NULL,
    unit     ENUM('mg','mcg','ml','g','tablet','capsule','drop','IU')
                           NOT NULL DEFAULT 'mg',
    purpose  VARCHAR(255)  NOT NULL,
    notes    TEXT,

    CONSTRAINT pk_medications PRIMARY KEY (id)
);

-- ============================================================
--  TABLE 4: medication_logs  (Transaction Header)
--  One row = one medication session.
--  Who gave the meds (given_by_id) to whom (patient_id)
--  on what date. Mom abroad sees this table in real time.
-- ============================================================
CREATE TABLE IF NOT EXISTS medication_logs (
    id           INT     NOT NULL AUTO_INCREMENT,
    patient_id   INT     NOT NULL,
    given_by_id  INT     NOT NULL,
    log_date     DATE    NOT NULL,
    notes        TEXT,
    logged_at    DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_logs          PRIMARY KEY (id),
    CONSTRAINT fk_log_patient   FOREIGN KEY (patient_id)
        REFERENCES members(id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,
    CONSTRAINT fk_log_givenby   FOREIGN KEY (given_by_id)
        REFERENCES members(id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
);

-- ============================================================
--  TABLE 5: log_details  (Transaction Detail)
--  One row = one medicine within one log session.
--  Multiple rows can belong to one medication_log.
-- ============================================================
CREATE TABLE IF NOT EXISTS log_details (
    id             INT          NOT NULL AUTO_INCREMENT,
    log_id         INT          NOT NULL,
    medication_id  INT          NOT NULL,
    time_given     TIME         NOT NULL,
    was_taken      TINYINT(1)   NOT NULL DEFAULT 1,
    side_effects   VARCHAR(255) DEFAULT NULL,

    CONSTRAINT pk_log_details   PRIMARY KEY (id),
    CONSTRAINT fk_detail_log    FOREIGN KEY (log_id)
        REFERENCES medication_logs(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT fk_detail_med    FOREIGN KEY (medication_id)
        REFERENCES medications(id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
);

-- ============================================================
--  SAMPLE DATA — Santos Family
--  household → members → medications → logs → details
-- ============================================================

-- 1. One household
INSERT INTO households (family_name, address) VALUES
('Santos Family', 'Magugpo Poblacion, Davao Region, Philippines');

-- 2. Three family members
--    Passwords stored as plain text here for testing only.
--    In Java: hash with SHA-256 or BCrypt before saving.
INSERT INTO members (household_id, full_name, role, location, password_hash) VALUES
(1, 'Lola Maria Santos',  'patient',   'Philippines',  'not_a_real_user'),
(1, 'Juan Santos',        'caregiver', 'Philippines',  SHA2('juan1234', 256)),
(1, 'Ate Cora Santos',    'viewer',    'Dubai, UAE',   SHA2('cora1234', 256));

-- 3. Three medications
INSERT INTO medications (name, dosage, unit, purpose, notes) VALUES
('Metformin',    500,  'mg',     'Type 2 Diabetes',    'Take after meals. Morning and evening.'),
('Losartan',     50,   'mg',     'Hypertension',       'Take once daily in the morning.'),
('Amlodipine',   5,    'mg',     'Hypertension',       'Can cause ankle swelling. Monitor.');

-- 4. Two medication log headers
--    Log 1: Juan gave meds to Lola on June 1
--    Log 2: Juan gave meds to Lola on June 2
INSERT INTO medication_logs (patient_id, given_by_id, log_date, notes) VALUES
(1, 2, '2025-06-01', 'Morning dose. Lola ate lugaw before taking.'),
(1, 2, '2025-06-02', 'Evening dose. Lola complained of mild dizziness.');

-- 5. Log details — what meds were in each log
--    Log 1 (id=1): all 3 meds, all taken
INSERT INTO log_details (log_id, medication_id, time_given, was_taken, side_effects) VALUES
(1, 1, '08:00:00', 1, NULL),
(1, 2, '08:05:00', 1, NULL),
(1, 3, '08:05:00', 1, NULL);

--    Log 2 (id=2): Amlodipine was skipped, dizziness from Losartan
INSERT INTO log_details (log_id, medication_id, time_given, was_taken, side_effects) VALUES
(2, 1, '19:00:00', 1, NULL),
(2, 2, '19:00:00', 1, 'Mild dizziness after 30 mins'),
(2, 3, '19:00:00', 0, 'Skipped — lola refused');

-- ============================================================
--  VERIFY: Run these SELECT statements to check everything
-- ============================================================

-- Check 1: See all members of the household
SELECT m.id, m.full_name, m.role, m.location, h.family_name
FROM members m
JOIN households h ON m.household_id = h.id;

-- Check 2: The family activity feed (what Mom abroad sees)
SELECT
    ml.id        AS log_id,
    ml.log_date,
    p.full_name  AS patient,
    g.full_name  AS given_by,
    ml.notes
FROM medication_logs ml
JOIN members p ON ml.patient_id  = p.id
JOIN members g ON ml.given_by_id = g.id
ORDER BY ml.log_date DESC;

-- Check 3: Full detail view — every medicine in every log
SELECT
    ml.log_date,
    p.full_name  AS patient,
    g.full_name  AS given_by,
    med.name     AS medicine,
    med.dosage,
    med.unit,
    ld.time_given,
    CASE WHEN ld.was_taken = 1 THEN 'Taken' ELSE 'Skipped' END AS status,
    ld.side_effects
FROM log_details ld
JOIN medication_logs ml ON ld.log_id         = ml.id
JOIN members p          ON ml.patient_id     = p.id
JOIN members g          ON ml.given_by_id    = g.id
JOIN medications med    ON ld.medication_id  = med.id
ORDER BY ml.log_date DESC, ld.time_given;

