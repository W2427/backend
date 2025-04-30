package com.ose.tasks.domain.model.service.drawing.impl;

import java.util.List;
import java.util.Optional;

import com.ose.tasks.domain.model.repository.drawing.DrawingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ose.exception.BusinessError;
import com.ose.tasks.domain.model.repository.bpm.BpmActivityInstanceRepository;
import com.ose.tasks.domain.model.repository.drawing.DesignChangeReviewFormRepository;
import com.ose.tasks.entity.bpm.BpmActivityInstanceBase;
import com.ose.tasks.entity.DesignChangeReviewForm;
import com.ose.tasks.entity.drawing.Drawing;
import com.ose.tasks.vo.bpm.ActInstFinishState;
import com.ose.tasks.vo.SuspensionState;
import com.ose.vo.EntityStatus;

@Component
public class DesignChangeReviewFormService {


    private DesignChangeReviewFormRepository designChangeReviewFormRepository;

    private DrawingRepository drawingRepository;

    private BpmActivityInstanceRepository bpmActInstRepository;

    /**
     * 构造方法
     */
    @Autowired
    public DesignChangeReviewFormService(
        DesignChangeReviewFormRepository designChangeReviewFormRepository,
        BpmActivityInstanceRepository bpmActInstRepository,
        DrawingRepository drawingRepository
    ) {
        this.designChangeReviewFormRepository = designChangeReviewFormRepository;
        this.drawingRepository = drawingRepository;
        this.bpmActInstRepository = bpmActInstRepository;
    }

    /**
     * check 图纸
     *
     * @param projectId  项目ID
     * @param orgId      组织ID
     * @param id
     * @param actionItem
     */
    public boolean checkActionItem(Long orgId, Long projectId, Long id, List<String> actionItem) {
        for (String dwgNo : actionItem) {
            Optional<Drawing> opDwg = drawingRepository.findByOrgIdAndProjectIdAndDwgNoAndStatus(orgId, projectId, dwgNo, EntityStatus.ACTIVE);
            if (opDwg.isPresent()) {
                Drawing dwg = opDwg.get();
                List<BpmActivityInstanceBase> actList = bpmActInstRepository.findByProjectIdAndEntityIdAndFinishStateAndSuspensionState(
                    projectId, dwg.getId(), ActInstFinishState.NOT_FINISHED, SuspensionState.ACTIVE);
                if (!actList.isEmpty()) {
                    throw new BusinessError("图纸 " + dwgNo + " 有正在运行的流程任务.");
                }

                List<DesignChangeReviewForm> formList = designChangeReviewFormRepository.findByFinishStateAndActionItemLikeAndReviewRegisterIdNot(
                    ActInstFinishState.NOT_FINISHED, "%\"" + dwgNo + "\"%", id);
                if (!formList.isEmpty()) {
                    throw new BusinessError("图纸 " + dwgNo + " 有正在运行的变更流程.");
                }
            }
        }
        return true;
    }

    /**
     * 修改流程状态
     *
     * @param entityId
     * @return
     */
    public boolean modifyFinishState(Long entityId) {
        DesignChangeReviewForm form = designChangeReviewFormRepository.findByReviewRegisterId(entityId);
        if (form != null) {
            form.setFinishState(ActInstFinishState.FINISHED);
            designChangeReviewFormRepository.save(form);
        }
        return true;
    }

}
