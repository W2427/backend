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
-- Temporary view structure for view `user_available_privileges`
--

DROP TABLE IF EXISTS `user_available_privileges`;
/*!50001 DROP VIEW IF EXISTS `user_available_privileges`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `user_available_privileges` AS SELECT 
 1 AS `id`,
 1 AS `org_id`,
 1 AS `user_id`,
 1 AS `privileges`*/;
SET character_set_client = @saved_cs_client;

--
-- Final view structure for view `user_available_privileges`
--

/*!50001 DROP VIEW IF EXISTS `user_available_privileges`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`backend`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `user_available_privileges` AS select concat(`ur`.`user_id`,'@',`o`.`id`) AS `id`,`o`.`id` AS `org_id`,`ur`.`user_id` AS `user_id`,group_concat(`r`.`privileges` separator '') AS `privileges` from (((`organizations` `o` join `organizations` `co` on((((`co`.`id` = `o`.`id`) or (`co`.`path` like concat(`o`.`path`,`o`.`id`,'/%'))) and (`co`.`deleted` = 0)))) join `roles` `r` on(((`r`.`organization_id` = `co`.`id`) and (`r`.`deleted` = 0)))) join `user_role_relations` `ur` on(((`ur`.`role_id` = `r`.`id`) and (`ur`.`deleted` = 0)))) where (`o`.`deleted` = 0) group by `o`.`id`,`ur`.`user_id` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-04-06 11:48:15
