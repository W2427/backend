-- --------------------------------------------------------
-- 主机:                           localhost
-- 服务器版本:                        10.1.31-MariaDB - mariadb.org binary distribution
-- 服务器操作系统:                      Win32
-- HeidiSQL 版本:                  9.4.0.5125
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- 导出 ose_report 的数据库结构
CREATE DATABASE IF NOT EXISTS `ose_report` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `ose_report`;

-- 导出  表 ose_report.report_checklists 结构
CREATE TABLE IF NOT EXISTS `report_checklists` (
  `id` varchar(16) NOT NULL,
  `header_template` varchar(16) DEFAULT NULL,
  `name` varchar(128) NOT NULL,
  `org_id` varchar(16) NOT NULL,
  `preview_file` varchar(256) DEFAULT NULL,
  `project_id` varchar(16) NOT NULL,
  `serial` varchar(32) DEFAULT NULL,
  `signature_template` varchar(16) DEFAULT NULL,
  `title` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKhw1pwsn12hyjxcef6yn3v0476` (`serial`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。
-- 导出  表 ose_report.report_checklist_items 结构
CREATE TABLE IF NOT EXISTS `report_checklist_items` (
  `id` varchar(16) NOT NULL,
  `created_at` datetime NOT NULL,
  `last_modified_at` datetime NOT NULL,
  `status` varchar(16) NOT NULL,
  `checklist_id` varchar(16) NOT NULL,
  `description` varchar(256) DEFAULT NULL,
  `item_no` varchar(8) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。
-- 导出  表 ose_report.report_checklist_simulations 结构
CREATE TABLE IF NOT EXISTS `report_checklist_simulations` (
  `id` varchar(16) NOT NULL,
  `checklist_id` varchar(16) NOT NULL,
  `generated_file` varchar(256) DEFAULT NULL,
  `name` varchar(64) NOT NULL,
  `org_id` varchar(16) NOT NULL,
  `project_id` varchar(16) NOT NULL,
  `simulation_data` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。
-- 导出  表 ose_report.report_templates 结构
CREATE TABLE IF NOT EXISTS `report_templates` (
  `id` varchar(16) NOT NULL,
  `domain` varchar(16) NOT NULL,
  `fixed_height` int(11) DEFAULT NULL,
  `name` varchar(128) NOT NULL,
  `position` varchar(16) NOT NULL,
  `template_file` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
