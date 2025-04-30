package com.ose.docs.entity;

import com.ose.docs.constant.FileESTypeNames;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "ose")
public class WpsES extends FileDetailES {
    private static final long serialVersionUID = -3985516480823430029L;
}
