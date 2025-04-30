package com.ose.tasks.dto.bpm;

import com.ose.dto.BaseDTO;
import com.ose.vo.DisciplineCode;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;
import java.util.List;

public class DeliveryDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 1663208846470310343L;


    @Schema(description = "交接实体Id列表")
    private List<Long> deliveryEntityIds;

    @Schema(description = "工序id")
    private Long processId;

    @Schema(description = "交接单名称")
    private String name;

    @Schema(description = "交接时间")
    private Date date;

    @Schema(description = "备注")
    private String memo;

    @Schema(description = "专业")
    private DisciplineCode discipline = DisciplineCode.PIPING;

    public List<Long> getDeliveryEntityIds() {
        return deliveryEntityIds;
    }

    public void setDeliveryEntityIds(List<Long> deliveryEntityIds) {
        this.deliveryEntityIds = deliveryEntityIds;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Long getProcessId() {
        return processId;
    }

    public void setProcessId(Long processId) {
        this.processId = processId;
    }

    public DisciplineCode getDiscipline() {
        return discipline;
    }

    public void setDiscipline(DisciplineCode discipline) {
        this.discipline = discipline;
    }
}
