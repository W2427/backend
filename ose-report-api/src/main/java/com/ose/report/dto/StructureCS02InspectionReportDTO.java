package com.ose.report.dto;

import java.util.List;

public class StructureCS02InspectionReportDTO extends BaseListReportDTO{

    private static final long serialVersionUID = 5749866997007703330L;

    private String seriesNo;

    private String moduleNo;

    private String tagNo;

    private String drawingNo;

    private List<StructureCS02InspectionReportItemDTO> items;

    public String getSeriesNo() {
        return seriesNo;
    }

    public void setSeriesNo(String seriesNo) {
        this.seriesNo = seriesNo;
    }

    @Override
    public String getModuleNo() {
        return moduleNo;
    }

    @Override
    public void setModuleNo(String moduleNo) {
        this.moduleNo = moduleNo;
    }

    @Override
    public String getTagNo() {
        return tagNo;
    }

    @Override
    public void setTagNo(String tagNo) {
        this.tagNo = tagNo;
    }

    @Override
    public String getDrawingNo() {
        return drawingNo;
    }

    @Override
    public void setDrawingNo(String drawingNo) {
        this.drawingNo = drawingNo;
    }

    @Override
    public List<StructureCS02InspectionReportItemDTO> getItems() {
        return items;
    }

    public void setItems(List<StructureCS02InspectionReportItemDTO> items) {
        this.items = items;
    }
}
