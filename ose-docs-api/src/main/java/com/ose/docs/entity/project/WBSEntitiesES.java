package com.ose.docs.entity.project;

import com.ose.docs.constant.FileESTypeNames;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * WBS 实体导入文件 ES 数据实体。
 */
@Document(indexName = "ose")
public class WBSEntitiesES extends ProjectBaseES {

    private static final long serialVersionUID = -3585271152347447460L;

}
