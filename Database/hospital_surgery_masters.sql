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
-- Table structure for table `surgery_masters`
--

DROP TABLE IF EXISTS `surgery_masters`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `surgery_masters` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `active` bit(1) NOT NULL,
  `anesthesia_charge` double DEFAULT NULL,
  `consumable_charge` double DEFAULT NULL,
  `created_date` datetime(6) NOT NULL,
  `equipment_charge` double DEFAULT NULL,
  `estimated_duration_min` int DEFAULT NULL,
  `icu_charge` double DEFAULT NULL,
  `last_updated` datetime(6) NOT NULL,
  `notes` varchar(500) DEFAULT NULL,
  `nursing_charge` double DEFAULT NULL,
  `ot_charge` double DEFAULT NULL,
  `package_rate` double DEFAULT NULL,
  `standard_rate` double NOT NULL,
  `surgery_code` varchar(50) NOT NULL,
  `surgery_name` varchar(150) NOT NULL,
  `category_id` bigint DEFAULT NULL,
  `assistant_surgeon_fee` double DEFAULT NULL,
  `laboratory_charge` double DEFAULT NULL,
  `medicine_charge` double DEFAULT NULL,
  `radiology_charge` double DEFAULT NULL,
  `surgeon_fee` double DEFAULT NULL,
  `ward_cabin_charge` double DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKmltwh5f5qmqau8u7qr0ls0t2w` (`surgery_code`),
  KEY `FKgfo0e2t8utienjlwmi50d24o4` (`category_id`),
  CONSTRAINT `FKgfo0e2t8utienjlwmi50d24o4` FOREIGN KEY (`category_id`) REFERENCES `surgery_categories` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `surgery_masters`
--

LOCK TABLES `surgery_masters` WRITE;
/*!40000 ALTER TABLE `surgery_masters` DISABLE KEYS */;
INSERT INTO `surgery_masters` VALUES (1,_binary '',12000,10000,'2026-08-06 16:02:01.317841',15000,180,15000,'2026-08-06 16:02:01.317841',NULL,8000,15000,145000,75000,'LAMINECTOMY','Laminectomy',6,NULL,NULL,NULL,NULL,NULL,NULL),(2,_binary '',4000,2500,'2026-08-06 16:02:01.329864',4000,60,3000,'2026-08-06 16:02:01.329864',NULL,3000,5000,48000,30000,'STRABISMUS','Strabismus Correction',10,NULL,NULL,NULL,NULL,NULL,NULL),(3,_binary '',5000,3000,'2026-08-06 16:02:01.339815',4000,90,3500,'2026-08-06 16:02:01.339815',NULL,3000,6000,45000,25000,'JAW_FRACTURE','Open Reduction of Jaw Fracture',4,NULL,NULL,NULL,NULL,NULL,NULL),(4,_binary '',4000,2500,'2026-08-06 16:02:01.347816',3000,60,3000,'2026-08-06 16:02:01.347816',NULL,3000,5000,40000,22000,'SEPTOPLASTY','Septoplasty',3,NULL,NULL,NULL,NULL,NULL,NULL),(5,_binary '',8000,5000,'2026-08-06 16:02:01.357812',8000,120,6000,'2026-08-06 16:02:01.357812',NULL,5000,10000,80000,45000,'HYSTERECTOMY','Total Abdominal Hysterectomy',2,NULL,NULL,NULL,NULL,NULL,NULL),(6,_binary '',20000,20000,'2026-08-06 16:02:01.366814',30000,300,25000,'2026-08-08 15:52:51.290556','',15000,25000,270000,140000,'VALVE_REPLACEMENT','Heart Valve Replacement',7,0,0,5000,0,20000,0),(7,_binary '',10000,8000,'2026-08-06 16:02:01.374838',12000,120,8000,'2026-08-06 16:02:01.374838',NULL,6000,12000,115000,65000,'PCNL','Percutaneous Nephrolithotomy (PCNL)',5,NULL,NULL,NULL,NULL,NULL,NULL),(8,_binary '',8000,4000,'2026-08-06 16:02:01.380842',8000,120,8000,'2026-08-06 16:02:01.380842',NULL,5000,10000,80000,45000,'SPLENECTOMY','Splenectomy',9,NULL,NULL,NULL,NULL,NULL,NULL),(9,_binary '',6000,4000,'2026-08-06 16:02:01.387813',6000,90,5000,'2026-08-06 16:02:01.387813',NULL,4000,8000,58000,30000,'GRAFTING','Skin Grafting',1,NULL,NULL,NULL,NULL,NULL,NULL),(10,_binary '',8000,6000,'2026-08-06 16:02:01.393812',8000,120,6000,'2026-08-06 16:02:01.393812',NULL,6000,10000,70000,35000,'FIXATION_FRACTURE','Internal Fixation of Fracture',8,NULL,NULL,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `surgery_masters` ENABLE KEYS */;
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
