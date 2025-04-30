package com.ose.issues.dto;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

public class IssueCommentUpdateDTO extends BaseDTO {

    private static final long serialVersionUID = 2702071659081100059L;

    @Schema(description = "评论")
    private String comment;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
