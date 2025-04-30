package com.ose.docs.domain.model.repository;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.aggregations.ValueCountAggregate;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.ose.docs.dto.FileCriteriaDTO;
import com.ose.docs.entity.FileBaseES;
import com.ose.docs.entity.FileBasicViewES;
import com.ose.docs.entity.FileViewES;
import com.ose.docs.vo.FileCategory;
import com.ose.docs.vo.FileESType;
import com.ose.util.CollectionUtils;
import com.ose.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 文件信息数据仓库。
 */
@Component
public class FileESRepository {

    // 索引名称
    private static final String INDEX_NAME = "ose";

    // 所有类型名称数组
    private static final String[] ALL_TYPES;

    static {

        Set<String> fileESTypeNames = new HashSet<>();

        for (FileESType fileESType : FileESType.values()) {
            fileESTypeNames.add(fileESType.getName());
        }

        ALL_TYPES = fileESTypeNames.toArray(new String[0]);
    }

    // 查询结果分页逻辑
//    private static final SearchResultMapper SEARCH_RESULT_MAPPER = new FileBasicESResultMapper();

    // Elasticsearch 模板
    private final ElasticsearchClient elasticsearchClient;
//    private final TransportClient client;


    /**
     * 构造方法。
     */
    @Autowired
    public FileESRepository(
        ElasticsearchClient elasticsearchClient) {
        this.elasticsearchClient = elasticsearchClient;
    }

    /**
     * 查询文件。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param criteria  查询条件
     * @param pageable  分页参数
     * @return 文件分页数据
     */
    public Page<FileBasicViewES> search(
        String orgId,
        String projectId,
        FileCriteriaDTO criteria,
        Pageable pageable
    ) {
        return search(orgId, projectId, criteria, pageable, FileBasicViewES.class);
    }



    /**
     * 文档数量统计
     * @param index  索引
     * @param field  文档属性
     * @return
     */
    public double valueCount(String index, String field) throws Exception {
        // 构建 Value Count 聚合
        String aggName = "valueCount_" + field;
        try {
            // 使用 ValueCount 来统计字段的数量
            SearchRequest searchRequest = SearchRequest.of(s -> s
                .index(index)
                .aggregations(aggName,
                    a -> a.valueCount(v -> v.field(field)) // 使用 valueCount 聚合
                )
            );

            // 执行查询
            SearchResponse<Void> response = elasticsearchClient.search(searchRequest, Void.class);

            // 获取聚合结果
            ValueCountAggregate count = response.aggregations()
                .get(aggName)
                .valueCount(); // 这里返回的是 ValueCountAggregate 对象

            // 返回聚合结果的值
            return count.value();
        } catch (IOException e) {
            e.printStackTrace();  // 处理异常，或记录日志
            return 0;  // 如果出现异常，可以返回默认值或其他处理方式
        }
    }


