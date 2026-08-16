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
-- Table structure for table `pharmacy_sales`
--

DROP TABLE IF EXISTS `pharmacy_sales`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pharmacy_sales` (
  `discount` double DEFAULT NULL,
  `net_payable` double DEFAULT NULL,
  `total_amount` double DEFAULT NULL,
  `billing_id` bigint DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `sale_date` datetime(6) NOT NULL,
  `patient_type` varchar(255) DEFAULT NULL,
  `payment_status` varchar(255) DEFAULT NULL,
  `sale_invoice_no` varchar(255) NOT NULL,
  `change_amount` double DEFAULT NULL,
  `doctor_id` bigint DEFAULT NULL,
  `doctor_name` varchar(255) DEFAULT NULL,
  `paid_amount` double DEFAULT NULL,
  `patient_id` bigint DEFAULT NULL,
  `patient_name` varchar(255) DEFAULT NULL,
  `patient_phone` varchar(255) DEFAULT NULL,
  `payment_method` varchar(255) DEFAULT NULL,
  `prescription_id` bigint DEFAULT NULL,
  `sale_type` varchar(255) DEFAULT NULL,
  `vat` double DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKhij5d6ohtewu4ri613y35lox` (`sale_invoice_no`),
  KEY `FKevxr1tn3jo769ibvrnbwqg81i` (`billing_id`),
  CONSTRAINT `FKevxr1tn3jo769ibvrnbwqg81i` FOREIGN KEY (`billing_id`) REFERENCES `billings` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pharmacy_sales`
--

LOCK TABLES `pharmacy_sales` WRITE;
/*!40000 ALTER TABLE `pharmacy_sales` DISABLE KEYS */;
INSERT INTO `pharmacy_sales` VALUES (0,1.8,0,NULL,1,'2026-08-12 18:10:55.868394','OUTPATIENT','PAID','PHM-INV-CD811E06',10,NULL,'',11.8,NULL,'Test','','Cash',NULL,'PHARMACY',1.8),(0,1.8,0,NULL,2,'2026-08-12 18:11:02.404883','OUTPATIENT','PAID','PHM-INV-90B24A79',10,NULL,'',11.8,NULL,'Test','','Cash',NULL,'PHARMACY',1.8),(0,21,12,NULL,3,'2026-08-12 18:14:04.160535','OUTPATIENT','PAID','PHM-INV-C0932EB3',38,NULL,'',59,NULL,'','','Cash',NULL,'PHARMACY',9),(0,17,12,NULL,4,'2026-08-12 18:21:19.909249','OUTPATIENT','PAID','PHM-INV-F2FBAA09',0,NULL,'Fatema Akter',17,NULL,'Emon','12345678912','Cash',2,'PHARMACY',5),(0,87,82,NULL,5,'2026-08-12 18:38:10.558195','OUTPATIENT','PAID','PHM-INV-AADB1BED',0,NULL,'Fatema Akter',87,NULL,'Tanvir','0178946645','Cash',12,'PHARMACY',5);
/*!40000 ALTER TABLE `pharmacy_sales` ENABLE KEYS */;
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
