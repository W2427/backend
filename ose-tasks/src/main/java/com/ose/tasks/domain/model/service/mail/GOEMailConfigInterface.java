package com.ose.tasks.domain.model.service.mail;

import com.ose.tasks.entity.bpm.GOEMailConfig;

import java.util.List;


/**
 * GOE邮件发送服务接口。
 */
public interface GOEMailConfigInterface {

    /**
     * 查询。
     *
     * @param orgId       组织 ID
     * @param projectId   项目 ID
     * @param taskDefKey  任务节点
     * @return 分页数据
     */
    List<GOEMailConfig> searchList(
        Long orgId,
        Long projectId,
        Long actInstId,
        String taskDefKey
    );
}
