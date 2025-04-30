package com.ose.docs.entity.project;

import com.ose.docs.constant.FileESTypeNames;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * 项目模块流程定义文件 ES 数据实体。
 */
@Document(indexName = "ose")
public class ProjectModuleProcessDefinitionES extends ProjectBaseES {

    private static final long serialVersionUID = 6200493570147286918L;

}
