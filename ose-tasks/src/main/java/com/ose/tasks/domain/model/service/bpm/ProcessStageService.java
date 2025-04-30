package com.ose.tasks.domain.model.service.bpm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.ose.dto.BaseDTO;
import com.ose.tasks.entity.RatedTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.ose.dto.PageDTO;
import com.ose.tasks.domain.model.repository.bpm.BpmProcessRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmProcessStageRepository;
import com.ose.tasks.dto.bpm.ProcessStageDTO;
import com.ose.tasks.entity.bpm.BpmProcess;
import com.ose.tasks.entity.bpm.BpmProcessStage;
import com.ose.util.BeanUtils;
import com.ose.vo.EntityStatus;

@Component
public class ProcessStageService implements ProcessStageInterface {


    /**
     * 工序阶段 操作仓库
     */
    private final BpmProcessStageRepository processStageRepository;

    private final BpmProcessRepository processRepository;

    /**
     * 构造方法
     * <p>
     * 工序阶段 操作仓库
     */
    @Autowired
    public ProcessStageService(BpmProcessStageRepository processStageRepository,
                               BpmProcessRepository processRepository) {
        this.processStageRepository = processStageRepository;
        this.processRepository = processRepository;
    }

    @Override
    public BpmProcessStage create(ProcessStageDTO processStageDTO, Long projectId, Long orgId) {
        BpmProcessStage step = BeanUtils.copyProperties(processStageDTO, new BpmProcessStage());
        step.setProjectId(projectId);
        step.setOrgId(orgId);
        step.setCreatedAt();
        step.setStatus(EntityStatus.ACTIVE);
        return processStageRepository.save(step);
    }

    @Override
    public Page<BpmProcessStage> getList(PageDTO page, Long
        projectId, Long orgId) {
        return processStageRepository.findByStatusAndProjectIdAndOrgId(EntityStatus.ACTIVE, projectId, orgId, page.toPageable());
    }

    @Override
    public boolean delete(Long id, Long projectId, Long orgId) {
        Optional<BpmProcessStage> optionalSage = processStageRepository.findById(id);
        if (optionalSage.isPresent()) {
            BpmProcessStage stage = optionalSage.get();
            stage.setStatus(EntityStatus.DELETED);
            stage.setLastModifiedAt();
            processStageRepository.save(stage);
            return true;
        }
        return false;
    }

    @Override
    public BpmProcessStage modify(Long id, ProcessStageDTO processStageDTO, Long projectId, Long orgId) {
        Optional<BpmProcessStage> result = processStageRepository.findById(id);
        if (result.isPresent()) {
            BpmProcessStage stage = BeanUtils.copyProperties(processStageDTO, result.get());
            stage.setLastModifiedAt();
            return processStageRepository.save(stage);
        }
        return null;
    }

    @Override
    public BpmProcessStage getStage(Long id, Long projectId, Long orgId) {
        Optional<BpmProcessStage> result = processStageRepository.findById(id);
        if (result.isPresent()) {
            return result.get();
        }
        return null;
    }

    @Override
    public List<BpmProcess> getProcessesByStageId(Long id) {
        return processRepository.findProcessesByStageId(id);
    }

    /**
     * 设置返回结果引用数据。
     *
     * @param included 引用数据映射表
     * @param entities 查询结果列表
     * @param <T>      返回结果类型
     * @return 引用数据映射表
     */
    @Override
    public <T extends BaseDTO> Map<Long, Object> setIncluded(
        Map<Long, Object> included,
        List<T> entities
    ) {

        if (entities == null || entities.size() == 0) {
            return included;
        }

        List<Long> processStageIDs = new ArrayList<>();

        for (T entity : entities) {

            if (entity instanceof RatedTime && ((RatedTime) entity).getProcessStageId() != null) {
                processStageIDs.add(((RatedTime) entity).getProcessStageId());
            }
        }

        List<BpmProcessStage> processStages = processStageRepository.findByIdIn(processStageIDs);

        for (BpmProcessStage processStage : processStages) {
            included.put(processStage.getId(), processStage);
        }

        return included;
    }
}
