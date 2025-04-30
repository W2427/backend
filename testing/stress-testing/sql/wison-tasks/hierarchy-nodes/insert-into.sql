INSERT INTO `hierarchy_node` (
  `id`,
  `company_id`,
  `org_id`,
  `parent_id`,
  `project_id`,
  `path`,
  `depth`,
  `hierarchy_type`,
  `sort`,
  `node_id`,
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
  ?, -- Parent ID
  ?, -- Project ID
  ?, -- Hierarchy Path
  ?, -- Depth
  ?, -- Hierarchy Type
  ?, -- Sort
  ?, -- Project Node ID
  CURRENT_TIMESTAMP,
  ?, -- Created By User ID
  CURRENT_TIMESTAMP,
  ?, -- Last Modified By User ID
  ?, -- Version
  0,
  'ACTIVE'
);