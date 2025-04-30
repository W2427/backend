package com.ose.auth.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.ose.auth.vo.GenderType;
import com.ose.auth.vo.easyExcel.GenderTypeConverter;
import com.ose.auth.vo.easyExcel.TimeStampConverter;
import com.ose.entity.BaseVersionedBizEntity;
import com.ose.util.RegExpUtils;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 用户资料数据实体类。
 */
@EqualsAndHashCode(callSuper = true)
@MappedSuperclass
@Data
public abstract class UserBase extends BaseVersionedBizEntity {

    private static final long serialVersionUID = -2968374981451904737L;

    @Schema(description = "用户类型（system；super；administrator；user）")
    @Column(nullable = false, length = 16)
    @NotNull(message = "user's type is required")
    private String type;

    @Schema(description = "头像地址")
    @Column(length = 128)
    private String logo;

    @Schema(description = "姓名")
    @Column(nullable = false, length = 64)
    @NotNull(message = "user's name is required")
    private String name;

    @Schema(description = "手机号码")
    @Column(length = 16)
    @Pattern(regexp = RegExpUtils.MOBILE, message = "mobile number is invalid")
    private String mobile;

    @Schema(description = "电子邮箱地址")
    @Column(length = 64)
    @Pattern(regexp = RegExpUtils.EMAIL, message = "Email address is invalid")
    private String email;

    @Schema(description = "登录用户名")
    @Column(length = 32)
    @Pattern(regexp = RegExpUtils.USERNAME, message = "username is invalid")
    private String username;

    @Schema(description = "公司")
    @Column
    private String company;

    @Schema(description = "一级部门")
    @Column
    private String division;

    @Schema(description = "二级部门")
    @Column
    private String department;

    @Schema(description = "团队")
    @Column
    private String team;

    @Schema(description = "国籍")
    private Boolean nationality;

    @Schema(description = "性别")
    @Column
    @ExcelProperty(converter = GenderTypeConverter.class)
    private GenderType gender;

    @Schema(description = "角色")
    @Column
    private String title;

    @Schema(description = "入职日期")
    @Column
    @ExcelProperty(converter = TimeStampConverter.class)
    private Date dateOfEmployment;

    @Schema(description = "离职日期")
    @Column
    @ExcelProperty(converter = TimeStampConverter.class)
    private Date dateOfTermination;

    @Schema(description = "公司加班申请复核")
    @Column
    private Boolean reviewRole = false;

    @Schema(description = "")
    @Column
    private Boolean applyRole = false;

    @Schema(description = "团队领导")
    @Column
    private Boolean teamLeader = false;

    @Schema(description = "部门副总裁")
    @Column
    private Boolean divisionVP = false;

    @Schema(description = "公司经理")
    @Column
    private Boolean companyGM = false;

    @Schema(description = "加班申请不用审批")
    @Column
    private Boolean noApprovalRequired = false;

    @Schema(description = "自动填报工时")
    @Column
    private Boolean autoFillHours = false;

    @Schema(description = "自动填报工时")
    @Column
    private String reviewOtherCompany ;

    @Schema(description = "是否在项目上")
    @Column
    private Boolean onProject = false;

    @Column(name = "personal_email")
    @Schema(description = "个人邮箱")
    private String personalEmail;

    @Column(name = "ic_number")
    @Schema(description = "身份证号")
    private String icNumber;

    @Column(name = "birthday")
    @Schema(description = "生日")
    @ExcelProperty(converter = TimeStampConverter.class)
    private Date birthday;

    @Column(name = "working_years_in_industry")
    @Schema(description = "行业工作经验（年）")
    private Integer workingYearsInIndustry;

    @Column(name = "project_site_experience")
    @Schema(description = "项目现场经验（年）")
    private Integer projectSiteExperience;

    @Column(name = "contact_address")
    @Schema(description = "联系地址")
    private String contactAddress;

    @Column(name = "passport_no")
    @Schema(description = "护照编号")
    private String passportNo;

    @Column(name = "passport_date_of_issue")
    @Schema(description = "护照签发时间")
    private String passportDateOfIssue;

    @Column(name = "passport_date_of_expire")
    @Schema(description = "护照过期时间")
    private String passportDateOfExpire;

    @Column(name = "first_employment_date")
    @Schema(description = "开始工作日期")
    @ExcelProperty(converter = TimeStampConverter.class)
    private Date firstEmploymentDate;

    @Column(name = "willingness_for_business_travel")
    @Schema(description = "出差意愿（No, Business Trip = within 30 days, Assignment = within 60days, Shot-term：within 90days, Long-term: above 90 days）")
    private String willingnessForBusinessTravel;

    @Column(name = "mother_tongue")
    @Schema(description = "母语")
    private String motherTongue;

    @Column(name = "second_language")
    @Schema(description = "第二语言")
    private String secondLanguage;

    @Column(name = "third_language")
    @Schema(description = "第三语言")
    private String thirdLanguage;

    @Column(name = "overall")
    @Schema(description = "工作服尺寸")
    private String overall;

    @Column(name = "safety_shoes")
    @Schema(description = "工作鞋尺寸")
    private String safetyShoes;

    @Column(name = "location")
    @Schema(description = "工作地点")
    private String location;

    @Column(name = "contract_number")
    @Schema(description = "合同编号")
    private String contractNumber;

    @Column(name = "contract_company")
    @Schema(description = "合同签约地")
    private String contractCompany;

    @Column(name = "contract_type")
    @Schema(description = "合同类型：Long-term, Project-base, Retirement Rehire Agreement, Outsouring, Part-Time")
    private String contractType;

    @Column(name = "onboarding_date")
    @Schema(description = "入职时间")
    @ExcelProperty(converter = TimeStampConverter.class)
    private Date onboardingDate;

    @Column(name = "probation_period")
    @Schema(description = "转正月份")
    private Integer probationPeriod;

    @Column(name = "first_contract_period")
    @Schema(description = "隐藏数据 第一份合同周期 1,3,5")
    private Integer firstContractPeriod;

    @Column(name = "first_contract_end_date")
    @Schema(description = "隐藏数据 第一份合同结束日期")
    @ExcelProperty(converter = TimeStampConverter.class)
    private Date firstContractEndDate;

    @Column(name = "second_contract_period")
    @Schema(description = "隐藏数据 第二份合同周期 1,3,5")
    @ExcelProperty(converter = TimeStampConverter.class)
    private Integer secondContractPeriod;

    @Column(name = "second_contract_end_date")
    @Schema(description = "隐藏数据 第二份合同结束日期")
    @ExcelProperty(converter = TimeStampConverter.class)
    private Date secondContractEndDate;

    @Column(name = "first_degree")
    @Schema(description = "第一学历")
    private String firstDegree;

    @Column(name = "age_group")
    @Schema(description = "年龄段")
    private String ageGroup;

    @Column(name = "service_years")
    @Schema(description = "司龄")
    private String serviceYears;

    @Column(name = "onboarding_month")
    @Schema(description = "入职月份")
    private Integer onboardingMonth;

    @Column(name = "job_title")
    @Schema(description = "职称")
    private String jobTitle;

    /**
     * 构造方法。
     */
    public UserBase() {
        this(null);
    }

    /**
     * 构造方法。
     *
     * @param id 用户 ID
     */
    public UserBase(Long id) {
        super(id);
    }
}
