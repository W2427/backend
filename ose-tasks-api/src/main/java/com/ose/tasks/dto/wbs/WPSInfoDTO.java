package com.ose.tasks.dto.wbs;


import com.ose.dto.BaseDTO;

/**
 * WPS 信息
 */
public class WPSInfoDTO extends BaseDTO {

    private static final long serialVersionUID = 6280471215905165430L;

    /**
     * WPS ID
     */
    private Long wpsId;

    /**
     * WPS NO
     */
    private String wpsNo;

    public Long getWpsId() {
        return wpsId;
    }

    public void setWpsId(Long wpsId) {
        this.wpsId = wpsId;
    }

    public String getWpsNo() {
        return wpsNo;
    }

    public void setWpsNo(String wpsNo) {
        this.wpsNo = wpsNo;
    }
}
