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
-- Table structure for table `medicine_stocks`
--

DROP TABLE IF EXISTS `medicine_stocks`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `medicine_stocks` (
  `expiry_date` date NOT NULL,
  `purchase_price` double NOT NULL,
  `sale_price` double NOT NULL,
  `stock_quantity` int NOT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `supplier_id` bigint NOT NULL,
  `batch_number` varchar(50) NOT NULL,
  `medicine_name` varchar(150) NOT NULL,
  `generic_name` varchar(255) DEFAULT NULL,
  `active` bit(1) NOT NULL,
  `barcode` varchar(255) DEFAULT NULL,
  `created_date` datetime(6) NOT NULL,
  `damaged_quantity` int DEFAULT NULL,
  `dosage_form` varchar(255) DEFAULT NULL,
  `manufacturing_date` date NOT NULL,
  `minimum_stock_level` int DEFAULT NULL,
  `reorder_level` int DEFAULT NULL,
  `reserved_quantity` int DEFAULT NULL,
  `strength` varchar(255) DEFAULT NULL,
  `vat` double DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK4e5fv63ojjf0yye4ddejw5sfu` (`supplier_id`),
  CONSTRAINT `FK4e5fv63ojjf0yye4ddejw5sfu` FOREIGN KEY (`supplier_id`) REFERENCES `suppliers` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `medicine_stocks`
--

LOCK TABLES `medicine_stocks` WRITE;
/*!40000 ALTER TABLE `medicine_stocks` DISABLE KEYS */;
INSERT INTO `medicine_stocks` VALUES ('2028-01-01',0.8,1.2,88,1,1,'BAT-5485 4547 1254','Napa','Paracitamol',_binary '','BAR4789','2026-08-08 18:37:39.013848',0,'Tablet','2026-01-01',10,50,0,'500mg',5),('2028-08-01',6,7,90,2,1,'BAT-4578-249','Losectil','Omeprazole',_binary '','BAR47895','2026-08-08 18:45:53.266401',0,'Tablet','2026-08-01',10,10,0,'20mg',5);
/*!40000 ALTER TABLE `medicine_stocks` ENABLE KEYS */;
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
