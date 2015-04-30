CREATE database IF NOT EXISTS gym;

USE gym;

DROP TABLE if EXISTS sensorData;
   
CREATE TABLE sensorData (
    sensorTimeStamp BIGINT,
    count int,
<<<<<<< HEAD
    gymID varchar(15),
    primary key (sensorTimeStamp, gymID)
);

CREATE USER 'dev'@'localhost'
IDENTIFIED BY 'DevPass';

GRANT ALL ON gym.* TO 'dev'@'localhost';
=======
    gymName varchar(15),
    primary key (sensorTimeStamp, gymName)
);

DROP TABLE if EXISTS averageInTable;

CREATE TABLE averageInTable (
    dayOfWeek varchar(15),
    gymName varchar(15),
    averageIns int,
    numberOfWeeks int,
    primary key (dayOfWeek, gymName)
);

grant all on `gym`.* to 'dev'@'localhost' identified by 'DevPass';
>>>>>>> origin/Prez
