package com.ose.tasks.domain.model.service.wps.simple;
import com.ose.dto.ContextDTO;
import com.ose.service.EntityInterface;
import com.ose.tasks.dto.wps.simple.*;
import com.ose.tasks.entity.wps.simple.WelderGradeWpsSimplifiedRelation;
import com.ose.tasks.entity.wps.simple.WpsSimplified;
import org.springframework.data.domain.Page;

import java.util.List;

public interface WpsSimpleInterface extends EntityInterface {

    /**
     * 创建Wps
     *
     * @param orgId              组织ID
     * @param projectId          项目ID
     * @param context
     * @param wpsSimpleCreateDTO 创建信息
     */
    void create(
        Long orgId,
        Long projectId,
        ContextDTO context,
        WpsSimpleCreateDTO wpsSimpleCreateDTO
    );

    /**
     * 编辑Wps
     *
     * @param orgId              组织ID
     * @param projectId          项目ID
     * @param context
     * @param wpsSimpleUpdateDTO 创建信息
     */
    void update(
        Long orgId,
        Long projectId,
        Long welderGradeId,
        ContextDTO context,
        WpsSimpleUpdateDTO wpsSimpleUpdateDTO
    );

    /**
     * Wps详情
     *
     * @param orgId       组织ID
     * @param projectId   项目ID
     * @param wpsSimpleId 焊工等级id
     */
    WpsSimplified detail(
        Long orgId,
        Long projectId,
        Long wpsSimpleId
    );

    /**
     * Wps列表
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     */
    Page<WpsSimplified> search(
        Long orgId,
        Long projectId,
        WpsSimpleSearchDTO wpsSimpleSearchDTO
    );

    /**
     * 删除Wps
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param wpsId     焊工等级id
     */
    void delete(
        Long orgId,
        Long projectId,
        Long wpsId,
        ContextDTO context
    );

    /**
     * 添加焊工等级。
     *
     * @param orgId
     * @param projectId
     * @param wpsSimpleId
     * @param wpsSimpleRelationDTO
     * @param context
     */
    void addWelderGrade(
        Long orgId,
        Long projectId,
        Long wpsSimpleId,
        WpsSimpleRelationDTO wpsSimpleRelationDTO,
        ContextDTO context
    );

    /**
     * 查找焊工登记列表。
     *
     * @param orgId
     * @param projectId
     * @param welderSimpleId
     * @param wpsSimpleRelationSearchDTO
     * @return
     */
    Page<WelderGradeWpsSimplifiedRelation> searchWelderGrade(
        Long orgId,
        Long projectId,
        Long welderSimpleId,
        WpsSimpleRelationSearchDTO wpsSimpleRelationSearchDTO
    );

    /**
     * 删除焊工等级。
     *
     * @param orgId
     * @param projectId
     * @param wpsId
     * @param welderGradeSimpleId
     * @param context
     */
    void deleteWelderGrade(
        Long orgId,
        Long projectId,
        Long wpsId,
        Long welderGradeSimpleId,
        ContextDTO context
    );

    /**
     * 获取焊口下所有Wps列表
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     */
    List<WpsSimplified> searchWeldWps(
        Long orgId,
        Long projectId,
        WpsSimpleRelationDTO wpsSimpleRelationDTO
    );
}
