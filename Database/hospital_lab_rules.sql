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
-- Table structure for table `lab_rules`
--

DROP TABLE IF EXISTS `lab_rules`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `lab_rules` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `active` bit(1) DEFAULT NULL,
  `conditions` text,
  `final_impression` text NOT NULL,
  `priority` int DEFAULT NULL,
  `recommendation` text,
  `rule_code` varchar(50) NOT NULL,
  `rule_name` varchar(150) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lab_rules`
--

LOCK TABLES `lab_rules` WRITE;
/*!40000 ALTER TABLE `lab_rules` DISABLE KEYS */;
INSERT INTO `lab_rules` VALUES (1,_binary '','[{\"parameterCode\":\"PLATELET\",\"operator\":\"LT\",\"value\":\"150000\"},{\"parameterCode\":\"WBC\",\"operator\":\"LT\",\"value\":\"4000\"},{\"parameterCode\":\"NS1\",\"operator\":\"EQ\",\"value\":\"Positive\"}]','Strongly suggestive of Dengue infection.',1,'Immediate physician review recommended. Start supportive management and monitor platelet count.','DENGUE-1','Classic Dengue (NS1+, Low Platelet + Low WBC)'),(2,_binary '','[{\"parameterCode\":\"NS1\",\"operator\":\"EQ\",\"value\":\"Negative\"},{\"parameterCode\":\"IGM\",\"operator\":\"EQ\",\"value\":\"Positive\"}]','Probable recent dengue infection.',2,'Clinical correlation advised. Supportive management and follow-up serology recommended.','DENGUE-2','Probable Recent Dengue (NS1-, IgM+)'),(3,_binary '','[{\"parameterCode\":\"NS1\",\"operator\":\"EQ\",\"value\":\"Negative\"},{\"parameterCode\":\"IGM\",\"operator\":\"EQ\",\"value\":\"Negative\"}]','Dengue markers not detected.',3,'Consider alternative diagnosis and review clinical picture.','DENGUE-3','Dengue Markers Not Detected (NS1-, IgM-)');
/*!40000 ALTER TABLE `lab_rules` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-08-16 18:05:14
