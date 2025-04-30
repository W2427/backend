package com.ose.report.dto;

import com.ose.dto.BaseDTO;

/**
 * 报表模拟/测试 数据传输对象
 */
public class ChecklistSimulationDTO extends BaseDTO {

    private static final long serialVersionUID = 1297759946681525090L;

    // 模拟名称
    private String name;

    // 检查单ID
    private Long checklistId;

    // 检查单信息 数据传输对象
    private ChecklistInfoDTO simulationData;

    /**
     * 默认构造方法
     */
    public ChecklistSimulationDTO() {
    }

    /**
     * 构造方法
     *
     * @param name           模拟名称
     * @param checklistId    检查单ID
     * @param simulationData 检查单信息 数据传输对象
     */
    public ChecklistSimulationDTO(String name, Long checklistId,
                                  ChecklistInfoDTO simulationData) {

        this.name = name;
        this.checklistId = checklistId;
        this.simulationData = simulationData;
    }

    /**
     * Gets the value of name.
     *
     * @return the value of name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     *
     * <p>You can use getName() to get the value of name</p>
     *
     * @param name name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the value of checklistId.
     *
     * @return the value of checklistId
     */
    public Long getChecklistId() {
        return checklistId;
    }

    /**
     * Sets the checklistId.
     *
     * <p>You can use getChecklistId() to get the value of checklistId</p>
     *
     * @param checklistId checklistId
     */
    public void setChecklistId(Long checklistId) {
        this.checklistId = checklistId;
    }


    /**
     * Gets the value of simulationData.
     *
     * @return the value of simulationData
     */
    public ChecklistInfoDTO getSimulationData() {
        return simulationData;
    }

    /**
     * Sets the simulationData.
     *
     * <p>You can use getSimulationData() to get the value of simulationData</p>
     *
     * @param simulationData simulationData
     */
    public void setSimulationData(ChecklistInfoDTO simulationData) {
        this.simulationData = simulationData;
    }
}
