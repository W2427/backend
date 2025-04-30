USE `ose_auth`;

DROP VIEW IF EXISTS `ose_auth`.`user_privileges`;
CREATE VIEW `ose_auth`.`user_privileges` AS
  SELECT
	  concat( `m`.`user_id`, '@', `r`.`id` )  AS  `id`,
	  `m`.`user_id`                           AS  `user_id`,
	(CASE WHEN isnull( `o`.`children` ) THEN CONVERT (concat( ',', `o`.`id`, ',' ) USING utf8)
	      ELSE concat(',',`o`.`id`,',',`o`.`children`,',') END ) AS `orgs`,
	concat( ',', `r`.`privileges`, ',' )      AS  `privileges`
	FROM
		`user_role_relations` `m` JOIN `roles` `r`
		  ON `r`.`id` = `m`.`role_id`
			AND `r`.`deleted` = 0

		JOIN `organizations` `o`
		  ON `o`.`id` = `r`.`organization_id`
			AND `o`.`deleted` = 0
WHERE
	`m`.`deleted` = 0
;
