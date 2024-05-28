# Spring Batch Practice Project

## Overview
This project is a practice implementation of batch processing using Spring Batch. The primary goal is to understand the basics of Spring Batch and to implement various batch jobs for hands-on experience.

## Project Setup

### Tech Stack
- **Spring Boot**: 2.7.18
- **Spring Batch**
- **Spring Data JPA**
- **MySQL**: Database
- **Lombok**: For code simplification

### Configuration Files
Add database and Spring Batch configurations in `application.yml` or `application.properties`.

## Database Setup
Create the `spring_batch` database and necessary tables with the following SQL script:

```sql
CREATE DATABASE spring_batch;
USE spring_batch;

CREATE TABLE departments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE employees (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    department_id INT,
    salary DECIMAL(10, 2),
    hire_date DATE,
    FOREIGN KEY (department_id) REFERENCES departments(id)
);

INSERT INTO departments (name) VALUES ('HR');
INSERT INTO departments (name) VALUES ('Engineering');
INSERT INTO departments (name) VALUES ('Sales');

INSERT INTO employees (name, department_id, salary, hire_date) VALUES ('John Doe', 1, 50000, '2022-01-15');
INSERT INTO employees (name, department_id, salary, hire_date) VALUES ('Jane Smith', 2, 75000, '2021-07-23');
INSERT INTO employees (name, department_id, salary, hire_date) VALUES ('Emily Johnson', 3, 60000, '2020-03-10');