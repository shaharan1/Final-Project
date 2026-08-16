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
-- Table structure for table `test_masters`
--

DROP TABLE IF EXISTS `test_masters`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `test_masters` (
  `active` bit(1) DEFAULT NULL,
  `standard_price` double NOT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `test_code` varchar(50) NOT NULL,
  `test_name` varchar(150) NOT NULL,
  `normal_range` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKgmxv0vq5near5rykmpjmcir36` (`test_code`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `test_masters`
--

LOCK TABLES `test_masters` WRITE;
/*!40000 ALTER TABLE `test_masters` DISABLE KEYS */;
INSERT INTO `test_masters` VALUES (NULL,500,1,'CBC001','Complete Blood Count (CBC)','Normal'),(NULL,250,2,'BSF002','Blood Sugar (Fasting)','70-99 mg/dL'),(NULL,250,3,'BSR003','Blood Sugar (Random)','80-140 mg/dL'),(NULL,800,4,'HBA004','HbA1c','4.0-5.6 %'),(NULL,1200,5,'LFT005','Liver Function Test (LFT)','Normal'),(NULL,1000,6,'KFT006','Kidney Function Test (KFT)','Normal'),(NULL,1500,7,'LIP007','Lipid Profile','Normal'),(NULL,900,8,'TSH008','TSH','0.4-4.0 mIU/L'),(NULL,700,9,'T3009','T3','80-200 ng/dL'),(NULL,700,10,'T4010','T4','5.0-12.0 µg/dL'),(NULL,350,11,'UR011','Urine R/E','Normal'),(NULL,400,12,'ST012','Stool R/E','Normal'),(NULL,1500,13,'DEN013','Dengue NS1 Antigen','Negative'),(NULL,600,14,'MAL014','Malaria Parasite','Negative'),(NULL,2500,15,'COVID015','COVID-19 RT-PCR','Negative'),(NULL,700,16,'ECG016','Electrocardiogram (ECG)','Normal'),(NULL,1000,17,'XR017','Chest X-Ray','Normal'),(NULL,2500,18,'US018','Ultrasonography (USG Whole Abdomen)','Normal'),(NULL,3500,19,'ECHO019','Echocardiogram','Normal'),(NULL,2200,20,'VITD020','Vitamin D (25-OH)','20-50 ng/mL'),(_binary '',400,21,'CBC','Complete Blood Count (CBC)','See individual parameters'),(_binary '',1200,22,'DENGUE','Dengue Profile (NS1, IgM, IgG)','NS1 / IgM / IgG'),(_binary '',150,23,'HB','Hemoglobin','13.5 - 17.5 g/dL (M) / 12.0 - 15.5 g/dL (F)'),(_binary '',120,24,'FGLU','Fasting Blood Glucose','70 - 99 mg/dL'),(_binary '',150,25,'CREAT','Serum Creatinine','0.74 - 1.35 (M) / 0.59 - 1.04 (F) mg/dL'),(_binary '',120,26,'WBC','WBC Count','4000 - 11000 /µL'),(_binary '',150,27,'PLATELET','Platelet Count','150000 - 450000 /µL'),(_binary '',1800,28,'COVID-PCR','COVID-19 RT-PCR','Detected / Not Detected'),(_binary '',100,29,'PREGNANCY','Pregnancy Test (Urine HCG)','Positive / Negative'),(_binary '',1800,30,'DENGUE-PANEL','Dengue Screening Panel (NS1, IgM, IgG + CBC)','Dengue markers + CBC');
/*!40000 ALTER TABLE `test_masters` ENABLE KEYS */;
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
