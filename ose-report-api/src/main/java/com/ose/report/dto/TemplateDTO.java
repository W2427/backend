package com.ose.report.dto;

import com.ose.dto.BaseDTO;
import com.ose.report.vo.Domain;
import com.ose.report.vo.Position;

/**
 * 报表模板 数据传输对象
 */
public class TemplateDTO extends BaseDTO {

    private static final long serialVersionUID = 7834290249990374870L;

    // 模板名称
    private String name;

    // 模板分类（检查单，周报，......）
    private Domain domain;

    // 模板位置（Head，Title，Detail，......）
    private Position position;

    // 固定高度
    private int fixedHeight;

    // 模板文件
    private String templateFile;

    /**
     * 默认构造方法
     */
    public TemplateDTO() {
        super();
    }

    /**
     * 构造方法
     *
     * @param name         模板名称
     * @param domain       模板分类（检查单，周报，......）
     * @param position     模板位置（Head，Title，Detail，......）
     * @param fixedHeight  固定高度
     * @param templateFile 模板文件
     */
    public TemplateDTO(String name, Domain domain, Position position, int fixedHeight, String templateFile) {
        this.name = name;
        this.domain = domain;
        this.position = position;
        this.fixedHeight = fixedHeight;
        this.templateFile = templateFile;
    }

    /**
     * Gets the value of name.
     *
     * @return the value of name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     *
     * <p>You can use getName() to get the value of name</p>
     *
     * @param name name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the value of domain.
     *
     * @return the value of domain
     */
    public Domain getDomain() {
        return domain;
    }

    /**
     * Sets the domain.
     *
     * <p>You can use getDomain() to get the value of domain</p>
     *
     * @param domain domain
     */
    public void setDomain(Domain domain) {
        this.domain = domain;
    }

    /**
     * Gets the value of position.
     *
     * @return the value of position
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Sets the position.
     *
     * <p>You can use getPosition() to get the value of position</p>
     *
     * @param position position
     */
    public void setPosition(Position position) {
        this.position = position;
    }

    /**
     * Gets the value of fixedHeight.
     *
     * @return the value of fixedHeight
     */
    public int getFixedHeight() {
        return fixedHeight;
    }

    /**
     * Sets the fixedHeight.
     *
     * <p>You can use getFixedHeight() to get the value of fixedHeight</p>
     *
     * @param fixedHeight fixedHeight
     */
    public void setFixedHeight(int fixedHeight) {
        this.fixedHeight = fixedHeight;
    }

    /**
     * Gets the value of templateFile.
     *
     * @return the value of templateFile
     */
    public String getTemplateFile() {
        return templateFile;
    }

    /**
     * Sets the templateFile.
     *
     * <p>You can use getTemplateFile() to get the value of templateFile</p>
     *
     * @param templateFile templateFile
     */
    public void setTemplateFile(String templateFile) {
        this.templateFile = templateFile;
    }

    /**
     * 字符串转换
     *
     * @return 字符串
     */
    @Override
    public String toString() {
        return "TemplateDTO{" + "name='" + name + '\'' + ", domain=" + domain + ", position=" + position + ", templateFile='" + templateFile + '\'' + '}';
    }
}
