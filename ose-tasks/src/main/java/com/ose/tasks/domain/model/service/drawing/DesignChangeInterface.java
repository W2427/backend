package com.ose.tasks.domain.model.service.drawing;

import com.ose.dto.ContextDTO;
import com.ose.dto.OperatorDTO;
import com.ose.dto.PageDTO;
import com.ose.tasks.dto.drawing.DesignChangeReviewDTO;
import com.ose.tasks.dto.drawing.DesignChangeCriteriaDTO;
import com.ose.tasks.dto.drawing.DesignChangeReviewRegisterDTO;
import com.ose.tasks.dto.drawing.DrawingUploadDTO;
import com.ose.tasks.dto.drawing.UploadDrawingFileResultDTO;
import com.ose.tasks.entity.DesignChangeReviewRegister;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.drawing.Drawing;
import org.springframework.data.domain.Page;

/**
 * service接口
 */
public interface DesignChangeInterface {

    /**
     * 查询图纸修改评审单登记信息列表
     *
     * @param orgId       组织ID
     * @param projectId   项目ID
     * @param page
     * @param criteriaDTO
     * @return
     */
    Page<DesignChangeReviewRegister> searchDesignChangeReviewRegisterList(Long orgId, Long projectId, PageDTO page,
                                                                          DesignChangeCriteriaDTO criteriaDTO);

    /**
     * 上传图纸修改评审单登记表
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param uploadDTO
     * @return
     */
    UploadDrawingFileResultDTO upload(Long orgId, Long projectId, DrawingUploadDTO uploadDTO);

    /**
     * 创建图纸修改评审单登记记录
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param DTO
     * @return
     */
    DesignChangeReviewRegister create(Long orgId, Long projectId, DesignChangeReviewRegisterDTO DTO);

    /**
     * 删除图纸修改评审单登记记录
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param id
     * @return
     */
    boolean delete(Long orgId, Long projectId, Long id);

    /**
     * 获取新创建记录的vorNo
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param project
     * @return
     */
    String getNewVorNo(Long orgId, Long projectId, Project project);

    /**
     * 获取图纸评审单信息
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param id
     * @return
     */
    DesignChangeReviewDTO getDesignChangeReviewForm(Long orgId, Long projectId, Long id);

    /**
     * 填写图纸评审单信息
     *
     * @param orgId                 组织ID
     * @param projectId             项目ID
     * @param id
     * @param designChangeReviewDTO
     * @param operator
     * @return
     */
    boolean addDesignChangeReviewForm(ContextDTO contextDTO, Long orgId, Long projectId, Long id,
                                      DesignChangeReviewDTO designChangeReviewDTO, OperatorDTO operator);

    /**
     * 根据id 获取图纸修改评审单登记记录
     *
     * @param id
     * @return
     */
    DesignChangeReviewRegister getById(Long id);

    /**
     * 查询图纸清单
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param page
     * @param keyword
     * @return
     */
    Page<Drawing> searchDrawingList(Long orgId, Long projectId, PageDTO page, String keyword);

}
