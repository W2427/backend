package com.ose.tasks.domain.model.service.wps.simple;

import com.google.common.collect.Sets;
import com.ose.dto.ContextDTO;
import com.ose.exception.BusinessError;
import com.ose.tasks.domain.model.repository.wbs.piping.WeldEntityRepository;
import com.ose.tasks.domain.model.repository.wps.simple.WelderGradeSimpleRepository;
import com.ose.tasks.domain.model.repository.wps.simple.WelderGradeWpsRelationimpleRepository;
import com.ose.tasks.domain.model.repository.wps.simple.WpsSimpleRepository;
import com.ose.tasks.dto.wps.simple.*;
import com.ose.tasks.entity.wbs.entity.WeldEntity;
import com.ose.tasks.entity.wps.simple.WelderGradeSimplified;
import com.ose.tasks.entity.wps.simple.WelderGradeWpsSimplifiedRelation;
import com.ose.tasks.entity.wps.simple.WpsSimplified;
import com.ose.tasks.vo.WeldMaterialType;
import com.ose.vo.EntityStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.*;


@Component
public class WpsSimpleService implements WpsSimpleInterface {

    private WpsSimpleRepository wpsSimpleRepository;
    private WelderGradeWpsRelationimpleRepository welderGradeWpsRelationimpleRepository;
    private WelderGradeSimpleRepository welderGradeSimpleRepository;
    private final WeldEntityRepository weldEntityRepository;

    @Autowired
    public WpsSimpleService(
        WpsSimpleRepository wpsSimpleRepository,
        WelderGradeWpsRelationimpleRepository welderGradeWpsRelationimpleRepository,
        WelderGradeSimpleRepository welderGradeSimpleRepository,
        WeldEntityRepository weldEntityRepository
    ) {
        this.wpsSimpleRepository = wpsSimpleRepository;
        this.welderGradeWpsRelationimpleRepository = welderGradeWpsRelationimpleRepository;
        this.welderGradeSimpleRepository = welderGradeSimpleRepository;
        this.weldEntityRepository = weldEntityRepository;
    }

    /**
     * 创建Wps
     *
     * @param orgId              组织ID
     * @param projectId          项目ID
     * @param context
     * @param wpsSimpleCreateDTO 创建信息
     */
    @Override
    public void create(
        Long orgId,
        Long projectId,
        ContextDTO context,
        WpsSimpleCreateDTO wpsSimpleCreateDTO
    ) {
        WpsSimplified wpsSimplifiedById = wpsSimpleRepository.findByOrgIdAndProjectIdAndNoAndStatus(
            orgId,
            projectId,
            wpsSimpleCreateDTO.getNo(),
            EntityStatus.ACTIVE
        );
        if (wpsSimplifiedById != null) {
            throw new BusinessError("当前焊接工艺规范编号已存在");
        }
        WpsSimplified wpsSimplified = new WpsSimplified();
        wpsSimplified.setOrgId(orgId);
        wpsSimplified.setProjectId(projectId);
        wpsSimplified.setNo(wpsSimpleCreateDTO.getNo());
        String weldMaterial = "";
        for (WeldMaterialType type : wpsSimpleCreateDTO.getWeldMaterialType()) {
            if (wpsSimpleCreateDTO.getWeldMaterialType().size()-1 == wpsSimpleCreateDTO.getWeldMaterialType().indexOf(type)) {
                weldMaterial += type;
            } else {
                weldMaterial += type + ",";
            }
        }
        wpsSimplified.setWeldMaterialType(weldMaterial);
        wpsSimplified.setRemark(wpsSimpleCreateDTO.getRemark());
        wpsSimplified.setStatus(EntityStatus.ACTIVE);
        wpsSimplified.setCreatedAt(new Date());
        wpsSimplified.setCreatedBy(context.getOperator().getId());
        wpsSimplified.setLastModifiedAt(new Date());
        wpsSimplified.setLastModifiedBy(context.getOperator().getId());
        wpsSimpleRepository.save(wpsSimplified);
    }

