-- MySQL dump 10.13  Distrib 8.0.45, for Win64 (x86_64)
--
-- Host: localhost    Database: hospital
-- ------------------------------------------------------
-- Server version	8.0.45

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `prescriptions`
--

DROP TABLE IF EXISTS `prescriptions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `prescriptions` (
  `next_follow_up_date` date DEFAULT NULL,
  `appointment_id` bigint NOT NULL,
  `created_date` datetime(6) NOT NULL,
  `doctor_id` bigint NOT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `patient_id` bigint NOT NULL,
  `blood_pressure` varchar(255) DEFAULT NULL,
  `body_temperature` varchar(255) DEFAULT NULL,
  `chief_complaints` text,
  `diagnosis` text,
  `notes` text,
  `pulse_rate` varchar(255) DEFAULT NULL,
  `symptoms` text,
  `weight` varchar(255) DEFAULT NULL,
  `prescription_number` varchar(255) DEFAULT NULL,
  `dispensed` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKr0yn695qg51gn0iskc8p0h2ii` (`appointment_id`),
  KEY `FK24chc88e4so7cd6melh11rv6` (`doctor_id`),
  KEY `FKqydyol76jn1o37k1bdbkjgq74` (`patient_id`),
  CONSTRAINT `FK24chc88e4so7cd6melh11rv6` FOREIGN KEY (`doctor_id`) REFERENCES `doctors` (`id`),
  CONSTRAINT `FKe2fpvlkkcgcd40k4ufyyju2al` FOREIGN KEY (`appointment_id`) REFERENCES `appointments` (`id`),
  CONSTRAINT `FKqydyol76jn1o37k1bdbkjgq74` FOREIGN KEY (`patient_id`) REFERENCES `patients` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prescriptions`
--

LOCK TABLES `prescriptions` WRITE;
/*!40000 ALTER TABLE `prescriptions` DISABLE KEYS */;
INSERT INTO `prescriptions` VALUES ('2026-07-17',1,'2026-07-12 18:02:41.019416',5,1,1,'160','32','eeeeeeee','wwwwww','vvvvvvvvvvvv','60','rrrrrrrrr','40',NULL,_binary '\0'),('2026-07-30',2,'2026-07-23 18:17:00.208168',2,2,2,'56','444','t6yuio','rdtfyhj','ertyuiol;\'','44','rtyui','44',NULL,_binary ''),('2026-07-30',3,'2026-07-25 16:02:17.090307',1,3,3,'100','33','fffffff','ddd','fhaaaaaa','60','gggggg','88','RX-1784973737084',_binary '\0'),('2026-08-04',4,'2026-07-27 18:46:38.021965',1,9,4,'120','30','Heart pain','Pain ','Admit and full rest','80','Heart beart first','70','RX-1785156398009',_binary '\0'),('2026-08-07',8,'2026-08-02 14:56:04.693931',1,11,9,'110','33','ghjg','hgfgh','Avoid Cold','80','dshfg','90','RX-1785660964690',_binary '\0'),('2026-08-06',9,'2026-08-02 15:16:21.365943',2,12,10,'110','20','fghjgk','gfdhj','rtytru','66','ghj','80','RX-1785662181364',_binary '');
/*!40000 ALTER TABLE `prescriptions` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-08-16 18:05:16
