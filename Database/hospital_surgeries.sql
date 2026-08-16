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
-- Table structure for table `surgeries`
--

DROP TABLE IF EXISTS `surgeries`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `surgeries` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `advance_paid` double DEFAULT NULL,
  `anesthesia_fee` double DEFAULT NULL,
  `anesthesia_type` varchar(100) DEFAULT NULL,
  `assistant_surgeon_fee` double DEFAULT NULL,
  `billing_invoice_id` bigint DEFAULT NULL,
  `billing_invoice_number` varchar(50) DEFAULT NULL,
  `billing_status` varchar(20) DEFAULT NULL,
  `cancellation_reason` varchar(300) DEFAULT NULL,
  `clinical_notes` text,
  `consumable_charge` double DEFAULT NULL,
  `created_date` datetime(6) NOT NULL,
  `discount_amount` double DEFAULT NULL,
  `discount_percent` double DEFAULT NULL,
  `end_time` time DEFAULT NULL,
  `equipment_charge` double DEFAULT NULL,
  `estimated_duration_min` int DEFAULT NULL,
  `final_payable` double DEFAULT NULL,
  `icu_charge` double DEFAULT NULL,
  `insurance_coverage` double DEFAULT NULL,
  `laboratory_charge` double DEFAULT NULL,
  `last_updated` datetime(6) NOT NULL,
  `medicine_charge` double DEFAULT NULL,
  `nursing_charge` double DEFAULT NULL,
  `ot_charge` double DEFAULT NULL,
  `post_operative_diagnosis` text,
  `pre_operative_diagnosis` text,
  `priority` varchar(20) DEFAULT NULL,
  `radiology_charge` double DEFAULT NULL,
  `start_time` time DEFAULT NULL,
  `status` varchar(20) DEFAULT NULL,
  `surgeon_fee` double DEFAULT NULL,
  `surgery_charge` double DEFAULT NULL,
  `surgery_date` date NOT NULL,
  `surgery_number` varchar(50) NOT NULL,
  `vat_amount` double DEFAULT NULL,
  `vat_rate` double DEFAULT NULL,
  `ward_cabin_charge` double DEFAULT NULL,
  `admitted_patient_id` bigint DEFAULT NULL,
  `anesthesiologist_id` bigint DEFAULT NULL,
  `assistant_surgeon_id` bigint DEFAULT NULL,
  `category_id` bigint DEFAULT NULL,
  `department_id` bigint DEFAULT NULL,
  `operation_theatre_id` bigint DEFAULT NULL,
  `patient_id` bigint NOT NULL,
  `surgeon_id` bigint NOT NULL,
  `surgery_master_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK8hj44fh2xn6rlclehuip5m7te` (`surgery_number`),
  KEY `FK32soylltxg0cxlhiofjietpdq` (`admitted_patient_id`),
  KEY `FKimf5nirkh116jh3lqykobtpp0` (`anesthesiologist_id`),
  KEY `FK5vocpq3dt56ny2y5o0jk86eme` (`assistant_surgeon_id`),
  KEY `FKqasgenc0tjoy5at24tbbqnxrg` (`category_id`),
  KEY `FKa4frx201dq43swhxu72rvfrl8` (`department_id`),
  KEY `FK7l21qskdqhcm25fj6vcga18om` (`operation_theatre_id`),
  KEY `FKqnk7n13xdegf36tnah16hdner` (`patient_id`),
  KEY `FKg91tn2sxfc1xs9bhu0rqpp4qy` (`surgeon_id`),
  KEY `FKkir5f99t1wp65x5i5wg4x8k3i` (`surgery_master_id`),
  CONSTRAINT `FK32soylltxg0cxlhiofjietpdq` FOREIGN KEY (`admitted_patient_id`) REFERENCES `admitted_patients` (`id`),
  CONSTRAINT `FK5vocpq3dt56ny2y5o0jk86eme` FOREIGN KEY (`assistant_surgeon_id`) REFERENCES `doctors` (`id`),
  CONSTRAINT `FK7l21qskdqhcm25fj6vcga18om` FOREIGN KEY (`operation_theatre_id`) REFERENCES `operation_theatres` (`id`),
  CONSTRAINT `FKa4frx201dq43swhxu72rvfrl8` FOREIGN KEY (`department_id`) REFERENCES `doctor_departments` (`id`),
  CONSTRAINT `FKg91tn2sxfc1xs9bhu0rqpp4qy` FOREIGN KEY (`surgeon_id`) REFERENCES `doctors` (`id`),
  CONSTRAINT `FKimf5nirkh116jh3lqykobtpp0` FOREIGN KEY (`anesthesiologist_id`) REFERENCES `doctors` (`id`),
  CONSTRAINT `FKkir5f99t1wp65x5i5wg4x8k3i` FOREIGN KEY (`surgery_master_id`) REFERENCES `surgery_masters` (`id`),
  CONSTRAINT `FKqasgenc0tjoy5at24tbbqnxrg` FOREIGN KEY (`category_id`) REFERENCES `surgery_categories` (`id`),
  CONSTRAINT `FKqnk7n13xdegf36tnah16hdner` FOREIGN KEY (`patient_id`) REFERENCES `patients` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `surgeries`
--

LOCK TABLES `surgeries` WRITE;
/*!40000 ALTER TABLE `surgeries` DISABLE KEYS */;
INSERT INTO `surgeries` VALUES (1,0,200,'',500,6,'INV-060826-1000','DRAFT','','',100,'2026-08-06 16:23:10.389782',3620,10,NULL,500,60,34209,1000,0,1000,'2026-08-06 16:23:10.518599',5000,200,5000,'','','ELECTIVE',500,'09:00:00','SCHEDULED',1000,20000,'2026-08-06','SURG-20260806-001',1629,5,1200,5,NULL,NULL,NULL,NULL,1,3,1,NULL),(2,0,10000,'',0,7,'INV-160826-1000','DRAFT','','',8000,'2026-08-16 17:13:56.667529',12100,10,NULL,12000,120,114345,8000,0,0,'2026-08-16 17:13:56.694074',0,6000,12000,'','','ELECTIVE',0,'09:00:00','SCHEDULED',0,65000,'2026-08-18','SURG-20260816-001',5445,5,0,2,5,4,5,2,3,1,2,7);
/*!40000 ALTER TABLE `surgeries` ENABLE KEYS */;
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
