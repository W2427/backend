package com.ose.test.dto;

import com.ose.dto.PageDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 请购单查询条件数据传输对象类。
 */
public class ReqListCriteriaDTO extends PageDTO {

    private static final long serialVersionUID = -492739856088760105L;

    @Schema(description = "SPM 项目ID")
    private String spmProjId = "";

    @Schema(description = "SPM 请购单编号")
    private String reqcode;

    @Schema(description = "专业")
    private String dpid;

    @Schema(description = "采购员")
    private String buyer;

    public String getSpmProjId() {
        return spmProjId;
    }

    public void setSpmProjId(String spmProjId) {
        this.spmProjId = spmProjId;
    }

    public String getReqcode() {
        return reqcode;
    }

    public void setReqcode(String reqcode) {
        this.reqcode = reqcode;
    }

    public String getDpid() {
        return dpid;
    }

    public void setDpid(String dpid) {
        this.dpid = dpid;
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

}
