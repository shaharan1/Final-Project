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
-- Table structure for table `billing_invoice_items`
--

DROP TABLE IF EXISTS `billing_invoice_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `billing_invoice_items` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `amount` double DEFAULT NULL,
  `category_code` varchar(50) NOT NULL,
  `created_date` datetime(6) NOT NULL,
  `description` varchar(255) NOT NULL,
  `discount_amount` double DEFAULT NULL,
  `discount_percent` double DEFAULT NULL,
  `item_status` varchar(50) DEFAULT NULL,
  `quantity` int NOT NULL,
  `source_id` bigint DEFAULT NULL,
  `source_module` varchar(100) DEFAULT NULL,
  `unit_price` double NOT NULL,
  `charge_category_id` bigint NOT NULL,
  `invoice_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKx88qh43blln23b52v9oyoad5` (`charge_category_id`),
  KEY `FKbqo6shbgxpj64rmye70ao690r` (`invoice_id`),
  CONSTRAINT `FKbqo6shbgxpj64rmye70ao690r` FOREIGN KEY (`invoice_id`) REFERENCES `billing_invoices` (`id`),
  CONSTRAINT `FKx88qh43blln23b52v9oyoad5` FOREIGN KEY (`charge_category_id`) REFERENCES `charge_categories` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=52 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `billing_invoice_items`
--

LOCK TABLES `billing_invoice_items` WRITE;
/*!40000 ALTER TABLE `billing_invoice_items` DISABLE KEYS */;
INSERT INTO `billing_invoice_items` VALUES (21,33000,'WARD_BED','2026-08-02 17:37:20.868302','Private Cabin - Bed 11 (11 days @ ৳3000.0/day)',0,0,'ACTIVE',11,2,'ADMISSION',3000,6,1),(22,7000,'WARD_BED','2026-08-02 17:48:01.960835','General Ward A - Bed 1 (7 days @ ৳1000.0/day)',0,0,'ACTIVE',7,5,'ADMISSION',1000,6,2),(23,33000,'WARD_BED','2026-08-03 15:04:48.849737','Private Cabin - Bed 12 (11 days @ ৳3000.0/day)',0,0,'ACTIVE',11,4,'ADMISSION',3000,6,5),(24,500,'LAB_TEST','2026-08-03 15:04:48.853129','Complete Blood Count (CBC) (Order #3)',0,0,'ACTIVE',1,3,'LAB',500,20,5),(25,14000,'DIET_MEALS','2026-08-03 15:04:48.854098','Diabetic Diet Plan (2026-07-02 to 2026-07-30)',0,0,'ACTIVE',28,2,'DIET',500,13,5),(45,11000,'WARD_BED','2026-08-06 16:49:20.324551','General Ward A - Bed 1 (11 days @ ৳1000.0/day)',0,0,'ACTIVE',11,5,'ADMISSION',1000,6,6),(46,250,'LAB_TEST','2026-08-06 16:49:20.326550','Blood Sugar (Fasting) (Order #10)',0,0,'ACTIVE',1,10,'LAB',250,20,6),(47,1500,'LAB_TEST','2026-08-06 16:49:20.327550','Dengue NS1 Antigen (Order #11)',0,0,'ACTIVE',1,11,'LAB',1500,20,6),(48,1000,'LAB_TEST','2026-08-06 16:49:20.328558','Kidney Function Test (KFT) (Order #12)',0,0,'ACTIVE',1,12,'LAB',1000,20,6),(49,7500,'DIET_MEALS','2026-08-06 16:49:20.329550','Low Salt Cardiac Diet (2026-07-05 to 2026-07-20)',0,0,'ACTIVE',15,3,'DIET',500,13,6),(50,2500,'DIET_MEALS','2026-08-06 16:49:20.329550','Diabetic Diet Plan (2026-08-02 to 2026-08-07)',0,0,'ACTIVE',5,7,'DIET',500,13,6),(51,114345,'SURGERY','2026-08-16 17:13:56.688074','Surgery - Percutaneous Nephrolithotomy (PCNL) (SURG-20260816-001)',0,0,'ACTIVE',1,2,'SURGERY',114345,7,7);
/*!40000 ALTER TABLE `billing_invoice_items` ENABLE KEYS */;
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
