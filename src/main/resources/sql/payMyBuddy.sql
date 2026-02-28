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
-- Table structure for table `transaction`
--

DROP TABLE IF EXISTS `transaction`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `transaction` (
  `id` int NOT NULL AUTO_INCREMENT,
  `sender_id` int NOT NULL,
  `receiver_id` int NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `amount` double NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_transaction_sender` (`sender_id`),
  KEY `fk_transaction_receiver` (`receiver_id`),
  CONSTRAINT `fk_transaction_receiver` FOREIGN KEY (`receiver_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_transaction_sender` FOREIGN KEY (`sender_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=59 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `transaction`
--

LOCK TABLES `transaction` WRITE;
/*!40000 ALTER TABLE `transaction` DISABLE KEYS */;
INSERT INTO `transaction` VALUES (1,1,2,'Remboursement déjeuner',12.5),(2,2,1,'Café ☕',2.2),(3,1,3,'',0),(4,3,1,NULL,15),(5,5,6,'Participation cadeau',30),(6,6,5,'Retour trop-perçu',-5.75),(7,7,1,'Cotisation (janv.)',9.99),(8,4,2,'Paiement parking',35.5),(9,2,4,'Test très gros montant',9999999.99),(10,8,1,'Virement minute',0.01),(11,1,8,'Commande \"pizza\" + boisson',18.4),(12,9,1,'Pas de connexion mais transaction',7),(13,1,9,'Remboursement',7),(14,10,1,'Premier paiement',1),(15,5,7,'Annulation/ajustement',-0.5),(16,7,5,'Ajustement positif',0.5),(17,2,3,'Plein d\'accents: éèàçù',3.33),(18,3,2,'Long message xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx',45.67),(19,4,9,'',250),(20,8,10,NULL,1234567890.12),(21,999,1,'Crédit initial (seed)',100),(22,999,2,'Crédit initial (seed)',100),(23,999,3,'Crédit initial (seed)',100),(24,999,4,'Crédit initial (seed)',100),(25,999,5,'Crédit initial (seed)',100),(26,999,6,'Crédit initial (seed)',100),(27,999,7,'Crédit initial (seed)',100),(28,999,8,'Crédit initial (seed)',100),(29,999,9,'Crédit initial (seed)',100),(30,999,10,'Crédit initial (seed)',100),(36,1,2,'pistache',5),(37,1,8,'nuage',9),(38,1,3,'pistache',45),(39,1,8,'pistache',20.43),(40,1,4,'pistache',3),(41,999,1,'Initial funding',1000),(42,999,2,'Initial funding',1000),(43,999,1000,'Initial funding',1000),(44,999,3,'Initial funding',1000),(45,999,4,'Initial funding',1000),(46,999,5,'Initial funding',1000),(47,999,6,'Initial funding',1000),(48,999,7,'Initial funding',1000),(49,999,8,'Initial funding',1000),(50,999,9,'Initial funding',1000),(51,999,10,'Initial funding',1000),(56,1000,2,'pistache',30),(57,1000,2,'pistache',13),(58,1000,2,'pistache',957);
/*!40000 ALTER TABLE `transaction` ENABLE KEYS */;
UNLOCK TABLES;

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
) ENGINE=InnoDB AUTO_INCREMENT=1001 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'Alicia','alice.martin@example.com','$2b$10$mxk0C3Icm9fYKpryCcb6OuCXkC.oHbqQ3m8MEepSUddsFSsJqtIu.'),(2,'Bastien','bastien.dupont@example.com','$2b$10$cjF/H723mKfML.xuPHDlUepagg0h2Icwx4dzuqrZSXNF2uoFBNSRe'),(3,'Chloé','chloe.lefevre@example.com','$2b$10$6TTm3s.TQUlGm96214EHB.cNkcfkkZBFjNETdqNP2T7yj.ggFruEq'),(4,'David','david.bernard@example.com','$2b$10$Q6IvnNkvzyT.4LlRImwsse2dY8ZuEStjMwsrohmWhRaog8LXmPwVu'),(5,'Emma','emma.petit@example.com','$2b$10$vB153DnzOXYaKqzYh2N/vez2OY8pDgi3.FeA7IhIG61T1O57CnFXW'),(6,'Farid','farid.moreau@example.com','$2b$10$HfjhIwXEc1JG8XzzbJJZUur14qq1dLe3Q7bI2.v6FA.PM9SQjej0m'),(7,'Gaëlle','gaelle.robert@example.com','$2b$10$T5/wTqw5UjXd1KlOdnFN0OoIQnyUHtybKDrYBFotWN7XW5KnEG1ka'),(8,'Hugo','hugo.richard@example.com','$2b$10$mQ2uhQpe2EsJSPs9hNrhoegy8HEgy4JZiDPr8XszejAmsZKuW3qqK'),(9,'Inès','ines.durand@example.com','$2b$10$Qc5YAVVGLwNjpEE7As2iAeq04cKhEEhBGcoloETEFMxSYzpQkb4/G'),(10,'Jules','jules.laurent@example.com','$2b$10$c8THnqNxABgqk7Ki.z0Ybu5AlnRVjajylxxJsyBjbhLwXKa..R5GW'),(999,'System','system@paymybuddy.local','$2b$10$mxk0C3Icm9fYKpryCcb6OuCXkC.oHbqQ3m8MEepSUddsFSsJqtIu.'),(1000,'Bob','bob.gerard@example.com','$2a$10$7LbaIw2mYsXua75ILloqf.yQahxl16KU7eqw3pTfJwrecaKKIgJ06');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_connection`
--

DROP TABLE IF EXISTS `user_connection`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_connection` (
  `user_id` int NOT NULL,
  `user_budy` int NOT NULL,
  PRIMARY KEY (`user_id`,`user_budy`),
  KEY `user_budy` (`user_budy`),
  CONSTRAINT `user_connection_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
  CONSTRAINT `user_connection_ibfk_2` FOREIGN KEY (`user_budy`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_connection`
--

LOCK TABLES `user_connection` WRITE;
/*!40000 ALTER TABLE `user_connection` DISABLE KEYS */;
INSERT INTO `user_connection` VALUES (2,1),(3,1),(5,1),(7,1),(10,1),(1,2),(7,2),(1000,2),(1,3),(7,3),(1,4),(2,4),(1,5),(6,5),(5,6),(5,7),(1,8),(1,9);
/*!40000 ALTER TABLE `user_connection` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-02-28 20:00:00
