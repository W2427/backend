package com.ose.report.domain.repository;

import com.ose.report.entity.Template;
import com.ose.report.vo.Domain;
import com.ose.report.vo.Position;
import com.ose.repository.PagingAndSortingWithCrudRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 报表模板 CRUD 操作接口。
 */
@Transactional
public interface ReportTemplateRepository extends PagingAndSortingWithCrudRepository<Template, Long> {

    /**
     * 检查报表模板名是否已使用。
     *
     * @param templateName 报表模板名
     * @return 报表模板名是否已使用
     */
    Boolean existsByName(String templateName);

    /**
     * 根据分类和显示位置查询模板
     *
     * @param domain   报表分类
     * @param position 报表显示位置
     * @param pageable 分页信息
     * @return 报表模板
     */
    Page<Template> findByDomainAndPosition(Domain domain, Position position, Pageable pageable);

    /**
     * 根据模板ID数组查询对应的模板
     *
     * @param ids 报表模板ID数组
     * @return 报表模板
     */
    List<Template> findByIdIn(Long[] ids);
}
