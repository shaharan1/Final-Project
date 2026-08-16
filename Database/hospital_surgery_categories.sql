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
-- Table structure for table `surgery_categories`
--

DROP TABLE IF EXISTS `surgery_categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `surgery_categories` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `active` bit(1) NOT NULL,
  `code` varchar(50) NOT NULL,
  `created_date` datetime(6) NOT NULL,
  `description` varchar(500) DEFAULT NULL,
  `last_updated` datetime(6) NOT NULL,
  `name` varchar(100) NOT NULL,
  `sort_order` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKqaf4p2c8cn3beor3s6wme1oce` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `surgery_categories`
--

LOCK TABLES `surgery_categories` WRITE;
/*!40000 ALTER TABLE `surgery_categories` DISABLE KEYS */;
INSERT INTO `surgery_categories` VALUES (1,_binary '','PLASTIC','2026-08-06 16:02:01.211811','Reconstructive and cosmetic surgical procedures','2026-08-06 16:02:01.211811','Plastic Surgery',1),(2,_binary '','GYNAECOLOGY','2026-08-06 16:02:01.243836','Female reproductive system and childbirth procedures','2026-08-06 16:02:01.243836','Gynaecology & Obstetrics',2),(3,_binary '','ENT','2026-08-06 16:02:01.253858','Ear, nose and throat surgical procedures','2026-08-06 16:02:01.254851','ENT Surgery',3),(4,_binary '','DENTAL','2026-08-06 16:02:01.264840','Oral and maxillofacial surgical procedures','2026-08-06 16:02:01.264840','Dental Surgery',4),(5,_binary '','UROLOGY','2026-08-06 16:02:01.275816','Urinary tract and male reproductive procedures','2026-08-06 16:02:01.275816','Urology Surgery',5),(6,_binary '','NEURO','2026-08-06 16:02:01.284814','Brain, spine and nervous system procedures','2026-08-06 16:02:01.284814','Neurosurgery',6),(7,_binary '','CARDIAC','2026-08-06 16:02:01.291838','Heart and cardiovascular surgical procedures','2026-08-06 16:02:01.291838','Cardiac Surgery',7),(8,_binary '','ORTHOPEDIC','2026-08-06 16:02:01.297812','Bone, joint and musculoskeletal procedures','2026-08-06 16:02:01.297812','Orthopedic Surgery',8),(9,_binary '','GENERAL','2026-08-06 16:02:01.304813','Common abdominal and general surgical procedures','2026-08-06 16:02:01.304813','General Surgery',9),(10,_binary '','EYE','2026-08-06 16:02:01.309813','Ophthalmic surgical procedures','2026-08-06 16:02:01.309813','Eye Surgery',10);
/*!40000 ALTER TABLE `surgery_categories` ENABLE KEYS */;
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
