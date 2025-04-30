package com.ose.report.dto.report;

import com.ose.report.dto.subreport.DynamicColumnSubReport;
import com.ose.report.dto.subreport.DynamicImageSubReport;
import com.ose.report.dto.subreport.TemplateSubReport;

/**
 * 制作报表内容（Checklist Pattern）
 */
public class CommonCheckReportDTO extends BaseReportDTO {


    private String templateDomain;

    // 横板PDF
    private boolean isA4Landscape = false;
    /**
     * Header区内容
     */
    private TemplateSubReport header;

    /**
     * Title区内容
     */
    private TemplateSubReport title;

    /**
     * 详细区内容（Column）
     */
    private DynamicColumnSubReport[] detail;

    /**
     * 附件区（Image）
     */
    private DynamicImageSubReport[] attachment;

    /**
     * 总结区内容
     */
    private TemplateSubReport summary;

    /**
     * Footer区内容
     */
    private TemplateSubReport pageFooter;

    /**
     * Footer区内容
     */
    private TemplateSubReport signature;


    /**
     * Gets the value of header.
     *
     * @return the value of header
     */
    public TemplateSubReport getHeader() {
        return header;
    }

    /**
     * Sets the header.
     *
     * <p>You can use getHeader() to get the value of header</p>
     *
     * @param header header
     */
    public void setHeader(TemplateSubReport header) {
        this.header = header;
    }

    public TemplateSubReport getTitle() {
        return title;
    }

    public void setTitle(TemplateSubReport title) {
        this.title = title;
    }

    /**
     * Gets the value of detail.
     *
     * @return the value of detail
     */
    public DynamicColumnSubReport[] getDetail() {
        return detail;
    }

    /**
     * Sets the detail.
     *
     * <p>You can use getDetail() to get the value of detail</p>
     *
     * @param detail detail
     */
    public void setDetail(DynamicColumnSubReport[] detail) {
        this.detail = detail;
    }

    /**
     * Gets the value of attachment.
     *
     * @return the value of attachment
     */
    public DynamicImageSubReport[] getAttachment() {
        return attachment;
    }

    /**
     * Sets the attachment.
     *
     * <p>You can use getAttachment() to get the value of attachment</p>
     *
     * @param attachment attachment
     */
    public void setAttachment(DynamicImageSubReport[] attachment) {
        this.attachment = attachment;
    }

    /**
     * Gets the value of summary.
     *
     * @return the value of summary
     */
    public TemplateSubReport getSummary() {
        return summary;
    }

    /**
     * Sets the summary.
     *
     * <p>You can use getSummary() to get the value of summary</p>
     *
     * @param summary summary
     */
    public void setSummary(TemplateSubReport summary) {
        this.summary = summary;
    }

    /**
     * Gets the value of footer.
     *
     * @return the value of footer
     */
    public TemplateSubReport getPageFooter() {
        return pageFooter;
    }

    /**
     * Sets the footer.
     *
     * <p>You can use getFooter() to get the value of footer</p>
     *
     * @param footer footer
     */
    public void setPageFooter(TemplateSubReport pageFooter) {
        this.pageFooter = pageFooter;
    }

    public TemplateSubReport getSignature() {
        return signature;
    }

    public void setSignature(TemplateSubReport signature) {
        this.signature = signature;
    }

    public String getTemplateDomain() {
        return templateDomain;
    }

    public void setTemplateDomain(String templateDomain) {
        this.templateDomain = templateDomain;
    }

    public boolean isA4Landscape() {
        return isA4Landscape;
    }

    public void setA4Landscape(boolean isA4Landscape) {
        this.isA4Landscape = isA4Landscape;
    }

}
