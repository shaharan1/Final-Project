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
-- Table structure for table `prescription_items`
--

DROP TABLE IF EXISTS `prescription_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `prescription_items` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `medicine_id` bigint NOT NULL,
  `prescription_id` bigint NOT NULL,
  `dosage` varchar(255) DEFAULT NULL,
  `duration` varchar(255) DEFAULT NULL,
  `instruction` varchar(255) DEFAULT NULL,
  `medicine_type` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKacx7kmy9ksr443xpn5dccidro` (`medicine_id`),
  KEY `FK6uh7tdy2lv6sx34u1365acqsf` (`prescription_id`),
  CONSTRAINT `FK6uh7tdy2lv6sx34u1365acqsf` FOREIGN KEY (`prescription_id`) REFERENCES `prescriptions` (`id`),
  CONSTRAINT `FKacx7kmy9ksr443xpn5dccidro` FOREIGN KEY (`medicine_id`) REFERENCES `medicines` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prescription_items`
--

LOCK TABLES `prescription_items` WRITE;
/*!40000 ALTER TABLE `prescription_items` DISABLE KEYS */;
INSERT INTO `prescription_items` VALUES (1,7,1,'1+0+1','7 days','After Meal',NULL),(2,9,1,'1+0+0','7 days','Before Meal',NULL),(3,1,1,'1+1+1','7 days','After Meal',NULL),(4,1,2,'1+0+1','7 Days','After Meal',NULL),(15,4,9,'1+1+1','7 Days','After Meal',NULL),(16,1,9,'1+1+1','7 Days','After Meal',NULL),(23,1,11,'1+1+1','7 Days','After Meal',NULL),(24,9,11,'1+0+0','7 Days','Empty Stomach',NULL),(25,4,11,'1+1+0','10 Days','After Breakfast',NULL),(28,1,12,'0+1+0','5 Days','After Breakfast',NULL),(29,9,12,'0+1+1','10 Days','After Meal',NULL),(32,1,3,'1+1+1','7 Days','After Meal',NULL),(33,9,3,'1+0+0','7 Days','Empty Stomach',NULL),(34,20,3,'0+1+0','7 Days','After Meal',NULL);
/*!40000 ALTER TABLE `prescription_items` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-08-16 18:05:19
