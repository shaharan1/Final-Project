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
-- Table structure for table `kitchen_orders`
--

DROP TABLE IF EXISTS `kitchen_orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `kitchen_orders` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `bed_number` varchar(255) DEFAULT NULL,
  `cancelled_at` datetime(6) DEFAULT NULL,
  `cooking_at` datetime(6) DEFAULT NULL,
  `created_at` datetime(6) NOT NULL,
  `delivered_at` datetime(6) DEFAULT NULL,
  `delivered_by` varchar(255) DEFAULT NULL,
  `diet_type` varchar(255) NOT NULL,
  `kitchen_notes` text,
  `meal_time` varchar(255) NOT NULL,
  `meal_type` varchar(255) NOT NULL,
  `order_number` varchar(255) NOT NULL,
  `prepared_by` varchar(255) DEFAULT NULL,
  `preparing_at` datetime(6) DEFAULT NULL,
  `priority` varchar(255) NOT NULL,
  `ready_at` datetime(6) DEFAULT NULL,
  `special_diet` bit(1) DEFAULT NULL,
  `status` varchar(255) NOT NULL,
  `updated_at` datetime(6) NOT NULL,
  `admitted_patient_id` bigint DEFAULT NULL,
  `diet_assignment_id` bigint DEFAULT NULL,
  `diet_plan_id` bigint NOT NULL,
  `patient_id` bigint NOT NULL,
  `ward_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK5caq8263d09j5yxh9pr4bcr9n` (`order_number`),
  KEY `FK4ij2o5644krg7jbseolis95gv` (`admitted_patient_id`),
  KEY `FKe9t5qh5ew1fxyhxvsvgr5t0d8` (`diet_assignment_id`),
  KEY `FK90sqkibxvocjydxa72rtifo8g` (`diet_plan_id`),
  KEY `FKl9o809euk28p5njx7gnhfxafl` (`patient_id`),
  KEY `FKgwe7biwo6ll2xeod404lpolb` (`ward_id`),
  CONSTRAINT `FK4ij2o5644krg7jbseolis95gv` FOREIGN KEY (`admitted_patient_id`) REFERENCES `admitted_patients` (`id`),
  CONSTRAINT `FK90sqkibxvocjydxa72rtifo8g` FOREIGN KEY (`diet_plan_id`) REFERENCES `diet_plans` (`id`),
  CONSTRAINT `FKe9t5qh5ew1fxyhxvsvgr5t0d8` FOREIGN KEY (`diet_assignment_id`) REFERENCES `diet_assignments` (`id`),
  CONSTRAINT `FKgwe7biwo6ll2xeod404lpolb` FOREIGN KEY (`ward_id`) REFERENCES `wards` (`id`),
  CONSTRAINT `FKl9o809euk28p5njx7gnhfxafl` FOREIGN KEY (`patient_id`) REFERENCES `patients` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `kitchen_orders`
--

LOCK TABLES `kitchen_orders` WRITE;
/*!40000 ALTER TABLE `kitchen_orders` DISABLE KEYS */;
/*!40000 ALTER TABLE `kitchen_orders` ENABLE KEYS */;
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
