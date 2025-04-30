package com.ose.tasks.domain.model.service;

import com.ose.exception.BusinessError;
import com.ose.exception.NotFoundError;
import com.ose.tasks.domain.model.repository.ItpRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmProcessRepository;
import com.ose.tasks.dto.ItpCreateDTO;
import com.ose.tasks.dto.ItpCriteriaDTO;
import com.ose.tasks.dto.ItpUpdateDTO;
import com.ose.tasks.entity.Itp;
import com.ose.tasks.entity.bpm.BpmProcess;
import com.ose.util.BeanUtils;
import com.ose.vo.EntityStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ItpService implements ItpInterface {

    private ItpRepository itpRepository;
    private BpmProcessRepository bpmProcessRepository;

    @Autowired
    public ItpService(
        ItpRepository itpRepository,
        BpmProcessRepository bpmProcessRepository
    ) {
        this.itpRepository = itpRepository;
        this.bpmProcessRepository = bpmProcessRepository;
    }

    /**
     * 创建ITP信息。
     *
     * @param operatorId   操作人ID
     * @param orgId        组织ID
     * @param projectId    项目ID
     * @param itpCreateDTO 创建ITP信息
     */
    @Override
    public void create(
        Long operatorId,
        Long orgId,
        Long projectId,
        ItpCreateDTO itpCreateDTO
    ) {
        if (itpCreateDTO.getEntityType() == null) {
            throw new BusinessError("wbsEntityType is required");
        }
        if (itpCreateDTO.getProcessStageId() == null) {
            throw new BusinessError("processStageId is required");
        }
        if (itpCreateDTO.getProcessId() == null) {
            throw new BusinessError("processId is required");
        }
        if (itpCreateDTO.getInternalInspection() == null) {
            throw new BusinessError("internalInspection is required");
        }
        if (itpCreateDTO.getOwnerInspection() == null) {
            throw new BusinessError("ownerInspection is required");
        }
        if (itpCreateDTO.getThirdPartyInspection() == null) {
            throw new BusinessError("thirdPartyInspection is required");
        }
        if (itpCreateDTO.getOtherInspection() == null) {
            throw new BusinessError("otherInspection is required");
        }

        Itp itp = new Itp();
        BeanUtils.copyProperties(itpCreateDTO, itp);
        Date now = new Date();

        itp.setOrgId(orgId);
        itp.setProjectId(projectId);
        itp.setCreatedAt(now);
        itp.setCreatedBy(operatorId);
        itp.setLastModifiedAt(now);
        itp.setLastModifiedBy(operatorId);
        itp.setStatus(EntityStatus.ACTIVE);

        itpRepository.save(itp);

    }

    /**
     * 获取ITP列表。
     *
     * @param orgId          组织ID
     * @param projectId      项目ID
     * @param itpCriteriaDTO 查询条件
     * @return ITP列表
     */
    @Override
    public Page<Itp> search(Long orgId, Long projectId, ItpCriteriaDTO itpCriteriaDTO) {

        Page<Itp> itps = itpRepository.search(orgId, projectId, itpCriteriaDTO);


        List<Long> processIds = new ArrayList<>();



        for (Itp itp : itps.getContent()) {
        }


        Map<Long, BpmProcess> processMap = new HashMap<>();
        List<BpmProcess> processes = bpmProcessRepository.findByIdIn(processIds);
        if (processes != null && processes.size() > 0) {
            for (BpmProcess bpmProcess : processes) {
                processMap.put(bpmProcess.getId(), bpmProcess);
            }
        }

        /*

        Map<String, BpmProcessStage> processStageMap = new HashMap<>();
        List<BpmProcessStage> processStages = bpmProcessStageRepository.findByIdIn(processStageIds);
        if (processStages != null && processStages.size() > 0) {
            for (BpmProcessStage bpmProcessStage : processStages) {
                processStageMap.put(bpmProcessStage.getId(), bpmProcessStage);
            }
        }
        */


        return itps;
    }

    /**
     * 获取ITP详情。
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param itpId     ITPID
     * @return ITP详情
     */
    @Override
    public Itp get(Long orgId, Long projectId, Long itpId) {

        Itp itp = itpRepository.findByIdAndDeletedIsFalse(itpId);

        if (itp == null) {
            throw new NotFoundError("ITP is not found");
        }





        return itp;
    }

    /**
     * 更新ITP信息。
     *
     * @param operatorId   操作人ID
     * @param orgId        组织ID
     * @param projectId    项目ID
     * @param itpId        ITPID
     * @param itpUpdateDTO 更新ITP信息
     */
    @Override
    public void update(
        Long operatorId,
        Long orgId,
        Long projectId,
        Long itpId,
        ItpUpdateDTO itpUpdateDTO
    ) {
        Itp itp = itpRepository.findByIdAndDeletedIsFalse(itpId);

        if (itp == null) {
            throw new NotFoundError("ITP is not found");
        }

        itp.setLastModifiedBy(operatorId);
        itp.setLastModifiedAt(new Date());

        itpRepository.save(itp);
    }


    /**
     * 删除ITP信息。
     *
     * @param operatorId 操作人ID
     * @param orgId      组织ID
     * @param projectId  项目ID
     * @param itpId      ITPID
     */
    @Override
    public void delete(Long operatorId, Long orgId, Long projectId, Long itpId) {

        Itp itp = itpRepository.findByIdAndDeletedIsFalse(itpId);

        if (itp == null) {
            throw new NotFoundError("ITP is not found");
        }

        Date now = new Date();

        itp.setLastModifiedAt(now);
        itp.setLastModifiedBy(operatorId);
        itp.setDeletedAt(now);
        itp.setDeletedBy(operatorId);
        itp.setDeleted(true);
        itp.setStatus(EntityStatus.DELETED);

        itpRepository.save(itp);
    }
}
