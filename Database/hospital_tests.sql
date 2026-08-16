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
-- Table structure for table `tests`
--

DROP TABLE IF EXISTS `tests`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tests` (
  `doctor_id` bigint NOT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `invoice_id` bigint DEFAULT NULL,
  `ordered_date` datetime(6) NOT NULL,
  `patient_id` bigint NOT NULL,
  `prescription_id` bigint DEFAULT NULL,
  `test_master_id` bigint NOT NULL,
  `order_status` varchar(255) NOT NULL,
  `report_file_path` varchar(255) DEFAULT NULL,
  `result_entered_by` varchar(255) DEFAULT NULL,
  `result_entered_date` datetime(6) DEFAULT NULL,
  `result_notes` varchar(255) DEFAULT NULL,
  `result_value` varchar(255) DEFAULT NULL,
  `sample_collected_date` datetime(6) DEFAULT NULL,
  `sample_collector_name` varchar(255) DEFAULT NULL,
  `sample_received_by` varchar(255) DEFAULT NULL,
  `sample_received_date` datetime(6) DEFAULT NULL,
  `sample_type` varchar(255) DEFAULT NULL,
  `testing_start_date` datetime(6) DEFAULT NULL,
  `verification_notes` varchar(255) DEFAULT NULL,
  `verified_by` varchar(255) DEFAULT NULL,
  `verified_date` datetime(6) DEFAULT NULL,
  `last_updated` datetime DEFAULT NULL,
  `lab_report_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK1qkgkri4w0j500etitw3brbrr` (`patient_id`),
  KEY `FKt9hbara22bqu6be8dhw5jb2id` (`doctor_id`),
  KEY `FK4bj7th1ne9u2o29r46cmw1nxn` (`prescription_id`),
  KEY `FKmddx7jkq1oaxrgdbmx2hopk2k` (`test_master_id`),
  KEY `FKsojwg4puliduseue15tmeq98b` (`invoice_id`),
  CONSTRAINT `FK1qkgkri4w0j500etitw3brbrr` FOREIGN KEY (`patient_id`) REFERENCES `patients` (`id`),
  CONSTRAINT `FK4bj7th1ne9u2o29r46cmw1nxn` FOREIGN KEY (`prescription_id`) REFERENCES `prescriptions` (`id`),
  CONSTRAINT `FKmddx7jkq1oaxrgdbmx2hopk2k` FOREIGN KEY (`test_master_id`) REFERENCES `test_masters` (`id`),
  CONSTRAINT `FKsojwg4puliduseue15tmeq98b` FOREIGN KEY (`invoice_id`) REFERENCES `invoices` (`id`),
  CONSTRAINT `FKt9hbara22bqu6be8dhw5jb2id` FOREIGN KEY (`doctor_id`) REFERENCES `doctors` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tests`
--

