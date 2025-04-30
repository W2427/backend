package com.ose.tasks.domain.model.service.delegate;

import com.ose.dto.ContextDTO;
import com.ose.tasks.domain.model.repository.bpm.BpmActivityInstanceRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmActivityInstanceStateRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmRuTaskRepository;
import com.ose.tasks.domain.model.repository.drawing.DrawingDetailRepository;
import com.ose.tasks.domain.model.repository.drawing.DrawingRepository;
import com.ose.tasks.domain.model.repository.drawing.SubDrawingRepository;
import com.ose.tasks.dto.bpm.ActInstSuspendDTO;
import com.ose.tasks.dto.bpm.SuspendDTO;
import com.ose.tasks.entity.drawing.Drawing;
import com.ose.tasks.entity.drawing.DrawingDetail;
import com.ose.vo.EntityStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 检查 节点 代理。
 */
@Component
public class DrawingSuspendDelegate extends BaseBpmTaskDelegate implements BaseBpmTaskInterfaceDelegate {

    private final DrawingRepository drawingRepository;

    private final SubDrawingRepository subDrawingRepository;

    private final DrawingDetailRepository drawingDetailRepository;

    /**
     * 构造方法。
     */
    @Autowired
    public DrawingSuspendDelegate(BpmActivityInstanceRepository bpmActInstRepository,
                                  DrawingRepository drawingRepository,
                                  BpmRuTaskRepository ruTaskRepository,
                                  SubDrawingRepository subDrawingRepository,
                                  StringRedisTemplate stringRedisTemplate,
                                  BpmActivityInstanceStateRepository bpmActivityInstanceStateRepository,
                                  DrawingDetailRepository drawingDetailRepository) {
        super(bpmActInstRepository, stringRedisTemplate, ruTaskRepository, bpmActivityInstanceStateRepository);
        this.drawingRepository = drawingRepository;
        this.subDrawingRepository = subDrawingRepository;
        this.drawingDetailRepository = drawingDetailRepository;
    }

    /**
     * 预处理。
     *
     * @param contextDTO
     * @param orgId
     * @param projectId
     * @param actInstSuspendDTO
     * @return
     */
    @Override
    public SuspendDTO suspendExecute(ContextDTO contextDTO, Long orgId, Long projectId, ActInstSuspendDTO actInstSuspendDTO) {

        Long drawingId = actInstSuspendDTO.getActInst().getEntityId();
        if (drawingId == null || drawingId.equals("")) {
            return null;
        }
        Drawing drawing = drawingRepository.findByOrgIdAndProjectIdAndId(orgId, projectId, drawingId);
        if (drawing == null) {
            return null;
        }
        Long actInstId = actInstSuspendDTO.getActInst().getId();
        if (actInstId == null || actInstId.equals("")) {
            return null;
        }

        subDrawingRepository.deleteSubDrawing(orgId, projectId, drawingId, actInstId);

        if (!("DRAWING-REDMARK".equals(actInstSuspendDTO.getActInst().getProcess()))) {

            List<EntityStatus> statusList = new ArrayList<>();

            statusList.add(EntityStatus.ACTIVE);
            List<DrawingDetail> drawingDetails = drawingDetailRepository.findByOrgIdAndProjectIdAndDrawingIdAndStatusIn(orgId, projectId, drawingId, statusList);
            if (drawingDetails != null && drawingDetails.size() > 0) {
                drawing.setLatestApprovedRev(drawingDetails.get(0).getRev());
                drawing.setLatestRev(drawingDetails.get(0).getRev());
                drawing.setLocked(true);
                drawingRepository.save(drawing);
            }else{
                drawing.setLatestApprovedRev(null);
                drawing.setLatestRev(null);
                drawing.setLocked(true);
                drawingRepository.save(drawing);
            }


            DrawingDetail opD = drawingDetailRepository
                .findByDrawingIdAndRev(drawing.getId(), actInstSuspendDTO.getActInst().getVersion()).orElse(null);

            opD.setStatus(EntityStatus.DELETED);
            drawingDetailRepository.save(opD);

        }

        return new SuspendDTO();
    }

}
