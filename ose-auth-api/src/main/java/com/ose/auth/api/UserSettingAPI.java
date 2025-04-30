package com.ose.auth.api;

import com.ose.auth.dto.*;
import com.ose.auth.entity.*;
import com.ose.dto.PageDTO;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * 用户信息接口。
 */
@FeignClient(name = "ose-auth", contextId = "userSettingFeign")
public interface UserSettingAPI {

    /**
     * 创建公司。
     *
     * @param dto 公司信息
     * @return 用户信息
     */
    @RequestMapping(
        method = POST,
        value = "/users/company",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<Company> createCompany(
        @RequestBody CompanyDTO dto
    );

    /**
     * 查询公司
     *
     * @return 公司列表
     */
    @RequestMapping(
        method = GET,
        value = "/users/company",
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<Company> searchCompany(
        CompanyDTO dto,
        PageDTO page
    );

    @Operation(
        summary = "获取公司详细信息",
        description = "获取公司详细信息"
    )
    @RequestMapping(
        method = GET,
        value = "/users/company/{id}",
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(OK)
    JsonObjectResponseBody<Company> detailCompany(
        @PathVariable @Parameter(description = "id") Long id);

    @Operation(
        summary = "编辑Company",
        description = "编辑Company"
    )
    @RequestMapping(
        method = POST,
        value = "/users/company/{id}"
    )
    @ResponseStatus(OK)
    JsonResponseBody editCompany(
        @PathVariable @Parameter(description = "Company id") Long id,
        @RequestBody @Parameter(description = "Company信息") CompanyDTO dto

    );

    @Operation(
        summary = "删除Company",
        description = "删除Company"
    )
    @RequestMapping(
        method = DELETE,
        value = "/users/company/{id}",
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(OK)
    JsonResponseBody deleteCompany(
        @PathVariable @Parameter(description = "id") Long id);


    /**
     * 创建division
     * @param dto
     * @return
     */
    @RequestMapping(
        method = POST,
        value = "/users/division",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<Division> createDivision(
        @RequestBody CompanyDTO dto
    );

    @RequestMapping(
        method = GET,
        value = "/users/division",
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<Division> searchDivision(
        CompanyDTO dto,
        PageDTO page
    );


    @RequestMapping(
        method = GET,
        value = "/users/division/{id}",
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(OK)
    JsonObjectResponseBody<Division> detailDivision(
        @PathVariable @Parameter(description = "id") Long id);


    @RequestMapping(
        method = POST,
        value = "/users/division/{id}"
    )
    @ResponseStatus(OK)
    JsonResponseBody editDivision(
        @PathVariable Long id,
        @RequestBody  CompanyDTO dto

    );


    @RequestMapping(
        method = DELETE,
        value = "/users/division/{id}",
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(OK)
    JsonResponseBody deleteDivision(
        @PathVariable @Parameter(description = "id") Long id);


    /**
     * 创建department
     * @param dto
     * @return
     */
    @RequestMapping(
        method = POST,
        value = "/users/department",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<Department> createDepartment(
        @RequestBody CompanyDTO dto
    );

    @RequestMapping(
        method = GET,
        value = "/users/department",
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<Department> searchDepartment(
        CompanyDTO dto,
        PageDTO page
    );


    @RequestMapping(
        method = GET,
        value = "/users/department/{id}",
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(OK)
    JsonObjectResponseBody<Department> detailDepartment(
        @PathVariable @Parameter(description = "id") Long id);


    @RequestMapping(
        method = POST,
        value = "/users/department/{id}"
    )
    @ResponseStatus(OK)
    JsonResponseBody editDepartment(
        @PathVariable Long id,
        @RequestBody  CompanyDTO dto

    );


    @RequestMapping(
        method = DELETE,
        value = "/users/department/{id}",
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(OK)
    JsonResponseBody deleteDepartment(
        @PathVariable @Parameter(description = "id") Long id);

    /**
     * 创建team
     * @param dto
     * @return
     */
    @RequestMapping(
        method = POST,
        value = "/users/team",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<Team> createTeam(
        @RequestBody CompanyDTO dto
    );

    @RequestMapping(
        method = GET,
        value = "/users/team",
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<Team> searchTeam(
        CompanyDTO dto,
        PageDTO page
    );


    @RequestMapping(
        method = GET,
        value = "/users/team/{id}",
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(OK)
    JsonObjectResponseBody<Team> detailTeam(
        @PathVariable @Parameter(description = "id") Long id);


    @RequestMapping(
        method = POST,
        value = "/users/team/{id}"
    )
    @ResponseStatus(OK)
    JsonResponseBody editTeam(
        @PathVariable Long id,
        @RequestBody  CompanyDTO dto

    );


    @RequestMapping(
        method = DELETE,
        value = "/users/team/{id}",
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(OK)
    JsonResponseBody deleteTeam(
        @PathVariable @Parameter(description = "id") Long id);


    /**
     * 查询所有公司
     *
     * @return 公司列表
     */
    @RequestMapping(
        method = GET,
        value = "/users/company/all",
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<Company> searchAllCompany();

    @RequestMapping(
        method = GET,
        value = "/users/division/all",
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<Division> searchAllDivision();

    @RequestMapping(
        method = GET,
        value = "/users/department/all",
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<Department> searchAllDepartment();

    @RequestMapping(
        method = GET,
        value = "/users/team/all",
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<Team> searchAllTeam();

}
