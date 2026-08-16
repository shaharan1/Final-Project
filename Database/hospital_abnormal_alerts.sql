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
-- Table structure for table `abnormal_alerts`
--

DROP TABLE IF EXISTS `abnormal_alerts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `abnormal_alerts` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_date` datetime(6) NOT NULL,
  `lab_report_id` bigint DEFAULT NULL,
  `parameter_name` varchar(255) DEFAULT NULL,
  `resolved` bit(1) DEFAULT NULL,
  `result_value` varchar(255) DEFAULT NULL,
  `severity` varchar(30) DEFAULT NULL,
  `status` varchar(30) DEFAULT NULL,
  `patient_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKlar0vlg53jeb59gjimf7m4oye` (`patient_id`),
  CONSTRAINT `FKlar0vlg53jeb59gjimf7m4oye` FOREIGN KEY (`patient_id`) REFERENCES `patients` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `abnormal_alerts`
--

LOCK TABLES `abnormal_alerts` WRITE;
/*!40000 ALTER TABLE `abnormal_alerts` DISABLE KEYS */;
INSERT INTO `abnormal_alerts` VALUES (1,'2026-08-06 17:41:28.565421',1,'Dengue NS1 Antigen',_binary '\0','Positive','WARNING','POSITIVE',1),(2,'2026-08-06 17:46:05.674251',2,'Dengue NS1 Antigen',_binary '\0','Positive','WARNING','POSITIVE',1),(3,'2026-08-06 17:46:05.676280',2,'Platelet Count',_binary '\0','90000','WARNING','LOW',1),(4,'2026-08-06 17:46:05.678250',2,'WBC Count',_binary '\0','3500','WARNING','LOW',1),(5,'2026-08-06 17:46:05.680250',2,'Hemoglobin',_binary '\0','10.2','WARNING','LOW',1),(6,'2026-08-06 17:46:05.683272',2,'Hematocrit (PCV)',_binary '\0','29.0','WARNING','LOW',1),(7,'2026-08-06 17:47:31.878171',3,'Hemoglobin',_binary '\0','10.2','WARNING','LOW',1),(8,'2026-08-06 17:47:31.880171',3,'Hematocrit (PCV)',_binary '\0','30.5','WARNING','LOW',1),(9,'2026-08-06 17:55:04.725096',4,'Hematocrit (PCV)',_binary '\0','38.0','WARNING','LOW',1),(10,'2026-08-06 18:00:19.766570',5,'Hematocrit (PCV)',_binary '\0','38.0','WARNING','LOW',1);
/*!40000 ALTER TABLE `abnormal_alerts` ENABLE KEYS */;
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
