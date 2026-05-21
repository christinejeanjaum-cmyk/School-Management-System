-- =============================================================
--  KNHS Student Information & Grading System
--  Database: knhs_db
--  Compatible with: auth/Login.java
--  Charset: utf8mb4 / utf8mb4_unicode_ci
-- =============================================================

CREATE DATABASE IF NOT EXISTS knhs_db
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_unicode_ci;

USE knhs_db;

-- -------------------------------------------------------------
--  Safety: disable FK checks before dropping tables
-- -------------------------------------------------------------
SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS shs_grades;
DROP TABLE IF EXISTS jhs_grades;
DROP TABLE IF EXISTS shs_enrollment_subjects;
DROP TABLE IF EXISTS jhs_enrollment_subjects;
DROP TABLE IF EXISTS shs_enrollment;
DROP TABLE IF EXISTS jhs_enrollment;
DROP TABLE IF EXISTS shs_subjects_offerings;
DROP TABLE IF EXISTS jhs_subjects_offerings;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS shs_students;
DROP TABLE IF EXISTS jhs_students;
DROP TABLE IF EXISTS shs_subjects;
DROP TABLE IF EXISTS jhs_subjects;
DROP TABLE IF EXISTS shs_sections;
DROP TABLE IF EXISTS jhs_sections;
DROP TABLE IF EXISTS teachers;
DROP TABLE IF EXISTS cor_settings;
DROP TABLE IF EXISTS school_years;

SET FOREIGN_KEY_CHECKS = 1;

-- =============================================================
--  LOOKUP / SETTINGS TABLES
-- =============================================================

