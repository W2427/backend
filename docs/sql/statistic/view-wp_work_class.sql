USE `ose_tasks`;

CREATE OR REPLACE VIEW `wp_work_class` AS
SELECT
			'WP01'	WP,
			'' work_class,
			id,
			project_id

FROM entity_wp01

UNION ALL
SELECT
			'WP02'	WP,
			work_class,
			id,
			project_id

FROM entity_wp02
UNION ALL
SELECT
			'WP03'	WP,
			work_class,
			id,
			project_id

FROM entity_wp03

UNION ALL
SELECT
			'WP04'	WP,
			work_class,
			id,
			project_id

FROM entity_wp04
UNION ALL
SELECT
			'WP05'	WP,
			work_class,
			id,
			project_id

FROM entity_wp05
;

