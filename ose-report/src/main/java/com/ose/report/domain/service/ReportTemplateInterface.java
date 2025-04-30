package com.ose.report.domain.service;

import com.ose.dto.PageDTO;
import com.ose.report.dto.TemplateDTO;
import com.ose.report.entity.Template;
import com.ose.report.vo.Domain;
import com.ose.report.vo.Position;
import com.ose.service.EntityInterface;
import org.springframework.data.domain.Page;

/**
 * 报表模板接口
 */
public interface ReportTemplateInterface extends EntityInterface {

    /**
     * 检查报表模板名是否可用。
     *
     * @param templateName 报表模板名
     * @return 报表模板名是否可用
     */
    Boolean isNameAvailable(String templateName);

    /**
     * 创建报表模板
     *
     * @param templateDTO 模板数据
     */
    Template create(TemplateDTO templateDTO);

    /**
     * 查询报表模板信息
     *
     * @param domain     模板分类（检查单，周报，......）
     * @param position   模板位置（Head，Title，Detail，......）
     * @param pagination 分页参数
     * @return 报表模板分页数据
     */
    Page<Template> search(Domain domain, Position position, PageDTO pagination);
}
