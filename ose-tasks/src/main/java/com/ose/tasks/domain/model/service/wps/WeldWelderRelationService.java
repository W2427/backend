package com.ose.tasks.domain.model.service.wps;
import com.ose.dto.ContextDTO;
import com.ose.exception.BusinessError;
import com.ose.tasks.domain.model.repository.wbs.piping.WeldEntityRepository;
import com.ose.tasks.domain.model.repository.wbs.structure.StructureWeldEntityRepository;
import com.ose.tasks.domain.model.repository.wps.WeldWelderRelationRepository;
import com.ose.tasks.dto.wps.WeldWelderRelationCreateDTO;
import com.ose.tasks.dto.wps.WeldWelderRelationCreateItemDTO;
import com.ose.tasks.dto.wps.WeldWelderRelationSearchDTO;
import com.ose.tasks.entity.wbs.entity.WeldEntityBase;
import com.ose.tasks.entity.wbs.structureEntity.StructureWeldEntity;
import com.ose.tasks.entity.wps.WeldWelderRelation;
import com.ose.vo.EntityStatus;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class WeldWelderRelationService implements WeldWelderRelationInterface {

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private final static Logger logger = LoggerFactory.getLogger(WeldWelderRelationService.class);

    private final WeldWelderRelationRepository weldWelderRelationRepository;

    private final WeldEntityRepository weldEntityRepository;

    private final StructureWeldEntityRepository structureWeldEntityRepository;

    @Autowired
    public WeldWelderRelationService(
        WeldWelderRelationRepository weldWelderRelationRepository,
        WeldEntityRepository weldEntityRepository,
        StructureWeldEntityRepository structureWeldEntityRepository
    ) {
        this.weldWelderRelationRepository = weldWelderRelationRepository;
        this.weldEntityRepository = weldEntityRepository;
        this.structureWeldEntityRepository = structureWeldEntityRepository;
    }

    /**
     * 创建焊口焊工关系
     *
     * @param orgId                       组织ID
     * @param projectId                   项目ID
     * @param context
     * @param weldWelderRelationCreateDTO 创建信息
     */
    @Override
    @Transactional
    public Boolean create(
        Long orgId,
        Long projectId,
        ContextDTO context,
        WeldWelderRelationCreateDTO weldWelderRelationCreateDTO
    ) {

        Boolean result = true;
        try {
            if (weldWelderRelationCreateDTO.getWeldWelderRelation() == null || weldWelderRelationCreateDTO.getWeldWelderRelation().size() == 0) {
                throw new BusinessError("焊口焊工关系数组不为空");
            }

            List<Long> weldList = new ArrayList<>();
            List<Long> wwList = new ArrayList<>();
            for (int i=0; i<weldWelderRelationCreateDTO.getWeldWelderRelation().size(); i++) {
                weldList.add(weldWelderRelationCreateDTO.getWeldWelderRelation().get(i).getWeldId());
            }
            if (weldList != null && weldList.size() > 0) {
                wwList = weldList.stream().distinct().collect(Collectors.toList());
            }
            for (int i=0; i<wwList.size(); i++) {

                Integer num = 0;
                if (weldWelderRelationCreateDTO.getDiscipline().toString().equals("PIPING")) {
                    WeldEntityBase weldEntityBase = weldEntityRepository.findByIdAndDeletedIsFalse(wwList.get(i));
                    if (weldEntityBase == null) {
                        throw new BusinessError("管系焊口不存在");
                    } else {
                        num = weldEntityBase.getRepairCount();
                    }
                    List<WeldWelderRelation> weldWelderRelationRuleList = weldWelderRelationRepository.findByOrgIdAndProjectIdAndWeldIdAndRepairCountAndStatus(
                        orgId,
                        projectId,
                        weldEntityBase.getId(),
                        num,
                        EntityStatus.ACTIVE
                    );
                    if (weldWelderRelationRuleList != null && weldWelderRelationRuleList.size() != 0) {
                        for (WeldWelderRelation wwRelation:
                            weldWelderRelationRuleList) {
                            wwRelation.setStatus(EntityStatus.DELETED);
                            weldWelderRelationRepository.save(wwRelation);
                        }
                    }
                } else if (weldWelderRelationCreateDTO.getDiscipline().toString().equals("STRUCTURE")) {
                    StructureWeldEntity sWeldEntityBase = structureWeldEntityRepository.findByIdAndDeletedIsFalse(wwList.get(i));
                    if (sWeldEntityBase == null) {
                        throw new BusinessError("结构焊口不存在");
                    } else {
                        num = sWeldEntityBase.getRepairCount();
                    }
                    List<WeldWelderRelation> weldWelderRelationRuleList = weldWelderRelationRepository.findByOrgIdAndProjectIdAndWeldIdAndRepairCountAndStatus(
                        orgId,
                        projectId,
                        sWeldEntityBase.getId(),
                        num,
                        EntityStatus.ACTIVE
                    );
                    if (weldWelderRelationRuleList != null && weldWelderRelationRuleList.size() != 0) {
                        for (WeldWelderRelation wwRelation:
                            weldWelderRelationRuleList) {
                            wwRelation.setStatus(EntityStatus.DELETED);
                            weldWelderRelationRepository.save(wwRelation);
                        }
                    }
                }

                for (WeldWelderRelationCreateItemDTO weldWelderRelationCreateItemDTO : weldWelderRelationCreateDTO.getWeldWelderRelation()) {
                    if (weldWelderRelationCreateItemDTO.getWeldId().equals(wwList.get(i))) {
                        WeldWelderRelation weldWelderRelation = new WeldWelderRelation();
                        weldWelderRelation.setOrgId(orgId);
                        weldWelderRelation.setProjectId(projectId);
                        weldWelderRelation.setWelderId(weldWelderRelationCreateItemDTO.getWelderId());
                        weldWelderRelation.setWelderNo(weldWelderRelationCreateItemDTO.getWelderNo());
                        weldWelderRelation.setWeldId(weldWelderRelationCreateItemDTO.getWeldId());
                        weldWelderRelation.setWeldNo(weldWelderRelationCreateItemDTO.getWeldNo());
                        weldWelderRelation.setProcess(weldWelderRelationCreateItemDTO.getProcess());
                        weldWelderRelation.setStatus(EntityStatus.ACTIVE);
                        weldWelderRelation.setCreatedAt(new Date());
                        weldWelderRelation.setCreatedBy(context.getOperator().getId());
                        weldWelderRelation.setLastModifiedAt(new Date());
                        weldWelderRelation.setLastModifiedBy(context.getOperator().getId());
                        weldWelderRelation.setRepairCount(num);
                        weldWelderRelation.setDiscipline(weldWelderRelationCreateDTO.getDiscipline());
                        weldWelderRelation.setWeldTime(sdf.parse(weldWelderRelationCreateDTO.getWeldTime()));
                        weldWelderRelationRepository.save(weldWelderRelation);
                    }
                }
            }
        } catch (Exception e) {
            result = false;
        }
        return result;
    }


    /**
     * 查询焊口焊工管系
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     */
    @Override
    public Page<WeldWelderRelation> search(
        Long orgId,
        Long projectId,
        WeldWelderRelationSearchDTO weldWelderRelationSearchDTO
    ) {
        return weldWelderRelationRepository.search(orgId, projectId, weldWelderRelationSearchDTO);
    }


}