    /**
     * 编辑Wps
     *
     * @param orgId              组织ID
     * @param projectId          项目ID
     * @param context
     * @param wpsSimpleUpdateDTO 创建信息
     */
    @Override
    public void update(
        Long orgId,
        Long projectId,
        Long wpsId,
        ContextDTO context,
        WpsSimpleUpdateDTO wpsSimpleUpdateDTO
    ) {
        WpsSimplified wpsSimplifiedById = wpsSimpleRepository.findByOrgIdAndProjectIdAndNoAndStatus(
            orgId,
            projectId,
            wpsSimpleUpdateDTO.getNo(),
            EntityStatus.ACTIVE
        );
        if (wpsSimplifiedById != null && !wpsSimplifiedById.getId().equals(wpsId)) {
            throw new BusinessError("当前焊接工艺规范编号已存在");
        }
        WpsSimplified wpsSimplified = wpsSimpleRepository.findByOrgIdAndProjectIdAndIdAndStatus(
            orgId,
            projectId,
            wpsId,
            EntityStatus.ACTIVE
        );
        if (wpsSimplified == null) {
            throw new BusinessError("当前焊接工艺规范不存在");
        }
        wpsSimplified.setOrgId(orgId);
        wpsSimplified.setProjectId(projectId);
        wpsSimplified.setNo(wpsSimpleUpdateDTO.getNo());
        String weldMaterial = "";
        for (WeldMaterialType type : wpsSimpleUpdateDTO.getWeldMaterialType()) {
            if (wpsSimpleUpdateDTO.getWeldMaterialType().size()-1 == wpsSimpleUpdateDTO.getWeldMaterialType().indexOf(type)) {
                weldMaterial += type;
            } else {
                weldMaterial += type + ",";
            }

        }
        wpsSimplified.setWeldMaterialType(weldMaterial);
        wpsSimplified.setRemark(wpsSimpleUpdateDTO.getRemark());
        wpsSimplified.setCreatedAt(new Date());
        wpsSimplified.setCreatedBy(context.getOperator().getId());
        wpsSimplified.setLastModifiedAt(new Date());
        wpsSimplified.setLastModifiedBy(context.getOperator().getId());
        wpsSimpleRepository.save(wpsSimplified);
    }

    /**
     * Wps详情
     *
     * @param orgId       组织ID
     * @param projectId   项目ID
     * @param wpsSimpleId 焊工等级id
     */
    @Override
    public WpsSimplified detail(
        Long orgId,
        Long projectId,
        Long wpsSimpleId
    ) {
        WpsSimplified wpsSimplified = wpsSimpleRepository.findByOrgIdAndProjectIdAndIdAndStatus(
            orgId,
            projectId,
            wpsSimpleId,
            EntityStatus.ACTIVE
        );
        if (wpsSimplified == null) {
            throw new BusinessError("当前焊接工艺规范不存在");
        } else {
            return wpsSimplified;
        }
    }

    /**
     * Wps列表
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     */
    @Override
    public Page<WpsSimplified> search(
        Long orgId,
        Long projectId,
        WpsSimpleSearchDTO wpsSimpleSearchDTO
    ) {
        if (wpsSimpleSearchDTO.getKeyword() != null) {
            return wpsSimpleRepository.findByOrgIdAndProjectIdAndNoLikeAndStatusOrderByNo(
                orgId,
                projectId,
                "%" + wpsSimpleSearchDTO.getKeyword() + "%",
                EntityStatus.ACTIVE,
                wpsSimpleSearchDTO.toPageable()
            );
        } else {
            return wpsSimpleRepository.findByOrgIdAndProjectIdAndStatusOrderByNo(
                orgId,
                projectId,
                EntityStatus.ACTIVE,
                wpsSimpleSearchDTO.toPageable()
            );
        }

    }

