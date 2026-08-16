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
-- Table structure for table `pharmacy_sale_items`
--

DROP TABLE IF EXISTS `pharmacy_sale_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pharmacy_sale_items` (
  `quantity` int DEFAULT NULL,
  `subtotal` double DEFAULT NULL,
  `unit_price` double DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `medicine_stock_id` bigint NOT NULL,
  `pharmacy_sale_id` bigint NOT NULL,
  `batch_number` varchar(255) DEFAULT NULL,
  `discount` double NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKluatsr5w15t1igk7w4vyo1ou` (`medicine_stock_id`),
  KEY `FKfwrk550wmwwwbbtdodcd70e54` (`pharmacy_sale_id`),
  CONSTRAINT `FKfwrk550wmwwwbbtdodcd70e54` FOREIGN KEY (`pharmacy_sale_id`) REFERENCES `pharmacy_sales` (`id`),
  CONSTRAINT `FKluatsr5w15t1igk7w4vyo1ou` FOREIGN KEY (`medicine_stock_id`) REFERENCES `medicine_stocks` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pharmacy_sale_items`
--

LOCK TABLES `pharmacy_sale_items` WRITE;
/*!40000 ALTER TABLE `pharmacy_sale_items` DISABLE KEYS */;
INSERT INTO `pharmacy_sale_items` VALUES (1,12,12,1,1,3,NULL,0),(1,12,12,2,1,4,NULL,0),(10,12,1.2,3,1,5,NULL,0),(10,70,7,4,2,5,NULL,0);
/*!40000 ALTER TABLE `pharmacy_sale_items` ENABLE KEYS */;
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
