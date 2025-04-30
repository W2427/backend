package com.ose.dto;



/**
 * AnnotationRawLine。
 */
public class AnnotationDTO extends BaseDTO {

    private static final long serialVersionUID = -2482183563937166466L;

    private String base64;

    private Integer pageTotal;

    private Integer currentPage;

    public String getBase64() {
        return base64;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }

    public Integer getPageTotal() {
        return pageTotal;
    }

    public void setPageTotal(Integer pageTotal) {
        this.pageTotal = pageTotal;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }
}
