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
-- Table structure for table `ambulance_trips`
--

DROP TABLE IF EXISTS `ambulance_trips`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ambulance_trips` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `arrival_time` datetime(6) DEFAULT NULL,
  `completion_time` datetime(6) DEFAULT NULL,
  `created_at` datetime(6) NOT NULL,
  `dispatch_time` datetime(6) DEFAULT NULL,
  `distance_travelled` double DEFAULT NULL,
  `dropoff_location` varchar(255) DEFAULT NULL,
  `notes` text,
  `pickup_location` varchar(255) DEFAULT NULL,
  `response_time_minutes` double DEFAULT NULL,
  `status` varchar(255) NOT NULL,
  `trip_type` varchar(255) DEFAULT NULL,
  `updated_at` datetime(6) NOT NULL,
  `ambulance_id` bigint NOT NULL,
  `emergency_patient_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK2gsep9c401c397xw5cxmgv0d8` (`ambulance_id`),
  KEY `FKhrfj5wg383pf21tejmu6vemk6` (`emergency_patient_id`),
  CONSTRAINT `FK2gsep9c401c397xw5cxmgv0d8` FOREIGN KEY (`ambulance_id`) REFERENCES `ambulances` (`id`),
  CONSTRAINT `FKhrfj5wg383pf21tejmu6vemk6` FOREIGN KEY (`emergency_patient_id`) REFERENCES `emergency_patients` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ambulance_trips`
--

LOCK TABLES `ambulance_trips` WRITE;
/*!40000 ALTER TABLE `ambulance_trips` DISABLE KEYS */;
/*!40000 ALTER TABLE `ambulance_trips` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-08-16 18:05:13
