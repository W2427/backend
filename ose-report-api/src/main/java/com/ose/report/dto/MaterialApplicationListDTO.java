package com.ose.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;
import java.util.List;

public class MaterialApplicationListDTO extends BaseListReportDTO {

    private static final long serialVersionUID = 3555546617645071621L;

    @Schema(description = "项目名称")
    private String projectName;

    @Schema(description = "使用日期")
    private Date date;

    @Schema(description = "领料单号")
    private String materialApplicationNo;
    private List<MaterialApplicationItemDTO> items;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getMaterialApplicationNo() {
        return materialApplicationNo;
    }

    public void setMaterialApplicationNo(String materialApplicationNo) {
        this.materialApplicationNo = materialApplicationNo;
    }

    @Override
    public List<MaterialApplicationItemDTO> getItems() {
        return items;
    }

    public void setItems(List<MaterialApplicationItemDTO> items) {
        this.items = items;
    }
}
