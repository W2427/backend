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

/**
 * 用户数据传输对象类。
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserDTO extends BaseDTO {

    private static final long serialVersionUID = 1890200029283759079L;

    @Schema(description = "用户类型（system；super；administrator；user）")
    private String type;

    @Schema(description = "头像地址")
    private String logo;

    @Schema(description = "姓名")
    private String name;

    @Schema(description = "手机号码")
    private String mobile;

    @Schema(description = "电子邮箱地址")
    private String email;

    @Schema(description = "登录用户名")
    private String username;

    @Schema(description = "登录密码")
    private String password;

    @Schema(description = "公司")
    private String company;

    @Schema(description = "状态")
    private EntityStatus status;

    @Schema(description = "默认发送邮箱")
    private String defaultSendEmail;

    @Schema(description = "一级部门")
    private String division;

    @Schema(description = "二级部门")
    private String department;

    @Schema(description = "二级部门")
    private String team;

    @Schema(description = "国籍")
    private Boolean nationality;

    @Schema(description = "性别")
    private GenderType gender;

    @Schema(description = "角色")
    private String title;

    @Schema(description = "入职日期")
    private Date dateOfEmployment;

    @Schema(description = "离职日期")
    private Date dateOfTermination;

    @Schema(description = "审核角色")
    private Boolean reviewRole;

    @Schema(description = "审核角色")
    private Boolean teamLeader;

    @Schema(description = "审核角色")
    private Boolean divisionVP;

    @Schema(description = "审核角色")
    private Boolean companyGM;

    @Schema(description = "加班申请不用审批")
    private Boolean noApprovalRequired;

    @Schema(description = "自动填报工时")
    private Boolean autoFillHours;

    @Schema(description = "是否在项目上")
    private Boolean onProject;

    @Schema(description = "个人邮箱")
    private String personalEmail;

    @Schema(description = "身份证号")
    private String icNumber;

    @Schema(description = "生日")
    private String birthday;

    @Schema(description = "行业工作经验（年）")
    private Integer workingYearsInIndustry;

    @Schema(description = "项目现场经验（年）")
    private Integer projectSiteExperience;

    @Schema(description = "联系地址")
    private String contactAddress;

    @Schema(description = "护照编号")
    private String passportNo;

    @Schema(description = "护照签发时间")
    private String passportDateOfIssue;

    @Schema(description = "护照过期时间")
    private String passportDateOfExpire;

    @Schema(description = "开始工作日期")
    private LocalDateTime firstEmploymentDate;

    @Schema(description = "出差意愿（No, Business Trip = within 30 days, Assignment = within 60days, Shot-term：within 90days, Long-term: above 90 days）")
    private String willingnessForBusinessTravel;

    @Schema(description = "母语")
    private String motherTongue;

    @Schema(description = "第二语言")
    private String secondLanguage;

    @Schema(description = "第三语言")
    private String thirdLanguage;

    @Schema(description = "工作服尺寸")
    private String overall;

    @Schema(description = "工作鞋尺寸")
    private String safetyShoes;

    @Schema(description = "工作地点")
    private String location;

    @Schema(description = "合同签约地")
    private String contractCompany;

    @Schema(description = "合同类型：Long-term, Project-base, Retirement Rehire Agreement, Outsouring, Part-Time")
    private String contractType;

    @Schema(description = "入职时间")
    private LocalDateTime onboardingDate;

    @Schema(description = "转正月份")
    private Integer probationPeriod;

    @Schema(description = "隐藏数据 第一份合同周期 1,3,5")
    private Integer firstContractPeriod;

    @Schema(description = "隐藏数据 第一份合同结束日期")
    private LocalDateTime firstContractEndDate;

    @Schema(description = "隐藏数据 第二份合同周期 1,3,5")
    private Integer secondContractPeriod;

    @Schema(description = "隐藏数据 第二份合同结束日期")
    private LocalDateTime secondContractEndDate;

    @Schema(description = "司龄（年）")
    private Integer serviceYearsInGroup;
}
