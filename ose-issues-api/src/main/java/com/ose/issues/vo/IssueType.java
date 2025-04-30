package com.ose.issues.vo;

/**
 * 问题类型。
 */
public enum IssueType {

    ISSUE("import-issues.xlsx"),
    EXPERIENCE("import-experiences.xlsx");

    private String importFileName;

    IssueType(String importFileName) {
        this.importFileName = importFileName;
    }

    public String getImportFileName() {
        return importFileName;
    }

}
