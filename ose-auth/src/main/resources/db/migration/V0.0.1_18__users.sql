-- MySQL dump 10.13  Distrib 8.0.32, for macos13 (x86_64)
--
-- Host: 47.100.252.221    Database: saint_whale_auth
-- ------------------------------------------------------
-- Server version	8.0.32

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `users`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE IF NOT EXISTS `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime DEFAULT NULL,
  `last_modified_at` datetime DEFAULT NULL,
  `status` varchar(16) NOT NULL,
  `created_by` bigint NOT NULL,
  `deleted` bit(1) NOT NULL,
  `deleted_at` datetime DEFAULT NULL,
  `deleted_by` bigint DEFAULT NULL,
  `last_modified_by` bigint NOT NULL,
  `version` bigint DEFAULT NULL,
  `email` varchar(64) DEFAULT NULL,
  `logo` varchar(128) DEFAULT NULL,
  `mobile` varchar(16) DEFAULT NULL,
  `name` varchar(64) NOT NULL,
  `type` varchar(16) NOT NULL,
  `username` varchar(32) DEFAULT NULL,
  `disabled` bit(1) NOT NULL,
  `password` varchar(60) NOT NULL,
  `default_send_email` varchar(64) DEFAULT NULL,
  `company` varchar(255) DEFAULT NULL,
  `department` varchar(255) DEFAULT NULL,
  `division` varchar(255) DEFAULT NULL,
  `date_of_employment` datetime DEFAULT NULL,
  `date_of_termination` datetime DEFAULT NULL,
  `first_name_cn` varchar(255) DEFAULT NULL,
  `first_name_en` varchar(255) DEFAULT NULL,
  `gender` tinyint DEFAULT NULL,
  `last_name_cn` varchar(255) DEFAULT NULL,
  `last_name_en` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `nationality` bit(1) DEFAULT NULL,
  `apply_role` bit(1) DEFAULT NULL,
  `companygm` bit(1) DEFAULT NULL,
  `divisionvp` bit(1) DEFAULT NULL,
  `review_role` bit(1) DEFAULT NULL,
  `team` varchar(255) DEFAULT NULL,
  `team_leader` bit(1) DEFAULT NULL,
  `no_approval_required` bit(1) DEFAULT NULL,
  `auto_fill_hours` bit(1) DEFAULT NULL,
  `review_other_company` varchar(255) DEFAULT NULL,
  `on_project` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `IDXbmsemntdglxb3j971hxcujp18` (`username`,`deleted`) USING BTREE,
  KEY `IDX4sfn5ffb264f4ku3061t5kul4` (`email`,`deleted`) USING BTREE,
  KEY `IDXvdge15vi4buj4ppqmu0s4ew6` (`mobile`,`deleted`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-04-06 11:48:34
