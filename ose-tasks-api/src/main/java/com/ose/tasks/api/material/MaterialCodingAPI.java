package com.ose.tasks.api.material;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.ose.dto.PageDTO;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.dto.material.MaterialBizCodeDTO;
import com.ose.tasks.dto.material.MaterialBizCodeLengthDTO;
import com.ose.tasks.dto.material.MaterialBizTableDTO;
import com.ose.tasks.dto.material.MaterialCodeCriteriaDTO;
import com.ose.tasks.dto.material.MaterialCodeDTO;
import com.ose.tasks.dto.material.MaterialCodeSearchDTO;
import com.ose.tasks.dto.material.MaterialCodingTemplateDTO;
import com.ose.tasks.dto.material.MaterialCodingTemplateDetailDTO;
import com.ose.tasks.entity.material.MaterialBizCode;
import com.ose.tasks.entity.material.MaterialBizCodeTable;
import com.ose.tasks.entity.material.MaterialCode;
import com.ose.tasks.entity.material.MaterialCodingTemplate;
import com.ose.tasks.entity.material.MaterialCodingTemplateDetail;

@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/material-coding/")
public interface MaterialCodingAPI {

    @RequestMapping(
        method = GET,
        value = "templates"
    )
    @ResponseStatus(OK)
    JsonListResponseBody<MaterialCodingTemplate> templateList(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        PageDTO page
    );

    @RequestMapping(
        method = POST,
        value = "templates"
    )
    @ResponseStatus(CREATED)
    JsonObjectResponseBody<MaterialCodingTemplate> addTemplate(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestBody MaterialCodingTemplateDTO dto
    );

    @RequestMapping(
        method = GET,
        value = "templates/{id}"
    )
    @ResponseStatus(OK)
    JsonObjectResponseBody<MaterialCodingTemplate> getTemplate(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("projectId") Long id
    );

    @RequestMapping(
        method = POST,
        value = "templates/{id}"
    )
    @ResponseStatus(OK)
    JsonObjectResponseBody<MaterialCodingTemplate> modifyTemplate(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("id") Long id,
        @RequestBody MaterialCodingTemplateDTO dto
    );

    @RequestMapping(
        method = DELETE,
        value = "templates/{id}"
    )
    @ResponseStatus(OK)
    JsonResponseBody deleteTemplate(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("id") Long id
    );

    @RequestMapping(
        method = GET,
        value = "templates/{matCodingTemplatesId}/template-details"
    )
    @ResponseStatus(OK)
    JsonListResponseBody<MaterialCodingTemplateDetail> templateDetail(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("matCodingTemplatesId") Long matCodingTemplatesId
    );

    @RequestMapping(
        method = POST,
        value = "template-details"
    )
    @ResponseStatus(CREATED)
    JsonObjectResponseBody<MaterialCodingTemplateDetail> addTemplateDetail(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestBody MaterialCodingTemplateDetailDTO dto
    );

    @RequestMapping(
        method = GET,
        value = "template-details/{matCodingTemplateDetailId}"
    )
    @ResponseStatus(OK)
    JsonObjectResponseBody<MaterialCodingTemplateDetail> getTemplateDetail(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("matCodingTemplateDetailId") Long matCodingTemplateDetailId
    );

    @RequestMapping(
        method = GET,
        value = "template-details/{matCodingTemplateDetailId}/codes"
    )
    @ResponseStatus(OK)
    JsonListResponseBody<MaterialCode> templateDetailCodeList(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("matCodingTemplateDetailId") Long matCodingTemplateDetailId
    );

    @RequestMapping(
        method = POST,
        value = "template-details/{matCodingTemplateDetailId}/codes"
    )
    @ResponseStatus(CREATED)
    JsonObjectResponseBody<MaterialCode> addTemplateDetailCode(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("matCodingTemplateDetailId") Long matCodingTemplateDetailId,
        @RequestBody MaterialCodeDTO dto
    );

    @RequestMapping(
        method = GET,
        value = "codes"
    )
    @ResponseStatus(OK)
    JsonListResponseBody<MaterialCode> codeList(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        PageDTO page,
        MaterialCodeCriteriaDTO criteriaDTO
    );

