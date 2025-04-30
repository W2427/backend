package com.ose.auth.controller;

import com.ose.auth.annotation.WithPrivilege;
import com.ose.auth.api.UserSettingAPI;
import com.ose.auth.domain.model.service.*;
import com.ose.auth.dto.*;
import com.ose.auth.entity.*;
import com.ose.controller.BaseController;
import com.ose.dto.ContextDTO;
import com.ose.dto.PageDTO;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.*;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@Tag(name = "用户接口")
@RestController
public class UserSettingController extends BaseController implements UserSettingAPI {

    // 用户操作服务
    private final UserInterface userService;
    private final UserSettingInterface userSettingService;

    // 图形验证码操作服务
    private final CaptchaInterface captchaInterface;

    // 验证码操作服务
    private final VerificationInterface verificationService;
    private OrganizationInterface organizationService;
    private OrgMemberInterface orgMemberService;

    /**
     * 构造方法。
     *
     * @param userService         用户操作服务
     * @param captchaInterface    图形验证码操作服务
     * @param verificationService 验证码操作服务
     */
    @Autowired
    public UserSettingController(
        UserInterface userService,
        CaptchaInterface captchaInterface,
        VerificationInterface verificationService,
        OrganizationInterface organizationService,
        OrgMemberInterface orgMemberService,
        UserSettingInterface userSettingService
    ) {
        this.userService = userService;
        this.captchaInterface = captchaInterface;
        this.verificationService = verificationService;
        this.organizationService = organizationService;
        this.orgMemberService = orgMemberService;
        this.userSettingService = userSettingService;
    }

