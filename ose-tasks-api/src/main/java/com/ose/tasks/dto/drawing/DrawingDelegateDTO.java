package com.ose.tasks.dto.drawing;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 生产设计图纸清单导入数据传输对象。
 */
public class DrawingDelegateDTO extends BaseDTO {

    private static final long serialVersionUID = -7928999976126450169L;
    @Schema(description = "设计人员")
    private String design;

    @Schema(description = "校对人员")
    private String review;

    @Schema(description = "审核人员")
    private String approve;

    public String getDesign() {
        return design;
    }

    public void setDesign(String design) {
        this.design = design;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getApprove() {
        return approve;
    }

    public void setApprove(String approve) {
        this.approve = approve;
    }
}
