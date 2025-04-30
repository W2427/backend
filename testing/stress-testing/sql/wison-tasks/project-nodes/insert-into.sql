INSERT INTO `project_node` (
  `id`,
  `company_id`,
  `org_id`,
  `project_id`,
  `module_type`,
  `node_type`,
  `entity_type`,
  `entity_sub_type`,
  `entity_business_type`,
  `entity_id`,
  `no`,
  `display_name`,
  `created_at`,
  `created_by`,
  `last_modified_at`,
  `last_modified_by`,
  `version`,
  `deleted`,
  `status`
) VALUES (
  ?, -- ID
  ?, -- Company ID
  ?, -- Organization ID
  ?, -- Project ID
  ?, -- Module Type
  ?, -- Hierarchy Node Type
  ?, -- Entity Type
  ?, -- Entity Sub Type
  ?, -- Entity Business Type
  ?, -- Entity ID
  ?, -- No.
  ?, -- Display Name
  CURRENT_TIMESTAMP,
  ?, -- Created By User ID
  CURRENT_TIMESTAMP,
  ?, -- Last Modified By User ID
  ?, -- Version
  0,
  'ACTIVE'
);