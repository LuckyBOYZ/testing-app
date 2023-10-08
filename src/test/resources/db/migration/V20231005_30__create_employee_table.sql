CREATE TABLE `EMPLOYEE`
(
    `ID`            int(11) NOT NULL AUTO_INCREMENT,
    `NAME`          varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
    `SURNAME`       varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
    `PESEL`         varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
    `PHONE_NUMBER`  varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
    `DATE_OF_BIRTH` date                                                   DEFAULT NULL,
    `DEPARTMENT_ID` int(11) DEFAULT NULL,
    PRIMARY KEY (`ID`),
    CONSTRAINT `DEPARTMENT_ID_FK` FOREIGN KEY (`DEPARTMENT_ID`) REFERENCES `DEPARTMENT` (`ID`)
)
