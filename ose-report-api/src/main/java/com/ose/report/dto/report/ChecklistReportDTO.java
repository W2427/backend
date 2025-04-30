package com.ose.report.dto.report;

import com.ose.report.dto.subreport.DynamicColumnSubReport;
import com.ose.report.dto.subreport.DynamicImageSubReport;
import com.ose.report.dto.subreport.TemplateSubReport;

/**
 * 制作报表内容（Checklist Pattern）
 */
public class ChecklistReportDTO extends BaseReportDTO {

    /**
     * Header区内容
     */
    private TemplateSubReport header;

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
    private TemplateSubReport footer;

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
    public TemplateSubReport getFooter() {
        return footer;
    }

    /**
     * Sets the footer.
     *
     * <p>You can use getFooter() to get the value of footer</p>
     *
     * @param footer footer
     */
    public void setFooter(TemplateSubReport footer) {
        this.footer = footer;
    }
}
