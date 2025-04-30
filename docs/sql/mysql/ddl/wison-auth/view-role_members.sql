USE `ose_auth`;

DROP VIEW IF EXISTS `ose_auth`.`role_members`;
CREATE VIEW `ose_auth`.`role_members` AS
SELECT
	`u`.`id` AS `id`,
	`u`.`id` AS `user_id`,
	`r`.`id` AS `role_id`,
	`u`.`name` AS `name`,
	`u`.`mobile` AS `mobile`,
	`u`.`username` AS `username`,
	`u`.`email` AS `email`
FROM
	(
	(
	`roles` `r`
	JOIN `user_role_relations` `ur` ON ( ( `r`.`id` = `ur`.`role_id` ) )
	)
	LEFT JOIN `users` `u` ON ( ( `u`.`id` = `ur`.`user_id` ) )
	)
WHERE
	(
	( `r`.`deleted` = FALSE )
	AND ( `ur`.`deleted` = FALSE )
	AND ( `u`.`deleted` = FALSE )
	)
;
