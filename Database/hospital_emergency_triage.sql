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
-- Table structure for table `emergency_triage`
--

DROP TABLE IF EXISTS `emergency_triage`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `emergency_triage` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `assessed_at` datetime(6) NOT NULL,
  `assessed_by` varchar(255) DEFAULT NULL,
  `assessment_notes` text,
  `blood_pressure_diastolic` int DEFAULT NULL,
  `blood_pressure_systolic` int DEFAULT NULL,
  `created_at` datetime(6) NOT NULL,
  `glasgow_coma_scale` int DEFAULT NULL,
  `oxygen_saturation` int DEFAULT NULL,
  `pain_score` int DEFAULT NULL,
  `pulse` int DEFAULT NULL,
  `respiration_rate` int DEFAULT NULL,
  `temperature` double DEFAULT NULL,
  `triage_color` varchar(255) DEFAULT NULL,
  `triage_level` int DEFAULT NULL,
  `updated_at` datetime(6) NOT NULL,
  `emergency_patient_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKmjvjoo8fmyla0kt3btkrq3lef` (`emergency_patient_id`),
  CONSTRAINT `FKmjvjoo8fmyla0kt3btkrq3lef` FOREIGN KEY (`emergency_patient_id`) REFERENCES `emergency_patients` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `emergency_triage`
--

LOCK TABLES `emergency_triage` WRITE;
/*!40000 ALTER TABLE `emergency_triage` DISABLE KEYS */;
/*!40000 ALTER TABLE `emergency_triage` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-08-16 18:05:18
