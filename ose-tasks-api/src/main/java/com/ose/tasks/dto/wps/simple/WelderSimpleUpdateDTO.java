package com.ose.tasks.dto.wps.simple;


import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;

public class WelderSimpleUpdateDTO {

    private static final long serialVersionUID = -63378699672653298L;

    @Schema(description = "焊工编号")
    private String no;

    @Schema(description = "焊工名")
    private String name;

    @Schema(description = "用户 ID")
    private Long userId;

    @Schema(description = "用户头像")
    private String photo;

    @Schema(description = "考试WPS")
    private String wpsWelded;

    @Schema(description = "考试焊接方法")
    private String processWelded;

    @Schema(description = "公司 ID")
    private Long subConId;

    @Schema(description = "身份证号")
    private String idCard;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "证书过期时间")
    private Date cerExpirationAt;

    @Schema(description = "证书编号")
    private String cerId;

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Long getSubConId() {
        return subConId;
    }

    public void setSubConId(Long subConId) {
        this.subConId = subConId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getWpsWelded() {
        return wpsWelded;
    }

    public void setWpsWelded(String wpsWelded) {
        this.wpsWelded = wpsWelded;
    }

    public String getProcessWelded() {
        return processWelded;
    }

    public void setProcessWelded(String processWelded) {
        this.processWelded = processWelded;
    }

    public Date getCerExpirationAt() {
        return cerExpirationAt;
    }

    public void setCerExpirationAt(Date cerExpirationAt) {
        this.cerExpirationAt = cerExpirationAt;
    }

    public String getCerId() {
        return cerId;
    }

    public void setCerId(String cerId) {
        this.cerId = cerId;
    }
}
