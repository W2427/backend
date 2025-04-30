package com.ose.tasks.api.drawing;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.ose.dto.PageDTO;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.dto.drawing.ConstructionChangeRegisterDTO;
import com.ose.tasks.dto.drawing.DesignChangeReviewDTO;
import com.ose.tasks.dto.drawing.DesignChangeCriteriaDTO;
import com.ose.tasks.entity.bpm.BpmActivityInstanceBase;
import com.ose.tasks.entity.ConstructionChangeRegister;

@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/")
public interface ConstructionChangeAPI {

    /**
     * 图纸修改评审单登记列表
     *
     * @param orgId       组织ID
     * @param projectId   项目ID
     * @param page
     * @param criteriaDTO
     * @return
     */
    @RequestMapping(
        method = GET,
        value = "construction-change-register",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<ConstructionChangeRegister> searchConstructionChangeRegisterList(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        PageDTO page,
        DesignChangeCriteriaDTO criteriaDTO
    );

    /**
     * 新建图纸修改评审单登记记录
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param DTO
     * @return
     */

    @RequestMapping(
        method = POST,
        value = "construction-change-register"
    )
    @ResponseStatus(CREATED)
    JsonObjectResponseBody<ConstructionChangeRegister> create(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestBody ConstructionChangeRegisterDTO DTO
    );

    /**
     * 删除图纸修改评审单登记记录
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param id
     * @return
     */
    @RequestMapping(
        method = DELETE,
        value = "construction-change-register/{id}"
    )
    JsonResponseBody delete(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("id") Long id
    );

    /**
     * 新建建造变更流程
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param id        变更主键ID
     * @return
     */
    @RequestMapping(
        method = POST,
        value = "construction-change-register/{id}/create-task"
    )
    @ResponseStatus(CREATED)
    JsonObjectResponseBody<BpmActivityInstanceBase> createTask(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("id") Long id
    );

    /**
     * 获取建造变更申请
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param actInstId bpmActivity 主键 ID
     * @return
     */

    @RequestMapping(
        method = GET,
        value = "construction-change-register/activity/{actInstId}"
    )
    JsonObjectResponseBody<ConstructionChangeRegister> getByActInstId(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("actInstId") Long actInstId);

    /**
     * 填写设计评审单信息。
     *
     * @param orgId                 组织id
     * @param projectId             项目id
     * @param id                    建造变更id
     * @param designChangeReviewDTO 设计评审DTO
     * @return
     */
    @RequestMapping(
        method = POST,
        value = "construction-change-register/{id}/design-change-review-form"
    )
    JsonResponseBody addDesignChangeReviewForm(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("id") Long id,
        @RequestBody DesignChangeReviewDTO designChangeReviewDTO
    );

    /**
     * 获取设计评审单信息
     *
     * @param orgId     组织id
     * @param projectId 项目id
     * @param id        建造变更id
     * @return
     */
    @RequestMapping(
        method = GET,
        value = "construction-change-register/{id}/design-change-review-form",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<DesignChangeReviewDTO> getDesignChangeReviewForm(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("id") Long id
    );
}
