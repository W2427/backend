package com.ose.tasks.domain.model.service.wps.simple;

import com.ose.dto.ContextDTO;
import com.ose.service.EntityInterface;
import com.ose.tasks.dto.wps.simple.*;
import com.ose.tasks.entity.wps.simple.WelderGradeSimplified;
import com.ose.tasks.entity.wps.simple.WelderGradeWpsSimplifiedRelation;
import org.springframework.data.domain.Page;

public interface WelderGradeSimpleInterface extends EntityInterface {

    /**
     * 创建焊工等级
     *
     * @param orgId                      组织ID
     * @param projectId                  项目ID
     * @param context
     * @param welderGradeSimpleCreateDTO 创建信息
     */
    void create(
        Long orgId,
        Long projectId,
        ContextDTO context,
        WelderGradeSimpleCreateDTO welderGradeSimpleCreateDTO
    );

    /**
     * 编辑焊工等级
     *
     * @param orgId                      组织ID
     * @param projectId                  项目ID
     * @param context
     * @param welderGradeSimpleCreateDTO 创建信息
     */
    void update(
        Long orgId,
        Long projectId,
        Long welderGradeId,
        ContextDTO context,
        WelderGradeSimpleUpdateDTO welderGradeSimpleCreateDTO
    );

    /**
     * 焊工等级详情
     *
     * @param orgId         组织ID
     * @param projectId     项目ID
     * @param welderGradeId 焊工等级id
     */
    WelderGradeSimplified detail(
        Long orgId,
        Long projectId,
        Long welderGradeId
    );

    /**
     * 焊工等级列表
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     */
    Page<WelderGradeSimplified> search(
        Long orgId,
        Long projectId,
        WelderGradeSimpleSearchDTO welderGradeSimpleSearchDTO
    );

    /**
     * 删除焊工等级
     *
     * @param orgId         组织ID
     * @param projectId     项目ID
     * @param welderGradeId 焊工等级id
     */
    void delete(
        Long orgId,
        Long projectId,
        Long welderGradeId,
        ContextDTO context
    );






    /**
     * 添加WPS。
     *
     * @param orgId
     * @param projectId
     * @param gradeId
     * @param welderGradeRelationDTO
     * @param context
     */
    void addWps(
        Long orgId,
        Long projectId,
        Long gradeId,
        WelderGradeRelationDTO welderGradeRelationDTO,
        ContextDTO context
    );

    /**
     * 查找焊工登记列表。
     *
     * @param orgId
     * @param projectId
     * @param gradeId
     * @param wpsSimpleRelationSearchDTO
     * @return
     */
    Page<WelderGradeWpsSimplifiedRelation> searchWpsSimplify(
        Long orgId,
        Long projectId,
        Long gradeId,
        WpsSimpleRelationSearchDTO wpsSimpleRelationSearchDTO
    );

    /**
     * 删除WPS。
     *
     * @param orgId
     * @param projectId
     * @param gradeId
     * @param wpsId
     * @param context
     */
    void deleteWps(
        Long orgId,
        Long projectId,
        Long gradeId,
        Long wpsId,
        ContextDTO context
    );













}
