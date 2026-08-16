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
-- Table structure for table `payments`
--

DROP TABLE IF EXISTS `payments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payments` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `vat` double DEFAULT NULL,
  `amount` double NOT NULL,
  `bank_name` varchar(255) DEFAULT NULL,
  `card_last4` varchar(255) DEFAULT NULL,
  `created_date` datetime(6) NOT NULL,
  `discount` double DEFAULT NULL,
  `insurance_company_id` bigint DEFAULT NULL,
  `insurance_coverage` double DEFAULT NULL,
  `invoice_number` varchar(255) NOT NULL,
  `mobile_provider` varchar(255) DEFAULT NULL,
  `net_amount` double DEFAULT NULL,
  `notes` varchar(255) DEFAULT NULL,
  `patient_id` bigint NOT NULL,
  `patient_name` varchar(255) NOT NULL,
  `payment_date` datetime(6) NOT NULL,
  `payment_method` enum('BANK_TRANSFER','CARD','CASH','INSURANCE','MOBILE_BANKING','ONLINE_PAYMENT','SPLIT') NOT NULL,
  `payment_reference` varchar(255) NOT NULL,
  `payment_status` enum('COMPLETED','FAILED','PARTIAL','PENDING','REFUNDED') NOT NULL,
  `processed_by` varchar(255) DEFAULT NULL,
  `refund_amount` double DEFAULT NULL,
  `self_pay_amount` double DEFAULT NULL,
  `transaction_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK4jacl30fsqtdp5mhmg5wnvn7q` (`payment_reference`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payments`
--

LOCK TABLES `payments` WRITE;
/*!40000 ALTER TABLE `payments` DISABLE KEYS */;
INSERT INTO `payments` VALUES (1,NULL,1416,NULL,NULL,'2026-07-28 18:20:23.814174',NULL,NULL,NULL,'BIL-280726-3947',NULL,NULL,'',3,'Pk','2026-07-28 18:20:23.813408','CASH','PAY-2026-0001','COMPLETED',NULL,NULL,NULL,''),(2,NULL,2360,NULL,NULL,'2026-08-02 16:12:18.542721',NULL,NULL,NULL,'BIL-020826-8816',NULL,NULL,'',6,'Md.Miskat','2026-08-02 16:12:18.541707','CASH','PAY-2026-0002','COMPLETED',NULL,NULL,NULL,'');
/*!40000 ALTER TABLE `payments` ENABLE KEYS */;
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
