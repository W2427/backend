package com.ose.tasks.dto.bpm;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;


/**
 * 实体数据传输对象
 */
public class TaskNodePrivilegeDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 7615182766136985152L;

    @Schema(description = "权限")
    private String category;

    @Schema(description = "权限名")
    private String categoryName;

    @Schema(description = "指定用户id")
    private Long userId;

    @Schema(description = "指定用户姓名")
    private String userName;


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
