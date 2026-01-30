-- MySQL dump 10.13  Distrib 8.0.43, for Win64 (x86_64)
--
-- Host: localhost    Database: paymybuddy
-- ------------------------------------------------------
-- Server version	8.0.44

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
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(100) NOT NULL,
  `email` varchar(150) NOT NULL,
  `password` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'Alice','alice.martin@example.com','$2b$10$mxk0C3Icm9fYKpryCcb6OuCXkC.oHbqQ3m8MEepSUddsFSsJqtIu.'),(2,'Bastien','bastien.dupont@example.com','$2b$10$cjF/H723mKfML.xuPHDlUepagg0h2Icwx4dzuqrZSXNF2uoFBNSRe'),(3,'Chloé','chloe.lefevre@example.com','$2b$10$6TTm3s.TQUlGm96214EHB.cNkcfkkZBFjNETdqNP2T7yj.ggFruEq'),(4,'David','david.bernard@example.com','$2b$10$Q6IvnNkvzyT.4LlRImwsse2dY8ZuEStjMwsrohmWhRaog8LXmPwVu'),(5,'Emma','emma.petit@example.com','$2b$10$vB153DnzOXYaKqzYh2N/vez2OY8pDgi3.FeA7IhIG61T1O57CnFXW'),(6,'Farid','farid.moreau@example.com','$2b$10$HfjhIwXEc1JG8XzzbJJZUur14qq1dLe3Q7bI2.v6FA.PM9SQjej0m'),(7,'Gaëlle','gaelle.robert@example.com','$2b$10$T5/wTqw5UjXd1KlOdnFN0OoIQnyUHtybKDrYBFotWN7XW5KnEG1ka'),(8,'Hugo','hugo.richard@example.com','$2b$10$mQ2uhQpe2EsJSPs9hNrhoegy8HEgy4JZiDPr8XszejAmsZKuW3qqK'),(9,'Inès','ines.durand@example.com','$2b$10$Qc5YAVVGLwNjpEE7As2iAeq04cKhEEhBGcoloETEFMxSYzpQkb4/G'),(10,'Jules','jules.laurent@example.com','$2b$10$c8THnqNxABgqk7Ki.z0Ybu5AlnRVjajylxxJsyBjbhLwXKa..R5GW');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-01-30 16:20:10
