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
-- Table structure for table `meal_schedules`
--

DROP TABLE IF EXISTS `meal_schedules`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `meal_schedules` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `cancelled_orders` int DEFAULT NULL,
  `completed_orders` int DEFAULT NULL,
  `created_at` datetime(6) NOT NULL,
  `is_current_meal` bit(1) DEFAULT NULL,
  `meal_name` varchar(50) NOT NULL,
  `notes` text,
  `pending_orders` int DEFAULT NULL,
  `preparation_end_time` time DEFAULT NULL,
  `preparation_start_time` time NOT NULL,
  `serving_time` time NOT NULL,
  `status` varchar(255) NOT NULL,
  `total_orders_today` int NOT NULL,
  `updated_at` datetime(6) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `meal_schedules`
--

LOCK TABLES `meal_schedules` WRITE;
/*!40000 ALTER TABLE `meal_schedules` DISABLE KEYS */;
INSERT INTO `meal_schedules` VALUES (1,2,40,'2026-07-28 23:20:46.892155',_binary '','Breakfast','Standard hospital breakfast',3,'06:45:00','05:30:00','07:00:00','ACTIVE',45,'2026-07-28 23:20:46.892155'),(2,1,28,'2026-07-28 23:20:46.896866',_binary '\0','Morning Snacks','Light mid-morning refreshment',1,'10:15:00','10:00:00','10:30:00','ACTIVE',30,'2026-07-28 23:20:46.896866'),(3,2,58,'2026-07-28 23:20:46.902658',_binary '\0','Lunch','Main meal of the day',5,'12:15:00','11:00:00','12:30:00','ACTIVE',65,'2026-07-28 23:20:46.902658'),(4,1,32,'2026-07-28 23:20:46.906510',_binary '\0','Evening Snacks','Afternoon tea and snacks',2,'15:45:00','15:30:00','16:00:00','ACTIVE',35,'2026-07-28 23:20:46.906510'),(5,1,50,'2026-07-28 23:20:46.910887',_binary '\0','Dinner','Light dinner meal',4,'18:45:00','17:30:00','19:00:00','ACTIVE',55,'2026-07-28 23:20:46.910887'),(6,1,18,'2026-07-28 23:20:46.915184',_binary '\0','Night Diet','Supplementary night nutrition',1,'21:45:00','21:30:00','22:00:00','ACTIVE',20,'2026-07-28 23:20:46.915184');
/*!40000 ALTER TABLE `meal_schedules` ENABLE KEYS */;
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
