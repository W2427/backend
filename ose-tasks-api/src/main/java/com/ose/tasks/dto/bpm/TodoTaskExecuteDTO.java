package com.ose.tasks.dto.bpm;

import com.ose.dto.BaseDTO;
import com.ose.vo.unit.LengthUnit;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 实体管理 数据传输对象
 */
public class TodoTaskExecuteDTO extends BaseDTO {

    /**
     * 前台传给后台的 id 为 task_id，是bpm服务中的主键 123456
     */
    private static final long serialVersionUID = 4517659110867097693L;

    private Long id; //task 服务 bpm_ru_task中的 id BT23RBQ334

    @Schema(description = "bpm 服务中的 task id 123456123456123456")
    private Long taskId;

    private Set<Long> ids;//bpm 服务 中的 task_id 123456

    //    WPS No
    private String wpsNo;

    private String wpsId;

    //    焊口实体id
    private Long weldEntityId;

    @Schema(description = "会签人员")
    private List<Long> assignees;

    // 实际工时
    private Double costHour;

    private List<String> attachFiles;

    @Schema(description = "焊工焊口关系A")
    private List<Long> processA;

    @Schema(description = "焊工焊口关系B")
    private List<Long> processB;

    @Schema(description = "焊工焊口关系C")
    private List<Long> processC;

    @Schema(description = "焊接日期")
    private String weldTime;

    @Schema(description = "探伤缺陷长度")
    private String length;

    @Schema(description = "探伤缺陷长度单位")
    @Enumerated(EnumType.STRING)
    private LengthUnit lengthUnit;

    @Schema(description = "探伤检测长度")
    private String testLength;

    @Schema(description = "探伤检测长度单位")
    @Enumerated(EnumType.STRING)
    private LengthUnit testLengthUnit;

    @Schema(description = "下个节点执行人id")
    private Long nextAssignee;

    @Schema(description = "teamId")
    private Long teamId;

    private String comment;

    private String barCode;

    private Long barCodeId;

    private List<String> pictures;

    private Map<String, Object> command;

    private Map<String, Object> variables;

    //外检时间
    private Date externalInspectionTime;

    //内检时间
    private Date internalInspectionTime;

    //外检报告邮件
    private String externalInspectionEmail;

    private String externalStatus;

    //材料信息
    private List<MaterialInfoDTO> materialInfo;

    private Boolean isOverrideDelegate;

    private Boolean isOverridePostDelegate;
    // 焊材批号
    private String weldMaterialNo;

    private String materialNo;

    private String materialNoId;

//    private String materialNoa;

    private String material71Ni;

    private String material81K2;

    private String materialNob;

    private String materialNoc;

    private String materialNod;

    private String materialNoe;

    private String recordResult;

    public Long getBarCodeId() {
        return barCodeId;
    }

