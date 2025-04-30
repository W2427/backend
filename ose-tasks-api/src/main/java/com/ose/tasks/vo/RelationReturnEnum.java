package com.ose.tasks.vo;

public enum RelationReturnEnum {

    RELATION_EXIST("relation exist"),
    RELATION_NOT_EXIST("relation not exist"),
    ENTITY_CATEGORY_NOT_FOUND("entity category not found"),
    TASK_PACKAGE_NOT_FOUND("work package not found"),
    PROCESS_NOT_FOUND("process not found"),
    COORDINATE_NOT_FOUND("coordinate not found"),
    SAVE_SUCCESS("save success"),
    DELETE_SUCCESS("delete success");


    private String message;

    private RelationReturnEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
