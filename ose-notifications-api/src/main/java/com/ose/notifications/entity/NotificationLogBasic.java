package com.ose.notifications.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * 通知记录数据实体。
 */
@Entity
@Table(name = "logs")
public class NotificationLogBasic extends NotificationLogBase {

    private static final long serialVersionUID = -4957423703354893579L;

}
