package com.ose.exception;

/**
 * SharePoint连接参数为空异常
 * @author: DaiZeFeng
 * @date: 2022/12/19
 */
public class SharePointConnectionParamException extends Exception{

    private static final long serialVersionUID = 1134294408143425339L;

    private final String message;

    public SharePointConnectionParamException(String message) {
        super(message);
        this.message = message;
    }
}
