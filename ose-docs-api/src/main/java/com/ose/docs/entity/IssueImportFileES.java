package com.ose.docs.entity;

import com.ose.docs.constant.FileESTypeNames;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "ose")
public class IssueImportFileES extends FileDetailES {

    private static final long serialVersionUID = -793076623535777606L;
}
