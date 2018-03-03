DROP DATABASE IF EXISTS project2_db;
CREATE DATABASE project2_db;
SET foreign_key_checks = 0;

use project2_db;

DROP TABLE IF EXISTS department;
CREATE TABLE department(
	dept_name VARCHAR(40) NOT NULL PRIMARY KEY,
	dept_address VARCHAR(30),
	dept_city VARCHAR(40),
	dept_state VARCHAR(40),
	dept_country VARCHAR(40),
	dept_lead VARCHAR(40));
	
DROP TABLE IF EXISTS employee;
CREATE TABLE employee(
	f_name VARCHAR(20) NOT NULL,
	l_name VARCHAR(20) NOT NULL,
	emp_id INTEGER NOT NULL PRIMARY KEY,
	title VARCHAR(30) NOT NULL,
	birthdate VARCHAR(10) DEFAULT "1/1/1988",
	hire_date VARCHAR(10) DEFAULT "2/19/2011",
	address VARCHAR(40),
	city VARCHAR(20),
	addr_state VARCHAR(20),
	country VARCHAR(25),
	postal_code INTEGER,
	phone VARCHAR(20) NOT NULL,
	email VARCHAR(40),
	dept VARCHAR(35),
	FOREIGN KEY (dept) REFERENCES department(dept_name)
);

DROP TABLE IF EXISTS customer;
CREATE TABLE customer(
	customer_id INTEGER NOT NULL PRIMARY KEY,
	f_name VARCHAR(20),
	l_name VARCHAR(20),
	address VARCHAR(40),
	city VARCHAR(20),
	addr_state VARCHAR(20),
	country VARCHAR(25),
	postal_code INTEGER,
	phone VARCHAR(20),
	email VARCHAR(40),
	support_rep INTEGER,
	FOREIGN KEY (support_rep) REFERENCES employee(emp_id)
	ON DELETE CASCADE
	ON UPDATE CASCADE
);

LOAD DATA LOCAL INFILE 'C:\\Program Files\\MySQL\\MySQL Server 5.7\\bin\\data\\project2\\departments.csv' INTO TABLE department
FIELDS TERMINATED BY ',' 
ENCLOSED BY '"' 
LINES TERMINATED BY '\r\n'
IGNORE 1 LINES
(dept_name, dept_address, dept_city, dept_state, dept_country, dept_lead);


LOAD DATA LOCAL INFILE 'C:\\Program Files\\MySQL\\MySQL Server 5.7\\bin\\data\\project2\\employees.csv' INTO TABLE employee
FIELDS TERMINATED BY ',' 
ENCLOSED BY '"' 
LINES TERMINATED BY '\r\n'
IGNORE 1 LINES
(f_name, l_name, emp_id, title, @d1, @d2, address, city, addr_state, country, postal_code,
phone, email, dept)
SET birthdate = STR_TO_DATE(@d1, '%m/%d/%Y'),
hire_date = STR_TO_DATE(@d2, '%m/%d/%Y');


LOAD DATA LOCAL INFILE 'C:\\Program Files\\MySQL\\MySQL Server 5.7\\bin\\data\\project2\\customer.csv' INTO TABLE customer
FIELDS TERMINATED BY ',' 
ENCLOSED BY '"' 
LINES TERMINATED BY '\r\n'
IGNORE 1 LINES
(customer_id, f_name, l_name, address, city, addr_state, country, postal_code, phone, email, support_rep);

