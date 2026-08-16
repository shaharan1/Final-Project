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
-- Table structure for table `emergency_patients`
--

DROP TABLE IF EXISTS `emergency_patients`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `emergency_patients` (
  `arrival_time` datetime(6) NOT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `patient_id` bigint NOT NULL,
  `brought_by` varchar(255) DEFAULT NULL,
  `chief_complaint` text,
  `contact_number` varchar(255) DEFAULT NULL,
  `severity_level` varchar(255) NOT NULL,
  `status` varchar(255) DEFAULT NULL,
  `accident_type` varchar(255) DEFAULT NULL,
  `address` text,
  `age` int DEFAULT NULL,
  `ambulance_id` varchar(255) DEFAULT NULL,
  `blood_group` varchar(255) DEFAULT NULL,
  `created_at` datetime(6) NOT NULL,
  `discharge_time` datetime(6) DEFAULT NULL,
  `doctor_assigned_time` datetime(6) DEFAULT NULL,
  `emergency_contact_name` varchar(255) DEFAULT NULL,
  `emergency_contact_phone` varchar(255) DEFAULT NULL,
  `emergency_contact_relation` varchar(255) DEFAULT NULL,
  `emergency_notes` text,
  `emergency_number` varchar(255) NOT NULL,
  `gender` varchar(255) DEFAULT NULL,
  `injury_details` text,
  `is_police_case` bit(1) DEFAULT NULL,
  `is_referral` bit(1) DEFAULT NULL,
  `is_unknown_patient` bit(1) DEFAULT NULL,
  `national_id` varchar(255) DEFAULT NULL,
  `patient_name` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `referral_hospital` varchar(255) DEFAULT NULL,
  `symptoms` text,
  `triage_level` int DEFAULT NULL,
  `triage_time` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKhob69gkyaa1344drvq2onkldb` (`emergency_number`),
  KEY `FKf7wnlanc4ula7isd54ybtax44` (`patient_id`),
  CONSTRAINT `FKf7wnlanc4ula7isd54ybtax44` FOREIGN KEY (`patient_id`) REFERENCES `patients` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `emergency_patients`
--

LOCK TABLES `emergency_patients` WRITE;
/*!40000 ALTER TABLE `emergency_patients` DISABLE KEYS */;
INSERT INTO `emergency_patients` VALUES ('2026-07-28 15:20:28.957296',1,5,NULL,'fgh',NULL,'MODERATE','WAITING','BURN','dfgdh',25,'sfgdhf','B-','2026-07-28 15:20:28.958295',NULL,NULL,'Emon','0178455566','Father','dfsDGH','EMG-260728-0001','Male','fgdf',_binary '\0',_binary '',_binary '\0','7845691234','Sabbir','0178945612','fgd','dgsfdzf',3,NULL,'2026-07-28 15:20:28.958295');
/*!40000 ALTER TABLE `emergency_patients` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-08-16 18:05:20
