package com.ose.docs.entity;

import com.ose.docs.constant.FileESTypeNames;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "ose")
public class WpqrAttachmentES extends FileDetailES {
    private static final long serialVersionUID = -3874107118885907725L;
}
