package com.ose.docs.entity;

import com.ose.docs.constant.FileESTypeNames;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "ose")
public class SubConAttachmentES extends FileDetailES {
    private static final long serialVersionUID = 769653277184966736L;
}
