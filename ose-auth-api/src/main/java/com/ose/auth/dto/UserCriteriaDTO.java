package com.ose.auth.dto;

import com.ose.auth.vo.GenderType;
import com.ose.dto.BaseDTO;
import com.ose.vo.EntityStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Column;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * 用户查询条件数据传输对象类。
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserCriteriaDTO extends BaseDTO {

    private static final long serialVersionUID = -492739856088760105L;

    @Schema(description = "用户类型")
    private List<String> type;

    @Schema(description = "姓名")
    private String name;

    @Schema(description = "登录用户名")
    private String username;

    @Schema(description = "手机号码")
    private String mobile;

    @Schema(description = "电子邮箱地址")
    private String email;

    @Schema(description = "用户登录账号状态")
    private List<EntityStatus> status;

    @Schema(description = "关键字")
    private String keyword;

    @Schema(description = "公司")
    private String company;

    @Schema(description = "一级部门")
    private String division;

    @Schema(description = "二级部门")
    private String department;

    @Schema(description = "二级部门")
    private String team;

    @Schema(description = "性别")
    private GenderType gender;

    @Schema(description = "角色")
    private String title;

    @Schema(description = "入职日期")
    private Date dateOfEmployment;

    @Schema(description = "离职日期")
    private Date dateOfTermination;

    @Schema(description = "是否是转换界面")
    private Boolean transfer;

    @Schema(description = "是否CompangGM")
    private Boolean companyGM;

    @Schema(description = "是否divisionVP")
    private Boolean divisionVP;

    @Schema(description = "是否teamLeader")
    private Boolean teamLeader;

    @Schema(description = "是否reviewRole")
    private Boolean reviewRole;

    @Schema(description = "是否noApprovalRequired")
    private Boolean noApprovalRequired;

    @Schema(description = "是否autoFillHours")
    private Boolean autoFillHours;

    @Schema(description = "身份证号")
    private String icNumber;

    @Schema(description = "入职时间开始时间")
    private LocalDateTime onboardingDateStartTime;

    @Schema(description = "入职时间结束时间")
    private LocalDateTime onboardingDateEndTime;
}
