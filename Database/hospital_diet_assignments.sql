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
-- Table structure for table `diet_assignments`
--

DROP TABLE IF EXISTS `diet_assignments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `diet_assignments` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `end_date` date DEFAULT NULL,
  `reason` text,
  `special_instructions` text,
  `start_date` date NOT NULL,
  `status` varchar(255) NOT NULL,
  `target_calories` double DEFAULT NULL,
  `target_weight` double DEFAULT NULL,
  `updated_at` datetime(6) NOT NULL,
  `admitted_patient_id` bigint DEFAULT NULL,
  `assigned_by_doctor_id` bigint DEFAULT NULL,
  `diet_plan_id` bigint NOT NULL,
  `dietician_id` bigint DEFAULT NULL,
  `patient_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKhqb50pibkngj8hw3woo3h0f9j` (`admitted_patient_id`),
  KEY `FKo6q00ijrus67ahbjih3pkfmjr` (`assigned_by_doctor_id`),
  KEY `FKqvkeynriyvogwf39mirtxcqd8` (`diet_plan_id`),
  KEY `FKtndyrhjbfqpowuyhkgv806vhl` (`dietician_id`),
  KEY `FK8svh4isjb6dyffa26h68xdji9` (`patient_id`),
  CONSTRAINT `FK8svh4isjb6dyffa26h68xdji9` FOREIGN KEY (`patient_id`) REFERENCES `patients` (`id`),
  CONSTRAINT `FKhqb50pibkngj8hw3woo3h0f9j` FOREIGN KEY (`admitted_patient_id`) REFERENCES `admitted_patients` (`id`),
  CONSTRAINT `FKo6q00ijrus67ahbjih3pkfmjr` FOREIGN KEY (`assigned_by_doctor_id`) REFERENCES `doctors` (`id`),
  CONSTRAINT `FKqvkeynriyvogwf39mirtxcqd8` FOREIGN KEY (`diet_plan_id`) REFERENCES `diet_plans` (`id`),
  CONSTRAINT `FKtndyrhjbfqpowuyhkgv806vhl` FOREIGN KEY (`dietician_id`) REFERENCES `dieticians` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `diet_assignments`
--

LOCK TABLES `diet_assignments` WRITE;
/*!40000 ALTER TABLE `diet_assignments` DISABLE KEYS */;
INSERT INTO `diet_assignments` VALUES (1,'2026-07-28 23:20:47.008232','2026-08-07','Regular diet for general recovery','Monitor intake','2026-07-01','ACTIVE',2100,65,'2026-08-03 17:51:24.495867',NULL,NULL,1,NULL,1),(2,'2026-07-28 23:20:47.016928','2026-07-30','Diabetic diet management','Blood sugar before each meal','2026-07-02','ACTIVE',1800,75,'2026-07-28 23:20:47.016928',NULL,NULL,2,1,2),(3,'2026-07-28 23:20:47.023470','2026-07-20','Cardiac recovery diet','Sodium restriction','2026-07-05','COMPLETED',1900,62,'2026-07-28 23:20:47.023470',NULL,NULL,3,1,3),(4,'2026-07-28 23:20:47.047606',NULL,'Post-surgery high protein diet','Track protein intake daily','2026-07-10','ACTIVE',2500,70,'2026-08-03 15:40:05.199883',NULL,NULL,4,NULL,4),(5,'2026-07-28 23:20:47.053163','2026-08-08','Renal diet management','Weekly blood tests','2026-07-08','ACTIVE',1800,55,'2026-07-28 23:20:47.053163',NULL,NULL,5,1,5),(6,'2026-07-28 23:20:47.058697',NULL,'Soft diet after procedure','Gradual transition plan','2026-07-15','ACTIVE',1700,60,'2026-07-28 23:20:47.058697',NULL,NULL,6,1,1),(7,'2026-08-02 14:37:16.022151','2026-08-07','Leg Pain & Leg Surgery','Avoid Sweet','2026-08-02','ACTIVE',2100,NULL,'2026-08-02 14:37:16.022151',NULL,NULL,2,NULL,3),(8,'2026-08-02 16:08:56.654823','2026-08-03','fdhyg','lpw fg','2026-08-02','ACTIVE',2100,NULL,'2026-08-02 16:08:56.654823',NULL,NULL,1,NULL,6);
/*!40000 ALTER TABLE `diet_assignments` ENABLE KEYS */;
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
