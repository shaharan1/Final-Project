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
-- Table structure for table `appointments`
--

DROP TABLE IF EXISTS `appointments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `appointments` (
  `appointment_date` date DEFAULT NULL,
  `appointment_time` time DEFAULT NULL,
  `fee_charged` double DEFAULT NULL,
  `created_date` datetime(6) DEFAULT NULL,
  `doctor_id` bigint NOT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `patient_id` bigint DEFAULT NULL,
  `schedule_slot_id` bigint NOT NULL,
  `appointment_number` varchar(255) NOT NULL,
  `mobile_number` varchar(255) DEFAULT NULL,
  `patient_name` varchar(255) DEFAULT NULL,
  `payment_method` varchar(255) DEFAULT NULL,
  `problem_description` text,
  `specialization` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `transaction_id` varchar(255) DEFAULT NULL,
  `serial_no` bigint DEFAULT NULL,
  `token_number` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKq05c6c2pua24e7e6ixsnp2due` (`schedule_slot_id`),
  UNIQUE KEY `UKqwnib98dh4h1lxbfk1rgeb1e7` (`appointment_number`),
  KEY `FKmujeo4tymoo98cmf7uj3vsv76` (`doctor_id`),
  KEY `FK8exap5wmg8kmb1g1rx3by21yt` (`patient_id`),
  CONSTRAINT `FK48syob0xa66d1tfi5ems3hpo4` FOREIGN KEY (`schedule_slot_id`) REFERENCES `schedule_slots` (`id`),
  CONSTRAINT `FK8exap5wmg8kmb1g1rx3by21yt` FOREIGN KEY (`patient_id`) REFERENCES `patients` (`id`),
  CONSTRAINT `FKmujeo4tymoo98cmf7uj3vsv76` FOREIGN KEY (`doctor_id`) REFERENCES `doctors` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `appointments`
--

LOCK TABLES `appointments` WRITE;
/*!40000 ALTER TABLE `appointments` DISABLE KEYS */;
INSERT INTO `appointments` VALUES ('2026-07-13','19:00:00',800,'2026-07-12 17:56:55.525057',5,1,1,1,'APT-21D3DCA1','01759461325','Md. Masud','Cash','Pain',NULL,'CONFIRMED','',NULL,NULL),('2026-07-24','10:16:00',1200,'2026-07-23 18:13:37.101453',2,2,2,2,'APT-77EDA2E9','12345678912','Emon','Bkash','ghjk',NULL,'PENDING_VERIFICATION','',NULL,NULL),('2026-07-26','22:00:00',1000,'2026-07-25 15:14:46.090601',1,3,3,3,'APT-05E45170','01845312578','Pk','Cash','dddddd',NULL,'CONFIRMED','',NULL,NULL),('2026-07-27','22:00:00',1000,'2026-07-27 18:35:37.186616',1,4,4,6,'APT-E52A0379','01547812365','Md.Badrul Amin','Cash','Heart Pain',NULL,'CONFIRMED','',NULL,NULL),('2026-07-30','09:00:00',1200,'2026-07-29 14:57:32.969776',2,5,6,12,'APT-8657D291','01745623138','Md.Miskat','Cash','Brain Pain',NULL,'CONFIRMED','',NULL,NULL),('2026-07-30','10:00:00',1000,'2026-07-29 14:58:28.436672',1,6,7,10,'APT-1C9C8C67','0184512397','Md.Mahabub','Cash','Heart Pain',NULL,'CONFIRMED','',NULL,NULL),('2026-07-30','09:00:00',800,'2026-07-29 15:04:28.694794',5,7,8,16,'APT-C5E23A5E','0194578631','Tonni','Cash','Pain',NULL,'CONFIRMED','',NULL,NULL),('2026-07-30','10:30:00',1000,'2026-08-02 14:47:31.605322',1,8,9,11,'APT-D4872C04','0175646855','Lima','Cash','trury',NULL,'CONFIRMED','',NULL,NULL),('2026-07-30','10:10:00',1200,'2026-08-02 15:08:01.596429',2,9,10,9,'APT-6C065EA6','0178946645','Tanvir','Cash','fgsg',NULL,'CONFIRMED','',NULL,NULL),('2026-08-18','09:00:00',1000,'2026-08-16 16:45:36.231561',1,10,19,18,'APT-D7A655F3','0175468461','Md.Miskat','Cash','fgdhf',NULL,'CONFIRMED','',NULL,NULL),('2026-07-26','22:00:00',1200,'2026-08-16 17:29:36.795230',2,11,NULL,4,'APT-0012184E','01500011122','TokenTest Patient','Cash',NULL,'Test','CONFIRMED','TXNTEST1',11,1),('2026-07-26','22:10:00',1000,'2026-08-16 17:30:55.316120',1,12,NULL,5,'APT-C6D63CCB','01500011133','TokenTest2','Cash',NULL,'Test','CONFIRMED','TXNTEST2',12,2);
/*!40000 ALTER TABLE `appointments` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-08-16 18:05:12
