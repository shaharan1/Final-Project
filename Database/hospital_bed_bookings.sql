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
-- Table structure for table `bed_bookings`
--

DROP TABLE IF EXISTS `bed_bookings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bed_bookings` (
  `active` bit(1) NOT NULL,
  `admitted_patient_id` bigint DEFAULT NULL,
  `bed_id` bigint NOT NULL,
  `emergency_patient_id` bigint DEFAULT NULL,
  `end_time` datetime(6) DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `start_time` datetime(6) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK4xso2lb6lvww2ny7ujxbwvvyy` (`admitted_patient_id`),
  KEY `FK472cgissype8afuny2sjqxw0t` (`bed_id`),
  KEY `FKg0xdql80gbohulog80mckq0ny` (`emergency_patient_id`),
  CONSTRAINT `FK472cgissype8afuny2sjqxw0t` FOREIGN KEY (`bed_id`) REFERENCES `beds` (`id`),
  CONSTRAINT `FK4xso2lb6lvww2ny7ujxbwvvyy` FOREIGN KEY (`admitted_patient_id`) REFERENCES `admitted_patients` (`id`),
  CONSTRAINT `FKg0xdql80gbohulog80mckq0ny` FOREIGN KEY (`emergency_patient_id`) REFERENCES `emergency_patients` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bed_bookings`
--

LOCK TABLES `bed_bookings` WRITE;
/*!40000 ALTER TABLE `bed_bookings` DISABLE KEYS */;
INSERT INTO `bed_bookings` VALUES (_binary '\0',1,1,NULL,'2026-07-23 01:59:50.243760',1,'2026-07-23 01:49:31.496548'),(_binary '',2,11,NULL,NULL,2,'2026-07-23 02:00:43.721848'),(_binary '',3,8,NULL,NULL,3,'2026-07-23 15:17:44.112886'),(_binary '',4,12,NULL,NULL,4,'2026-07-23 18:18:53.628680'),(_binary '',5,1,NULL,NULL,5,'2026-07-26 18:15:25.617179'),(_binary '',6,2,NULL,NULL,6,'2026-07-27 17:25:29.209425'),(_binary '',7,5,NULL,NULL,7,'2026-07-28 16:33:11.451147'),(_binary '',8,6,NULL,NULL,8,'2026-08-02 16:06:29.823578');
/*!40000 ALTER TABLE `bed_bookings` ENABLE KEYS */;
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
