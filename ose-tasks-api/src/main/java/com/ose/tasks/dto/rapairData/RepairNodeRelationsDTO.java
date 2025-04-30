package com.ose.tasks.dto.rapairData;

import com.ose.dto.BaseDTO;

import java.util.List;

/**
 * 修改项目数据接口。
 */
public class RepairNodeRelationsDTO extends BaseDTO {

    private static final long serialVersionUID = -4771861571004723103L;

    private List<Long> wp02Ids ;

    public List<Long> getWp02Ids() {
        return wp02Ids;
    }

    public void setWp02Ids(List<Long> wp02Ids) {
        this.wp02Ids = wp02Ids;
    }
}
