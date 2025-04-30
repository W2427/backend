package com.ose.tasks.dto.wps.simple;

import io.swagger.v3.oas.annotations.media.Schema;
public class WelderGradeSimpleUpdateDTO {

    private static final long serialVersionUID = 5102578568493123666L;

    @Schema(description = "焊工等级编号")
    private String no;

    @Schema(description = "焊工等级证照片")
    private Long photo;

    @Schema(description = "备注")
    private String remark;

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public Long getPhoto() {
        return photo;
    }

    public void setPhoto(Long photo) {
        this.photo = photo;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
