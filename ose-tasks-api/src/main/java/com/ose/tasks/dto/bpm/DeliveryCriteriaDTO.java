package com.ose.tasks.dto.bpm;


import com.ose.dto.BaseDTO;
import com.ose.vo.DisciplineCode;
import io.swagger.v3.oas.annotations.media.Schema;

public class DeliveryCriteriaDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 1663208846470310343L;

    @Schema(description = "实体编号")
    private String entityNo;

    @Schema(description = "编号，名称")
    private String keyword;

    @Schema(description = "工序")
    private Long process;

    @Schema(description = "专业")
    private DisciplineCode discipline;

    @Schema(description = "完成状态")
    private String finishState;

    public String getEntityNo() {
        return entityNo;
    }

    public void setEntityNo(String entityNo) {
        this.entityNo = entityNo;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public DisciplineCode getDiscipline() {
        return discipline;
    }

    public void setDiscipline(DisciplineCode discipline) {
        this.discipline = discipline;
    }

    public String getFinishState() {
        return finishState;
    }

    public void setFinishState(String finishState) {
        this.finishState = finishState;
    }

    public Long getProcess() {
        return process;
    }

    public void setProcess(Long process) {
        this.process = process;
    }
}
