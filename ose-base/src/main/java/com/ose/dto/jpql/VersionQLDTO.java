package com.ose.dto.jpql;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 数据实体版本号 JPQL 数据传输对象。
 */
public class VersionQLDTO extends BaseDTO {

    private static final long serialVersionUID = -6073064285417513778L;

    @Schema(description = "数据实体版本号")
    private long version;

    public VersionQLDTO(long version) {
        this.version = version;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

}
