package com.ose.materialspm.dto;

import com.ose.dto.BaseDTO;

/**
 * 入库清单DTO
 */
public class ReceiveReceiptResultDTO extends BaseDTO {

    public ReceiveReceiptResultDTO() {
        super();
        // TODO Auto-generated constructor stub
    }

    public ReceiveReceiptResultDTO(String result, String message) {
        super();
        this.result = result;
        this.message = message;
    }

    /**
     *
     */
    private static final long serialVersionUID = 7548521809152380829L;

    private String result;

    private String message;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