    /**
     * 删除Wps
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param wpsId     焊工等级id
     */
    @Override
    public void delete(
        Long orgId,
        Long projectId,
        Long wpsId,
        ContextDTO context
    ) {
        WpsSimplified wpsSimplified = wpsSimpleRepository.findByOrgIdAndProjectIdAndIdAndStatus(
            orgId,
            projectId,
            wpsId,
            EntityStatus.ACTIVE
        );
        if (wpsSimplified == null) {
            throw new BusinessError("当前焊接工艺规范不存在");
        }


        WpsSimpleRelationSearchDTO wpsSimpleRelationSearchDTO = new WpsSimpleRelationSearchDTO();
        wpsSimpleRelationSearchDTO.setFetchAll(true);
        Page<WelderGradeWpsSimplifiedRelation> pageList = welderGradeWpsRelationimpleRepository.findByOrgIdAndProjectIdAndWpsIdAndStatus(
            orgId,
            projectId,
            wpsId,
            EntityStatus.ACTIVE,
            wpsSimpleRelationSearchDTO.toPageable()
        );
        List<WelderGradeWpsSimplifiedRelation> list = pageList.getContent();

        if (list.size() > 0) {
            for (WelderGradeWpsSimplifiedRelation welderGradeWpsSimplifiedRelation : list) {
                welderGradeWpsSimplifiedRelation.setStatus(EntityStatus.DELETED);
                welderGradeWpsSimplifiedRelation.setLastModifiedAt(new Date());
                welderGradeWpsSimplifiedRelation.setLastModifiedBy(context.getOperator().getId());
            }
            welderGradeWpsRelationimpleRepository.saveAll(list);
        }

        wpsSimplified.setStatus(EntityStatus.DELETED);
        wpsSimplified.setLastModifiedAt(new Date());
        wpsSimplified.setLastModifiedBy(context.getOperator().getId());
        wpsSimpleRepository.save(wpsSimplified);
    }

    /**
     * 添加wps和焊工证关联信息。
     */
    @Override
    public void addWelderGrade(
        Long orgId,
        Long projectId,
        Long wpsSimpleId,
        WpsSimpleRelationDTO wpsSimpleRelationDTO,
        ContextDTO context
    ) {
        List<WelderGradeWpsSimplifiedRelation> relationList = new ArrayList<>();
        WpsSimplified wpsSimplifiedByUserId = wpsSimpleRepository.findByOrgIdAndProjectIdAndIdAndStatus(
            orgId,
            projectId,
            wpsSimpleId,
            EntityStatus.ACTIVE
        );
        if (wpsSimplifiedByUserId == null) {
            throw new BusinessError("焊工不存在");
        }

        if (wpsSimpleRelationDTO.getWelderGradeIds() == null || wpsSimpleRelationDTO.getWelderGradeIds().size() == 0) {
            throw new BusinessError("添加的焊工证不能为空");
        }
        for (Long welderGradeId : wpsSimpleRelationDTO.getWelderGradeIds()) {

            WelderGradeWpsSimplifiedRelation welderGradeWpsSimplifiedRelationNew = new WelderGradeWpsSimplifiedRelation();

            WelderGradeSimplified welderGradeSimplified = welderGradeSimpleRepository.findByOrgIdAndProjectIdAndIdAndStatus(
                orgId,
                projectId,
                welderGradeId,
                EntityStatus.ACTIVE
            );
            if (welderGradeSimplified == null) {
                throw new BusinessError("焊工证不存在");
            }
            WelderGradeWpsSimplifiedRelation welderGradeWpsSimplifiedRelation = welderGradeWpsRelationimpleRepository.findByOrgIdAndProjectIdAndWpsIdAndWelderGradeIdAndStatus(
                orgId,
                projectId,
                wpsSimpleId,
                welderGradeId,
                EntityStatus.ACTIVE
            );
            if (welderGradeWpsSimplifiedRelation != null) {
                continue;
            }
            welderGradeWpsSimplifiedRelationNew.setOrgId(orgId);
            welderGradeWpsSimplifiedRelationNew.setProjectId(projectId);
            welderGradeWpsSimplifiedRelationNew.setOrgId(orgId);
            welderGradeWpsSimplifiedRelationNew.setWelderGradeNo(welderGradeSimplified.getNo());
            welderGradeWpsSimplifiedRelationNew.setWelderGradeId(welderGradeSimplified.getId());
            welderGradeWpsSimplifiedRelationNew.setWpsId(wpsSimplifiedByUserId.getId());
            welderGradeWpsSimplifiedRelationNew.setWpsNo(wpsSimplifiedByUserId.getNo());
            welderGradeWpsSimplifiedRelationNew.setStatus(EntityStatus.ACTIVE);
            welderGradeWpsSimplifiedRelationNew.setCreatedAt(new Date());
            welderGradeWpsSimplifiedRelationNew.setCreatedBy(context.getOperator().getId());
            welderGradeWpsSimplifiedRelationNew.setLastModifiedAt(new Date());
            welderGradeWpsSimplifiedRelationNew.setLastModifiedBy(context.getOperator().getId());
            relationList.add(welderGradeWpsSimplifiedRelationNew);
        }
        welderGradeWpsRelationimpleRepository.saveAll(relationList);

    }

