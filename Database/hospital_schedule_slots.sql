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
-- Table structure for table `schedule_slots`
--

DROP TABLE IF EXISTS `schedule_slots`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `schedule_slots` (
  `date` date NOT NULL,
  `end_time` time NOT NULL,
  `is_booked` bit(1) NOT NULL,
  `start_time` time NOT NULL,
  `doctor_id` bigint NOT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`),
  KEY `FK6m0hypgh2x7qfda0l38bpmegu` (`doctor_id`),
  CONSTRAINT `FK6m0hypgh2x7qfda0l38bpmegu` FOREIGN KEY (`doctor_id`) REFERENCES `doctors` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `schedule_slots`
--

LOCK TABLES `schedule_slots` WRITE;
/*!40000 ALTER TABLE `schedule_slots` DISABLE KEYS */;
INSERT INTO `schedule_slots` VALUES ('2026-07-13','19:10:00',_binary '','19:00:00',5,1),('2026-07-24','22:18:00',_binary '','10:16:00',2,2),('2026-07-26','22:00:00',_binary '','22:00:00',1,3),('2026-07-26','22:10:00',_binary '','22:00:00',2,4),('2026-07-26','22:20:00',_binary '','22:10:00',1,5),('2026-07-27','22:10:00',_binary '','22:00:00',1,6),('2026-07-27','22:40:00',_binary '','22:30:00',1,7),('2026-07-30','10:10:00',_binary '\0','10:00:00',4,8),('2026-07-30','10:20:00',_binary '','10:10:00',2,9),('2026-07-30','10:10:00',_binary '','10:00:00',1,10),('2026-07-30','10:40:00',_binary '','10:30:00',1,11),('2026-07-30','09:10:00',_binary '','09:00:00',2,12),('2026-07-30','10:10:00',_binary '\0','10:00:00',3,13),('2026-07-30','10:40:00',_binary '\0','10:30:00',3,14),('2026-07-30','09:10:00',_binary '\0','09:00:00',4,15),('2026-07-30','09:10:00',_binary '','09:00:00',5,16),('2026-07-30','10:10:00',_binary '\0','10:00:00',5,17),('2026-08-18','09:10:00',_binary '','09:00:00',1,18),('2026-08-18','09:40:00',_binary '\0','09:30:00',1,19),('2026-08-18','10:10:00',_binary '\0','10:00:00',2,20),('2026-08-18','10:40:00',_binary '\0','10:30:00',2,21),('2026-08-18','10:10:00',_binary '\0','10:00:00',3,22),('2026-08-18','09:10:00',_binary '\0','09:00:00',4,23),('2026-08-18','10:10:00',_binary '\0','10:00:00',5,24);
/*!40000 ALTER TABLE `schedule_slots` ENABLE KEYS */;
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
