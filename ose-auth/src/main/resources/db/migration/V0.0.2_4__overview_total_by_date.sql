CREATE TABLE IF NOT EXISTS `overview_total_by_date`(
    id int not null auto_increment primary key,
    type int not null default 0 comment '类型 1:用户总数，2：离职人数',
    location varchar(128) not null default '' comment '地点',
    total int not null default 0 comment '总数',
    month int not null default 0 comment '月份',
    update_time timestamp not null default now() comment '更新时间',
    UNIQUE KEY `type`(type, month) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='总数统计表';


INSERT INTO saint_whale_auth.overview_total_by_date (type, total, month)
SELECT
    1 AS type,
    (SELECT COUNT(*) FROM saint_whale_auth.users
     WHERE onboarding_month <= m.month AND status != 'DELETED') AS total,
    m.month
FROM (
    WITH RECURSIVE months AS (
        SELECT 202301 AS month
        UNION ALL
        SELECT
            CASE
                WHEN month % 100 = 12 THEN month + 89  -- 处理跨年（如 202312 → 202401）
                ELSE month + 1
            END
        FROM months
        WHERE month < 202504
    )
    SELECT month FROM months
) AS m
ON DUPLICATE KEY UPDATE total = VALUES(total);
