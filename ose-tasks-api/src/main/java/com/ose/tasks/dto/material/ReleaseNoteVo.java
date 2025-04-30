package com.ose.tasks.dto.material;

import java.util.List;

import com.ose.dto.BaseDTO;
import com.ose.tasks.entity.material.ReleaseNoteEntity;
import com.ose.tasks.entity.material.ReleaseNoteItemEntity;

/**
 * Demo查询DTO
 */
public class ReleaseNoteVo extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 7548521809152380829L;

    private ReleaseNoteEntity releaseNote;

    private List<ReleaseNoteItemEntity> releaseNoteItems;

    public ReleaseNoteEntity getReleaseNote() {
        return releaseNote;
    }

    public void setReleaseNote(ReleaseNoteEntity releaseNote) {
        this.releaseNote = releaseNote;
    }

    public List<ReleaseNoteItemEntity> getReleaseNoteItems() {
        return releaseNoteItems;
    }

    public void setReleaseNoteItems(List<ReleaseNoteItemEntity> releaseNoteItems) {
        this.releaseNoteItems = releaseNoteItems;
    }

}
