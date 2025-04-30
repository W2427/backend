package com.ose.test.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;

/**
 * SpmMatchLnNode 查询DTO
 */
public class SpmMatchLnNodeDTO {

    /**
     *
     */

    @Schema(description = "SPM 项目ID")
    private String spmProjId = "";

    @Schema(description = "SPM BOM Match节点 ID")
    private String lnId = "";

    @Schema(description = "SPM BOM Match 节点 编号")
    private String lnCode = "";

    @Schema(description = "SPM BOM Match 节点 总数")
    private Long size = 0L;

    @Schema(description = "pageSize 一次查询条数")
    private Integer pageSize = 1000;

    @Schema(description = "pageNo 查询的页码")
    private Integer pageNo = 1;

    @Schema(description = "last modified/updated")
    private Date lMod;


    public String getSpmProjId() {
        return spmProjId;
    }

    public void setSpmProjId(String spmProjId) {
        this.spmProjId = spmProjId;
    }

    public String getLnId() {
        return lnId;
    }

    public void setLnId(String lnId) {
        this.lnId = lnId;
    }

    public String getLnCode() {
        return lnCode;
    }

    public void setLnCode(String lnCode) {
        this.lnCode = lnCode;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Date getlMod() {
        return lMod;
    }

    public void setlMod(Date lMod) {
        this.lMod = lMod;
    }
}
