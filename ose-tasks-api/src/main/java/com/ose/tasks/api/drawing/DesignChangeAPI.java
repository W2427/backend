package com.ose.tasks.api.drawing;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;

import com.ose.tasks.entity.drawing.Drawing;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.ose.dto.PageDTO;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.dto.drawing.DesignChangeReviewDTO;
import com.ose.tasks.dto.drawing.DesignChangeCriteriaDTO;
import com.ose.tasks.dto.drawing.DesignChangeReviewRegisterDTO;
import com.ose.tasks.dto.drawing.DrawingUploadDTO;
import com.ose.tasks.dto.drawing.UploadDrawingFileResultDTO;
import com.ose.tasks.entity.bpm.BpmActivityInstanceBase;
import com.ose.tasks.entity.DesignChangeReviewRegister;

@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/")
public interface DesignChangeAPI {

    /**
     * 图纸修改评审单登记列表
     *
     * @param orgId       组织ID
     * @param projectId   项目ID
     * @param page        分页
     * @param criteriaDTO 评审DTO
     * @return 图纸修改评审单登记列表
     */
    @RequestMapping(
        method = GET,
        value = "modification-review-register",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<DesignChangeReviewRegister> searchModificationReviewRegisterList(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        PageDTO page,
        DesignChangeCriteriaDTO criteriaDTO
    );

    /**
     * 上传图纸修改评审单登记表
     */
    @RequestMapping(
        method = POST,
        value = "modification-review-register/upload"
    )
    JsonObjectResponseBody<UploadDrawingFileResultDTO> upload(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestBody DrawingUploadDTO uploadDTO
    );

    /**
     * 新建图纸修改评审单登记记录
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param DTO       设计变更DTO
     * @return 新建图纸修改评审单登记记录
     */
    @RequestMapping(
        method = POST,
        value = "modification-review-register"
    )
    @ResponseStatus(CREATED)
    JsonObjectResponseBody<DesignChangeReviewRegister> create(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestBody DesignChangeReviewRegisterDTO DTO
    );

    /**
     * 删除图纸修改评审单登记记录
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param id        图纸修改ID
     * @return 删除图纸修改评审单登记记录
     */
    @RequestMapping(
        method = DELETE,
        value = "modification-review-register/{id}"
    )
    JsonResponseBody delete(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("id") Long id
    );

    /**
     * 获取设计评审单信息
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param id        设计评审ID
     * @return 获取设计评审单信息
     */
    @RequestMapping(
        method = GET,
        value = "modification-review-register/{id}/design-change-review-form",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<DesignChangeReviewDTO> getDesignChangeReviewForm(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("id") Long id
    );

    /**
     * 图纸清单
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param page      分页
     * @param keyword   关键字
     * @return 图纸清单
     */
    @RequestMapping(
        method = GET,
        value = "modification-review-register/drawing-list",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<Drawing> searchDrawingList(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        String keyword,
        PageDTO page
    );

    /**
     * 填写设计评审单信息
     */
    @RequestMapping(
        method = POST,
        value = "modification-review-register/{id}/design-change-review-form"
    )
    JsonResponseBody addDesignChangeReviewForm(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("id") Long id,
        @RequestBody DesignChangeReviewDTO designChangeReviewDTO
    );

    /**
     * 新建设计变更流程
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param id        变更ID
     * @return 新建设计变更流程
     */
    @RequestMapping(
        method = POST,
        value = "modification-review-register/{id}/create-task"
    )
    @ResponseStatus(CREATED)
    JsonObjectResponseBody<BpmActivityInstanceBase> createTask(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("id") Long id
    );
}
