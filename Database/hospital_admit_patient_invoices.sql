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
-- Table structure for table `admit_patient_invoices`
--

DROP TABLE IF EXISTS `admit_patient_invoices`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `admit_patient_invoices` (
  `bed_charges` double NOT NULL,
  `discount` double NOT NULL,
  `doctor_charges` double NOT NULL,
  `due_amount` double NOT NULL,
  `meal_charges` double NOT NULL,
  `medicine_charges` double NOT NULL,
  `net_payable` double NOT NULL,
  `other_charges` double NOT NULL,
  `paid_amount` double NOT NULL,
  `tax` double NOT NULL,
  `test_charges` double NOT NULL,
  `total_amount` double NOT NULL,
  `admitted_patient_id` bigint NOT NULL,
  `created_date` datetime(6) NOT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `generated_by` varchar(255) DEFAULT NULL,
  `invoice_number` varchar(255) NOT NULL,
  `payment_status` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKc6un5c4wcx5r6o1xiw4ehwjmr` (`admitted_patient_id`),
  UNIQUE KEY `UKal9gxwjml7bc7w7gtqkbhisnl` (`invoice_number`),
  CONSTRAINT `FK6tfafv4k6gfenvrrwbg5fxe04` FOREIGN KEY (`admitted_patient_id`) REFERENCES `admitted_patients` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admit_patient_invoices`
--

LOCK TABLES `admit_patient_invoices` WRITE;
/*!40000 ALTER TABLE `admit_patient_invoices` DISABLE KEYS */;
/*!40000 ALTER TABLE `admit_patient_invoices` ENABLE KEYS */;
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
