package com.ose.tasks.dto.drawing;

import com.ose.tasks.dto.WBSEntryCriteriaBaseDTO;
import com.ose.vo.DrawingStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.io.Serial;
import java.util.List;

/**
 * 图纸筛选dto
 */
public class DrawingFilterDTO extends WBSEntryCriteriaBaseDTO {

    private static final long serialVersionUID = -1901300014648715156L;

    private List<String> disciplineList;

    private List<String> docTypeList;

    public List<String> getDisciplineList() {
        return disciplineList;
    }

    public void setDisciplineList(List<String> disciplineList) {
        this.disciplineList = disciplineList;
    }

    public List<String> getDocTypeList() {
        return docTypeList;
    }

    public void setDocTypeList(List<String> docTypeList) {
        this.docTypeList = docTypeList;
    }
}
