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
-- Table structure for table `test_parameters`
--

DROP TABLE IF EXISTS `test_parameters`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `test_parameters` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `active` bit(1) DEFAULT NULL,
  `allowed_values` text,
  `decimal_precision` int DEFAULT NULL,
  `display_order` int DEFAULT NULL,
  `normal_text` text,
  `parameter_code` varchar(50) NOT NULL,
  `parameter_name` varchar(100) NOT NULL,
  `result_type` enum('MULTI_OPTION','NUMERIC','POSITIVE_NEGATIVE','TEXT') NOT NULL,
  `unit` varchar(30) DEFAULT NULL,
  `test_master_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKppj8dcip9lug20ks25ah77u8q` (`test_master_id`),
  CONSTRAINT `FKppj8dcip9lug20ks25ah77u8q` FOREIGN KEY (`test_master_id`) REFERENCES `test_masters` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `test_parameters`
--

LOCK TABLES `test_parameters` WRITE;
/*!40000 ALTER TABLE `test_parameters` DISABLE KEYS */;
INSERT INTO `test_parameters` VALUES (1,_binary '',NULL,1,1,NULL,'HGB','Hemoglobin','NUMERIC','g/dL',21),(2,_binary '',NULL,0,2,NULL,'WBC','WBC Count','NUMERIC','/µL',21),(3,_binary '',NULL,0,3,NULL,'PLATELET','Platelet Count','NUMERIC','/µL',21),(4,_binary '',NULL,1,4,NULL,'HCT','Hematocrit (PCV)','NUMERIC','%',21),(5,_binary '',NULL,1,5,NULL,'NEUT','Neutrophils','NUMERIC','%',21),(6,_binary '',NULL,1,6,NULL,'LYMPH','Lymphocytes','NUMERIC','%',21),(7,_binary '','Positive,Negative',0,1,NULL,'NS1','Dengue NS1 Antigen','POSITIVE_NEGATIVE',NULL,22),(8,_binary '','Positive,Negative',0,2,NULL,'IGM','Dengue IgM Antibody','POSITIVE_NEGATIVE',NULL,22),(9,_binary '','Positive,Negative',0,3,NULL,'IGG','Dengue IgG Antibody','POSITIVE_NEGATIVE',NULL,22),(10,_binary '',NULL,1,1,NULL,'HGB','Hemoglobin','NUMERIC','g/dL',23),(11,_binary '',NULL,1,1,NULL,'FGLU','Fasting Glucose','NUMERIC','mg/dL',24),(12,_binary '',NULL,2,1,NULL,'CREAT','Creatinine','NUMERIC','mg/dL',25),(13,_binary '',NULL,0,1,NULL,'WBC','WBC Count','NUMERIC','/µL',26),(14,_binary '',NULL,0,1,NULL,'PLATELET','Platelet Count','NUMERIC','/µL',27),(15,_binary '','Detected,Not Detected',0,1,NULL,'COVID','SARS-CoV-2 PCR','POSITIVE_NEGATIVE',NULL,28),(16,_binary '','Positive,Negative',0,1,NULL,'PREGNANCY','HCG (Urine)','POSITIVE_NEGATIVE',NULL,29),(17,_binary '','Positive,Negative',0,1,NULL,'NS1','Dengue NS1 Antigen','POSITIVE_NEGATIVE',NULL,30),(18,_binary '','Positive,Negative',0,2,NULL,'IGM','Dengue IgM Antibody','POSITIVE_NEGATIVE',NULL,30),(19,_binary '','Positive,Negative',0,3,NULL,'IGG','Dengue IgG Antibody','POSITIVE_NEGATIVE',NULL,30),(20,_binary '',NULL,0,4,NULL,'PLATELET','Platelet Count','NUMERIC','/µL',30),(21,_binary '',NULL,0,5,NULL,'WBC','WBC Count','NUMERIC','/µL',30),(22,_binary '',NULL,1,6,NULL,'HGB','Hemoglobin','NUMERIC','g/dL',30),(23,_binary '',NULL,1,7,NULL,'HCT','Hematocrit (PCV)','NUMERIC','%',30);
/*!40000 ALTER TABLE `test_parameters` ENABLE KEYS */;
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
