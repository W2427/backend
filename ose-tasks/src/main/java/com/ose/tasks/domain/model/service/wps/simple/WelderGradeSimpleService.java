package com.ose.tasks.domain.model.service.wps.simple;
import com.ose.dto.ContextDTO;
import com.ose.exception.BusinessError;
import com.ose.tasks.domain.model.repository.wbs.piping.WeldEntityRepository;
import com.ose.tasks.domain.model.repository.wps.simple.WelderGradeSimpleRepository;
import com.ose.tasks.domain.model.repository.wps.simple.WelderGradeWelderSimpleRelationRepository;
import com.ose.tasks.domain.model.repository.wps.simple.WelderGradeWpsRelationimpleRepository;
import com.ose.tasks.domain.model.repository.wps.simple.WpsSimpleRepository;
import com.ose.tasks.dto.wps.simple.*;
import com.ose.tasks.entity.wps.simple.WelderGradeSimplified;
import com.ose.tasks.entity.wps.simple.WelderGradeWelderSimplifiedRelation;
import com.ose.tasks.entity.wps.simple.WelderGradeWpsSimplifiedRelation;
import com.ose.tasks.entity.wps.simple.WpsSimplified;
import com.ose.vo.EntityStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class WelderGradeSimpleService implements WelderGradeSimpleInterface {

    private WelderGradeSimpleRepository welderGradeSimpleRepository;
    private WelderGradeWelderSimpleRelationRepository welderGradeWelderSimpleRelationRepository;
    private WelderGradeWpsRelationimpleRepository welderGradeWpsRelationimpleRepository;
    private WeldEntityRepository weldEntityRepository;
    private WpsSimpleRepository wpsSimpleRepository;

    @Autowired
    public WelderGradeSimpleService(
        WelderGradeSimpleRepository welderGradeSimpleRepository,
        WelderGradeWelderSimpleRelationRepository welderGradeWelderSimpleRelationRepository,
        WelderGradeWpsRelationimpleRepository welderGradeWpsRelationimpleRepository,
        WeldEntityRepository weldEntityRepository,
        WpsSimpleRepository wpsSimpleRepository
    ) {
        this.welderGradeSimpleRepository = welderGradeSimpleRepository;
        this.welderGradeWelderSimpleRelationRepository = welderGradeWelderSimpleRelationRepository;
        this.welderGradeWpsRelationimpleRepository = welderGradeWpsRelationimpleRepository;
        this.weldEntityRepository = weldEntityRepository;
        this.wpsSimpleRepository = wpsSimpleRepository;
    }

    /**
     * 创建焊工等级
     *
     * @param orgId                      组织ID
     * @param projectId                  项目ID
     * @param context
     * @param welderGradeSimpleCreateDTO 创建信息
     */
    @Override
    public void create(
        Long orgId,
        Long projectId,
        ContextDTO context,
        WelderGradeSimpleCreateDTO welderGradeSimpleCreateDTO
    ) {
        WelderGradeSimplified welderGradeSimplified = welderGradeSimpleRepository.findByOrgIdAndProjectIdAndNoAndStatus(
            orgId,
            projectId,
            welderGradeSimpleCreateDTO.getNo(),
            EntityStatus.ACTIVE
        );
        if (welderGradeSimplified != null) {
            throw new BusinessError("焊工等级编号已经存在");
        }
        WelderGradeSimplified newWelderGradeSimplified = new WelderGradeSimplified();
        newWelderGradeSimplified.setNo(welderGradeSimpleCreateDTO.getNo());
        newWelderGradeSimplified.setPhoto(welderGradeSimpleCreateDTO.getPhoto());
        newWelderGradeSimplified.setRemark(welderGradeSimpleCreateDTO.getRemark());
        newWelderGradeSimplified.setOrgId(orgId);
        newWelderGradeSimplified.setProjectId(projectId);
        newWelderGradeSimplified.setStatus(EntityStatus.ACTIVE);
        newWelderGradeSimplified.setCreatedAt(new Date());
        newWelderGradeSimplified.setCreatedBy(context.getOperator().getId());
        newWelderGradeSimplified.setLastModifiedAt(new Date());
        newWelderGradeSimplified.setLastModifiedBy(context.getOperator().getId());
        welderGradeSimpleRepository.save(newWelderGradeSimplified);
    }

    /**
     * 编辑焊工等级
     *
     * @param orgId                      组织ID
     * @param projectId                  项目ID
     * @param context
     * @param welderGradeSimpleUpdateDTO 创建信息
     */
    @Override
    public void update(
        Long orgId,
        Long projectId,
        Long welderGradeId,
        ContextDTO context,
        WelderGradeSimpleUpdateDTO welderGradeSimpleUpdateDTO
    ) {
        WelderGradeSimplified welderGradeSimplified = welderGradeSimpleRepository.findByOrgIdAndProjectIdAndNoAndStatus(
            orgId,
            projectId,
            welderGradeSimpleUpdateDTO.getNo(),
            EntityStatus.ACTIVE
        );
        if (welderGradeSimplified != null && !welderGradeSimplified.getId().equals(welderGradeId)) {
            throw new BusinessError("焊工等级编号已经存在");
        }
        WelderGradeSimplified oldWelderGradeSimplified = welderGradeSimpleRepository.findByOrgIdAndProjectIdAndIdAndStatus(
            orgId,
            projectId,
            welderGradeId,
            EntityStatus.ACTIVE
        );
        if (oldWelderGradeSimplified == null) {
            throw new BusinessError("焊工等级不存在");
        }
        oldWelderGradeSimplified.setNo(welderGradeSimpleUpdateDTO.getNo());
        oldWelderGradeSimplified.setPhoto(welderGradeSimpleUpdateDTO.getPhoto());
        oldWelderGradeSimplified.setRemark(welderGradeSimpleUpdateDTO.getRemark());
        oldWelderGradeSimplified.setLastModifiedAt(new Date());
        oldWelderGradeSimplified.setLastModifiedBy(context.getOperator().getId());
        welderGradeSimpleRepository.save(oldWelderGradeSimplified);

    }

    /**
     * 焊工等级详情
     *
     * @param orgId         组织ID
     * @param projectId     项目ID
     * @param welderGradeId 焊工等级id
     */
    @Override
    public WelderGradeSimplified detail(
        Long orgId,
        Long projectId,
        Long welderGradeId
    ) {
        WelderGradeSimplified oldWelderGradeSimplified = welderGradeSimpleRepository.findByOrgIdAndProjectIdAndIdAndStatus(
            orgId,
            projectId,
            welderGradeId,
            EntityStatus.ACTIVE
        );
        if (oldWelderGradeSimplified == null) {
            throw new BusinessError("焊工等级不存在");
        } else {
            return oldWelderGradeSimplified;
        }
    }

    /**
     * 焊工等级列表
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     */
    @Override
    public Page<WelderGradeSimplified> search(
        Long orgId,
        Long projectId,
        WelderGradeSimpleSearchDTO welderGradeSimpleSearchDTO
    ) {
        return welderGradeSimpleRepository.findByOrgIdAndProjectIdAndStatus(
            orgId,
            projectId,
            EntityStatus.ACTIVE,
            welderGradeSimpleSearchDTO.toPageable()
        );
    }

    /**
     * 删除焊工等级
     *
     * @param orgId         组织ID
     * @param projectId     项目ID
     * @param welderGradeId 焊工等级id
     */
    @Override
    public void delete(
        Long orgId,
        Long projectId,
        Long welderGradeId,
        ContextDTO context
    ) {
        WelderGradeSimplified oldWelderGradeSimplified = welderGradeSimpleRepository.findByOrgIdAndProjectIdAndIdAndStatus(
            orgId,
            projectId,
            welderGradeId,
            EntityStatus.ACTIVE
        );
        if (oldWelderGradeSimplified == null) {
            throw new BusinessError("焊工等级不存在");
        }

        List<WelderGradeWpsSimplifiedRelation> welderGradeWpsSimplifiedRelation = welderGradeWpsRelationimpleRepository.findByOrgIdAndProjectIdAndWelderGradeNoAndStatus(
            orgId,
            projectId,
            oldWelderGradeSimplified.getNo(),
            EntityStatus.ACTIVE
        );
        if (welderGradeWpsSimplifiedRelation != null && welderGradeWpsSimplifiedRelation.size() > 0) {
            throw new BusinessError("焊工等级存在于其他WPS中");
        }

        List<WelderGradeWelderSimplifiedRelation> welderGradeWelderSimplifiedRelation = welderGradeWelderSimpleRelationRepository.findByOrgIdAndProjectIdAndWelderGradeNoAndStatus(
            orgId,
            projectId,
            oldWelderGradeSimplified.getNo(),
            EntityStatus.ACTIVE
        );
        if (welderGradeWelderSimplifiedRelation != null && welderGradeWelderSimplifiedRelation.size() > 0) {
            throw new BusinessError("焊工等级存在于其他Welder中");
        }

        oldWelderGradeSimplified.setStatus(EntityStatus.DELETED);
        oldWelderGradeSimplified.setLastModifiedAt(new Date());
        oldWelderGradeSimplified.setLastModifiedBy(context.getOperator().getId());
        welderGradeSimpleRepository.save(oldWelderGradeSimplified);
    }





    /**
     * 添加wps和焊工证关联信息。
     */
    @Override
    public void addWps(
        Long orgId,
        Long projectId,
        Long gradeId,
        WelderGradeRelationDTO welderGradeRelationDTO,
        ContextDTO context
    ) {
        List<WelderGradeWpsSimplifiedRelation> relationList = new ArrayList<>();
        WelderGradeSimplified welderGradeSimplifiedById = welderGradeSimpleRepository.findByOrgIdAndProjectIdAndIdAndStatus(
            orgId,
            projectId,
            gradeId,
            EntityStatus.ACTIVE
        );
        if (welderGradeSimplifiedById == null) {
            throw new BusinessError("焊工证不存在");
        }

        if (welderGradeRelationDTO.getWpsSimplifyIds() == null || welderGradeRelationDTO.getWpsSimplifyIds().size() == 0) {
            throw new BusinessError("添加的WPS不能为空");
        }
        for (Long wpsId : welderGradeRelationDTO.getWpsSimplifyIds()) {

            WelderGradeWpsSimplifiedRelation welderGradeWpsSimplifiedRelationNew = new WelderGradeWpsSimplifiedRelation();

            WpsSimplified wpsSimplified = wpsSimpleRepository.findByOrgIdAndProjectIdAndIdAndStatus(
                orgId,
                projectId,
                wpsId,
                EntityStatus.ACTIVE
            );
            if (wpsSimplified == null) {
                throw new BusinessError("WPS不存在");
            }
            WelderGradeWpsSimplifiedRelation welderGradeWpsSimplifiedRelation = welderGradeWpsRelationimpleRepository.findByOrgIdAndProjectIdAndWpsIdAndWelderGradeIdAndStatus(
                orgId,
                projectId,
                wpsId,
                gradeId,
                EntityStatus.ACTIVE
            );
            if (welderGradeWpsSimplifiedRelation != null) {
                continue;
            }
            welderGradeWpsSimplifiedRelationNew.setOrgId(orgId);
            welderGradeWpsSimplifiedRelationNew.setProjectId(projectId);
            welderGradeWpsSimplifiedRelationNew.setOrgId(orgId);
            welderGradeWpsSimplifiedRelationNew.setWpsNo(wpsSimplified.getNo());
            welderGradeWpsSimplifiedRelationNew.setWpsId(wpsSimplified.getId());
            welderGradeWpsSimplifiedRelationNew.setWelderGradeId(welderGradeSimplifiedById.getId());
            welderGradeWpsSimplifiedRelationNew.setWelderGradeNo(welderGradeSimplifiedById.getNo());
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
     * 焊工证下的WPS关系表。
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     */
    @Override
    public Page<WelderGradeWpsSimplifiedRelation> searchWpsSimplify(
        Long orgId,
        Long projectId,
        Long gradeId,
        WpsSimpleRelationSearchDTO wpsSimpleRelationSearchDTO
    ) {
        return welderGradeWpsRelationimpleRepository.findByOrgIdAndProjectIdAndWelderGradeIdAndStatusOrderByWpsNo(
            orgId,
            projectId,
            gradeId,
            EntityStatus.ACTIVE,
            wpsSimpleRelationSearchDTO.toPageable()
        );
    }



    /**
     * 删除WPS
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param gradeId    焊工等级id
     */
    @Override
    public void deleteWps(
        Long orgId,
        Long projectId,
        Long gradeId,
        Long wpsId,
        ContextDTO context
    ) {

        WelderGradeWpsSimplifiedRelation welderGradeWpsSimplifiedRelation = welderGradeWpsRelationimpleRepository.findByOrgIdAndProjectIdAndIdAndStatus(
            orgId,
            projectId,
            wpsId,
            EntityStatus.ACTIVE
        );
        if (welderGradeWpsSimplifiedRelation == null) {
            throw new BusinessError("WPS关系不存在");
        }
        welderGradeWpsSimplifiedRelation.setStatus(EntityStatus.DELETED);
        welderGradeWpsSimplifiedRelation.setLastModifiedAt(new Date());
        welderGradeWpsSimplifiedRelation.setLastModifiedBy(context.getOperator().getId());
        welderGradeWpsRelationimpleRepository.save(welderGradeWpsSimplifiedRelation);
    }












































































}
