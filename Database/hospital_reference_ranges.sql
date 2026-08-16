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
-- Table structure for table `reference_ranges`
--

DROP TABLE IF EXISTS `reference_ranges`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reference_ranges` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `active` bit(1) DEFAULT NULL,
  `critical_high` double DEFAULT NULL,
  `critical_low` double DEFAULT NULL,
  `display_range` varchar(100) DEFAULT NULL,
  `gender_scope` varchar(20) DEFAULT NULL,
  `max_age_years` int DEFAULT NULL,
  `max_value` double DEFAULT NULL,
  `min_age_years` int DEFAULT NULL,
  `min_value` double DEFAULT NULL,
  `priority` int DEFAULT NULL,
  `test_parameter_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKdulpu05uvt36w33p04xjas4rd` (`test_parameter_id`),
  CONSTRAINT `FKdulpu05uvt36w33p04xjas4rd` FOREIGN KEY (`test_parameter_id`) REFERENCES `test_parameters` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reference_ranges`
--

LOCK TABLES `reference_ranges` WRITE;
/*!40000 ALTER TABLE `reference_ranges` DISABLE KEYS */;
INSERT INTO `reference_ranges` VALUES (1,_binary '',20,8,'13.5 - 17.5','MALE',NULL,17.5,NULL,13.5,2,1),(2,_binary '',20,8,'12.0 - 15.5','FEMALE',NULL,15.5,NULL,12,2,1),(3,_binary '',20,8,'11.0 - 16.0 (Child)','ANY',12,16,0,11,1,1),(4,_binary '',50000,2000,'4000 - 11000','ANY',NULL,11000,NULL,4000,1,2),(5,_binary '',1000000,50000,'150000 - 450000','ANY',NULL,450000,NULL,150000,1,3),(6,_binary '',60,25,'40 - 50','MALE',NULL,50,NULL,40,2,4),(7,_binary '',60,25,'36 - 46','FEMALE',NULL,46,NULL,36,2,4),(8,_binary '',NULL,NULL,'40 - 70','ANY',NULL,70,NULL,40,1,5),(9,_binary '',NULL,NULL,'20 - 40','ANY',NULL,40,NULL,20,1,6),(10,_binary '',20,8,'13.5 - 17.5','MALE',NULL,17.5,NULL,13.5,2,10),(11,_binary '',20,8,'12.0 - 15.5','FEMALE',NULL,15.5,NULL,12,2,10),(12,_binary '',20,8,'11.0 - 16.0 (Child)','ANY',12,16,0,11,1,10),(13,_binary '',500,40,'70 - 99','ANY',NULL,99,NULL,70,1,11),(14,_binary '',NULL,2.5,'0.74 - 1.35','MALE',NULL,1.35,NULL,0.74,2,12),(15,_binary '',NULL,2.5,'0.59 - 1.04','FEMALE',NULL,1.04,NULL,0.59,2,12),(16,_binary '',50000,2000,'4000 - 11000','ANY',NULL,11000,NULL,4000,1,13),(17,_binary '',1000000,50000,'150000 - 450000','ANY',NULL,450000,NULL,150000,1,14),(18,_binary '',1000000,50000,'150000 - 450000','ANY',NULL,450000,NULL,150000,1,20),(19,_binary '',50000,2000,'4000 - 11000','ANY',NULL,11000,NULL,4000,1,21),(20,_binary '',20,8,'13.5 - 17.5','MALE',NULL,17.5,NULL,13.5,2,22),(21,_binary '',20,8,'12.0 - 15.5','FEMALE',NULL,15.5,NULL,12,2,22),(22,_binary '',20,8,'11.0 - 16.0 (Child)','ANY',12,16,0,11,1,22),(23,_binary '',60,25,'40 - 50','MALE',NULL,50,NULL,40,2,23),(24,_binary '',60,25,'36 - 46','FEMALE',NULL,46,NULL,36,2,23);
/*!40000 ALTER TABLE `reference_ranges` ENABLE KEYS */;
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
