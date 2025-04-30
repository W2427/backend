package com.ose.report.dto;

import com.ose.dto.BaseDTO;

/**
 * 检查单信息 数据传输对象
 */
public class ChecklistInfoDTO extends BaseDTO {

    private static final long serialVersionUID = -1462430027833858309L;

    // 设备代码
    private String tagNo = "";

    // 位置
    private String location = "";

    // 设备描述
    private String tagDesc = "";

    // 系统代码
    private String sysNo = "";

    // 子系统代码
    private String cpNo = "";

    // 图纸代码
    private String dwgNo = "";

    // 子系统描述
    private String subSysDesc = "";

    /**
     * Gets the value of tagNo.
     *
     * @return the value of tagNo
     */
    public String getTagNo() {
        return tagNo;
    }

    /**
     * Sets the tagNo.
     *
     * <p>You can use getTagNo() to get the value of tagNo</p>
     *
     * @param tagNo tagNo
     */
    public void setTagNo(String tagNo) {
        this.tagNo = tagNo;
    }

    /**
     * Gets the value of location.
     *
     * @return the value of location
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets the location.
     *
     * <p>You can use getLocation() to get the value of location</p>
     *
     * @param location location
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Gets the value of tagDesc.
     *
     * @return the value of tagDesc
     */
    public String getTagDesc() {
        return tagDesc;
    }

    /**
     * Sets the tagDesc.
     *
     * <p>You can use getTagDesc() to get the value of tagDesc</p>
     *
     * @param tagDesc tagDesc
     */
    public void setTagDesc(String tagDesc) {
        this.tagDesc = tagDesc;
    }

    /**
     * Gets the value of sysNo.
     *
     * @return the value of sysNo
     */
    public String getSysNo() {
        return sysNo;
    }

    /**
     * Sets the sysNo.
     *
     * <p>You can use getSysNo() to get the value of sysNo</p>
     *
     * @param sysNo sysNo
     */
    public void setSysNo(String sysNo) {
        this.sysNo = sysNo;
    }

    /**
     * Gets the value of cpNo.
     *
     * @return the value of cpNo
     */
    public String getCpNo() {
        return cpNo;
    }

    /**
     * Sets the cpNo.
     *
     * <p>You can use getCpNo() to get the value of cpNo</p>
     *
     * @param cpNo cpNo
     */
    public void setCpNo(String cpNo) {
        this.cpNo = cpNo;
    }

    /**
     * Gets the value of dwgNo.
     *
     * @return the value of dwgNo
     */
    public String getDwgNo() {
        return dwgNo;
    }

    /**
     * Sets the dwgNo.
     *
     * <p>You can use getDwgNo() to get the value of dwgNo</p>
     *
     * @param dwgNo String
     */
    public void setDwgNo(String dwgNo) {
        this.dwgNo = dwgNo;
    }

    /**
     * Gets the value of subSysDesc.
     *
     * @return the value of subSysDesc
     */
    public String getSubSysDesc() {
        return subSysDesc;
    }

    /**
     * Sets the subSysDesc.
     *
     * <p>You can use getSubSysDesc() to get the value of subSysDesc</p>
     *
     * @param subSysDesc subSysDesc
     */
    public void setSubSysDesc(String subSysDesc) {
        this.subSysDesc = subSysDesc;
    }

    /**
     * 字符串转换
     *
     * @return 字符串
     */
    @Override
    public String toString() {
        return "ChecklistInfoDTO{" + "tagNo='" + tagNo + '\'' + ", location='" + location + '\'' + ", tagDesc='" + tagDesc + '\'' + ", sysNo='" + sysNo + '\'' + ", cpNo='" + cpNo + '\'' + ", dwgNo='" + dwgNo + '\'' + ", subSysDesc='" + subSysDesc + '\'' + '}';
    }
}
