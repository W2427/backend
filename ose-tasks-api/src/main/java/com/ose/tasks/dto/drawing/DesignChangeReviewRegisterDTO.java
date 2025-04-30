package com.ose.tasks.dto.drawing;

import java.util.Date;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

public class DesignChangeReviewRegisterDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Schema(description = "传送单号")
    private String transferNo;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "问题提出时间")
    private Date raisedDate;

    @Schema(description = "设计变更审批单号")
    private String vorNo;

    @Schema(description = "修改发生根源")
    private String originatedBy;

    public String getTransferNo() {
        return transferNo;
    }

    public void setTransferNo(String transferNo) {
        this.transferNo = transferNo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getRaisedDate() {
        return raisedDate;
    }

    public void setRaisedDate(Date raisedDate) {
        this.raisedDate = raisedDate;
    }

    public String getVorNo() {
        return vorNo;
    }

    public void setVorNo(String vorNo) {
        this.vorNo = vorNo;
    }

}
