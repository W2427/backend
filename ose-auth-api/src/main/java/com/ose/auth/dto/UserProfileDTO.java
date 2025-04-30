package com.ose.auth.dto;

import com.ose.auth.vo.GenderType;
import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Column;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserProfileDTO extends BaseDTO {

    private static final long serialVersionUID = 664854712922532752L;

    @Schema(description = "用户名称")
    private String name;

    @Schema(description = "用户登录名")
    private String username;

    @Schema(description = "用户手机号")
    private String mobile;

    @Schema(description = "公司邮箱")
    private String email;

    @Schema(description = "用户类型")
    private String type;

    @Schema(description = "公司")
    private String company;

    @Schema(description = "一级部门")
    private String division;

    @Schema(description = "二级部门")
    private String department;

    @Schema(description = "二级部门")
    private String team;

    @Schema(description = "英文姓")
    private String firstNameEn;

    @Schema(description = "英文名")
    private String lastNameEn;

    @Schema(description = "中文姓")
    private String firstNameCn;

    @Schema(description = "中文名")
    private String lastNameCn;

    @Schema(description = "性别")
    private GenderType gender;

    @Schema(description = "角色")
    @Column
    private String title;

    @Schema(description = "入职日期")
    private Date dateOfEmployment;

    @Schema(description = "离职日期")
    private Date dateOfTermination;

    @Schema(description = "国籍")
    private Boolean nationality;

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
    private Boolean autoFillHours = false;

    @Schema(description = "是否在项目上")
    private Boolean onProject = false;

    public Boolean getOnProject() {
        return onProject;
    }

    public void setOnProject(Boolean onProject) {
        this.onProject = onProject;
    }

    private List<String> reviewOtherCompany;
}
