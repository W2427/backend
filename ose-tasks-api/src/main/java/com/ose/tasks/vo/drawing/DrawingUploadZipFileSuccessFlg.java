package com.ose.tasks.vo.drawing;

/**
 * 上传成功标识。
 */
public enum DrawingUploadZipFileSuccessFlg {

    SUCCESS("成功"),
    FAILED("失败");

    private String displayName;

    DrawingUploadZipFileSuccessFlg(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