    public void setBarCodeId(Long barCodeId) {
        this.barCodeId = barCodeId;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getWeldMaterialNo() {
        return weldMaterialNo;
    }

    public void setWeldMaterialNo(String weldMaterialNo) {
        this.weldMaterialNo = weldMaterialNo;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Double getCostHour() {
        return costHour;
    }

    public void setCostHour(Double costHour) {
        this.costHour = costHour;
    }

    public List<String> getAttachFiles() {
        return attachFiles;
    }

    public void setAttachFiles(List<String> attachFiles) {
        this.attachFiles = attachFiles;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<String> getPictures() {
        return pictures;
    }

    public void setPictures(List<String> pictures) {
        this.pictures = pictures;
    }

    public Map<String, Object> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, Object> variables) {
        this.variables = variables;
    }

    public Map<String, Object> getCommand() {
        return command;
    }

    public void setCommand(Map<String, Object> command) {
        this.command = command;
    }

    public Date getExternalInspectionTime() {
        return externalInspectionTime;
    }

    public void setExternalInspectionTime(Date externalInspectionTime) {
        this.externalInspectionTime = externalInspectionTime;
    }

    public String getMaterial71Ni() {
        return material71Ni;
    }

    public void setMaterial71Ni(String material71Ni) {
        this.material71Ni = material71Ni;
    }

    public String getMaterial81K2() {
        return material81K2;
    }

    public void setMaterial81K2(String material81K2) {
        this.material81K2 = material81K2;
    }

    public Date getInternalInspectionTime() {
        return internalInspectionTime;
    }

    public void setInternalInspectionTime(Date internalInspectionTime) {
        this.internalInspectionTime = internalInspectionTime;
    }

    public String getExternalInspectionEmail() {
        return externalInspectionEmail;
    }

    public void setExternalInspectionEmail(String externalInspectionEmail) {
        this.externalInspectionEmail = externalInspectionEmail;
    }

    public String getExternalStatus() {
        return externalStatus;
    }

    public void setExternalstatus(String externalStatus) {
        this.externalStatus = externalStatus;
    }

    public List<MaterialInfoDTO> getMaterialInfo() {
        return materialInfo;
    }

    public void setMaterialInfo(List<MaterialInfoDTO> materialInfo) {
        this.materialInfo = materialInfo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Long> getIds() {
        return ids;
    }

    public void setIds(Set<Long> ids) {
        this.ids = ids;
    }

    public List<Long> getAssignees() {
        return assignees;
    }

    public void setAssignees(List<Long> assignees) {
        this.assignees = assignees;
    }

    public void setExternalStatus(String externalStatus) {
        this.externalStatus = externalStatus;
    }

    public Long getNextAssignee() {
        return nextAssignee;
    }

    public void setNextAssignee(Long nextAssignee) {
        this.nextAssignee = nextAssignee;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public String getWpsNo() {
        return wpsNo;
    }

    public void setWpsNo(String wpsNo) {
        this.wpsNo = wpsNo;
    }

    public Long getWeldEntityId() {
        return weldEntityId;
    }

    public void setWeldEntityId(Long weldEntityId) {
        this.weldEntityId = weldEntityId;
    }

    public String getWpsId() {
        return wpsId;
    }

    public void setWpsId(String wpsId) {
        this.wpsId = wpsId;
    }

    public Boolean getOverrideDelegate() {
        return isOverrideDelegate;
    }

    public void setOverrideDelegate(Boolean overrideDelegate) {
        isOverrideDelegate = overrideDelegate;
    }

    public Boolean getOverridePostDelegate() {
        return isOverridePostDelegate;
    }

    public void setOverridePostDelegate(Boolean overridePostDelegate) {
        isOverridePostDelegate = overridePostDelegate;
    }

    public List<Long> getProcessA() {
        return processA;
    }

    public void setProcessA(List<Long> processA) {
        this.processA = processA;
    }

    public List<Long> getProcessB() {
        return processB;
    }

    public void setProcessB(List<Long> processB) {
        this.processB = processB;
    }

    public List<Long> getProcessC() {
        return processC;
    }

    public void setProcessC(List<Long> processC) {
        this.processC = processC;
    }

    public String getWeldTime() {
        return weldTime;
    }

    public void setWeldTime(String weldTime) {
        this.weldTime = weldTime;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public LengthUnit getLengthUnit() {
        return lengthUnit;
    }

    public void setLengthUnit(LengthUnit lengthUnit) {
        this.lengthUnit = lengthUnit;
    }

    public String getMaterialNo() {
        return materialNo;
    }

    public void setMaterialNo(String materialNo) {
        this.materialNo = materialNo;
    }

    public String getMaterialNoId() {
        return materialNoId;
    }

    public void setMaterialNoId(String materialNoId) {
        this.materialNoId = materialNoId;
    }

//    public String getMaterialNoa() {
//        return materialNoa;
//    }
//
//    public void setMaterialNoa(String materialNoa) {
//        this.materialNoa = materialNoa;
//    }

    public String getMaterialNob() {
        return materialNob;
    }

    public void setMaterialNob(String materialNob) {
        this.materialNob = materialNob;
    }

    public String getMaterialNoc() {
        return materialNoc;
    }

    public void setMaterialNoc(String materialNoc) {
        this.materialNoc = materialNoc;
    }

    public String getMaterialNod() {
        return materialNod;
    }

    public void setMaterialNod(String materialNod) {
        this.materialNod = materialNod;
    }

    public String getMaterialNoe() {
        return materialNoe;
    }

    public void setMaterialNoe(String materialNoe) {
        this.materialNoe = materialNoe;
    }

    public String getTestLength() {
        return testLength;
    }

    public void setTestLength(String testLength) {
        this.testLength = testLength;
    }

    public LengthUnit getTestLengthUnit() {
        return testLengthUnit;
    }

    public void setTestLengthUnit(LengthUnit testLengthUnit) {
        this.testLengthUnit = testLengthUnit;
    }

    public String getRecordResult() {
        return recordResult;
    }

    public void setRecordResult(String recordResult) {
        this.recordResult = recordResult;
    }
}
