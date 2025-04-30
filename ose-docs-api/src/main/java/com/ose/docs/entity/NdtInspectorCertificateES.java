package com.ose.docs.entity;

import com.ose.docs.constant.FileESTypeNames;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "ose")
public class NdtInspectorCertificateES extends FileDetailES {
    private static final long serialVersionUID = -5304395025374031735L;
}
