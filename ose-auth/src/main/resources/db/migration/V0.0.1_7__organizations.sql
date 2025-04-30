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
-- Table structure for table `organizations`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE IF NOT EXISTS `organizations` (
  `id` bigint NOT NULL,
  `created_at` datetime DEFAULT NULL,
  `last_modified_at` datetime DEFAULT NULL,
  `status` varchar(16) NOT NULL,
  `created_by` bigint NOT NULL,
  `deleted` bit(1) NOT NULL,
  `deleted_at` datetime DEFAULT NULL,
  `deleted_by` bigint DEFAULT NULL,
  `last_modified_by` bigint NOT NULL,
  `version` bigint NOT NULL,
  `children` text,
  `depth` int DEFAULT NULL,
  `name` varchar(128) NOT NULL,
  `no` varchar(255) DEFAULT NULL,
  `parent_id` bigint DEFAULT NULL,
  `path` varchar(1020) DEFAULT NULL,
  `sort` int DEFAULT NULL,
  `type` varchar(32) NOT NULL,
  `company_id` bigint DEFAULT NULL,
  `idc` bit(1) DEFAULT NULL,
  `todo_task_count` int DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `IDXlfwtth3nocgljl1jbd263fv9j` (`deleted`,`depth`) USING BTREE,
  KEY `IDXhd5clg50diht2cod1498fh2lg` (`deleted`,`parent_id`) USING BTREE,
  KEY `IDXrqkxadpuphg0i7f1m80fhkqco` (`deleted`,`path`) USING BTREE,
  KEY `IDX2amobjb841cdgdn77ww1o0fhr` (`deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-04-06 11:47:57
