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
-- Table structure for table `billing_invoices`
--

DROP TABLE IF EXISTS `billing_invoices`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `billing_invoices` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_date` datetime(6) NOT NULL,
  `discount_amount` double DEFAULT NULL,
  `discount_percent` double DEFAULT NULL,
  `due_amount` double DEFAULT NULL,
  `finalized_by` varchar(100) DEFAULT NULL,
  `finalized_date` datetime(6) DEFAULT NULL,
  `invoice_number` varchar(50) NOT NULL,
  `invoice_status` varchar(20) NOT NULL,
  `invoice_type` varchar(20) NOT NULL,
  `last_updated` datetime(6) NOT NULL,
  `net_amount` double DEFAULT NULL,
  `notes` varchar(500) DEFAULT NULL,
  `payment_status` varchar(20) NOT NULL,
  `prepared_by` varchar(100) DEFAULT NULL,
  `subtotal` double DEFAULT NULL,
  `tax_amount` double DEFAULT NULL,
  `tax_rate` double DEFAULT NULL,
  `total_paid` double DEFAULT NULL,
  `admitted_patient_id` bigint DEFAULT NULL,
  `patient_id` bigint NOT NULL,
  `doctor_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKim57aybidf7lye81st8lv8qg5` (`invoice_number`),
  KEY `FKjx42ag65x1qa1v0bwfedt08fd` (`admitted_patient_id`),
  KEY `FKciqt26eds9wtmen50kqk87wuh` (`patient_id`),
  KEY `FKr5al2hucco8wref6r83rhpalv` (`doctor_id`),
  CONSTRAINT `FKciqt26eds9wtmen50kqk87wuh` FOREIGN KEY (`patient_id`) REFERENCES `patients` (`id`),
  CONSTRAINT `FKjx42ag65x1qa1v0bwfedt08fd` FOREIGN KEY (`admitted_patient_id`) REFERENCES `admitted_patients` (`id`),
  CONSTRAINT `FKr5al2hucco8wref6r83rhpalv` FOREIGN KEY (`doctor_id`) REFERENCES `doctors` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `billing_invoices`
--

LOCK TABLES `billing_invoices` WRITE;
/*!40000 ALTER TABLE `billing_invoices` DISABLE KEYS */;
INSERT INTO `billing_invoices` VALUES (1,'2026-08-02 17:27:07.275679',0,0,13855,'Admin','2026-08-02 17:38:06.223026','INV-020826-1001','FINALIZED','INPATIENT','2026-08-16 17:09:38.451284',33000,NULL,'PARTIAL',NULL,33000,0,0,19145,2,1,NULL),(2,'2026-08-02 17:29:14.243927',0,0,980,'Admin','2026-08-02 17:48:35.077440','INV-020826-1002','FINALIZED','INPATIENT','2026-08-02 17:48:35.082409',7000,'','PARTIAL',NULL,7000,0,0,6020,5,3,NULL),(5,'2026-08-03 15:04:48.824156',0,0,47500,NULL,NULL,'INV-030826-1000','DRAFT','INPATIENT','2026-08-03 15:04:48.824156',47500,NULL,'UNPAID',NULL,47500,0,0,0,4,2,NULL),(6,'2026-08-06 16:23:10.475464',0,0,23750,NULL,NULL,'INV-060826-1000','DRAFT','INPATIENT','2026-08-06 16:26:46.672477',23750,NULL,'UNPAID',NULL,23750,0,0,0,5,3,NULL),(7,'2026-08-16 17:13:56.678071',0,0,114345,NULL,NULL,'INV-160826-1000','DRAFT','INPATIENT','2026-08-16 17:13:56.695074',114345,NULL,'UNPAID',NULL,114345,0,0,0,2,1,NULL);
/*!40000 ALTER TABLE `billing_invoices` ENABLE KEYS */;
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
