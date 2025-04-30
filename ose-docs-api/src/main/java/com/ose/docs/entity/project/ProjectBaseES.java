package com.ose.docs.entity.project;

import com.ose.docs.entity.FileDetailES;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * 项目数据导入文件 ES 数据实体基类。
 */
public abstract class ProjectBaseES extends FileDetailES {

    private static final long serialVersionUID = 4786938473169026864L;

    @Schema(description = "项目 ID")
    @Field(type = FieldType.Keyword)
    private String projectId;

    @Schema(description = "项目层级结构节点 ID")
    @Field(type = FieldType.Keyword)
    private String nodeId;

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

}
