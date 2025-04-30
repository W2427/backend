package com.ose.notifications.dto;

import com.ose.notifications.vo.NotificationContentType;

/**
 * 通知信息模版更新数据传输对象接口。
 */
public interface NotificationTemplateDTOInterface {

    String getTitle();

    NotificationContentType getContentType();

    String getContent();

    String getText();

}
