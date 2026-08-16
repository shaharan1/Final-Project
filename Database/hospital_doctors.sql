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
-- Table structure for table `doctors`
--

DROP TABLE IF EXISTS `doctors`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `doctors` (
  `consultation_fee` double DEFAULT NULL,
  `experience_years` int DEFAULT NULL,
  `follow_up_fee` double DEFAULT NULL,
  `join_date` date DEFAULT NULL,
  `doctor_department_id` bigint NOT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `available_days` varchar(255) DEFAULT NULL,
  `chamber` varchar(255) DEFAULT NULL,
  `designation` varchar(255) DEFAULT NULL,
  `duty_hours` varchar(255) DEFAULT NULL,
  `gender` varchar(255) DEFAULT NULL,
  `photo` varchar(255) DEFAULT NULL,
  `registration_number` varchar(255) DEFAULT NULL,
  `specialization` varchar(255) NOT NULL,
  `status` varchar(255) DEFAULT NULL,
  `study` varchar(255) DEFAULT NULL,
  `signature` longtext,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKt1f6cueqyjwx5ghew9ar1exe3` (`user_id`),
  UNIQUE KEY `UKbkgggqwkuor8s25yniqblxnag` (`registration_number`),
  KEY `FK2h7fim0kvqmul9gl0spldnpie` (`doctor_department_id`),
  CONSTRAINT `FK2h7fim0kvqmul9gl0spldnpie` FOREIGN KEY (`doctor_department_id`) REFERENCES `doctor_departments` (`id`),
  CONSTRAINT `FKe9pf5qtxxkdyrwibaevo9frtk` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `doctors`
--

LOCK TABLES `doctors` WRITE;
/*!40000 ALTER TABLE `doctors` DISABLE KEYS */;
INSERT INTO `doctors` VALUES (1000,12,500,'2023-01-15',1,1,1,'Sun, Mon, Tue','Room 201','Senior Consultant','9:00 AM - 3:00 PM','Male','https://randomuser.me/api/portraits/men/32.jpg','BMDC-10001','Cardiology','ACTIVE','MBBS, MD (Cardiology)',NULL),(1200,8,600,'2022-06-10',2,2,2,'Sun, Tue, Thu','Room 202','Consultant','10:00 AM - 4:00 PM','Female','https://randomuser.me/api/portraits/women/44.jpg','BMDC-10002','Neurology','ACTIVE','MBBS, FCPS (Neurology)',NULL),(900,10,450,'2021-09-01',3,3,3,'Mon, Wed, Fri','Room 203','Associate Consultant','8:00 AM - 2:00 PM','Male','https://randomuser.me/api/portraits/men/52.jpg','BMDC-10003','Orthopedics','ACTIVE','MBBS, MS (Orthopedics)',NULL),(1100,15,550,'2020-04-12',4,4,5,'Sun, Mon, Wed','Room 204','Senior Consultant','9:30 AM - 3:30 PM','Female','https://randomuser.me/api/portraits/women/68.jpg','BMDC-10004','Gynecology','ACTIVE','MBBS, FCPS (Gynecology)',NULL),(800,7,400,'2024-02-01',5,5,6,'Tue, Thu, Sat','Room 205','Consultant','11:00 AM - 5:00 PM','Male','https://randomuser.me/api/portraits/men/71.jpg','BMDC-10005','Pediatrics','ACTIVE','MBBS, DCH',NULL);
/*!40000 ALTER TABLE `doctors` ENABLE KEYS */;
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
