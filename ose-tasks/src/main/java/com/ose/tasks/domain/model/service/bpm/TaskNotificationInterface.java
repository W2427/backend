package com.ose.tasks.domain.model.service.bpm;

import com.ose.tasks.entity.bpm.BpmActivityInstanceBase;

import java.util.List;

public interface TaskNotificationInterface {

    void sendNotification(Long orgId, Long projectId, List<Long> taskIds);

    void sendDrawingVersionNotification(Long orgId, Long projectId, String subDrawingNo, String subDrawingVersion);

    void sendExternalInspectionNotification(Long orgId, Long projectId,
                                            List<Long> externalInspectionApplyScheduleIds);

    void sendInternalInspectionNotification(Long orgId, Long projectId, BpmActivityInstanceBase actInst);

}
