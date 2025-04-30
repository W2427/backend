package com.ose.tasks.dto.bpm;

import com.ose.dto.PageDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * AnnotationRawLine。
 */
public class PdfAnnotationSearchDTO extends PageDTO {

    private static final long serialVersionUID = -345769494236348390L;

    @Schema(description = "搜索关键字")
    public String keyword;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
