package com.ose.tasks.vo.qc;

import com.ose.vo.ValueObject;

public enum SpotCheckCategory implements ValueObject {

    NDT("无损探伤");

    private String displayName;

    SpotCheckCategory(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String getDisplayName() {
        return this.displayName;
    }
}
