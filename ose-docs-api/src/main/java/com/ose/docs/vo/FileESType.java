package com.ose.docs.vo;

import com.ose.docs.constant.FileESTypeNames;

/**
 * 文件 Elasticsearch 类型名。
 */
public enum FileESType {

    PROJECT_MODULE_PROCESS_DEFINITION(FileESTypeNames.PROJECT_MODULE_PROCESS_DEFINITIONS),
    PROJECT_HIERARCHY(FileESTypeNames.PROJECT_HIERARCHIES),
    WBS_ENTITIES(FileESTypeNames.WBS_ENTITIES),
    WBS_ENTRIES(FileESTypeNames.WBS_ENTRIES),
    PROJECT_DOC(FileESTypeNames.PROJECT_DOCS),
    ISSUE_ATTACHMENT(FileESTypeNames.ISSUE_ATTACHMENTS),
    WELDERS(FileESTypeNames.WELDERS),
    WPS(FileESTypeNames.WPS),
    ISSUE_IMPORT_FILES(FileESTypeNames.ISSUE_IMPORT_FILES),
    EXPERIENCE_ATTACHMENT(FileESTypeNames.EXPERIENCE_ATTACHMENTS),
    WPS_ATTACHMENTS(FileESTypeNames.WPS_ATTACHMENTS),
    WPQR_ATTACHMENTS(FileESTypeNames.WPQR_ATTACHMENTS),
    SUBCON_ATTACHMENTS(FileESTypeNames.SUBCON_ATTACHMENTS),
    NDT_INSPECTOR_CERTIFICATE(FileESTypeNames.NDT_INSPECTOR_CERTIFICATE),
    RATED_TIME_IMPORT_FILE(FileESTypeNames.RATED_TIME_IMPORT_FILES),
    MATERIAL_STRUCTURE_IMPORT_FILES(FileESTypeNames.MATERIAL_STRUCTURE_IMPORT_FILES);

    private String name;

    FileESType(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

}