    @RequestMapping(
        method = POST,
        value = "codes/{matCodeId}"
    )
    @ResponseStatus(OK)
    JsonObjectResponseBody<MaterialCode> modifyCode(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("matCodeId") Long matCodeId,
        @RequestBody MaterialCodeDTO dto
    );

    @RequestMapping(
        method = DELETE,
        value = "codes/{matCodeId}"
    )
    @ResponseStatus(OK)
    JsonResponseBody deleteCode(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("matCodeId") Long matCodeId
    );

    @RequestMapping(
        method = GET,
        value = "biz-codes"
    )
    @ResponseStatus(OK)
    JsonListResponseBody<MaterialBizCode> bizCodeList(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        PageDTO page,
        MaterialCodeCriteriaDTO criteriaDTO
    );

    @RequestMapping(
        method = GET,
        value = "biz-codes/biz-tables"
    )
    @ResponseStatus(OK)
    JsonListResponseBody<MaterialBizTableDTO> bizCodeTableList(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId
    );

    @RequestMapping(
        method = GET,
        value = "biz-codes/{bizTable}/code-length"
    )
    @ResponseStatus(OK)
    JsonObjectResponseBody<MaterialBizCodeLengthDTO> bizCodeLength(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("bizTable") String bizTable
    );

    @RequestMapping(
        method = POST,
        value = "biz-codes"
    )
    @ResponseStatus(CREATED)
    JsonObjectResponseBody<MaterialBizCode> addBizCode(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestBody MaterialBizCodeDTO dto
    );

    @RequestMapping(
        method = POST,
        value = "biz-codes/{matBizCodeId}"
    )
    @ResponseStatus(OK)
    JsonObjectResponseBody<MaterialBizCode> modifyBizCode(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("matBizCodeId") Long matBizCodeId,
        @RequestBody MaterialBizCodeDTO dto
    );

    @RequestMapping(
        method = GET,
        value = "biz-codes/{matBizCodeId}"
    )
    @ResponseStatus(OK)
    JsonObjectResponseBody<MaterialBizCode> getBizCode(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("matBizCodeId") Long matBizCodeId
    );

    @RequestMapping(
        method = DELETE,
        value = "biz-codes/{matBizCodeId}"
    )
    @ResponseStatus(OK)
    JsonResponseBody deleteBizCode(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("matBizCodeId") Long matBizCodeId
    );

    @RequestMapping(
        method = POST,
        value = "template-details/{matCodingTemplateDetailId}"
    )
    @ResponseStatus(OK)
    JsonObjectResponseBody<MaterialCodingTemplateDetail> modifyTemplateDetail(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("matCodingTemplateDetailId") Long matCodingTemplateDetailId,
        @RequestBody MaterialCodingTemplateDetailDTO dto
    );

    @RequestMapping(
        method = DELETE,
        value = "template-details/{matCodingTemplateDetailId}"
    )
    @ResponseStatus(OK)
    JsonResponseBody deleteTemplateDetail(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("matCodingTemplateDetailId") Long matCodingTemplateDetailId
    );

    @RequestMapping(
        method = GET,
        value = "codes/{matCodeId}"
    )
    @ResponseStatus(OK)
    JsonObjectResponseBody<MaterialCode> getCode(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("matCodeId") Long matCodeId
    );

    @RequestMapping(
        method = POST,
        value = "biz-tables"
    )
    @ResponseStatus(CREATED)
    JsonObjectResponseBody<MaterialBizCodeTable> addBizCodeTable(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestBody MaterialBizTableDTO dto
    );

    @RequestMapping(
        method = GET,
        value = "biz-tables"
    )
    @ResponseStatus(OK)
    JsonListResponseBody<MaterialBizCodeTable> bizCodeTableList(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        PageDTO page
    );

    @RequestMapping(
        method = GET,
        value = "search"
    )
    @ResponseStatus(OK)
    JsonObjectResponseBody<MaterialCodeSearchDTO> search(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        MaterialCodeSearchDTO dto);
}
