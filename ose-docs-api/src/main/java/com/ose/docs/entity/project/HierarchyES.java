package com.ose.docs.entity.project;

import com.ose.docs.constant.FileESTypeNames;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * 项目层级结构节点导入文件 ES 数据实体。
 */
@Document(indexName = "ose")
public class HierarchyES extends ProjectBaseES {

    private static final long serialVersionUID = 7352804057119862986L;

}
