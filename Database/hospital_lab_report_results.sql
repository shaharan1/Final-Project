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
-- Table structure for table `lab_report_results`
--

DROP TABLE IF EXISTS `lab_report_results`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `lab_report_results` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `abnormal` bit(1) DEFAULT NULL,
  `critical` bit(1) DEFAULT NULL,
  `display_order` int DEFAULT NULL,
  `interpretation` text,
  `parameter_code` varchar(255) DEFAULT NULL,
  `parameter_name` varchar(255) DEFAULT NULL,
  `result_value` varchar(255) DEFAULT NULL,
  `status` enum('ABNORMAL','BORDERLINE','CRITICAL_HIGH','CRITICAL_LOW','HIGH','LOW','NEGATIVE','NON_REACTIVE','NORMAL','PENDING','POSITIVE','REACTIVE') NOT NULL,
  `unit` varchar(255) DEFAULT NULL,
  `lab_report_id` bigint NOT NULL,
  `reference_range_id` bigint DEFAULT NULL,
  `test_parameter_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK3bw9cmsw2hgo43qcfkarex6w8` (`lab_report_id`),
  KEY `FKpeqx0m5ehb60cxdk69bey8gy3` (`reference_range_id`),
  KEY `FK9u7s4weowytt884wpf9w79etc` (`test_parameter_id`),
  CONSTRAINT `FK3bw9cmsw2hgo43qcfkarex6w8` FOREIGN KEY (`lab_report_id`) REFERENCES `lab_reports` (`id`),
  CONSTRAINT `FK9u7s4weowytt884wpf9w79etc` FOREIGN KEY (`test_parameter_id`) REFERENCES `test_parameters` (`id`),
  CONSTRAINT `FKpeqx0m5ehb60cxdk69bey8gy3` FOREIGN KEY (`reference_range_id`) REFERENCES `reference_ranges` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lab_report_results`
--

LOCK TABLES `lab_report_results` WRITE;
/*!40000 ALTER TABLE `lab_report_results` DISABLE KEYS */;
INSERT INTO `lab_report_results` VALUES (1,_binary '',_binary '\0',0,'Dengue NS1 antigen detected. Suggestive of acute dengue infection.','NS1','Dengue NS1 Antigen','Positive','POSITIVE',NULL,1,NULL,7),(2,_binary '\0',_binary '\0',1,'Dengue IgM antibody not detected.','IGM','Dengue IgM Antibody','Negative','NEGATIVE',NULL,1,NULL,8),(3,_binary '\0',_binary '\0',2,'Dengue IgG antibody not detected.','IGG','Dengue IgG Antibody','Negative','NEGATIVE',NULL,1,NULL,9),(4,_binary '',_binary '\0',0,'Dengue NS1 antigen detected. Suggestive of acute dengue infection.','NS1','Dengue NS1 Antigen','Positive','POSITIVE',NULL,2,NULL,17),(5,_binary '\0',_binary '\0',1,'Dengue IgM antibody not detected.','IGM','Dengue IgM Antibody','Negative','NEGATIVE',NULL,2,NULL,18),(6,_binary '\0',_binary '\0',2,'Dengue IgG antibody not detected.','IGG','Dengue IgG Antibody','Negative','NEGATIVE',NULL,2,NULL,19),(7,_binary '',_binary '\0',3,'Platelet count is below the normal range. Possible thrombocytopenia. Consider dengue or other hematological conditions.','PLATELET','Platelet Count','90000','LOW','/µL',2,18,20),(8,_binary '',_binary '\0',4,'WBC count is below the normal range. Possible leukopenia.','WBC','WBC Count','3500','LOW','/µL',2,19,21),(9,_binary '',_binary '\0',5,'Hemoglobin is below the normal range. Possible anemia.','HGB','Hemoglobin','10.2','LOW','g/dL',2,20,22),(10,_binary '',_binary '\0',6,'Hematocrit is below the normal range. Possible anemia.','HCT','Hematocrit (PCV)','29.0','LOW','%',2,23,23),(11,_binary '',_binary '\0',0,'Hemoglobin is below the normal range. Possible anemia.','HGB','Hemoglobin','10.2','LOW','g/dL',3,1,1),(12,_binary '\0',_binary '\0',1,'WBC Count is within the normal range. Result: 8500. Reference range: N/A.','WBC','WBC Count','8500','NORMAL','/µL',3,4,2),(13,_binary '\0',_binary '\0',2,'Platelet Count is within the normal range. Result: 250000. Reference range: N/A.','PLATELET','Platelet Count','250000','NORMAL','/µL',3,5,3),(14,_binary '',_binary '\0',3,'Hematocrit is below the normal range. Possible anemia.','HCT','Hematocrit (PCV)','30.5','LOW','%',3,6,4),(15,_binary '\0',_binary '\0',4,'Neutrophils is within the normal range. Result: 55.0. Reference range: N/A.','NEUT','Neutrophils','55.0','NORMAL','%',3,8,5),(16,_binary '\0',_binary '\0',5,'Lymphocytes is within the normal range. Result: 32.0. Reference range: N/A.','LYMPH','Lymphocytes','32.0','NORMAL','%',3,9,6),(23,_binary '\0',_binary '\0',0,'Hemoglobin is within the normal range. Result: 12.5. Reference range: N/A.','HGB','Hemoglobin','12.5','NORMAL','g/dL',5,3,1),(24,_binary '\0',_binary '\0',1,'WBC Count is within the normal range. Result: 8500. Reference range: N/A.','WBC','WBC Count','8500','NORMAL','/µL',5,4,2),(25,_binary '\0',_binary '\0',2,'Platelet Count is within the normal range. Result: 250000. Reference range: N/A.','PLATELET','Platelet Count','250000','NORMAL','/µL',5,5,3),(26,_binary '',_binary '\0',3,'Hematocrit is below the normal range. Possible anemia.','HCT','Hematocrit (PCV)','38.0','LOW','%',5,6,4),(27,_binary '\0',_binary '\0',4,'Neutrophils is within the normal range. Result: 55.0. Reference range: N/A.','NEUT','Neutrophils','55.0','NORMAL','%',5,8,5),(28,_binary '\0',_binary '\0',5,'Lymphocytes is within the normal range. Result: 35.0. Reference range: N/A.','LYMPH','Lymphocytes','35.0','NORMAL','%',5,9,6);
/*!40000 ALTER TABLE `lab_report_results` ENABLE KEYS */;
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
