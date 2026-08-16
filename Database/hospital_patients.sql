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
-- Table structure for table `patients`
--

DROP TABLE IF EXISTS `patients`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `patients` (
  `date_of_birth` date DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `address` varchar(255) DEFAULT NULL,
  `alternate_phone` varchar(255) DEFAULT NULL,
  `blood_group` varchar(255) DEFAULT NULL,
  `city` varchar(255) DEFAULT NULL,
  `district` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `emergency_contact_name` varchar(255) DEFAULT NULL,
  `emergency_contact_number` varchar(255) DEFAULT NULL,
  `gender` varchar(255) DEFAULT NULL,
  `marital_status` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `national_id` varchar(255) DEFAULT NULL,
  `patient_code` varchar(255) NOT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `postal_code` varchar(255) DEFAULT NULL,
  `relationship` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKpdu5f0e015icwwcx7otn46rv8` (`patient_code`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `patients`
--

LOCK TABLES `patients` WRITE;
/*!40000 ALTER TABLE `patients` DISABLE KEYS */;
INSERT INTO `patients` VALUES ('2020-01-01',1,'Narsindi','014784512','A+','Dhaka','Dhaka','masud@gmail.com','Miskat','0157846130','Male','Single','Md. Masud','784512369','PAT000001','01759461325','1206','Brother'),('2026-07-12',2,'y6ui','','O+','Dhaka','Dhaka',NULL,'rtyhjkl','365465313','Male','Single','Emon','','PAT000002','12345678912','1205','yui'),('2026-07-01',3,'ddddddddd','01845612312','B+','Dhaka','Dhaka','pk@gmail.com','Emon','01546789123','Male','Single','Pk','6904016893','PAT000003','01845312578','1205','Brother'),('1996-01-01',4,'Projpur','01845612233','B+','Barishal','Barishal','badrul@gmail.com','Emon','01546789123','Male','Married','Md.Badrul Amin','745863214','PAT000004','01547812365','9205','Boss'),(NULL,5,'dfgdh',NULL,'B-',NULL,NULL,NULL,'Emon','0178455566',NULL,NULL,'Sabbir','7845691234','PAT000005','0178945612',NULL,'Father'),('2026-07-01',6,'Mohammodpur','01345678952','B+','Dhaka','Dhaka','miskat@gmail.com','Emon','01456789324','Male','Married','Md.Miskat','7845691234','PAT000006','01745623138','1205','Brother'),('2021-05-04',7,'Mohammodpur','0145794625','AB+','Dhaka','Dhaka','m@gmail.com','Md.Miskat','01745623138','Male','Single','Md.Mahabub','7894563211','PAT000007','0184512397','1205','Friend'),('2016-01-20',8,'Satkhria','0184561238','B+','Khulna','Satkhira','tonni@gmail.com','Shaharan Hossain','01745623138','Female','Married','Tonni','6904016893','PAT000008','0194578631','9430','Friend'),('2019-12-31',9,'dddddddgh','','B+','Dhaka','Dhaka','superadmin@mederp.com','Emon','01745623138','Female','Married','Lima','7845691234','PAT000009','0175646855','1205','Brother'),('2026-07-29',10,'hgjgk','','B+','Dhaka','Dhaka','tanvir@gmail.com','Emon','01745623138','Male','Married','Tanvir','7894563211','PAT000010','0178946645','1205','Friend'),('1990-01-01',14,'Addr','','A+','City','Dist','repro1999@example.com','EC','19998887772','Male','Single','Test Repro','NID1999','PAT000011','19998887771','1234','Friend'),('1990-01-01',15,'Addr','','A+','City','Dist','repro2000@example.com','EC','19998887774','Male','Single','Test Repro2','NID2000','PAT000012','19998887773','1234','Friend'),(NULL,17,NULL,NULL,NULL,NULL,NULL,'ph2002@example.com',NULL,NULL,'Male',NULL,'DupPhone Test',NULL,'PAT000013','19998887773',NULL,NULL),(NULL,18,NULL,NULL,NULL,NULL,NULL,'misk@example.com',NULL,NULL,'Female',NULL,'DupEmail Test',NULL,'PAT000014','19998887790',NULL,NULL),('2020-01-01',19,'Mohammodpur','01745623138','B+','Dhaka','Dhaka','miskat@gmail.com','Emon','01745623138','Male','Married','Md.Miskat','7894563211','PAT000015','0175468461','1205','Brother');
/*!40000 ALTER TABLE `patients` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-08-16 18:05:16
