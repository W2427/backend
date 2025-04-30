package com.ose.tasks.api;

import com.ose.dto.PageDTO;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.dto.bizcode.BizCodePatchDTO;
import com.ose.tasks.dto.bizcode.BizCodePostDTO;
import com.ose.tasks.dto.bizcode.BizCodeTypeDTO;
import com.ose.tasks.dto.bpm.BpmDelegateClassDTO;
import com.ose.tasks.entity.BizCode;
import com.ose.tasks.vo.setting.BizCodeType;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * 业务代码接口。
 */
public interface BizCodeAPI {

    /**
     * 取得业务代码列表。
     *
     * @return 业务代码列表
     */
    @RequestMapping(
        method = GET,
        value = "/biz-code-types/{bizCodeType}/biz-codes",
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<BizCode> list(
        @PathVariable("bizCodeType") BizCodeType bizCodeType,
        PageDTO pageDTO
    );

    /**
     * 取得业务代码列表。
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/biz-code-types/{bizCodeType}/biz-codes",
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<BizCode> list(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("bizCodeType") String bizCodeType,
        PageDTO pageDTO
    );

    /**
     * 取得枚举类型的业务代码大类型列表。
     * 这部分是系统业务代码大类型，不可变更
     */
    @RequestMapping(
        method = GET,
        value = "/biz-code-types-test",
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<BizCodeTypeDTO> listSystemBizCodeTypes();

    /**
     * 添加枚举类型的业务代码大类型。
     * 这部分是系统业务代码大类型
     */
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/biz-code-types",
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<BizCode> addType(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestBody BizCodePostDTO bizCodePostDTO
    );

    /**
     * 取得业务代码大类型列表。
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/biz-code-types",
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<BizCodeTypeDTO> listBizCodeTypes(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId
    );

    /**
     * 添加业务代码。
     */
    @RequestMapping(
        method = POST,
        value = "/biz-code-types/{bizCodeType}/biz-codes",
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<BizCode> add(
        @PathVariable("bizCodeType") BizCodeType bizCodeType,
        @RequestBody BizCodePostDTO bizCodePostDTO
    );

    /**
     * 添加业务代码。
     */
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/biz-code-types/{bizCodeType}/biz-codes",
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<BizCode> add(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("bizCodeType") String bizCodeType,
        @RequestBody BizCodePostDTO bizCodePostDTO
    );

    /**
     * 更新业务代码。
     */
    @RequestMapping(
        method = PATCH,
        value = "/biz-code-types/{bizCodeType}/biz-codes/{bizCode}",
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<BizCode> update(
        @PathVariable("bizCodeType") BizCodeType bizCodeType,
        @PathVariable("bizCode") String bizCode,
        @RequestParam("version") Long version,
        @RequestBody BizCodePatchDTO bizCodePatchDTO
    );

    /**
     * 更新业务代码。
     */
    @RequestMapping(
        method = PATCH,
        value = "/orgs/{orgId}/projects/{projectId}/biz-code-types/{bizCodeType}/biz-codes/{bizCode}",
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<BizCode> update(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("bizCodeType") String bizCodeType,
        @PathVariable("bizCode") String bizCode,
        @RequestParam("version") Long version,
        @RequestBody BizCodePatchDTO bizCodePatchDTO
    );

    /**
     * 删除业务代码。
     */
    @RequestMapping(
        method = DELETE,
        value = "/biz-code-types/{bizCodeType}/biz-codes/{bizCode}",
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody delete(
        @PathVariable("bizCodeType") BizCodeType bizCodeType,
        @PathVariable("bizCode") String bizCode,
        @RequestParam("version") Long version
    );

    /**
     * 删除业务代码。
     */
    @RequestMapping(
        method = DELETE,
        value = "/orgs/{orgId}/projects/{projectId}/biz-code-types/{bizCodeType}/biz-codes/{bizCode}",
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody delete(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("bizCodeType") String bizCodeType,
        @PathVariable("bizCode") String bizCode,
        @RequestParam("version") Long version
    );


    /**
     * 查询任务代理类名称列表。
     *
     * @return
     */
    @RequestMapping(
        method = GET,
        value = "/delegate-classes",
        produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    JsonListResponseBody<BpmDelegateClassDTO> getDelegateClasses();

    /**
     * 查询日志代理类名称列表。
     *
     * @return
     */
    @RequestMapping(
        method = GET,
        value = "/log-delegate-classes",
        produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    JsonListResponseBody<BpmDelegateClassDTO> getLogDelegateClasses();



    /**
     * 查询报告子类型列表。
     *
     * @return 报告子类型
     */
    @RequestMapping(
        method = GET,
        value = "/report-sub-type/{reportType}",
        produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    JsonListResponseBody<BizCode> getReportSubTypes(@PathVariable("reportType") String reportType);

}
