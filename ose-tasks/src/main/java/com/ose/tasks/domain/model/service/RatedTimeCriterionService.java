package com.ose.tasks.domain.model.service;

import com.ose.exception.BusinessError;
import com.ose.exception.NotFoundError;
import com.ose.tasks.domain.model.repository.RatedTimeCriterionRepository;
import com.ose.tasks.domain.model.repository.RatedTimeRepository;
import com.ose.tasks.dto.RatedTimeCriterionCriteriaDTO;
import com.ose.tasks.dto.RatedTimeCriterionDTO;
import com.ose.tasks.entity.RatedTimeCriterion;
import com.ose.tasks.entity.RatedTime;
import com.ose.tasks.vo.RatedTimeUnit;
import com.ose.util.BeanUtils;
import com.ose.vo.EntityStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class RatedTimeCriterionService implements RatedTimeCriterionInterface {

    private RatedTimeCriterionRepository ratedTimeCriterionRepository;
    private RatedTimeRepository ratedTimeRepository;

    @Autowired
    public RatedTimeCriterionService(
        RatedTimeRepository ratedTimeRepository,
        RatedTimeCriterionRepository ratedTimeCriterionRepository
    ) {
        this.ratedTimeRepository = ratedTimeRepository;
        this.ratedTimeCriterionRepository = ratedTimeCriterionRepository;
    }

    /**
     * 创建定额工时查询条件。
     *
     * @param operatorId            操作人ID
     * @param orgId                 组织ID
     * @param projectId             项目ID
     * @param processStageId        工序阶段ID
     * @param processId             工序ID
     * @param entitySubTypeId      实体类型ID
     * @param ratedTimeCriterionDTO 创建信息
     */
    @Override
    public void create(
        Long operatorId,
        Long orgId,
        Long projectId,
        Long processStageId,
        Long processId,
        Long entitySubTypeId,
        RatedTimeCriterionDTO ratedTimeCriterionDTO) {

        List<RatedTimeCriterion> ratedTimeCriterionList = ratedTimeCriterionRepository.findByOrgIdAndProjectIdAndDeletedIsFalse(orgId, projectId);
        for (RatedTimeCriterion ratedTimeCriterionItem : ratedTimeCriterionList) {
            if (ratedTimeCriterionItem.getProcessStageId().equals(processStageId) &&
                ratedTimeCriterionItem.getProcessId().equals(processId) &&
                ratedTimeCriterionItem.getEntitySubTypeId().equals(entitySubTypeId)) {
                throw new BusinessError("group is already exists");
            }
        }

        RatedTimeCriterion ratedTimeCriterion = new RatedTimeCriterion();

        BeanUtils.copyProperties(ratedTimeCriterionDTO, ratedTimeCriterion);

        ratedTimeCriterion.setOrgId(orgId);
        ratedTimeCriterion.setProjectId(projectId);
        ratedTimeCriterion.setProcessStageId(processStageId);
        ratedTimeCriterion.setProcessId(processId);
        ratedTimeCriterion.setEntitySubTypeId(entitySubTypeId);

        Date now = new Date();

        ratedTimeCriterion.setCreatedBy(operatorId);
        ratedTimeCriterion.setCreatedAt(now);
        ratedTimeCriterion.setLastModifiedBy(operatorId);
        ratedTimeCriterion.setLastModifiedAt(now);
        ratedTimeCriterion.setStatus(EntityStatus.ACTIVE);

        ratedTimeCriterionRepository.save(ratedTimeCriterion);
    }

    /**
     * 更新定额工时查询信息。
     *
     * @param operatorId            操作人ID
     * @param orgId                 组织ID
     * @param projectId             项目ID
     * @param ratedTimeCriterionId  定额工时查询条件ID
     * @param ratedTimeCriterionDTO 更新信息
     */
    @Override
    public void update(
        Long operatorId,
        Long orgId,
        Long projectId,
        Long ratedTimeCriterionId,
        RatedTimeCriterionDTO ratedTimeCriterionDTO
    ) {
        List<RatedTime> ratedTimeList = ratedTimeRepository.findByRatedTimeCriterionIdAndDeletedIsFalse(ratedTimeCriterionId);
        if (ratedTimeList.size() > 0) {
            throw new BusinessError("ratedTimeCriterion with ratedTime cannot be updated ");
        } else {
            RatedTimeCriterion ratedTimeCriterion = ratedTimeCriterionRepository
                .findByIdAndDeletedIsFalse(ratedTimeCriterionId);

            if (ratedTimeCriterion == null) {
                throw new NotFoundError("ratedTimeCriterion is not found");
            }

            BeanUtils.copyProperties(ratedTimeCriterionDTO, ratedTimeCriterion);



            for (RatedTime ratedTimeItem : ratedTimeList) {
                if (ratedTimeCriterionDTO.getUnitM() && !ratedTimeCriterionDTO.getUnitN()) {
                    ratedTimeItem.setUnit(RatedTimeUnit.M);
                }
                if (ratedTimeCriterionDTO.getUnitN() && !ratedTimeCriterionDTO.getUnitM()) {
                    ratedTimeItem.setUnit(RatedTimeUnit.N);
                }
                if (!ratedTimeCriterionDTO.getUnitN() && !ratedTimeCriterionDTO.getUnitM()) {
                    ratedTimeItem.setUnit("");
                }
                ratedTimeRepository.save(ratedTimeItem);
            }

            ratedTimeCriterion.setLastModifiedAt(new Date());
            ratedTimeCriterion.setLastModifiedBy(operatorId);

            ratedTimeCriterionRepository.save(ratedTimeCriterion);
        }

    }

    /**
     * 获取定额工时查询条件列表。
     *
     * @param orgId                         组织ID
     * @param projectId                     项目ID
     * @param ratedTimeCriterionCriteriaDTO 查询条件
     * @return 定额工时查询条件列表
     */
    @Override
    public Page<RatedTimeCriterion> search(
        Long orgId,
        Long projectId,
        RatedTimeCriterionCriteriaDTO ratedTimeCriterionCriteriaDTO) {

        return ratedTimeCriterionRepository.search(orgId, projectId, ratedTimeCriterionCriteriaDTO);
    }

    /**
     * 获取定额工时查询条件详情。
     *
     * @param orgId                组织ID
     * @param projectId            项目ID
     * @param ratedTimeCriterionId 定额工时查询条件ID
     * @return 定额工时查询条件详情
     */
    @Override
    public RatedTimeCriterion get(Long orgId, Long projectId, Long ratedTimeCriterionId) {

        RatedTimeCriterion ratedTimeCriterion = ratedTimeCriterionRepository
            .findByIdAndDeletedIsFalse(ratedTimeCriterionId);

        if (ratedTimeCriterion == null) {
            throw new NotFoundError("ratedTimeCriterion is not found");
        }
        return ratedTimeCriterion;
    }

    /**
     * 删除定额工时信息。
     *
     * @param operatorId           操作人ID
     * @param orgId                组织ID
     * @param projectId            项目ID
     * @param ratedTimeCriterionId 定额工时查询条件ID
     */
    @Override
    public void delete(Long operatorId, Long orgId, Long projectId, Long ratedTimeCriterionId) {

        List<RatedTime> ratedTimeList = ratedTimeRepository
            .findByRatedTimeCriterionIdAndDeletedIsFalse(ratedTimeCriterionId);
        if (ratedTimeList.size() > 0) {
            throw new BusinessError("ratedTimeCriterion with ratedTime cannot be deleted ");
        } else {
            RatedTimeCriterion ratedTimeCriterion = ratedTimeCriterionRepository
                .findByIdAndDeletedIsFalse(ratedTimeCriterionId);

            if (ratedTimeCriterion == null) {
                throw new NotFoundError("ratedTimeCriterion is not found");
            }
            Date now = new Date();
            ratedTimeCriterion.setStatus(EntityStatus.DELETED);
            ratedTimeCriterion.setDeletedAt(now);
            ratedTimeCriterion.setDeletedBy(operatorId);
            ratedTimeCriterion.setDeleted(true);
            ratedTimeCriterion.setLastModifiedAt(now);
            ratedTimeCriterion.setLastModifiedBy(operatorId);

            ratedTimeCriterionRepository.save(ratedTimeCriterion);
        }
    }
}
