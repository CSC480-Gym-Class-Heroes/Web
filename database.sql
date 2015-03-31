CREATE database IF NOT EXISTS csc480;

USE csc480;

DROP TABLE if EXISTS sensorData;
   
CREATE TABLE sensorData (
    sensorTimeStamp BIGINT,
    count int,
    gymID varchar(15),
    primary key (sensorTimeStamp, gymID)
);
