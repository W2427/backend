package com.ose.docs.entity;

import com.ose.docs.constant.FileESTypeNames;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * 额定工时导入文件 ES 数据实体。
 */
@Document(indexName = "ose")
public class RatedTimeES extends FileDetailES {
    private static final long serialVersionUID = -828101963253970431L;
}
