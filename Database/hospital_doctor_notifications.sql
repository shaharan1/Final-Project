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
-- Table structure for table `doctor_notifications`
--

DROP TABLE IF EXISTS `doctor_notifications`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `doctor_notifications` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_date` datetime(6) NOT NULL,
  `is_read` bit(1) DEFAULT NULL,
  `message` text,
  `severity` varchar(30) DEFAULT NULL,
  `test_order_id` bigint DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `type` varchar(30) DEFAULT NULL,
  `doctor_id` bigint NOT NULL,
  `patient_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK9la6uoa2o81lowrwu7v86fcfg` (`doctor_id`),
  KEY `FK9i2vmg6hyr2u2i4gipr8pktw` (`patient_id`),
  CONSTRAINT `FK9i2vmg6hyr2u2i4gipr8pktw` FOREIGN KEY (`patient_id`) REFERENCES `patients` (`id`),
  CONSTRAINT `FK9la6uoa2o81lowrwu7v86fcfg` FOREIGN KEY (`doctor_id`) REFERENCES `doctors` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `doctor_notifications`
--

LOCK TABLES `doctor_notifications` WRITE;
/*!40000 ALTER TABLE `doctor_notifications` DISABLE KEYS */;
INSERT INTO `doctor_notifications` VALUES (1,'2026-08-06 17:46:15.279508',_binary '\0','Lab report LR-000002 for Md. Masud is ready. Status: DENGUE_POSITIVE. Strongly suggestive of Dengue infection.','CRITICAL',20,'CRITICAL Lab Result','LAB_REPORT_READY',1,1);
/*!40000 ALTER TABLE `doctor_notifications` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-08-16 18:05:17
