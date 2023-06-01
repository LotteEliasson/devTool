CREATE DATABASE IF NOT EXISTS projectdb;
USE projectdb;

CREATE TABLE `users`
(
    `user_id`       int NOT NULL AUTO_INCREMENT,
    `user_name`     varchar(255) DEFAULT NULL,
    `user_password` varchar(255) DEFAULT NULL,
    `has_role`      varchar(45)  DEFAULT 'UNASSIGNED',
    PRIMARY KEY (`user_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 9
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
CREATE TABLE `project`
(
    `projectID`          int NOT NULL AUTO_INCREMENT,
    `project_name`       varchar(255) DEFAULT NULL,
    `startdate`          date         DEFAULT NULL,
    `expected_enddate`   date         DEFAULT NULL,
    `due_date`           date         DEFAULT NULL,
    `project_manager`    varchar(255) DEFAULT NULL,
    `customer_name`      varchar(255) DEFAULT NULL,
    `project_manager_id` int          DEFAULT '0',
    `status`             varchar(45)  DEFAULT 'OPEN',
    PRIMARY KEY (`projectID`),
    KEY `project_ibfk_1` (`project_manager_id`),
    CONSTRAINT `project_ibfk_1` FOREIGN KEY (`project_manager_id`) REFERENCES `projectmanager` (`project_manager_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 23
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `processes`
(
    `processID`           int NOT NULL AUTO_INCREMENT,
    `projectID`           int          DEFAULT NULL,
    `process_name`        varchar(255) DEFAULT NULL,
    `expected_start_date` date         DEFAULT NULL,
    `expected_finish`     date         DEFAULT NULL,
    `start_after_task`    int          DEFAULT NULL,
    PRIMARY KEY (`processID`),
    KEY `projectID` (`projectID`),
    CONSTRAINT `processes_ibfk_1` FOREIGN KEY (`projectID`) REFERENCES `project` (`projectID`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 18
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `task`
(
    `taskID`             int NOT NULL AUTO_INCREMENT,
    `processID`          int          DEFAULT NULL,
    `task_name`          varchar(255) DEFAULT NULL,
    `effort`             int          DEFAULT NULL,
    `expected_startdate` date         DEFAULT NULL,
    `min_allocation`     int          DEFAULT NULL,
    `task_status`        varchar(255) DEFAULT NULL,
    `assignedname`       varchar(255) DEFAULT NULL,
    `tasksequencenumber` int          DEFAULT NULL,
    `projectID`          int          DEFAULT NULL,
    `developerID`        int          DEFAULT NULL,
    `expected_finish`    date         DEFAULT NULL,
    PRIMARY KEY (`taskID`),
    KEY `processID` (`processID`),
    KEY `projectID` (`projectID`),
    CONSTRAINT `task_ibfk_1` FOREIGN KEY (`processID`) REFERENCES `processes` (`processID`),
    CONSTRAINT `task_ibfk_2` FOREIGN KEY (`projectID`) REFERENCES `project` (`projectID`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 26
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `projectmanager`
(
    `project_manager_id` int NOT NULL AUTO_INCREMENT,
    `user_id`            int NOT NULL,
    PRIMARY KEY (`project_manager_id`),
    KEY `user_id` (`user_id`),
    CONSTRAINT `projectmanager_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 2
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;


