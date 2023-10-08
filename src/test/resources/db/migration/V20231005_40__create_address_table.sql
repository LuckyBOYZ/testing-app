CREATE TABLE `ADDRESS`
(
    `ID`              int(11) NOT NULL AUTO_INCREMENT,
    `EMPLOYEE_ID`     int(11) DEFAULT NULL,
    `STREET`          varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
    `HOUSE_NUMBER`    varchar(6) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
    `PREMISES_NUMBER` varchar(6) CHARACTER SET utf8 COLLATE utf8_general_ci   DEFAULT NULL,
    `POSTCODE`        varchar(6) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
    `CITY`            varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci  DEFAULT NULL,
    PRIMARY KEY (`ID`),
    CONSTRAINT `EMPLOYEE_ID_FK` FOREIGN KEY (`EMPLOYEE_ID`) REFERENCES `EMPLOYEE` (`ID`)
)
