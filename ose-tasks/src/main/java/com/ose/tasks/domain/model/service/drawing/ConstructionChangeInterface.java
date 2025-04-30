package com.ose.tasks.domain.model.service.drawing;

import org.springframework.data.domain.Page;

import com.ose.dto.ContextDTO;
import com.ose.dto.OperatorDTO;
import com.ose.dto.PageDTO;
import com.ose.tasks.dto.drawing.ConstructionChangeRegisterDTO;
import com.ose.tasks.dto.drawing.DesignChangeReviewDTO;
import com.ose.tasks.dto.drawing.DesignChangeCriteriaDTO;
import com.ose.tasks.entity.ConstructionChangeRegister;
import com.ose.tasks.entity.Project;

/**
 * service接口
 */
public interface ConstructionChangeInterface {

    /**
     * 查询建造变更申请列表
     *
     * @param orgId       组织ID
     * @param projectId   项目ID
     * @param page
     * @param criteriaDTO
     * @return
     */
    Page<ConstructionChangeRegister> searchConstructionChangeRegisterList(Long orgId, Long projectId, PageDTO page,
                                                                          DesignChangeCriteriaDTO criteriaDTO);

    /**
     * 添加建造变更申请
     *
     * @param orgId                         组织ID
     * @param projectId                     项目ID
     * @param constructionChangeRegisterDTO
     * @param operatorDTO
     * @param project
     * @return
     */
    ConstructionChangeRegister create(Long orgId, Long projectId,
                                      ConstructionChangeRegisterDTO constructionChangeRegisterDTO, OperatorDTO operatorDTO, Project project);

    /**
     * 删除建造变更申请单
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param id
     * @return
     */
    boolean delete(Long orgId, Long projectId, Long id);

    /**
     * 根据id获取建造变更申请
     *
     * @param id
     * @return
     */
    ConstructionChangeRegister getById(Long id);

    /**
     * 填写设计变更评审单信息
     *
     * @param orgId                 组织ID
     * @param projectId             项目ID
     * @param id
     * @param designChangeReviewDTO
     * @param operator
     */
    boolean addDesignChangeReviewForm(ContextDTO contextDTO, Long orgId, Long projectId, Long id,
                                      DesignChangeReviewDTO designChangeReviewDTO, OperatorDTO operator);


}
