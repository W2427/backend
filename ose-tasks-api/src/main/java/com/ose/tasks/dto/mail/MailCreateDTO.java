package com.ose.tasks.dto.mail;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Column;

public class MailCreateDTO extends BaseDTO {

    private static final long serialVersionUID = 122715744433292511L;
    @Schema(description = "标题")
    @Column
    private String email;
    @Schema(description = "标题")
    @Column
    private String title;

    @Schema(description = "内容")
    @Column
    private String content;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
