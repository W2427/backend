package com.ose.notifications.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * 通知批次数据实体。
 */
@Entity
@Table(name = "batches")
public class NotificationBatchBasic extends NotificationBatchBase {

    private static final long serialVersionUID = -8407167541542070648L;

}
