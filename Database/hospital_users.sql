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
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `active` bit(1) NOT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `email` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `role` varchar(50) NOT NULL,
  `image` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK6dotkott2kjsp8vw4d0m25fb7` (`email`),
  UNIQUE KEY `UKdu5v5sr43g5bfnji4vb8hg5s3` (`phone`)
) ENGINE=InnoDB AUTO_INCREMENT=44 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (_binary '',1,'ahmed.rahman@hospital.com','Ahmed Rahman','$2a$10$iX2wX5Z4aXyNvT46cQ4oWe3AnT/FASDD/XaFibbkrgymMYXCWn2ly','01710000001','Doctor','/images/4165b28b-3d28-4a21-969e-6e9fa52f6bff_Doctor-2.jpeg'),(_binary '',2,'fatema.akter@hospital.com','Fatema Akter','$2a$10$urbWFm/Jc78uoc7aZUcETeAL3ZAzouuEUALQK.iS7gTmrV2YGQBla','01710000002','Doctor','/images/f5c9d320-45c5-4af8-9fd9-e76d69c4127f_Doctor-4.jpeg'),(_binary '',3,'mahmud.hasan@hospital.com',' Mahmud Hasan','$2a$10$jaRgUo10Eb6uTtfoQa16M.E76kZnqpt1rbOrD9SkJ8VX0b/BqtoXK','01710000003','Doctor','/images/a9aa0cd1-e927-4388-930f-cd26149d21e5_Dr. Ahmed Rahman.png'),(_binary '',5,'nusrat.jahan@hospital.com','Nusrat Jahan','$2a$10$lotz6BAlFLy7cD3n7KBB.uK/sk3zPMlzTl2wgnkdMeBk9I2q.CNCi','01710000004','Doctor','/images/31d96610-be5c-4a89-ba01-0842d75b61bc_Doctor-5.jpeg'),(_binary '',6,'tanvir.islam@hospital.com',' Tanvir Islam','$2a$10$KBB7mtw3yOP283gPNcKE7uvvRwB.7kyv.yFzM2gmZWxmihxEoMIt6','01710000005','Doctor','/images/392a7792-3f99-4b3e-85e6-0c98f1ef8806_Doctor -1.jpeg'),(_binary '',17,'sarah@gmail.com','Sarah Ahmed','$2a$10$KBB7mtw3yOP283gPNcKE7uvvRwB.7kyv.yFzM2gmZWxmihxEoMIt6','01711111111','Nurse','/images/ec945172-1ca7-4003-a287-5aa6e148000d_Nurse-01.png'),(_binary '',32,'admin@example.com','Admin','$2a$10$KBB7mtw3yOP283gPNcKE7uvvRwB.7kyv.yFzM2gmZWxmihxEoMIt6','6565656','Admin',NULL),(_binary '',33,'admin@elitecare.com','Admin','$2a$10$Pqluk0JAv9uuDoKAAlay5e5rz5qGZBvCvkzFybSV4nK4Lu1G4wzG.','01700000000','Admin',NULL),(_binary '',34,'sajin@elitecare.com','Sajin','$2a$10$V80WVcnmPhI/.FebIOrYFucd0FmonEx5XrteoCvedlFCHpsRZ447K','01534303326','LabTechnician','/images/c698e884-f58c-420c-a9c1-3a459acc197f_Sajin.jpg'),(_binary '',35,'rehana@elitecare.com','Rehana','$2a$10$lJtFelJ9BLhPQk3ZWGRw1uBqCRNArMDqnB0XLEtA4Alv2i0ojh19a','01512347896','Receptionist','/images/d7a6d417-dd91-4c59-89b6-17774ac2acc9_Rehana.jpg'),(_binary '',39,'nusratjahan@gmail.com','Nusrat Jahan','$2a$10$I0nQqzqc6V2e7a6giG1lWO9yEqrEVQw8ozoGm.vV3S41WfauhYEl.','01711111122','Nurse','/images/42f5e3f2-028a-486a-a684-f32e37649e87_Nurse-02.png'),(_binary '',40,'sadia.rahman@elitecare.com','Sadia Rahman','$2a$10$l9dav1tiIQUlsKtF8cbmpOOlwyKjOhJdzU0N4qG.5pcMS7pjM8bxq','01721000001','Nurse','/images/bf4d1078-77af-4d2a-afdb-f00b11c4a3f0_Nusrat Jahan.webp'),(_binary '',41,'tasnia.islam@elitecare.com','Tasnia Islam','$2a$10$lJRF/kzTp1hS04P9W0OQEeVwSRCO5B.Y.iAA7joWiNM9se37zrQ/e','01721000002','Nurse',NULL),(_binary '',42,'nasreen.diet@elitecare.com','Dr. Nasreen Akter','$2a$10$82r1HD/g3IfZVTJgkddh9ulZEHwOlZ4TTt9sPfHQArya2xa2osEru','01812001001','Dietician',NULL),(_binary '',43,'mahmuda.diet@elitecare.com','Dr. Mahmuda Khatun','$2a$10$PqRj22tHu1UvTzepUvBAU.9S0cktYhypg4vz72BpnmwR9qLqfZ.zy','01812002002','Dietician',NULL);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-08-16 18:05:17
