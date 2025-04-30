package com.ose.tasks.domain.model.service.delegate;

import com.ose.dto.ContextDTO;
import com.ose.exception.BusinessError;
import com.ose.material.api.MmReleaseReceiveFeignAPI;
import com.ose.material.dto.MmReleaseReceiveUpdateRunningStatusDTO;
import com.ose.material.entity.MmReleaseReceiveEntity;
import com.ose.tasks.domain.model.repository.bpm.BpmActivityInstanceRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmActivityInstanceStateRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmRuTaskRepository;
import com.ose.tasks.dto.bpm.ExecResultDTO;
import com.ose.tasks.vo.bpm.BpmCode;
import com.ose.vo.EntityStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import java.util.*;

/**
 * 检查 节点 代理。
 */
@Component
public class MaterialReleaseReceiveRunningStatusDelegate extends BaseBpmTaskDelegate implements BaseBpmTaskInterfaceDelegate {

    private final MmReleaseReceiveFeignAPI mmReleaseReceiveFeignAPI;
    /**
     * 构造方法。
     */
    @Autowired
    public MaterialReleaseReceiveRunningStatusDelegate(BpmActivityInstanceRepository bpmActInstRepository,
                                                       BpmRuTaskRepository ruTaskRepository,
                                                       StringRedisTemplate stringRedisTemplate,
                                                       BpmActivityInstanceStateRepository bpmActivityInstanceStateRepository,
                                                       MmReleaseReceiveFeignAPI mmReleaseReceiveFeignAPI) {
        super(bpmActInstRepository, stringRedisTemplate, ruTaskRepository, bpmActivityInstanceStateRepository);
        this.mmReleaseReceiveFeignAPI = mmReleaseReceiveFeignAPI;
    }

    /**
     * 预处理。
     *
     * @param contextDTO
     * @param data
     * @param execResult
     * @return
     */
    @Override
    public ExecResultDTO preExecute(ContextDTO contextDTO, Map<String, Object> data, ExecResultDTO execResult) {

        Map<String, Object> variables = execResult.getVariables();
        Long orgId = (Long) data.get("orgId");
        Long projectId = (Long) data.get("projectId");

        Long entityId = execResult.getActInst().getEntityId();

        MmReleaseReceiveEntity mmReleaseReceiveEntity = mmReleaseReceiveFeignAPI.detail(
            orgId,
            projectId,
            entityId
        ).getData();

        if (mmReleaseReceiveEntity != null && execResult.getTodoTaskDTO() != null && execResult.getTodoTaskDTO().getCommand() != null
            && BpmCode.EXCLUSIVE_GATEWAY_RESULT_ACCEPT.equals(execResult.getTodoTaskDTO().getCommand().get(BpmCode.EXCLUSIVE_GATEWAY_RESULT))) {

            // 完成当前入库单的入库操作
            MmReleaseReceiveUpdateRunningStatusDTO dto = new MmReleaseReceiveUpdateRunningStatusDTO();
            dto.setRunningStatus(EntityStatus.APPROVED);
            dto.setReceiveFinished(true);
            mmReleaseReceiveFeignAPI.updateRunningStatus(orgId, projectId, entityId, dto);
        }

        return execResult;
    }
}
