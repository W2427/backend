package com.ose.tasks.domain.model.service.bpm;

import com.ose.dto.OperatorDTO;
import com.ose.dto.PageDTO;
import com.ose.exception.BusinessError;
import com.ose.exception.NotFoundError;
import com.ose.tasks.domain.model.repository.bpm.BpmExecuteCaseRepository;
import com.ose.tasks.dto.bpm.BpmExecuteCaseCreateDTO;
import com.ose.tasks.dto.bpm.BpmExecuteCaseDTO;
import com.ose.tasks.entity.bpm.BpmExecuteCase;
import com.ose.util.BeanUtils;
import com.ose.vo.EntityStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * 任务处理case服务
 */
@Component
public class BpmExecuteCaseService implements BpmExecuteCaseInterface {

    private final BpmExecuteCaseRepository bpmExecuteCaseRepository;

    /**
     * 构造方法。
     */
    @Autowired
    public BpmExecuteCaseService(
        BpmExecuteCaseRepository bpmExecuteCaseRepository
    ) {
        this.bpmExecuteCaseRepository = bpmExecuteCaseRepository;
    }

    /**
     * 查找任务处理case列表。
     *
     * @param bpmExecuteCaseDTO 查询参数
     * @param pageDTO           分页参数
     * @return
     */
    @Override
    public Page<BpmExecuteCase> list(
        BpmExecuteCaseDTO bpmExecuteCaseDTO,
        PageDTO pageDTO
    ) {
        return bpmExecuteCaseRepository.list(bpmExecuteCaseDTO, pageDTO.toPageable());
    }

    /**
     * 查找特殊任务处理的特殊case。
     *
     * @param bpmExecuteCaseDTO 查询参数
     * @return
     */
    @Override
    public List<BpmExecuteCase> search(
        BpmExecuteCaseDTO bpmExecuteCaseDTO
    ) {
        return bpmExecuteCaseRepository.search(bpmExecuteCaseDTO);
    }

    /**
     * 创建任务处理case。
     *
     * @param bpmExecuteCaseCreateDTO 传输对象
     * @param operatorDTO             操作人信息
     * @return
     */
    @Override
    public BpmExecuteCase add(
        BpmExecuteCaseCreateDTO bpmExecuteCaseCreateDTO,
        OperatorDTO operatorDTO
    ) {
        BpmExecuteCase bpmExecuteCase = new BpmExecuteCase();

        BeanUtils.copyProperties(bpmExecuteCaseCreateDTO, bpmExecuteCase);

        Date now = new Date();
        bpmExecuteCase.setCreatedBy(operatorDTO.getId());
        bpmExecuteCase.setCreatedAt(now);
        bpmExecuteCase.setLastModifiedBy(operatorDTO.getId());
        bpmExecuteCase.setLastModifiedAt(now);
        bpmExecuteCase.setStatus(EntityStatus.ACTIVE);

        bpmExecuteCaseRepository.save(bpmExecuteCase);

        return bpmExecuteCase;
    }

    /**
     * 任务处理case详情。
     *
     * @param orgId            组织id
     * @param projectId        项目id
     * @param bpmExecuteCaseId 任务处理caseid
     * @return
     */
    @Override
    public BpmExecuteCase detail(
        Long orgId,
        Long projectId,
        Long bpmExecuteCaseId
    ) {
        BpmExecuteCase bpmExecuteCase = bpmExecuteCaseRepository.findByOrgIdAndProjectIdAndIdAndDeletedIsFalse(orgId, projectId, bpmExecuteCaseId);
        if (bpmExecuteCase == null) {
            throw new BusinessError("Bpm Execute Case not found");
        }
        return bpmExecuteCase;
    }

    /**
     * 更新任务处理case详情。
     *
     * @param bpmExecuteCaseId        任务处理caseid
     * @param bpmExecuteCaseCreateDTO 传输对象
     * @param operatorDTO             操作人
     * @return
     */
    @Override
    public BpmExecuteCase edit(
        Long bpmExecuteCaseId,
        BpmExecuteCaseCreateDTO bpmExecuteCaseCreateDTO,
        OperatorDTO operatorDTO
    ) {
        BpmExecuteCase bpmExecuteCase = bpmExecuteCaseRepository.findByOrgIdAndProjectIdAndIdAndDeletedIsFalse(bpmExecuteCaseCreateDTO.getOrgId(), bpmExecuteCaseCreateDTO.getProjectId(), bpmExecuteCaseId);
        if (bpmExecuteCase == null) {
            throw new BusinessError("Bpm Execute Case not found");
        }

        BeanUtils.copyProperties(bpmExecuteCaseCreateDTO, bpmExecuteCase);

        Date now = new Date();
        bpmExecuteCase.setCreatedBy(operatorDTO.getId());
        bpmExecuteCase.setCreatedAt(now);
        bpmExecuteCase.setLastModifiedBy(operatorDTO.getId());
        bpmExecuteCase.setLastModifiedAt(now);
        bpmExecuteCase.setStatus(EntityStatus.ACTIVE);

        bpmExecuteCaseRepository.save(bpmExecuteCase);

        return bpmExecuteCase;
    }

    /**
     * 删除任务处理case。
     *
     * @param orgId            组织id
     * @param projectId        项目id
     * @param bpmExecuteCaseId 任务处理caseid
     * @param operatorDTO      操作人
     * @return
     */
    @Override
    public BpmExecuteCase delete(
        Long orgId,
        Long projectId,
        Long bpmExecuteCaseId,
        OperatorDTO operatorDTO
    ) {

        BpmExecuteCase bpmExecuteCase = bpmExecuteCaseRepository.findByOrgIdAndProjectIdAndIdAndDeletedIsFalse(orgId, projectId, bpmExecuteCaseId);
        if (bpmExecuteCase == null) {
            throw new NotFoundError("Bpm Execute Case is not found");
        }

        Date now = new Date();
        bpmExecuteCase.setStatus(EntityStatus.DELETED);
        bpmExecuteCase.setLastModifiedBy(operatorDTO.getId());
        bpmExecuteCase.setLastModifiedAt(now);
        bpmExecuteCase.setDeletedBy(operatorDTO.getId());
        bpmExecuteCase.setDeletedAt(now);
        bpmExecuteCase.setDeleted(true);

        bpmExecuteCaseRepository.save(bpmExecuteCase);
        return bpmExecuteCase;
    }

}
