package com.ose.report.dto.report;

import com.ose.report.dto.subreport.DynamicColumnSubReport;
import com.ose.report.dto.subreport.TemplateSubReport;

/**
 * 制作报表内容（Checklist Pattern）
 */
public class WeeklyReportDTO extends BaseReportDTO {

    /**
     * Header区内容
     */
    private TemplateSubReport header;

    /**
     * 详细区内容（Column）
     */
    private DynamicColumnSubReport[] detail;

    /**
     * Footer区内容
     */
    private TemplateSubReport footer;

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
}