    /**
     * 查询文件。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param criteria  查询条件
     * @param pageable  分页参数
     * @param clazz     返回数据类型
     * @return 文件分页数据
     */
    public <T> Page<T> search(
        String orgId,
        String projectId,
        FileCriteriaDTO criteria,
        Pageable pageable,
        Class<T> clazz
    ) {


        // 构建 Bool Query
        BoolQuery.Builder boolQueryBuilder = new BoolQuery.Builder();

        if (!StringUtils.isEmpty(criteria.getId())) {
            boolQueryBuilder.must(QueryBuilders.term(t -> t.field("id").value(criteria.getId())));
        }

        if (!StringUtils.isEmpty(orgId)) {
            boolQueryBuilder.must(QueryBuilders.term(t -> t.field("orgId").value(orgId)));
        }

        if (!StringUtils.isEmpty(projectId)) {
            boolQueryBuilder.must(QueryBuilders.term(t -> t.field("projectId").value(projectId)));
        }

        if (!StringUtils.isEmpty(criteria.getCommittedBy())) {
            boolQueryBuilder.must(QueryBuilders.term(t -> t.field("committedBy").value(criteria.getCommittedBy())));
        }

        if (!StringUtils.isEmpty(criteria.getContent())) {
            boolQueryBuilder.must(QueryBuilders.match(m -> m.field("content").query(criteria.getContent())));
        }

        if (!CollectionUtils.isEmpty(criteria.getEntitySubType())) {
            for (FileCategory category : criteria.getEntitySubType()) {
                boolQueryBuilder.must(QueryBuilders.term(t -> t.field("categories").value(category.name())));
            }
        }

        if (!CollectionUtils.isEmpty(criteria.getKeyword())) {
            for (String keyword : criteria.getKeyword()) {
                boolQueryBuilder.must(QueryBuilders.term(t -> t.field("keywords").value(keyword)));
            }
        }

        if (!CollectionUtils.isEmpty(criteria.getTag())) {
            for (String tag : criteria.getTag()) {
                boolQueryBuilder.must(QueryBuilders.term(t -> t.field("tags").value(tag)));
            }
        }

        // 设置类型查询条件
        String[] types = ALL_TYPES;
        if (!CollectionUtils.isEmpty(criteria.getBizType())) {
            Set<String> fileESTypeNames = criteria.getBizType().stream()
                .map(FileESType::getName)
                .collect(Collectors.toSet());
            types = fileESTypeNames.toArray(new String[0]);
        }

        try {
            // 构建 SearchRequest
            SearchRequest.Builder searchRequestBuilder = new SearchRequest.Builder()
                .index(INDEX_NAME)
                .query(q -> q.bool(boolQueryBuilder.build()))
                .from((int) pageable.getOffset())
                .size(pageable.getPageSize());



            // 执行查询
            SearchResponse<T> searchResponse = elasticsearchClient.search(
                searchRequestBuilder.build(), clazz
            );

            // 获取查询结果
            List<T> content = searchResponse.hits().hits().stream()
                .map(Hit::source)
                .collect(Collectors.toList());

            // 构建 Page 对象
            return new PageImpl<>(content, pageable, searchResponse.hits().total().value());

        } catch (IOException e) {
            e.printStackTrace();  // 处理异常，或记录日志
            return null;  // 如果出现异常，可以返回默认值或其他处理方式
        }
    }

    /**
     * 取得文件信息。
     *
     * @param <T>   文件数据实体范型
     * @param id    文件 ID
     * @param orgId 组织 ID
     * @param clazz 文件数据实体类型
     * @return 文件数据实体
     */
    private <T extends FileBaseES> Optional<T> getFileInfo(
        String id, String orgId, Class<T> clazz
    ) {

        FileCriteriaDTO criteria = new FileCriteriaDTO();
        criteria.setId(id);

        Page<T> page = search(orgId, null, criteria, PageRequest.of(0, 1), clazz);

        if (page.getTotalElements() == 0) {
            return Optional.empty();
        }

        return Optional.of(page.getContent().get(0));

    }

    /**
     * 取得项目文件信息。
     *
     * @param <T>   文件数据实体范型
     * @param id    文件 ID
     * @param orgId 组织 ID
     * @param projectId 项目 ID
     * @param clazz 文件数据实体类型
     * @return 文件数据实体
     */
    private <T extends FileBaseES> Optional<T> getFileInfo(
        String id, String orgId, String projectId, Class<T> clazz
    ) {

        FileCriteriaDTO criteria = new FileCriteriaDTO();
        criteria.setId(id);

        Page<T> page = search(orgId, projectId, criteria, PageRequest.of(0, 1), clazz);

        if (page.getTotalElements() == 0) {
            return Optional.empty();
        }

        return Optional.of(page.getContent().get(0));

    }

    /**
     * 取得文件详细信息。
     *
     * @param id    文件 ID
     * @param orgId 组织 ID
     * @return 文件详细信息
     */
    public Optional<FileViewES> getFileInfo(String id, String orgId) {
        return getFileInfo(id, orgId, FileViewES.class);
    }

    /**
     * 取得文件路径信息。
     *
     * @param id    文件 ID
     * @param orgId 组织 ID
     * @return 文件路径信息
     */
    public Optional<FileBaseES> getFilePaths(String id, String orgId) {
        return getFileInfo(id, orgId, FileBaseES.class);
    }

    /**
     * 取得项目文件路径信息。
     *
     * @param id    文件 ID
     * @param orgId 组织 ID
     * @param projectId 项目 ID
     * @return 文件路径信息
     */
    public Optional<FileBaseES> getFilePaths(String id, String orgId, String projectId) {
        return getFileInfo(id, orgId, projectId, FileBaseES.class);
    }

}