    /**
     * wps下的焊工证关系表。
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     */
    @Override
    public Page<WelderGradeWpsSimplifiedRelation> searchWelderGrade(
        Long orgId,
        Long projectId,
        Long wpsId,
        WpsSimpleRelationSearchDTO wpsSimpleRelationSearchDTO
    ) {
        return welderGradeWpsRelationimpleRepository.findByOrgIdAndProjectIdAndWpsIdAndStatus(
            orgId,
            projectId,
            wpsId,
            EntityStatus.ACTIVE,
            wpsSimpleRelationSearchDTO.toPageable()
        );
    }

    /**
     * 删除焊工证
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param wpsId     焊工id
     */
    @Override
    public void deleteWelderGrade(
        Long orgId,
        Long projectId,
        Long wpsId,
        Long welderGradeSimpleId,
        ContextDTO context
    ) {

        WelderGradeWpsSimplifiedRelation welderGradeWpsSimplifiedRelation = welderGradeWpsRelationimpleRepository.findByOrgIdAndProjectIdAndIdAndStatus(
            orgId,
            projectId,
            welderGradeSimpleId,
            EntityStatus.ACTIVE
        );
        if (welderGradeWpsSimplifiedRelation == null) {
            throw new BusinessError("焊工证关系不存在");
        }
        welderGradeWpsSimplifiedRelation.setStatus(EntityStatus.DELETED);
        welderGradeWpsSimplifiedRelation.setLastModifiedAt(new Date());
        welderGradeWpsSimplifiedRelation.setLastModifiedBy(context.getOperator().getId());
        welderGradeWpsRelationimpleRepository.save(welderGradeWpsSimplifiedRelation);
    }

    /**
     * 获取焊口下所有焊工等级列表
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     */
    @Override
    public List<WpsSimplified> searchWeldWps(
        Long orgId,
        Long projectId,
        WpsSimpleRelationDTO wpsSimpleRelationDTO
    ) {
        List<Long> ids = new ArrayList<>();
        Map<Long, Long> commonMap = new HashMap<>();
        if (wpsSimpleRelationDTO.getWeldIds() != null && wpsSimpleRelationDTO.getWeldIds().size() > 0) {
            List<Map<Long, Long>> mapList = new ArrayList<>();
            for (Long weldId : wpsSimpleRelationDTO.getWeldIds()) {
                Map<Long, Long> weldMap = new HashMap<>();
                WeldEntity weldEntity = weldEntityRepository.findByIdAndDeletedIsFalse(weldId);
                if (weldEntity == null) {
                    continue;
                }
                if (weldEntity.getWpsId() == null || "".equals(weldEntity.getWpsId())) {
                    continue;
                }
                String[] strArr = weldEntity.getWpsId().split(",");
                for (int i = 0; i < strArr.length; i++) {
                    weldMap.put(Long.parseLong(strArr[i]), Long.parseLong(strArr[i]));
                }
                if (!weldMap.isEmpty()) {
                    mapList.add(weldMap);
                }
            }
            if (mapList.size() > 0 && mapList.size() == wpsSimpleRelationDTO.getWeldIds().size()) {
                for (Map<Long, Long> map : mapList) {
                    if (mapList.indexOf(map) == 0) {
                        commonMap = map;
                    } else {
                        Map<Long, Long> itemMap = new HashMap<>();
                        Set<Long> mapKey = map.keySet();
                        Set<Long> commonMapKey = commonMap.keySet();
                        Set<Long> differenceSet = Sets.intersection(commonMapKey, mapKey);
                        if (differenceSet.isEmpty()) {
                            throw new BusinessError("不存在共同的WPS信息");
                        } else {

                            for (Long key : differenceSet) {
                                itemMap.put(key, key);
                            }
                            commonMap = itemMap;
                        }
                    }
                }
            } else {
                throw new BusinessError("不存在共同的WPS信息");
            }
        } else {
            throw new BusinessError("焊口不存在");
        }
        if (commonMap.isEmpty()) {
            throw new BusinessError("所选焊口未匹配WPS");
        } else {
            for (Long key : commonMap.keySet()) {
                ids.add(commonMap.get(key));
            }
        }
        return wpsSimpleRepository.findByOrgIdAndProjectIdAndIdInAndStatus(
            orgId,
            projectId,
            ids,
            EntityStatus.ACTIVE
        );
    }

}
