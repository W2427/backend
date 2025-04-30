package com.ose.docs.entity;

import com.ose.docs.constant.FileESTypeNames;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * 结构套料导入文件 ES 数据实体。
 */
@Document(indexName = "ose")
public class MaterialStructureES extends FileDetailES {

    private static final long serialVersionUID = -6215651187851795462L;
}
