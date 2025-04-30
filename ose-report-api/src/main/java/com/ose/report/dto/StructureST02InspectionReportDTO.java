package com.ose.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;
import java.util.List;

public class StructureST02InspectionReportDTO extends BaseListReportDTO{

    private static final long serialVersionUID = -968205389356024457L;

    private String seriesNo;

    private String total;

    private String tagNo;

    private List<StructureST02InspectionReportItemDTO> items;

    public String getSeriesNo() {
        return seriesNo;
    }

    public void setSeriesNo(String seriesNo) {
        this.seriesNo = seriesNo;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    @Override
    public List<StructureST02InspectionReportItemDTO> getItems() {
        return items;
    }

    public void setItems(List<StructureST02InspectionReportItemDTO> items) {
        this.items = items;
    }

    @Override
    public String getTagNo() {
        return tagNo;
    }

    @Override
    public void setTagNo(String tagNo) {
        this.tagNo = tagNo;
    }
}
