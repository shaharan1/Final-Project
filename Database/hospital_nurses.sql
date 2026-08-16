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
-- Table structure for table `nurses`
--

DROP TABLE IF EXISTS `nurses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `nurses` (
  `active` bit(1) DEFAULT NULL,
  `experience_years` int DEFAULT NULL,
  `join_date` date DEFAULT NULL,
  `on_duty` bit(1) DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `address` varchar(300) DEFAULT NULL,
  `assigned_ward` varchar(255) DEFAULT NULL,
  `photo` varchar(255) DEFAULT NULL,
  `qualification` varchar(255) DEFAULT NULL,
  `registration_number` varchar(255) DEFAULT NULL,
  `remarks` varchar(255) DEFAULT NULL,
  `working_hours` varchar(255) DEFAULT NULL,
  `gender` enum('FEMALE','MALE','OTHER') DEFAULT NULL,
  `nurse_type` enum('EMERGENCY_NURSE','ICU_NURSE','OT_NURSE','SENIOR_NURSE','STAFF_NURSE') DEFAULT NULL,
  `shift` enum('EVENING','MORNING','NIGHT') DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKml2dkuvlx0yrr9ajhb9hx5sxl` (`user_id`),
  CONSTRAINT `FK91rtea8eoy5devpkpwuqsjk7c` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nurses`
--

LOCK TABLES `nurses` WRITE;
/*!40000 ALTER TABLE `nurses` DISABLE KEYS */;
INSERT INTO `nurses` VALUES (_binary '',5,'2022-01-15',_binary '',11,17,'Dhanmondi, Dhaka','General Male Ward','F:\\JEE69\\Final-Project\\SPRING\\Assets\\Nurses\\Sarah Ahmed.webp','B.Sc in Nursing','BNMC-10001','Senior Staff Nurse','8','FEMALE','STAFF_NURSE','MORNING'),(_binary '',5,'2020-01-01',_binary '',12,39,'Dhanmondi, Dhaka','General Ward','','B.Sc in Nursing','BNMC-N-10001','Experienced in patient care.','08:00 AM - 04:00 PM','FEMALE','STAFF_NURSE','MORNING'),(_binary '',4,'2021-01-03',_binary '',13,40,'House 18, Road 5, Dhanmondi, Dhaka','General Ward','','B.Sc in Nursing','BNMC-250101','Excellent communication and patient care.','08:00 AM - 04:00 PM','FEMALE','STAFF_NURSE','MORNING'),(_binary '',8,'2020-01-01',_binary '',14,41,'Uttara Sector 11, Dhaka','ICU','','M.Sc in Nursing','BNMC-250102','Critical care specialist.','10:00 PM - 06:00 AM','FEMALE','ICU_NURSE','NIGHT');
/*!40000 ALTER TABLE `nurses` ENABLE KEYS */;
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
