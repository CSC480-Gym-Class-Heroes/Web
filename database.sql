CREATE database IF NOT EXISTS gym;

USE gym;

DROP TABLE if EXISTS sensorData;
   
CREATE TABLE sensorData (
    sensorTimeStamp BIGINT,
    count int,
    gymID varchar(15),
    primary key (sensorTimeStamp, gymID)
);
