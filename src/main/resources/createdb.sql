CREATE DATABASE IF NOT EXISTS projectdb;

USE projectdb;

CREATE TABLE users(
user_id int NOT NULL PRIMARY KEY AUTO_INCREMENT,
user_name varchar(255),
user_password varchar(255)
);
CREATE TABLE projectmanagers(
project_manager_id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
 user_id int NOT NULL,
 FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE project (
projectID int NOT NULL PRIMARY KEY AUTO_INCREMENT,
project_name varchar(255),
startdate date,
expected_enddate date,
due_date date,
project_manager varchar(255),
customer_name varchar(255),
 project_manager_id int NOT NULL ,
 FOREIGN KEY (project_manager_id) REFERENCES projectmanagers(project_manager_id)
);

CREATE TABLE processes (
processID int NOT NULL PRIMARY KEY AUTO_INCREMENT,
projectID int,
process_name VARCHAR(255),
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
projectID int,
FOREIGN KEY (processID) REFERENCES project_process(processID),
FOREIGN KEY (projectID) REFERENCES project(projectID)
);