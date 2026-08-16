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
-- Table structure for table `emergency_billing`
--

DROP TABLE IF EXISTS `emergency_billing`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `emergency_billing` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `advance_paid` double DEFAULT NULL,
  `ambulance_charge` double DEFAULT NULL,
  `bed_charge` double DEFAULT NULL,
  `bill_number` varchar(255) NOT NULL,
  `consultation_fee` double DEFAULT NULL,
  `consumables_charge` double DEFAULT NULL,
  `created_at` datetime(6) NOT NULL,
  `discount_amount` double DEFAULT NULL,
  `discount_percent` double DEFAULT NULL,
  `doctor_fee` double DEFAULT NULL,
  `due_amount` double DEFAULT NULL,
  `grand_total` double DEFAULT NULL,
  `insurance_coverage` double DEFAULT NULL,
  `insurance_policy_number` varchar(255) DEFAULT NULL,
  `insurance_provider` varchar(255) DEFAULT NULL,
  `is_insurance_claimed` bit(1) DEFAULT NULL,
  `lab_charge` double DEFAULT NULL,
  `medicine_charge` double DEFAULT NULL,
  `notes` text,
  `nursing_charge` double DEFAULT NULL,
  `operation_charge` double DEFAULT NULL,
  `other_charges` double DEFAULT NULL,
  `paid_at` datetime(6) DEFAULT NULL,
  `payment_status` varchar(255) DEFAULT NULL,
  `procedure_charge` double DEFAULT NULL,
  `radiology_charge` double DEFAULT NULL,
  `registration_fee` double DEFAULT NULL,
  `status` varchar(255) NOT NULL,
  `subtotal` double DEFAULT NULL,
  `updated_at` datetime(6) NOT NULL,
  `vat_amount` double DEFAULT NULL,
  `vat_percent` double DEFAULT NULL,
  `emergency_patient_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKpjdcsb8nibhfnhaxu3il6gxxl` (`bill_number`),
  KEY `FK81665pqt1mrlc4ioc91ng4fbi` (`emergency_patient_id`),
  CONSTRAINT `FK81665pqt1mrlc4ioc91ng4fbi` FOREIGN KEY (`emergency_patient_id`) REFERENCES `emergency_patients` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `emergency_billing`
--

LOCK TABLES `emergency_billing` WRITE;
/*!40000 ALTER TABLE `emergency_billing` DISABLE KEYS */;
/*!40000 ALTER TABLE `emergency_billing` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-08-16 18:05:13