    /**
     * 创建用户账号。
     *
     * @param dto 用户信息
     * @return 用户信息
     */
    @Override
    @Operation(
        summary = "创建用户",
        description = "创建用户登录账号，需要系统管理员权限。"
    )
    @RequestMapping(
        method = POST,
        value = "/users/company",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(CREATED)
    public JsonObjectResponseBody<Company> createCompany(
        @RequestBody @Parameter(description = "公司信息") CompanyDTO dto
    ) {

        ContextDTO context = getContext();

        Long operator = 1L;
        if (context.getOperator() != null) {
            operator = context.getOperator().getId();
        }
        return new JsonObjectResponseBody<>(
            context,
            userSettingService.createCompany(
                operator,
                dto
            )
        );

    }

    @Override
    @RequestMapping(
        method = GET,
        value = "/users/company",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonListResponseBody<Company> searchCompany(
        CompanyDTO dto,
        PageDTO page) {

        return new JsonListResponseBody<>(
            getContext(),
            userSettingService.searchCompany(dto,page)
        );
    }

    @Override
    @Operation(
        summary = "获取company详细信息",
        description = "获取company详细信息"
    )
    @RequestMapping(
        method = GET,
        value = "/users/company/{id}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonObjectResponseBody<Company> detailCompany(
        @PathVariable @Parameter(description = "id") Long id) {
        return new JsonObjectResponseBody<>(
            getContext(), userSettingService.detailCompany(id)
        );
    }

    @Override
    @Operation(
        summary = "编辑Company",
        description = "编辑Company"
    )
    @RequestMapping(
        method = POST,
        value = "/users/company/{id}"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody editCompany(
        @PathVariable @Parameter(description = "Company id") Long id,
        @RequestBody @Parameter(description = "Company信息") CompanyDTO dto

    ) {
        userSettingService.updateCompany(id, dto, getContext());
        return new JsonResponseBody();
    }

    @Override
    @Operation(
        summary = "删除Company",
        description = "删除Company"
    )
    @RequestMapping(
        method = DELETE,
        value = "/users/company/{id}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody deleteCompany(
        @PathVariable @Parameter(description = "id") Long id) {
        userSettingService.deleteCompany(id, getContext());
        return new JsonResponseBody();
    }

    @Override
    @RequestMapping(
        method = POST,
        value = "/users/division",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(CREATED)
    public JsonObjectResponseBody<Division> createDivision(
        @RequestBody  CompanyDTO dto
    ) {

        ContextDTO context = getContext();

        Long operator = 1L;
        if (context.getOperator() != null) {
            operator = context.getOperator().getId();
        }
        return new JsonObjectResponseBody<>(
            context,
            userSettingService.createDivision(
                operator,
                dto
            )
        );

    }

    @Override
    @RequestMapping(
        method = GET,
        value = "/users/division",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonListResponseBody<Division> searchDivision(
        CompanyDTO dto,
        PageDTO page) {

        return new JsonListResponseBody<>(
            getContext(),
            userSettingService.searchDivision(dto,page)
        );
    }

    @Override
    @RequestMapping(
        method = GET,
        value = "/users/division/{id}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonObjectResponseBody<Division> detailDivision(
        @PathVariable  Long id) {
        return new JsonObjectResponseBody<>(
            getContext(), userSettingService.detailDivision(id)
        );
    }

    @Override
    @RequestMapping(
        method = POST,
        value = "/users/division/{id}"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody editDivision(
        @PathVariable  Long id,
        @RequestBody   CompanyDTO dto

    ) {
        userSettingService.updateDivision(id, dto, getContext());
        return new JsonResponseBody();
    }

    @Override
    @RequestMapping(
        method = DELETE,
        value = "/users/division/{id}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody deleteDivision(
        @PathVariable  Long id) {
        userSettingService.deleteDivision(id, getContext());
        return new JsonResponseBody();
    }

    @Override
    @RequestMapping(
        method = POST,
        value = "/users/department",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(CREATED)
    public JsonObjectResponseBody<Department> createDepartment(
        @RequestBody  CompanyDTO dto
    ) {

        ContextDTO context = getContext();

        Long operator = 1L;
        if (context.getOperator() != null) {
            operator = context.getOperator().getId();
        }
        return new JsonObjectResponseBody<>(
            context,
            userSettingService.createDepartment(
                operator,
                dto
            )
        );

    }

    @Override
    @RequestMapping(
        method = GET,
        value = "/users/department",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonListResponseBody<Department> searchDepartment(
        CompanyDTO dto,
        PageDTO page) {

        return new JsonListResponseBody<>(
            getContext(),
            userSettingService.searchDepartment(dto,page)
        );
    }

    @Override
    @RequestMapping(
        method = GET,
        value = "/users/department/{id}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonObjectResponseBody<Department> detailDepartment(
        @PathVariable  Long id) {
        return new JsonObjectResponseBody<>(
            getContext(), userSettingService.detailDepartment(id)
        );
    }

    @Override
    @RequestMapping(
        method = POST,
        value = "/users/department/{id}"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody editDepartment(
        @PathVariable  Long id,
        @RequestBody   CompanyDTO dto

    ) {
        userSettingService.updateDepartment(id, dto, getContext());
        return new JsonResponseBody();
    }

    @Override
    @RequestMapping(
        method = DELETE,
        value = "/users/department/{id}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody deleteDepartment(
        @PathVariable  Long id) {
        userSettingService.deleteDepartment(id, getContext());
        return new JsonResponseBody();
    }

    @Override
    @RequestMapping(
        method = POST,
        value = "/users/team",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(CREATED)
    public JsonObjectResponseBody<Team> createTeam(
        @RequestBody  CompanyDTO dto
    ) {

        ContextDTO context = getContext();

        Long operator = 1L;
        if (context.getOperator() != null) {
            operator = context.getOperator().getId();
        }
        return new JsonObjectResponseBody<>(
            context,
            userSettingService.createTeam(
                operator,
                dto
            )
        );

    }

    @Override
    @RequestMapping(
        method = GET,
        value = "/users/team",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonListResponseBody<Team> searchTeam(
        CompanyDTO dto,
        PageDTO page) {

        return new JsonListResponseBody<>(
            getContext(),
            userSettingService.searchTeam(dto,page)
        );
    }

    @Override
    @RequestMapping(
        method = GET,
        value = "/users/team/{id}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonObjectResponseBody<Team> detailTeam(
        @PathVariable  Long id) {
        return new JsonObjectResponseBody<>(
            getContext(), userSettingService.detailTeam(id)
        );
    }

    @Override
    @RequestMapping(
        method = POST,
        value = "/users/team/{id}"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody editTeam(
        @PathVariable  Long id,
        @RequestBody   CompanyDTO dto

    ) {
        userSettingService.updateTeam(id, dto, getContext());
        return new JsonResponseBody();
    }

    @Override
    @RequestMapping(
        method = DELETE,
        value = "/users/team/{id}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody deleteTeam(
        @PathVariable  Long id) {
        userSettingService.deleteTeam(id, getContext());
        return new JsonResponseBody();
    }

    @Override
    @RequestMapping(
        method = GET,
        value = "/users/company/all",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonListResponseBody<Company> searchAllCompany() {

        return new JsonListResponseBody<>(
            getContext(),
            userSettingService.searchAllCompany()
        );
    }

    @Override
    @RequestMapping(
        method = GET,
        value = "/users/division/all",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonListResponseBody<Division> searchAllDivision() {

        return new JsonListResponseBody<>(
            getContext(),
            userSettingService.searchAllDivision()
        );
    }

    @Override
    @RequestMapping(
        method = GET,
        value = "/users/department/all",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonListResponseBody<Department> searchAllDepartment() {

        return new JsonListResponseBody<>(
            getContext(),
            userSettingService.searchAllDepartment()
        );
    }

    @Override
    @RequestMapping(
        method = GET,
        value = "/users/team/all",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonListResponseBody<Team> searchAllTeam() {

        return new JsonListResponseBody<>(
            getContext(),
            userSettingService.searchAllTeam()
        );
    }

}
