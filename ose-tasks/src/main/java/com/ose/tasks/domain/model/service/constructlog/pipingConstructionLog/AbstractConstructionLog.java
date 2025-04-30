package com.ose.tasks.domain.model.service.constructlog.pipingConstructionLog;

import com.ose.tasks.domain.model.service.constructlog.ConstructLogInterface;
import com.ose.tasks.entity.qc.BaseConstructionLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public abstract class AbstractConstructionLog implements ConstructLogInterface {


    @Override
    public Page<? extends BaseConstructionLog> getTestResult(Long orgId, Long projectId, Long entityId, String processNameEn, Pageable pageable) {
        return null;
    }



}
