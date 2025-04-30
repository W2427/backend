package com.ose.docs.entity.project;

import org.springframework.data.elasticsearch.annotations.Document;

/**
 * 项目计划入文件 ES 数据实体。
 */
@Document(indexName = "ose")
public class PlanES extends ProjectBaseES {

    private static final long serialVersionUID = 8896405931431950410L;

}