CREATE TABLE school_years (
  sy_id         INT           NOT NULL AUTO_INCREMENT,
  school_year   VARCHAR(20)   NOT NULL,
  semester      VARCHAR(20)   NOT NULL,
  is_active     TINYINT(1)    NOT NULL DEFAULT 1,
  PRIMARY KEY (sy_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE cor_settings (
  cor_id         INT           NOT NULL AUTO_INCREMENT,
  school_name    VARCHAR(150)  NOT NULL DEFAULT 'KATIPUNAN NATIONAL HIGH SCHOOL',
  school_address VARCHAR(150)  NOT NULL DEFAULT 'KATIPUNAN, CARMEN',
  contact_no     VARCHAR(50)   NOT NULL DEFAULT '0917-000-0001',
  email          VARCHAR(150)  NOT NULL DEFAULT 'info@knhs.edu.ph',
  school_head    VARCHAR(100)  NOT NULL DEFAULT 'HEAD TEACHER',
  principal_name VARCHAR(150)  NOT NULL DEFAULT 'MARIA S. SANTOS, EdD',
  issued_date    DATE          NOT NULL,
  PRIMARY KEY (cor_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =============================================================
--  TEACHERS
-- =============================================================

CREATE TABLE teachers (
  teacher_id    INT          NOT NULL AUTO_INCREMENT,
  name          VARCHAR(150) NOT NULL,
  subject_taught VARCHAR(150) NOT NULL,
  contact_no    VARCHAR(50)  NOT NULL,
  email         VARCHAR(150),
  level         VARCHAR(20)  NOT NULL,
  PRIMARY KEY (teacher_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =============================================================
--  SECTIONS
-- =============================================================

CREATE TABLE jhs_sections (
  section_id       INT          NOT NULL AUTO_INCREMENT,
  section_name     VARCHAR(100) NOT NULL,
  grade_level      INT          NOT NULL,
  adviser_name     VARCHAR(150),
  room_number      VARCHAR(50),
  sec_room_number  VARCHAR(50) NOT NULL DEFAULT '',
  PRIMARY KEY (section_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE shs_sections (
  section_id       INT          NOT NULL AUTO_INCREMENT,
  section_name     VARCHAR(100) NOT NULL,
  grade_level      INT          NOT NULL,
  track            VARCHAR(100) NOT NULL,
  strand           VARCHAR(100) NOT NULL,
  adviser_name     VARCHAR(150),
  room_number      VARCHAR(50),
  sec_room_number  VARCHAR(50) NOT NULL DEFAULT '',
  PRIMARY KEY (section_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =============================================================
--  SUBJECTS
-- =============================================================

CREATE TABLE jhs_subjects (
  subj_id     INT          NOT NULL AUTO_INCREMENT,
  subj_code   VARCHAR(50)  NOT NULL,
  subj_name   VARCHAR(150) NOT NULL,
  grade_level INT          NOT NULL,
  description TEXT,
  PRIMARY KEY (subj_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE shs_subjects (
  subj_id   INT          NOT NULL AUTO_INCREMENT,
  subj_code VARCHAR(50)  NOT NULL,
  subj_name VARCHAR(150) NOT NULL,
  track     VARCHAR(100) NOT NULL,
  strand    VARCHAR(100) NOT NULL,
  semester  VARCHAR(20)  NOT NULL,
  units     INT          NOT NULL DEFAULT 3,
  PRIMARY KEY (subj_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =============================================================
--  STUDENTS
-- =============================================================

CREATE TABLE jhs_students (
  student_id      INT          NOT NULL AUTO_INCREMENT,
  student_no      VARCHAR(50)  NOT NULL UNIQUE,
  LRN             VARCHAR(12)  NOT NULL UNIQUE,
  last_name       VARCHAR(100) NOT NULL,
  first_name      VARCHAR(100) NOT NULL,
  middle_name     VARCHAR(100) NOT NULL,
  gender          VARCHAR(20)  NOT NULL,
  birthdate       DATE         NOT NULL,
  address         VARCHAR(255) NOT NULL,
  contact_no      VARCHAR(50)  NOT NULL,
  guardian_name   VARCHAR(150) NOT NULL,
  guardian_contact VARCHAR(50) NOT NULL,
  grade_level     INT          NOT NULL,
  section_id      INT          NOT NULL,
  date_enrolled   DATE         NOT NULL,
  status          VARCHAR(50)  NOT NULL,
  PRIMARY KEY (student_id),
  CONSTRAINT fk_jhs_students_section
    FOREIGN KEY (section_id) REFERENCES jhs_sections (section_id)
    ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE shs_students (
  student_id      INT          NOT NULL AUTO_INCREMENT,
  student_no      VARCHAR(50)  NOT NULL UNIQUE,
  LRN             VARCHAR(30)  NOT NULL UNIQUE,
  last_name       VARCHAR(100) NOT NULL,
  first_name      VARCHAR(100) NOT NULL,
  middle_name     VARCHAR(100) NOT NULL,
  gender          VARCHAR(20)  NOT NULL,
  birthdate       DATE         NOT NULL,
  address         VARCHAR(255) NOT NULL,
  contact_no      VARCHAR(50)  NOT NULL,
  guardian_name   VARCHAR(150) NOT NULL,
  guardian_contact VARCHAR(50) NOT NULL,
  grade_level     INT          NOT NULL,
  track           VARCHAR(100) NOT NULL,
  strand          VARCHAR(100) NOT NULL,
  section_id      INT          NOT NULL,
  date_enrolled   DATE         NOT NULL,
  status          VARCHAR(50)  NOT NULL,
  PRIMARY KEY (student_id),
  CONSTRAINT fk_shs_students_section
    FOREIGN KEY (section_id) REFERENCES shs_sections (section_id)
    ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =============================================================
--  SUBJECT OFFERINGS
-- =============================================================

CREATE TABLE jhs_subjects_offerings (
  offering_id INT         NOT NULL AUTO_INCREMENT,
  subj_id     INT         NOT NULL,
  section_id  INT         NOT NULL,
  teacher_id  INT         NOT NULL,
  school_year VARCHAR(20) NOT NULL,
  semester    VARCHAR(20) NOT NULL,
  schedule    VARCHAR(100) NOT NULL DEFAULT '',
  room        VARCHAR(50) NOT NULL DEFAULT '',
  PRIMARY KEY (offering_id),
  CONSTRAINT fk_jhs_offerings_subject
    FOREIGN KEY (subj_id)    REFERENCES jhs_subjects (subj_id)
    ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT fk_jhs_offerings_section
    FOREIGN KEY (section_id) REFERENCES jhs_sections (section_id)
    ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT fk_jhs_offerings_teacher
    FOREIGN KEY (teacher_id) REFERENCES teachers (teacher_id)
    ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE shs_subjects_offerings (
  offering_id INT         NOT NULL AUTO_INCREMENT,
  subj_id     INT         NOT NULL,
  section_id  INT         NOT NULL,
  teacher_id  INT         NOT NULL,
  school_year VARCHAR(20) NOT NULL,
  semester    VARCHAR(20) NOT NULL,
  schedule    VARCHAR(100) NOT NULL DEFAULT '',
  room        VARCHAR(50) NOT NULL DEFAULT '',
  PRIMARY KEY (offering_id),
  CONSTRAINT fk_shs_offerings_subject
    FOREIGN KEY (subj_id)    REFERENCES shs_subjects (subj_id)
    ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT fk_shs_offerings_section
    FOREIGN KEY (section_id) REFERENCES shs_sections (section_id)
    ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT fk_shs_offerings_teacher
    FOREIGN KEY (teacher_id) REFERENCES teachers (teacher_id)
    ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =============================================================
--  ENROLLMENT
-- =============================================================

CREATE TABLE jhs_enrollment (
  enroll_id   INT         NOT NULL AUTO_INCREMENT,
  student_no  VARCHAR(50) NOT NULL,
  LRN         VARCHAR(12) NOT NULL,
  school_year VARCHAR(20) NOT NULL,
  semester    VARCHAR(20) NOT NULL,
  date_enrolled DATE      NOT NULL,
  status      VARCHAR(50) NOT NULL DEFAULT 'PENDING',
  student_id  INT         NOT NULL,
  sy_id       INT         NOT NULL,
  PRIMARY KEY (enroll_id),
  CONSTRAINT fk_jhs_enrollment_student
    FOREIGN KEY (student_id) REFERENCES jhs_students (student_id)
    ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT fk_jhs_enrollment_sy
    FOREIGN KEY (sy_id) REFERENCES school_years (sy_id)
    ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE shs_enrollment (
  enroll_id   INT          NOT NULL AUTO_INCREMENT,
  student_no  VARCHAR(50)  NOT NULL,
  LRN         VARCHAR(12)  NOT NULL,
  school_year VARCHAR(20)  NOT NULL,
  semester    VARCHAR(20)  NOT NULL,
  date_enrolled DATE       NOT NULL,
  status      VARCHAR(50)  NOT NULL DEFAULT 'PENDING',
  student_id  INT          NOT NULL,
  sy_id       INT          NOT NULL,
  track       VARCHAR(100) NOT NULL,
  strand      VARCHAR(100) NOT NULL,
  PRIMARY KEY (enroll_id),
  CONSTRAINT fk_shs_enrollment_student
    FOREIGN KEY (student_id) REFERENCES shs_students (student_id)
    ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT fk_shs_enrollment_sy
    FOREIGN KEY (sy_id) REFERENCES school_years (sy_id)
    ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =============================================================
--  ENROLLMENT SUBJECTS
-- =============================================================

CREATE TABLE jhs_enrollment_subjects (
  enroll_subject_id INT            NOT NULL AUTO_INCREMENT,
  enroll_id         INT            NOT NULL,
  subj_id           INT            NOT NULL,
  teacher_id        INT            NOT NULL,
  units             DECIMAL(4,1)   NOT NULL DEFAULT 1.0,
  schedule          VARCHAR(100)   NOT NULL,
  room              VARCHAR(50)    NOT NULL,
  PRIMARY KEY (enroll_subject_id),
  CONSTRAINT fk_jhs_enroll_subject_enrollment
    FOREIGN KEY (enroll_id)  REFERENCES jhs_enrollment (enroll_id)
    ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT fk_jhs_enroll_subject_subject
    FOREIGN KEY (subj_id)    REFERENCES jhs_subjects (subj_id)
    ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT fk_jhs_enroll_subject_teacher
    FOREIGN KEY (teacher_id) REFERENCES teachers (teacher_id)
    ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE shs_enrollment_subjects (
  enroll_subject_id INT            NOT NULL AUTO_INCREMENT,
  enroll_id         INT            NOT NULL,
  subj_id           INT            NOT NULL,
  teacher_id        INT            NOT NULL,
  units             DECIMAL(4,1)   NOT NULL DEFAULT 3.0,
  schedule          VARCHAR(100)   NOT NULL,
  room              VARCHAR(50)    NOT NULL,
  PRIMARY KEY (enroll_subject_id),
  CONSTRAINT fk_shs_enroll_subject_enrollment
    FOREIGN KEY (enroll_id)  REFERENCES shs_enrollment (enroll_id)
    ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT fk_shs_enroll_subject_subject
    FOREIGN KEY (subj_id)    REFERENCES shs_subjects (subj_id)
    ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT fk_shs_enroll_subject_teacher
    FOREIGN KEY (teacher_id) REFERENCES teachers (teacher_id)
    ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =============================================================
--  GRADES
-- =============================================================

CREATE TABLE jhs_grades (
  grade_id       INT          NOT NULL AUTO_INCREMENT,
  student_id     INT          NOT NULL,
  subj_id        INT          NOT NULL,
  first_grading  DECIMAL(5,2),
  second_grading DECIMAL(5,2),
  third_grading  DECIMAL(5,2),
  fourth_grading DECIMAL(5,2),
  quizzes_score DECIMAL(5,2),
  projects_score DECIMAL(5,2),
  written_exam_score DECIMAL(5,2),
  performance_task_score DECIMAL(5,2),
  final_exam_score DECIMAL(5,2),
  grading_period VARCHAR(30) NOT NULL DEFAULT 'Final',
  quizzes_pct    DECIMAL(5,2) NOT NULL DEFAULT 30.00,
  projects_pct   DECIMAL(5,2) NOT NULL DEFAULT 20.00,
  exams_pct      DECIMAL(5,2) NOT NULL DEFAULT 20.00,
  average        DECIMAL(5,2),
  remarks        VARCHAR(30),
  PRIMARY KEY (grade_id),
  CONSTRAINT fk_jhs_grades_student
    FOREIGN KEY (student_id) REFERENCES jhs_students (student_id)
    ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT fk_jhs_grades_subject
    FOREIGN KEY (subj_id)    REFERENCES jhs_subjects (subj_id)
    ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE shs_grades (
  grade_id    INT          NOT NULL AUTO_INCREMENT,
  student_id  INT          NOT NULL,
  offering_id INT          NOT NULL,
  prelim      DECIMAL(5,2),
  midterm     DECIMAL(5,2),
  finals      DECIMAL(5,2),
  quizzes_score DECIMAL(5,2),
  projects_score DECIMAL(5,2),
  written_exam_score DECIMAL(5,2),
  performance_task_score DECIMAL(5,2),
  final_exam_score DECIMAL(5,2),
  grading_period VARCHAR(30) NOT NULL DEFAULT 'Final',
  quizzes_pct    DECIMAL(5,2) NOT NULL DEFAULT 30.00,
  projects_pct   DECIMAL(5,2) NOT NULL DEFAULT 20.00,
  exams_pct      DECIMAL(5,2) NOT NULL DEFAULT 20.00,
  average     DECIMAL(5,2),
  remarks     VARCHAR(30),
  PRIMARY KEY (grade_id),
  CONSTRAINT fk_shs_grades_student
    FOREIGN KEY (student_id)  REFERENCES shs_students (student_id)
    ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT fk_shs_grades_offering
    FOREIGN KEY (offering_id) REFERENCES shs_subjects_offerings (offering_id)
    ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =============================================================
--  USERS
--  Columns must match Login.java SELECT:
--    user_id, username, password, role, teacher_id, account_status
-- =============================================================

CREATE TABLE users (
  user_id        INT          NOT NULL AUTO_INCREMENT,
  full_name      VARCHAR(150) NOT NULL,
  username       VARCHAR(50)  NOT NULL UNIQUE,
  password       VARCHAR(100) NOT NULL,
  role           VARCHAR(20)  NOT NULL,
  email          VARCHAR(150) NOT NULL,
  contact_no     VARCHAR(50),
  teacher_id     INT          NULL,
  account_status VARCHAR(20)  NOT NULL DEFAULT 'PENDING',
  admin_permission VARCHAR(20) NOT NULL DEFAULT 'AUTHORIZED',
  profile_picture LONGBLOB NULL,
  user_type      VARCHAR(20)  NULL,
  PRIMARY KEY (user_id),
  CONSTRAINT fk_users_teacher
    FOREIGN KEY (teacher_id) REFERENCES teachers (teacher_id)
    ON UPDATE CASCADE ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =============================================================
--  SEED DATA
-- =============================================================

-- School years
INSERT INTO school_years (school_year, semester, is_active) VALUES
  ('2026-2027', 'Full Year',     1),
  ('2026-2027', '1st Semester',  1),
  ('2026-2027', '2nd Semester',  0);

-- COR settings
INSERT INTO cor_settings
  (school_name, school_address, contact_no, email, school_head, principal_name, issued_date)
VALUES
  ('KATIPUNAN NATIONAL HIGH SCHOOL', 'KATIPUNAN, CARMEN',
   '0917-000-0001', 'info@knhs.edu.ph',
   'HEAD TEACHER', 'MARIA S. SANTOS, EdD', '2026-01-01');

-- Teachers
INSERT INTO teachers (name, subject_taught, contact_no, email, level) VALUES
  ('Ms. Maria Santos', 'Mathematics',           '0917-000-0001', 'maria.santos@knhs.edu.ph', 'BOTH'),
  ('Mr. Jose Reyes',   'Filipino / Earth Science','0917-000-0002','jose.reyes@knhs.edu.ph',  'BOTH'),
  ('Ms. Ana Cruz',     'Science / Filipino',    '0917-000-0003', 'ana.cruz@knhs.edu.ph',     'BOTH'),
  ('Mr. Ramon Santos', 'AP / PE',               '0917-000-0004', 'ramon.santos@knhs.edu.ph', 'BOTH');

-- JHS Sections
INSERT INTO jhs_sections (section_name, grade_level, adviser_name, room_number) VALUES
  ('Hope',      7,  'Ms. Maria Santos', 'Hope'),
  ('Integrity', 8,  'Mr. Jose Reyes',   'Integrity'),
  ('Faith',     9,  'Ms. Ana Cruz',     'Faith'),
  ('Charity',   10, 'Mr. Ramon Santos', 'Charity');

-- SHS Sections
INSERT INTO shs_sections (section_name, grade_level, track, strand, adviser_name, room_number) VALUES
  ('Excellence', 11, 'Academic', 'STEM',  'Ms. Maria Santos', 'Excellence'),
  ('Wisdom',     12, 'Academic', 'HUMSS', 'Mr. Jose Reyes',   'Wisdom'),
  ('Truth',      11, 'TVL',      'ICT',   'Ms. Ana Cruz',     'Truth');

-- JHS Subjects
INSERT INTO jhs_subjects (subj_code, subj_name, grade_level, description) VALUES
  ('MATH7', 'Mathematics 7',             7, 'Junior High School Mathematics'),
  ('FIL7',  'Filipino 7',                7, 'Junior High School Filipino'),
  ('SCI7',  'Science 7',                 7, 'Junior High School Science'),
  ('AP7',   'Araling Panlipunan 7',      7, 'Junior High School Araling Panlipunan');

-- SHS Subjects
INSERT INTO shs_subjects (subj_code, subj_name, track, strand, semester, units) VALUES
  ('GENMATH',  'General Mathematics',              'Academic', 'STEM', '1st Semester', 3),
  ('EARTHSC',  'Earth Science',                    'Academic', 'STEM', '1st Semester', 3),
  ('FIL11',    'Komunikasyon at Pananaliksik',      'Academic', 'STEM', '1st Semester', 3),
  ('PHYSED1',  'Physical Education & Health 1',    'Academic', 'STEM', '1st Semester', 2);

-- JHS Student
INSERT INTO jhs_students
  (student_id, student_no, LRN, last_name, first_name, middle_name, gender, birthdate,
   address, contact_no, guardian_name, guardian_contact,
   grade_level, section_id, date_enrolled, status)
VALUES
  (3, 'JHS-000003', '123456789012', 'Dela Cruz', 'Juan', 'Reyes', 'Male', '2011-06-15',
   'Katipunan, Carmen', '09170000002',
   'Ana Dela Cruz', '09170000003', 7, 1, '2026-01-01', 'ENROLLED');

-- SHS Student
INSERT INTO shs_students
  (student_id, student_no, LRN, last_name, first_name, middle_name, gender, birthdate,
   address, contact_no, guardian_name, guardian_contact,
   grade_level, track, strand, section_id, date_enrolled, status)
VALUES
  (4, 'SHS-000004', '123456789013', 'Santos', 'Liza', 'Garcia', 'Female', '2008-03-20',
   'Katipunan, Carmen', '09170000004',
   'Ramon Santos', '09170000005', 11, 'Academic', 'STEM', 1, '2026-01-01', 'ENROLLED');

-- JHS Subject Offerings
INSERT INTO jhs_subjects_offerings (subj_id, section_id, teacher_id, school_year, semester) VALUES
  (1, 1, 1, '2026-2027', 'Full Year'),
  (2, 1, 2, '2026-2027', 'Full Year'),
  (3, 1, 3, '2026-2027', 'Full Year'),
  (4, 1, 4, '2026-2027', 'Full Year');

-- SHS Subject Offerings
INSERT INTO shs_subjects_offerings (subj_id, section_id, teacher_id, school_year, semester) VALUES
  (1, 1, 1, '2026-2027', '1st Semester'),
  (2, 1, 2, '2026-2027', '1st Semester'),
  (3, 1, 3, '2026-2027', '1st Semester'),
  (4, 1, 4, '2026-2027', '1st Semester');

-- JHS Enrollment
INSERT INTO jhs_enrollment (student_no, LRN, school_year, semester, date_enrolled, status, student_id, sy_id) VALUES
  ('JHS-000003', '123456789012', '2026-2027', '1st Semester', '2026-01-01', 'ENROLLED', 3, 2);

-- SHS Enrollment
INSERT INTO shs_enrollment
  (student_no, LRN, school_year, semester, date_enrolled, status, student_id, sy_id, track, strand)
VALUES
  ('SHS-000004', '123456789013', '2026-2027', '1st Semester', '2026-01-01', 'ENROLLED', 4, 2, 'Academic', 'STEM');

-- JHS Enrollment Subjects
INSERT INTO jhs_enrollment_subjects (enroll_id, subj_id, teacher_id, units, schedule, room) VALUES
  (1, 1, 1, 1.0, 'M-W-F 07:00AM - 08:30AM', 'Hope'),
  (1, 2, 2, 1.0, 'M-W-F 08:45AM - 10:15AM', 'Hope'),
  (1, 3, 3, 1.0, 'T-Th-S 07:00AM - 08:30AM', 'Hope'),
  (1, 4, 4, 1.0, 'T-Th-S 08:45AM - 10:15AM', 'Hope');

-- SHS Enrollment Subjects
INSERT INTO shs_enrollment_subjects (enroll_id, subj_id, teacher_id, units, schedule, room) VALUES
  (1, 1, 1, 3.0, 'T-Th-S 09:00AM - 10:30AM', 'Excellence'),
  (1, 2, 2, 3.0, 'T-Th-S 10:45AM - 12:15PM', 'Excellence'),
  (1, 3, 3, 3.0, 'M-W-F 09:00AM - 10:30AM',  'Excellence'),
  (1, 4, 4, 2.0, 'M-W-F 10:45AM - 12:15PM',  'Excellence');

-- JHS Grades
INSERT INTO jhs_grades
  (student_id, subj_id, first_grading, second_grading, third_grading, fourth_grading, quizzes_score, projects_score, written_exam_score, performance_task_score, final_exam_score, average, remarks)
VALUES
  (1, 1, 88.00, 90.00, 89.00, 91.00, 89.00, 90.00, 88.00, 90.00, 91.00, 89.30, 'Passed'),
  (1, 2, 89.00, 90.00, 88.00, 91.00, 89.00, 90.00, 88.00, 90.00, 91.00, 89.30, 'Passed'),
  (1, 3, 87.00, 90.00, 90.00, 91.00, 87.00, 90.00, 90.00, 90.00, 91.00, 89.00, 'Passed'),
  (1, 4, 90.00, 89.00, 89.00, 90.00, 90.00, 89.00, 89.00, 90.00, 90.00, 89.60, 'Passed');

-- SHS Grades
INSERT INTO shs_grades (student_id, offering_id, prelim, midterm, finals, quizzes_score, projects_score, written_exam_score, performance_task_score, final_exam_score, average, remarks) VALUES
  (1, 1, 87.00, 90.00, 92.00, 87.00, 90.00, 90.00, 91.00, 92.00, 89.70, 'Passed'),
  (1, 2, 88.00, 89.00, 91.00, 88.00, 89.00, 89.00, 90.00, 91.00, 89.10, 'Passed'),
  (1, 3, 90.00, 90.00, 89.00, 90.00, 90.00, 90.00, 89.00, 89.00, 89.70, 'Passed'),
  (1, 4, 89.00, 88.00, 90.00, 89.00, 88.00, 88.00, 90.00, 90.00, 89.00, 'Passed');

-- =============================================================
--  USERS
--  Login.java SELECT: user_id, username, password, role,
--                     teacher_id, account_status
--  Passwords stored as plain text (Login.java also accepts SHA-256)
-- =============================================================
INSERT INTO users
  (user_id, full_name, username, password, role, email, contact_no, teacher_id, account_status, admin_permission, user_type)
VALUES
  (1, 'KNHS Administrator', 'admin',    'knhs123', 'admin',   'admin@knhs.local',       '09170000000', NULL, 'ACTIVE', 'SUPER_ADMIN', NULL),
  (2, 'Maria Santos',       'teacher1', 'knhs123', 'teacher', 'teacher1@knhs.edu.ph',   '09170000001', 1,    'ACTIVE', 'AUTHORIZED', NULL),
  (3, 'Juan Dela Cruz',     'student1', 'knhs123', 'student', 'student1@knhs.edu.ph',   '09170000002', NULL, 'ACTIVE', 'AUTHORIZED', 'JHS'),
  (4, 'Liza Santos',        'student2', 'knhs123', 'student', 'student2@knhs.edu.ph',   '09170000004', NULL, 'ACTIVE', 'AUTHORIZED', 'SHS');
