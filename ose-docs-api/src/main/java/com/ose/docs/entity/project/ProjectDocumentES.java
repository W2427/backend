package com.ose.docs.entity.project;

import com.ose.docs.constant.FileESTypeNames;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * 项目文档 ES 数据实体。
 */
@Document(indexName = "ose")
public class ProjectDocumentES extends ProjectBaseES {

    private static final long serialVersionUID = 2987053429561621750L;

}
