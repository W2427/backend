USE `ose_auth`;

DROP VIEW IF EXISTS `ose_auth`.`user_available_privileges`;
CREATE VIEW `ose_auth`.`user_available_privileges` AS
SELECT
	concat( `ur`.`user_id`, '@', `o`.`id` )         AS  `id`,
	`o`.`id`                                        AS  `org_id`,
	`ur`.`user_id`                                  AS  `user_id`,
	group_concat( `r`.`privileges` SEPARATOR '' )   AS `privileges`
FROM
	`organizations` `o`
	  JOIN `organizations` `co`
			ON (`co`.`id` = `o`.`id`  OR  `co`.`path` LIKE concat( `o`.`path`, `o`.`id`, '/%' ))
			AND `co`.`deleted` = 0

		JOIN `roles` `r`
		  ON `r`.`organization_id` = `co`.`id`
			AND `r`.`deleted` = 0

		JOIN `user_role_relations` `ur`
		  ON `ur`.`role_id` = `r`.`id`
			AND `ur`.`deleted` = 0
WHERE
	`o`.`deleted` = 0
GROUP BY
	`o`.`id`,
	`ur`.`user_id`
;
