-- Run this once in MySQL Workbench / MySQL CLI.
CREATE
DATABASE IF NOT EXISTS bankease
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE
bankease;

-- Spring Boot + Hibernate creates/updates the application tables automatically
-- because spring.jpa.hibernate.ddl-auto=update.
-- Do not manually create the tables unless you intentionally want to manage the schema yourself.
