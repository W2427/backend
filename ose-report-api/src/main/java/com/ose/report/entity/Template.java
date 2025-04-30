package com.ose.report.entity;

import com.ose.entity.BaseEntity;
import com.ose.report.vo.Domain;
import com.ose.report.vo.Position;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

/**
 * 报表模板实体类。
 */
@Entity
@Table(name = "report_templates")
public class Template extends BaseEntity {

    private static final long serialVersionUID = -5705592037581554410L;

    // 模板名称
    @Column(nullable = false, length = 128)
    @NotNull(message = "report template's name is required")
    private String name;

    // 模板分类（检查单，周报，......）
    @Column(nullable = false, length = 16)
    @NotNull(message = "report template's domain is required")
    @Enumerated(EnumType.STRING)
    private Domain domain;

    // 模板位置（Head，Title，Detail，......）
    @Column(nullable = false, length = 16)
    @NotNull(message = "report template's position is required")
    @Enumerated(EnumType.STRING)
    private Position position;

    // 固定高度
    @Column(length = 4)
    private int fixedHeight;

    // 模板文件
    @Column(length = 128)
    private String templateFile;

    /**
     * 默认构造方法
     */
    public Template() {
        super();
    }

    /**
     * 构造方法
     *
     * @param id 模板ID
     */
    public Template(Long id) {
        super(id);
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
}
