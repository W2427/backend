package com.ose.tasks.dto.wps.simple;


import com.ose.dto.PageDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;
import java.util.Set;

public class WelderSimpleSearchDTO extends PageDTO {

    @Schema(description = "焊工编号")
    private String no;

    @Schema(description = "焊工名")
    private String name;

    @Schema(description = "用户 No")
    private String userNo;

    @Schema(description = "分包商 No")
    private String subConNo;

    @Schema(description = "分包商 Id")
    private String subConId;

    @Schema(description = "用户 Id集合")
    private Set<Long> userIds;

    @Schema(description = "公司 ID")
    private Set<Long> subConIds;

    @Schema(description = "身份证号")
    private String idCard;

    @Schema(description = "关键字")
    private String keyword;

    @Schema(description = "证书过期时间")
    private Date cerExpirationAt;

    @Schema(description = "焊工状态")
    private String welderStatus;

    @Schema(description = "排序方式")
    private String sortChange;

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

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    public String getSubConNo() {
        return subConNo;
    }

    public void setSubConNo(String subConNo) {
        this.subConNo = subConNo;
    }

    public Set<Long> getUserIds() {
        return userIds;
    }

    public void setUserIds(Set<Long> userIds) {
        this.userIds = userIds;
    }

    public Set<Long> getSubConIds() {
        return subConIds;
    }

    public void setSubConIds(Set<Long> subConIds) {
        this.subConIds = subConIds;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getSubConId() {
        return subConId;
    }

    public void setSubConId(String subConId) {
        this.subConId = subConId;
    }

    public Date getCerExpirationAt() {
        return cerExpirationAt;
    }

    public void setCerExpirationAt(Date cerExpirationAt) {
        this.cerExpirationAt = cerExpirationAt;
    }

    public String getWelderStatus() {
        return welderStatus;
    }

    public void setWelderStatus(String welderStatus) {
        this.welderStatus = welderStatus;
    }

    public String getSortChange() {
        return sortChange;
    }

    public void setSortChange(String sortChange) {
        this.sortChange = sortChange;
    }

}
