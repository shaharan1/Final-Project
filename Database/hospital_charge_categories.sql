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
-- Table structure for table `charge_categories`
--

DROP TABLE IF EXISTS `charge_categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `charge_categories` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `active` bit(1) NOT NULL,
  `code` varchar(50) NOT NULL,
  `default_unit_price` double DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(100) NOT NULL,
  `sort_order` int NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKfsbfsxcims82tvhu11x1tsjhu` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `charge_categories`
--

LOCK TABLES `charge_categories` WRITE;
/*!40000 ALTER TABLE `charge_categories` DISABLE KEYS */;
INSERT INTO `charge_categories` VALUES (1,_binary '','DISCHARGE',0,'Patient discharge processing fees','Discharge Fees',1),(2,_binary '','IMAGING',0,'X-Ray, CT Scan, MRI, Ultrasound charges','Imaging / Radiology',2),(3,_binary '','ADVANCE_DEPOSIT',0,'Patient advance deposit and prepayments','Advance Deposit',3),(4,_binary '','ADMINISTRATIVE',0,'Hospital administrative and registration fees','Administrative Fees',4),(5,_binary '','NURSING',0,'Specialized nursing care charges','Nursing Care',5),(6,_binary '','WARD_BED',0,'Daily room and bed rental charges','Ward / Bed Charges',6),(7,_binary '','SURGERY',0,'Surgical procedure charges','Surgery',7),(8,_binary '','OTHER',0,'Miscellaneous and unclassified charges','Other Charges',8),(9,_binary '','INSURANCE',0,'Insurance co-payment and coverage adjustments','Insurance Co-payment',9),(10,_binary '','BLOOD_BANK',0,'Blood and blood component charges','Blood Bank',10),(11,_binary '','PHYSIOTHERAPY',0,'Physiotherapy session charges','Physiotherapy',11),(12,_binary '','MEDICINE',0,'Pharmacy medicine charges','Medicine',12),(13,_binary '','DIET_MEALS',0,'Patient diet plan and meal charges','Diet / Meals',13),(14,_binary '','PROCEDURE',0,'Minor procedures and medical supplies','Procedure / Supplies',14),(15,_binary '','ADMISSION',0,'Patient admission processing fees','Admission Fees',15),(16,_binary '','ROOM_SERVICE',0,'Additional room service and amenities','Room Service',16),(17,_binary '','OXYGEN',0,'Oxygen therapy and respiratory support charges','Oxygen / Respiratory',17),(18,_binary '','EMERGENCY',0,'Emergency department service charges','Emergency Services',18),(19,_binary '','ICU_CCU',0,'Intensive Care / Coronary Care Unit charges','ICU / CCU',19),(20,_binary '','LAB_TEST',0,'Diagnostic laboratory test charges','Lab Tests',20),(21,_binary '','DOCTOR_CONSULTATION',0,'Physician consultation and visit fees','Doctor Consultation',21),(22,_binary '','AMBULANCE',0,'Ambulance service and transport charges','Ambulance',22);
/*!40000 ALTER TABLE `charge_categories` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-08-16 18:05:12
