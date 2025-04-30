package com.ose.tasks.Event;


import com.ose.dto.OperatorDTO;
import com.ose.tasks.dto.qc.TestResultDTO;
import org.springframework.context.ApplicationEvent;

/**
 * 产生建造日志事件
 */
public class ConstructionLogEvent<T extends TestResultDTO> extends ApplicationEvent {


    private OperatorDTO operator;


    private String wbsEntityType;

    private Long entityId;

    private String processNameEn;

    private Long processId;

    private String processStage;

    T processTestResultDTO;


    public ConstructionLogEvent(Object source,
                                OperatorDTO operator,
                                String wbsEntityType,
                                Long entityId,
                                String processNameEn,
                                Long processId,
                                String processStage,
                                T processTestResultDTO) {
        super(source);
        this.entityId = entityId;
        this.operator = operator;
        this.wbsEntityType = wbsEntityType;
        this.processNameEn = processNameEn;
        this.processTestResultDTO = processTestResultDTO;
        this.processId = processId;
        this.processStage = processStage;
    }

    public OperatorDTO getOperator() {
        return operator;
    }

    public void setOperator(OperatorDTO operator) {
        this.operator = operator;
    }

    public String getEntityType() {
        return wbsEntityType;
    }

    public void setWbsEntityType(String wbsEntityType) {
        this.wbsEntityType = wbsEntityType;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getProcessNameEn() {
        return processNameEn;
    }

    public void setProcessNameEn(String processNameEn) {
        this.processNameEn = processNameEn;
    }

    public T getProcessTestResultDTO() {
        return processTestResultDTO;
    }

    public void setProcessTestResultDTO(T processTestResultDTO) {
        this.processTestResultDTO = processTestResultDTO;
    }


    public Long getProcessId() {
        return processId;
    }

    public void setProcessId(Long processId) {
        this.processId = processId;
    }

    public String getProcessStage() {
        return processStage;
    }

    public void setProcessStage(String processStage) {
        this.processStage = processStage;
    }
}
