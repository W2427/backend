package com.ose.docs.entity;

import com.ose.docs.constant.FileESTypeNames;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "ose")
public class MaterialCodeAliasGroupES extends FileDetailES {
    private static final long serialVersionUID = 3725564521638018017L;
}
