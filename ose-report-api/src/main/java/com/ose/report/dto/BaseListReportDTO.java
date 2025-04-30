package com.ose.report.dto;

import com.ose.dto.ContextDTO;

import java.util.List;

public abstract class BaseListReportDTO<T extends BaseReportListItemDTO> extends BaseReportDTO {

    private static final long serialVersionUID = -7204698914423619430L;

    private ContextDTO contextDTO;

    public BaseListReportDTO() {
        super();
    }

    public BaseListReportDTO(String reportName, String reportNoPrefix) {
        super(reportName, reportNoPrefix);
    }

    public abstract List<T> getItems();

    public ContextDTO getContextDTO() {
        return contextDTO;
    }

    public void setContextDTO(ContextDTO contextDTO) {
        this.contextDTO = contextDTO;
    }
}
