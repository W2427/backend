package com.ose.report.dto;

import com.ose.dto.ContextDTO;
import com.ose.report.entity.dynamicreport.YnnData;

import java.util.List;

public abstract class BaseItrListReportDTO<T extends BaseReportListItemDTO> extends BaseReportDTO {


    private static final long serialVersionUID = 4366502384187433081L;
    private ContextDTO contextDTO;

    private List<YnnData> ynnItems;

    public BaseItrListReportDTO() {
        super();
    }

    public BaseItrListReportDTO(String reportName, String reportNoPrefix) {
        super(reportName, reportNoPrefix);
    }

    public abstract List<List<T>> getItems();

    public ContextDTO getContextDTO() {
        return contextDTO;
    }

    public void setContextDTO(ContextDTO contextDTO) {
        this.contextDTO = contextDTO;
    }

    public List<YnnData> getYnnItems() {
        return ynnItems;
    }

    public void setYnnItems(List<YnnData> ynnItems) {
        this.ynnItems = ynnItems;
    }
}
