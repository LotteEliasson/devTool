CREATE DATABASE IF NOT EXISTS projectdb;

USE projectdb;

CREATE TABLE users(
user_id int NOT NULL PRIMARY KEY AUTO_INCREMENT,
user_name varchar(255),
user_password varchar(255)
);

CREATE TABLE project (
projectID int NOT NULL PRIMARY KEY AUTO_INCREMENT,
project_name varchar(255),
startdate date,
expected_enddate date,
due_date date,
project_manager varchar(255),
customer_name varchar(255)
);

CREATE TABLE project_process (
processID int NOT NULL PRIMARY KEY AUTO_INCREMENT,
projectID int,
process_name int,
expected_start_date date,
expected_finish date,
start_after_task int,
FOREIGN KEY (projectID) REFERENCES project(projectID)
);

CREATE TABLE task (
taskID int NOT NULL PRIMARY KEY AUTO_INCREMENT,
processID int,
task_name varchar(255),
effort int,
expected_startdate date,
min_allocation int,
task_status varchar(255),
assignedID varchar(255),
tasksequencenumber int,
FOREIGN KEY (processID) REFERENCES project_process(processID)
);