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
-- Table structure for table `interpretation_rules`
--

DROP TABLE IF EXISTS `interpretation_rules`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `interpretation_rules` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `active` bit(1) DEFAULT NULL,
  `display_order` int DEFAULT NULL,
  `interpretation_text` text NOT NULL,
  `parameter_status` varchar(30) NOT NULL,
  `value_match` varchar(100) DEFAULT NULL,
  `test_parameter_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKhqt7uul0qiox0hd0plt62jxgu` (`test_parameter_id`),
  CONSTRAINT `FKhqt7uul0qiox0hd0plt62jxgu` FOREIGN KEY (`test_parameter_id`) REFERENCES `test_parameters` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=65 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `interpretation_rules`
--

LOCK TABLES `interpretation_rules` WRITE;
/*!40000 ALTER TABLE `interpretation_rules` DISABLE KEYS */;
INSERT INTO `interpretation_rules` VALUES (1,_binary '',1,'Hemoglobin is critically low. Possible severe anemia. Blood transfusion may be required.','CRITICAL_LOW',NULL,1),(2,_binary '',2,'Hemoglobin is below the normal range. Possible anemia.','LOW',NULL,1),(3,_binary '',3,'Hemoglobin is above the normal range. Possible polycythemia.','HIGH',NULL,1),(4,_binary '',4,'Hemoglobin is critically high. Possible severe polycythemia.','CRITICAL_HIGH',NULL,1),(5,_binary '',1,'WBC count is critically low. Risk of infection is high. Immediate medical attention required.','CRITICAL_LOW',NULL,2),(6,_binary '',2,'WBC count is below the normal range. Possible leukopenia.','LOW',NULL,2),(7,_binary '',3,'WBC count is above the normal range. Possible leukocytosis or infection.','HIGH',NULL,2),(8,_binary '',4,'WBC count is critically high. Possible severe infection or hematological condition.','CRITICAL_HIGH',NULL,2),(9,_binary '',1,'Platelet count is critically low. High risk of bleeding. Immediate medical attention required.','CRITICAL_LOW',NULL,3),(10,_binary '',2,'Platelet count is below the normal range. Possible thrombocytopenia. Consider dengue or other hematological conditions.','LOW',NULL,3),(11,_binary '',3,'Platelet count is above the normal range. Possible thrombocytosis.','HIGH',NULL,3),(12,_binary '',1,'Hematocrit is below the normal range. Possible anemia.','LOW',NULL,4),(13,_binary '',2,'Hematocrit is above the normal range.','HIGH',NULL,4),(14,_binary '',1,'Neutrophil percentage is below the normal range.','LOW',NULL,5),(15,_binary '',2,'Neutrophil percentage is above the normal range. Possible bacterial infection.','HIGH',NULL,5),(16,_binary '',1,'Lymphocyte percentage is below the normal range.','LOW',NULL,6),(17,_binary '',2,'Lymphocyte percentage is above the normal range. Possible viral infection.','HIGH',NULL,6),(18,_binary '',1,'Dengue NS1 antigen detected. Suggestive of acute dengue infection.','POSITIVE','Positive',7),(19,_binary '',2,'Dengue NS1 antigen not detected.','NEGATIVE','Negative',7),(20,_binary '',1,'Dengue IgM antibody detected. Suggests recent or ongoing dengue infection.','POSITIVE','Positive',8),(21,_binary '',2,'Dengue IgM antibody not detected.','NEGATIVE','Negative',8),(22,_binary '',1,'Dengue IgG antibody detected. Suggests past dengue infection.','POSITIVE','Positive',9),(23,_binary '',2,'Dengue IgG antibody not detected.','NEGATIVE','Negative',9),(24,_binary '',1,'Hemoglobin is critically low. Possible severe anemia. Blood transfusion may be required.','CRITICAL_LOW',NULL,10),(25,_binary '',2,'Hemoglobin is below the normal range. Possible anemia.','LOW',NULL,10),(26,_binary '',3,'Hemoglobin is above the normal range. Possible polycythemia.','HIGH',NULL,10),(27,_binary '',4,'Hemoglobin is critically high. Possible severe polycythemia.','CRITICAL_HIGH',NULL,10),(28,_binary '',1,'Fasting glucose is critically low. Risk of hypoglycemic emergency. Immediate attention required.','CRITICAL_LOW',NULL,11),(29,_binary '',2,'Fasting glucose is below the normal range. Possible hypoglycemia.','LOW',NULL,11),(30,_binary '',3,'Fasting glucose is above the normal range. Suggestive of impaired fasting glucose or diabetes mellitus.','HIGH',NULL,11),(31,_binary '',4,'Fasting glucose is critically high. Possible hyperglycemic emergency. Immediate attention required.','CRITICAL_HIGH',NULL,11),(32,_binary '',1,'Creatinine is below the normal range.','LOW',NULL,12),(33,_binary '',2,'Creatinine is above the normal range. Possible renal impairment.','HIGH',NULL,12),(34,_binary '',3,'Creatinine is critically high. Possible severe renal dysfunction. Immediate medical review required.','CRITICAL_HIGH',NULL,12),(35,_binary '',1,'WBC count is critically low. Risk of infection is high. Immediate medical attention required.','CRITICAL_LOW',NULL,13),(36,_binary '',2,'WBC count is below the normal range. Possible leukopenia.','LOW',NULL,13),(37,_binary '',3,'WBC count is above the normal range. Possible leukocytosis or infection.','HIGH',NULL,13),(38,_binary '',4,'WBC count is critically high. Possible severe infection or hematological condition.','CRITICAL_HIGH',NULL,13),(39,_binary '',1,'Platelet count is critically low. High risk of bleeding. Immediate medical attention required.','CRITICAL_LOW',NULL,14),(40,_binary '',2,'Platelet count is below the normal range. Possible thrombocytopenia. Consider dengue or other hematological conditions.','LOW',NULL,14),(41,_binary '',3,'Platelet count is above the normal range. Possible thrombocytosis.','HIGH',NULL,14),(42,_binary '',1,'SARS-CoV-2 RNA detected. Confirms active COVID-19 infection.','POSITIVE','Detected',15),(43,_binary '',2,'SARS-CoV-2 RNA not detected.','NEGATIVE','Not Detected',15),(44,_binary '',1,'HCG detected. Test is positive - suggestive of pregnancy.','POSITIVE','Positive',16),(45,_binary '',2,'HCG not detected. Test is negative.','NEGATIVE','Negative',16),(46,_binary '',1,'Dengue NS1 antigen detected. Suggestive of acute dengue infection.','POSITIVE','Positive',17),(47,_binary '',2,'Dengue NS1 antigen not detected.','NEGATIVE','Negative',17),(48,_binary '',1,'Dengue IgM antibody detected. Suggests recent or ongoing dengue infection.','POSITIVE','Positive',18),(49,_binary '',2,'Dengue IgM antibody not detected.','NEGATIVE','Negative',18),(50,_binary '',1,'Dengue IgG antibody detected. Suggests past dengue infection.','POSITIVE','Positive',19),(51,_binary '',2,'Dengue IgG antibody not detected.','NEGATIVE','Negative',19),(52,_binary '',1,'Platelet count is critically low. High risk of bleeding. Immediate medical attention required.','CRITICAL_LOW',NULL,20),(53,_binary '',2,'Platelet count is below the normal range. Possible thrombocytopenia. Consider dengue or other hematological conditions.','LOW',NULL,20),(54,_binary '',3,'Platelet count is above the normal range. Possible thrombocytosis.','HIGH',NULL,20),(55,_binary '',1,'WBC count is critically low. Risk of infection is high. Immediate medical attention required.','CRITICAL_LOW',NULL,21),(56,_binary '',2,'WBC count is below the normal range. Possible leukopenia.','LOW',NULL,21),(57,_binary '',3,'WBC count is above the normal range. Possible leukocytosis or infection.','HIGH',NULL,21),(58,_binary '',4,'WBC count is critically high. Possible severe infection or hematological condition.','CRITICAL_HIGH',NULL,21),(59,_binary '',1,'Hemoglobin is critically low. Possible severe anemia. Blood transfusion may be required.','CRITICAL_LOW',NULL,22),(60,_binary '',2,'Hemoglobin is below the normal range. Possible anemia.','LOW',NULL,22),(61,_binary '',3,'Hemoglobin is above the normal range. Possible polycythemia.','HIGH',NULL,22),(62,_binary '',4,'Hemoglobin is critically high. Possible severe polycythemia.','CRITICAL_HIGH',NULL,22),(63,_binary '',1,'Hematocrit is below the normal range. Possible anemia.','LOW',NULL,23),(64,_binary '',2,'Hematocrit is above the normal range.','HIGH',NULL,23);
/*!40000 ALTER TABLE `interpretation_rules` ENABLE KEYS */;
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
