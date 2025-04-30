package com.ose.tasks.dto.wps;

import com.ose.dto.PageDTO;
import com.ose.vo.DisciplineCode;
import com.ose.vo.EntityStatus;
import io.swagger.v3.oas.annotations.media.Schema;

public class WeldWelderRelationSearchDTO extends PageDTO {

    private static final long serialVersionUID = 7631214316923449273L;

    @Schema(description = "焊口编号")
    private String weldNo;

    @Schema(description = "焊工编号")
    private String welderNo;

    @Schema(description = "工序（打底A、填充B、盖面C）")
    private String process;

    @Schema(description = "状态")
    private EntityStatus status;

    @Schema(description = "专业")
    private DisciplineCode discipline;

    public String getWeldNo() {
        return weldNo;
    }

    public void setWeldNo(String weldNo) {
        this.weldNo = weldNo;
    }

    public String getWelderNo() {
        return welderNo;
    }

    public void setWelderNo(String welderNo) {
        this.welderNo = welderNo;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public EntityStatus getStatus() {
        return status;
    }

    public void setStatus(EntityStatus status) {
        this.status = status;
    }

    public DisciplineCode getDiscipline() {
        return discipline;
    }

    public void setDiscipline(DisciplineCode discipline) {
        this.discipline = discipline;
    }
}
