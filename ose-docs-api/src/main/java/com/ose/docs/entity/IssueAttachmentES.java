package com.ose.docs.entity;

import com.ose.docs.constant.FileESTypeNames;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * 遗留问题附件 ES 数据实体。
 */
@Document(indexName = "ose")
public class IssueAttachmentES extends FileDetailES {

    private static final long serialVersionUID = -772390697494598548L;

}
