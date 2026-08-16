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
-- Table structure for table `lab_reports`
--

DROP TABLE IF EXISTS `lab_reports`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `lab_reports` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_by` varchar(255) DEFAULT NULL,
  `created_date` datetime(6) DEFAULT NULL,
  `final_impression` text,
  `recommendation` text,
  `report_number` varchar(50) DEFAULT NULL,
  `report_status` enum('ABNORMAL','CRITICAL','DENGUE_POSITIVE','NEEDS_DOCTOR_REVIEW','NORMAL','PENDING','READY') NOT NULL,
  `reported_date` datetime(6) DEFAULT NULL,
  `specialist_designation` varchar(255) DEFAULT NULL,
  `specialist_name` varchar(255) DEFAULT NULL,
  `specialist_signature` longtext,
  `test_order_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKhoxxo8yxrloi437fov3b9554i` (`report_number`),
  UNIQUE KEY `UK1qlims9h8mmfs99tfr40bitvr` (`test_order_id`),
  CONSTRAINT `FK3a44qc6ic61axeq9thqlu2eqw` FOREIGN KEY (`test_order_id`) REFERENCES `tests` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lab_reports`
--

LOCK TABLES `lab_reports` WRITE;
/*!40000 ALTER TABLE `lab_reports` DISABLE KEYS */;
INSERT INTO `lab_reports` VALUES (1,'Test Tech','2026-08-06 17:41:28.501393','One or more parameters are outside the reference range.','Clinical correlation advised.','LR-000001','ABNORMAL',NULL,NULL,NULL,NULL,19),(2,'Test Tech','2026-08-06 17:46:05.550277','Strongly suggestive of Dengue infection.','Immediate physician review recommended. Start supportive management and monitor platelet count.','LR-000002','DENGUE_POSITIVE','2026-08-06 17:46:15.267510','Consultant Pathologist','Dr. Rashida Khanam','',20),(3,'Test Tech','2026-08-06 17:47:31.835470','Multiple abnormal parameters detected.','Report requires physician review.','LR-000003','NEEDS_DOCTOR_REVIEW',NULL,NULL,NULL,NULL,21),(5,'Test Tech','2026-08-06 18:00:19.667573','One or more parameters are outside the reference range.','Clinical correlation advised.','LR-000005','ABNORMAL',NULL,NULL,NULL,NULL,22);
/*!40000 ALTER TABLE `lab_reports` ENABLE KEYS */;
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
