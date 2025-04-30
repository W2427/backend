package com.ose.report.dto.report;

/**
 * 制作报表内容（基础信息）
 */
public class BaseReportDTO {

    /**
     * 编号/序号
     */
    private String serial;

    /**
     * 报表输出类型
     */
    private String outType;

    /**
     * 是否需要存储
     */
    private boolean needStore;

    /**
     * Gets the value of needStore.
     *
     * @return the value of needStore
     */
    public boolean isNeedStore() {
        return needStore;
    }

    /**
     * Sets the needStore.
     *
     * <p>You can use isNeedStore() to get the value of needStore</p>
     *
     * @param needStore outType
     */
    public void setNeedStore(boolean needStore) {
        this.needStore = needStore;
    }

    /**
     * Gets the value of outType.
     *
     * @return the value of outType
     */
    public String getOutType() {
        return outType;
    }

    /**
     * Sets the outType.
     *
     * <p>You can use getOutType() to get the value of outType</p>
     *
     * @param outType outType
     */
    public void setOutType(String outType) {
        this.outType = outType;
    }

    /**
     * Gets the value of serial.
     *
     * @return the value of serial
     */
    public String getSerial() {
        return serial;
    }

    /**
     * Sets the serial.
     *
     * <p>You can use getSerial() to get the value of serial</p>
     *
     * @param serial serial
     */
    public void setSerial(String serial) {
        this.serial = serial;
    }
}
