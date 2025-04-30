package com.ose.material.dto;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * SEQ Number 数据传输对象。
 */
public class SeqNumberDTO extends BaseDTO {

    private static final long serialVersionUID = -6073064285417513778L;

    @Schema(description = "流水号")
    private Integer seqNumber;

    public SeqNumberDTO(Integer seqNumber) {
        this.seqNumber = seqNumber;
    }

    public Integer getSeqNumber() {
        return seqNumber;
    }

    public void setSeqNumber(Integer seqNumber) {
        this.seqNumber = seqNumber;
    }

    public Integer getNewSeqNumber() {
        return seqNumber == null ? 1 : seqNumber + 1;
    }

}
