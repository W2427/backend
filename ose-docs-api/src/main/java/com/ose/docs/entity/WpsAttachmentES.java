package com.ose.docs.entity;

import com.ose.docs.constant.FileESTypeNames;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "ose")
public class WpsAttachmentES extends FileDetailES {
    private static final long serialVersionUID = 898189544031036356L;
}
