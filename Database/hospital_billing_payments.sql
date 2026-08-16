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
-- Table structure for table `billing_payments`
--

DROP TABLE IF EXISTS `billing_payments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `billing_payments` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `amount` double NOT NULL,
  `bank_name` varchar(100) DEFAULT NULL,
  `card_last4` varchar(100) DEFAULT NULL,
  `insurance_coverage` double DEFAULT NULL,
  `mobile_provider` varchar(100) DEFAULT NULL,
  `notes` varchar(500) DEFAULT NULL,
  `payment_date` datetime(6) NOT NULL,
  `payment_method` enum('BANK_TRANSFER','CARD','CASH','INSURANCE','MOBILE_BANKING','ONLINE_PAYMENT','SPLIT') NOT NULL,
  `payment_status` enum('COMPLETED','FAILED','PARTIAL','PENDING','REFUNDED') NOT NULL,
  `processed_by` varchar(100) DEFAULT NULL,
  `self_pay_amount` double DEFAULT NULL,
  `transaction_id` varchar(100) DEFAULT NULL,
  `insurance_company_id` bigint DEFAULT NULL,
  `invoice_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK90si5brwilotxtl2028ctvoxh` (`insurance_company_id`),
  KEY `FKbnlhbn1vcu67g368sklpnd27u` (`invoice_id`),
  CONSTRAINT `FK90si5brwilotxtl2028ctvoxh` FOREIGN KEY (`insurance_company_id`) REFERENCES `insurance` (`id`),
  CONSTRAINT `FKbnlhbn1vcu67g368sklpnd27u` FOREIGN KEY (`invoice_id`) REFERENCES `billing_invoices` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `billing_payments`
--

LOCK TABLES `billing_payments` WRITE;
/*!40000 ALTER TABLE `billing_payments` DISABLE KEYS */;
INSERT INTO `billing_payments` VALUES (1,5000,NULL,NULL,0,NULL,'df','2026-08-02 17:35:45.564243','CASH','COMPLETED','Admin',5000,'',NULL,2),(2,2145,NULL,NULL,0,NULL,'','2026-08-02 17:38:01.421694','CASH','COMPLETED','Admin',2145,'',NULL,1),(3,1020,NULL,NULL,0,NULL,'','2026-08-02 17:48:25.578421','CASH','COMPLETED','Admin',1020,'',NULL,2),(4,12000,NULL,NULL,0,NULL,'ghfj','2026-08-02 17:55:27.525001','CASH','COMPLETED','Admin',12000,'',NULL,1),(5,5000,NULL,NULL,0,NULL,'','2026-08-16 17:09:38.409705','CASH','COMPLETED','Admin',5000,'',NULL,1);
/*!40000 ALTER TABLE `billing_payments` ENABLE KEYS */;
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
