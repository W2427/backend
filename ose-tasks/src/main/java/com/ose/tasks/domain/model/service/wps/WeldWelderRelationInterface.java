package com.ose.tasks.domain.model.service.wps;

import com.ose.dto.ContextDTO;
import com.ose.service.EntityInterface;
import com.ose.tasks.dto.wps.WeldWelderRelationCreateDTO;
import com.ose.tasks.dto.wps.WeldWelderRelationSearchDTO;
import com.ose.tasks.entity.wps.WeldWelderRelation;
import org.springframework.data.domain.Page;

public interface WeldWelderRelationInterface extends EntityInterface {

    /**
     * 创建焊口焊工管系
     *
     * @param orgId              组织ID
     * @param projectId          项目ID
     * @param context
     * @param weldWelderRelationCreateDTO 创建信息
     */
    Boolean create(
        Long orgId,
        Long projectId,
        ContextDTO context,
        WeldWelderRelationCreateDTO weldWelderRelationCreateDTO
    );

    /**
     * 查询焊口焊工管系
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     */
    Page<WeldWelderRelation> search(
        Long orgId,
        Long projectId,
        WeldWelderRelationSearchDTO weldWelderRelationSearchDTO
    );


}
