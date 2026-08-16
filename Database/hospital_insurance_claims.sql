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
-- Table structure for table `insurance_claims`
--

DROP TABLE IF EXISTS `insurance_claims`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `insurance_claims` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `approved_amount` double DEFAULT NULL,
  `claim_amount` double NOT NULL,
  `claim_number` varchar(255) DEFAULT NULL,
  `claim_reference` varchar(255) NOT NULL,
  `claim_status` enum('APPROVED','PARTIALLY_APPROVED','REJECTED','SETTLED','SUBMITTED','UNDER_REVIEW') NOT NULL,
  `created_date` datetime(6) NOT NULL,
  `insurance_company_name` varchar(255) DEFAULT NULL,
  `insurance_id` bigint NOT NULL,
  `invoice_number` varchar(255) DEFAULT NULL,
  `notes` varchar(255) DEFAULT NULL,
  `paid_amount` double DEFAULT NULL,
  `patient_id` bigint NOT NULL,
  `patient_name` varchar(255) NOT NULL,
  `policy_number` varchar(255) DEFAULT NULL,
  `processed_by` varchar(255) DEFAULT NULL,
  `rejection_reason` varchar(255) DEFAULT NULL,
  `review_date` datetime(6) DEFAULT NULL,
  `settlement_date` datetime(6) DEFAULT NULL,
  `submission_date` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKof9kk229mqg64vnicibnixo0w` (`claim_reference`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `insurance_claims`
--

LOCK TABLES `insurance_claims` WRITE;
/*!40000 ALTER TABLE `insurance_claims` DISABLE KEYS */;
/*!40000 ALTER TABLE `insurance_claims` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-08-16 18:05:17