LOCK TABLES `tests` WRITE;
/*!40000 ALTER TABLE `tests` DISABLE KEYS */;
INSERT INTO `tests` VALUES (5,1,NULL,'2026-07-12 18:02:41.031386',1,1,1,'VERIFIED','lab_report_1.pdf','Admin','2026-07-27 18:53:06.060437','It\'s Normal','12.5g/dl','2026-07-27 18:49:01.636567','Jui Israt','Md.Sajin','2026-07-27 18:50:33.132615','Blood','2026-07-27 18:51:11.440508','','Admin','2026-07-27 18:55:30.588179','2026-07-27 18:55:31',NULL),(5,2,NULL,'2026-07-12 18:02:41.033404',1,1,13,'TESTING',NULL,NULL,NULL,NULL,NULL,'2026-07-29 15:13:53.159256','jui','Sajin','2026-08-16 16:59:24.620071','Blood','2026-08-16 16:59:50.439368',NULL,NULL,NULL,'2026-08-16 16:59:50',NULL),(2,3,NULL,'2026-07-23 18:17:00.229209',2,2,1,'PENDING',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(1,8,NULL,'2026-07-27 18:46:38.047602',4,9,17,'VERIFIED','lab_report_8.pdf','Admin','2026-07-28 14:42:00.414634','Take ECG','Heart Block','2026-07-28 14:39:50.669011','Md Tanvir','Sajin','2026-07-28 14:40:11.932745','Other','2026-07-28 14:40:22.284823','','Admin','2026-08-02 15:03:25.554331','2026-08-02 15:03:26',NULL),(1,9,NULL,'2026-07-27 18:46:38.049599',4,9,2,'SAMPLE_COLLECTED',NULL,NULL,NULL,NULL,NULL,'2026-07-27 18:48:25.559122','Jui Israt',NULL,NULL,'Blood',NULL,NULL,NULL,NULL,'2026-07-27 18:48:26',NULL),(1,15,NULL,'2026-08-02 15:05:51.441515',9,11,1,'PENDING',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-08-02 15:05:51',NULL),(1,16,NULL,'2026-08-02 15:05:51.442517',9,11,17,'PENDING',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-08-02 15:05:51',NULL),(2,18,NULL,'2026-08-02 15:21:14.173110',10,12,13,'PENDING',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-08-02 15:21:14',NULL),(1,19,NULL,'2026-08-06 17:41:23.000000',1,NULL,22,'RESULT_ENTERED','lab_report_19.pdf','Test Tech','2026-08-06 17:41:28.564422',NULL,'Dengue NS1 Antigen=Positive, Dengue IgM Antibody=Negative, Dengue IgG Antibody=Negative','2026-08-06 17:41:23.000000',NULL,NULL,'2026-08-06 17:41:23.000000','Blood','2026-08-06 17:41:23.000000',NULL,NULL,NULL,'2026-08-06 18:02:12',1),(1,20,NULL,'2026-08-06 17:45:47.000000',1,NULL,30,'VERIFIED',NULL,'Test Tech','2026-08-06 17:46:05.674252',NULL,'Dengue NS1 Antigen=Positive, Dengue IgM Antibody=Negative, Dengue IgG Antibody=Negative, Platelet Count=90000, WBC Count=3500, Hemoglobin=10.2, Hematocrit (PCV)=29.0','2026-08-06 17:45:47.000000',NULL,NULL,'2026-08-06 17:45:47.000000','Blood','2026-08-06 17:45:47.000000','Classic dengue presentation. Immediate review recommended.','Dr. Rashida Khanam','2026-08-06 17:46:15.269539','2026-08-06 17:46:15',2),(1,21,NULL,'2026-08-06 17:47:24.000000',1,NULL,21,'RESULT_ENTERED','lab_report_21.pdf','Test Tech','2026-08-06 17:47:31.878171',NULL,'Hemoglobin=10.2, WBC Count=8500, Platelet Count=250000, Hematocrit (PCV)=30.5, Neutrophils=55.0, Lymphocytes=32.0','2026-08-06 17:47:24.000000',NULL,NULL,'2026-08-06 17:47:24.000000','Blood','2026-08-06 17:47:24.000000',NULL,NULL,NULL,'2026-08-06 18:02:51',3),(1,22,NULL,'2026-08-06 17:54:53.000000',1,NULL,21,'RESULT_ENTERED','lab_report_22.pdf','Test Tech','2026-08-06 18:00:19.766571',NULL,'Hemoglobin=12.5, WBC Count=8500, Platelet Count=250000, Hematocrit (PCV)=38.0, Neutrophils=55.0, Lymphocytes=35.0','2026-08-06 17:54:53.000000',NULL,NULL,'2026-08-06 17:54:53.000000','Blood','2026-08-06 17:54:53.000000',NULL,NULL,NULL,'2026-08-16 17:01:06',5),(1,26,NULL,'2026-08-16 17:05:43.249930',3,3,2,'PENDING',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-08-16 17:05:43',NULL),(1,27,NULL,'2026-08-16 17:05:43.251933',3,3,13,'PENDING',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-08-16 17:05:43',NULL),(1,28,NULL,'2026-08-16 17:05:43.252932',3,3,6,'PENDING',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-08-16 17:05:43',NULL);
/*!40000 ALTER TABLE `tests` ENABLE KEYS */;
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
