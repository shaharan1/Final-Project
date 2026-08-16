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
-- Table structure for table `admitted_patients`
--

DROP TABLE IF EXISTS `admitted_patients`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `admitted_patients` (
  `admission_date` datetime(6) NOT NULL,
  `discharge_date` datetime(6) DEFAULT NULL,
  `doctor_id` bigint NOT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `patient_id` bigint NOT NULL,
  `admission_status` varchar(255) NOT NULL,
  `initial_diagnosis` text,
  PRIMARY KEY (`id`),
  KEY `FKddg9cb8ns2mogcru2ul8n9v9x` (`patient_id`),
  KEY `FKqs4j0lwk8608t8prdc9nch9cf` (`doctor_id`),
  CONSTRAINT `FKddg9cb8ns2mogcru2ul8n9v9x` FOREIGN KEY (`patient_id`) REFERENCES `patients` (`id`),
  CONSTRAINT `FKqs4j0lwk8608t8prdc9nch9cf` FOREIGN KEY (`doctor_id`) REFERENCES `doctors` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admitted_patients`
--

LOCK TABLES `admitted_patients` WRITE;
/*!40000 ALTER TABLE `admitted_patients` DISABLE KEYS */;
INSERT INTO `admitted_patients` VALUES ('2026-07-23 01:49:31.472039','2026-07-23 01:59:50.186812',1,1,1,'DISCHARGED','Jor'),('2026-07-23 02:00:43.707368',NULL,1,2,1,'ADMITTED','Jor'),('2026-07-23 15:17:44.091887',NULL,1,3,1,'ADMITTED','ggg'),('2026-07-23 18:18:53.623704',NULL,2,4,2,'ADMITTED','ggg'),('2026-07-26 18:15:25.612078',NULL,1,5,3,'ADMITTED','Heart pain'),('2026-07-27 17:25:29.205426',NULL,2,6,2,'ADMITTED','Kidney Heart Attack'),('2026-07-28 16:33:11.424150',NULL,2,7,5,'ADMITTED','Skin Burn'),('2026-08-02 16:06:29.817580',NULL,2,8,6,'ADMITTED','frtgyh');
/*!40000 ALTER TABLE `admitted_patients` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-08-16 18:05:15
