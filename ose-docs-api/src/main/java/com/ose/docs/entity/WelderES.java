package com.ose.docs.entity;

import com.ose.docs.constant.FileESTypeNames;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * Welder导出 ES 数据实体。
 */
@Document(indexName = "ose")
public class WelderES extends FileDetailES {

    private static final long serialVersionUID = -6549354996611603691L;
}
