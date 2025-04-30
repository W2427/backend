package com.ose.tasks.dto.material;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 用户查询条件数据传输对象类。
 */
public class ReqListCriteriaDTO extends BaseDTO {

    private static final long serialVersionUID = -492739856088760105L;


    @Schema(description = "项目ID")
    private Long projectId;

    @Schema(description = "请购单编号")
    private String reqCode;

    @Schema(description = "专业")
    private String dpId;

    @Schema(description = "采购员")
    private String buyer;


    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getReqCode() {
        return reqCode;
    }

    public void setReqCode(String reqCode) {
        this.reqCode = reqCode;
    }

    public String getDpId() {
        return dpId;
    }

    public void setDpId(String dpId) {
        this.dpId = dpId;
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }


}
