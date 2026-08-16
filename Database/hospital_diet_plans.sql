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
-- Table structure for table `diet_plans`
--

DROP TABLE IF EXISTS `diet_plans`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `diet_plans` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `active` bit(1) DEFAULT NULL,
  `breakfast` text,
  `breakfast_time` varchar(255) DEFAULT NULL,
  `carbohydrate` double DEFAULT NULL,
  `created_at` datetime(6) NOT NULL,
  `description` text,
  `diet_type` varchar(50) NOT NULL,
  `dietician_notes` text,
  `dinner` text,
  `dinner_time` varchar(255) DEFAULT NULL,
  `doctor_recommendation` text,
  `evening_snacks` text,
  `evening_snacks_time` varchar(255) DEFAULT NULL,
  `fat` double DEFAULT NULL,
  `fiber` double DEFAULT NULL,
  `lunch` text,
  `lunch_time` varchar(255) DEFAULT NULL,
  `morning_snacks` text,
  `morning_snacks_time` varchar(255) DEFAULT NULL,
  `name` varchar(100) NOT NULL,
  `night_diet` text,
  `night_diet_time` varchar(255) DEFAULT NULL,
  `potassium` double DEFAULT NULL,
  `protein` double DEFAULT NULL,
  `sodium` double DEFAULT NULL,
  `total_calories` double DEFAULT NULL,
  `updated_at` datetime(6) NOT NULL,
  `vitamin_recommendation` text,
  `water_intake_ml` double DEFAULT NULL,
  `approved_by_doctor_id` bigint DEFAULT NULL,
  `created_by_dietician_id` bigint DEFAULT NULL,
  `price_per_day` double DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKqnockkwof6qg2rmv3d00qw4q3` (`approved_by_doctor_id`),
  KEY `FKaekogrbfraw4yyg3lvup4rkuu` (`created_by_dietician_id`),
  CONSTRAINT `FKaekogrbfraw4yyg3lvup4rkuu` FOREIGN KEY (`created_by_dietician_id`) REFERENCES `dieticians` (`id`),
  CONSTRAINT `FKqnockkwof6qg2rmv3d00qw4q3` FOREIGN KEY (`approved_by_doctor_id`) REFERENCES `doctors` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `diet_plans`
--

LOCK TABLES `diet_plans` WRITE;
/*!40000 ALTER TABLE `diet_plans` DISABLE KEYS */;
INSERT INTO `diet_plans` VALUES (1,_binary '','Rice, Dal, Vegetables, Fish/Chicken','07:00',300,'2026-07-28 23:20:46.927517','Balanced nutrition for general patients','Regular','Balanced meal plan','Roti, Vegetables, Rice','19:00','No restrictions','Biscuits, Tea','16:00',55,25,'Rice, Meat Curry, Salad, Dal','12:30','Fruits, Yogurt','10:30','Standard Regular Diet','Milk, Biscuits','22:00',3500,65,2000,2100,'2026-07-28 23:20:46.927517','Multivitamin daily',2500,NULL,1,NULL),(2,_binary '','Whole wheat toast, Egg white, Unsweetened tea','07:00',220,'2026-07-28 23:20:46.933713','Low sugar, high fiber diet for diabetic patients','Diabetic','Monitor blood sugar before meals','Roti, Mixed vegetables, Dal','19:00','Strict sugar control','Cucumber, Green tea','16:00',45,35,'Brown rice, Grilled chicken, Steamed vegetables','12:30','Apple, Handful of nuts','10:30','Diabetic Diet Plan','Warm milk (no sugar)','22:00',3000,70,1500,1800,'2026-07-28 23:20:46.933713','Vitamin B complex',2800,NULL,1,NULL),(3,_binary '','Oatmeal with fruits, Skim milk','07:00',280,'2026-07-28 23:20:46.938109','Low sodium diet for heart patients','Cardiac','Daily weight monitoring','Roti, Paneer curry (low salt), Salad','19:00','Sodium less than 2g per day','Unsalted crackers, Water','16:00',50,30,'Steamed rice, Grilled fish, Boiled vegetables (no salt)','12:30','Fresh fruit salad','10:30','Low Salt Cardiac Diet','Warm water with lemon','22:00',3200,60,800,1900,'2026-07-28 23:20:46.938109','Potassium supplement',2600,NULL,1,NULL),(4,_binary '','Protein shake, Boiled eggs, Whole wheat bread','07:00',280,'2026-07-28 23:20:46.942142','High protein diet for post-surgery recovery','HighProtein','Monitor albumin levels weekly','Fish curry, Roti, Spinach, Rice','19:00','High protein intake','Protein bar, Banana','16:00',60,28,'Chicken breast, Brown rice, Mixed vegetables, Lentil soup','12:30','Greek yogurt, Almonds','10:30','High Protein Recovery Diet','Casein protein, Warm milk','22:00',3500,120,1800,2500,'2026-07-28 23:20:46.942142','Protein supplements',3000,NULL,1,NULL),(5,_binary '','White bread, Egg, Low potassium juice','07:00',260,'2026-07-28 23:20:46.946657','Low protein, low potassium diet for kidney patients','Renal','Monthly renal function tests','Roti, Low potassium curry, Rice','19:00','Restrict potassium and phosphorus','Unsalted pretzels, Water','16:00',50,20,'White rice, Small portion chicken, Low potassium vegetables','12:30','Rice cake, Apple slices','10:30','Renal Diet Plan','Herbal tea, Low potassium biscuit','22:00',2000,40,1000,1800,'2026-07-28 23:20:46.946657','Iron supplement',2200,NULL,1,NULL),(6,_binary '','Porridge, Warm milk','07:00',240,'2026-07-28 23:20:46.951274','Easy to digest soft foods for post-operative patients','Soft','Gradual return to regular diet','Mashed rice, Dal soup, Soft fish','19:00','No hard or spicy food','Custard, Soft biscuit','16:00',40,15,'Khichuri, Boiled chicken, Mashed vegetables','12:30','Banana mash, Yogurt','10:30','Soft Diet Plan','Warm milk, Soft toast','22:00',2800,55,1500,1700,'2026-07-28 23:20:46.951274','Vitamin C',2400,NULL,1,NULL),(7,_binary '','Milk, Banana, Toast','07:00',230,'2026-07-28 23:20:46.982686','Age-appropriate nutrition for children','Pediatric','Monthly growth monitoring','Rice, Fish, Mashed potato','19:00','Age-appropriate portions','Milkshake, Biscuits','16:00',45,20,'Rice, Chicken curry, Vegetables, Dal','12:30','Fruit puree, crackers','10:30','Pediatric Nutrition Plan','Warm milk','22:00',2500,50,1200,1600,'2026-07-28 23:20:46.982686','Calcium + Vitamin D',2000,NULL,1,NULL),(8,_binary '','Strained fruit juice, Broth','07:00',200,'2026-07-28 23:20:46.988204','Full liquid diet for patients unable to eat solid food','Liquid','Daily weight check','Bone broth, Milk','19:00','No solid food','Fruit juice, Yogurt drink','16:00',30,5,'Cream soup, Strained porridge, Milk','12:30','Clear soup, Gelatin','10:30','Liquid Diet Plan','Warm milk, Honey','22:00',2500,35,1500,1400,'2026-07-28 23:20:46.988204','Electrolyte supplements',2500,NULL,1,NULL);
/*!40000 ALTER TABLE `diet_plans` ENABLE KEYS */;
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
