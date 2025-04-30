package com.ose.tasks.util;

import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotation;

import java.util.List;

public class PageAnnoInfo {

    private PageInfo pageInfo;

    private List<PDAnnotation> annotations;

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    public List<PDAnnotation> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(List<PDAnnotation> annotations) {
        this.annotations = annotations;
    }

}
