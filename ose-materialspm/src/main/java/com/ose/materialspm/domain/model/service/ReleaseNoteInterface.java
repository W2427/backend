package com.ose.materialspm.domain.model.service;

import com.ose.dto.PageDTO;
import com.ose.materialspm.dto.ReleaseNoteListDTO;
import com.ose.materialspm.entity.ReleaseNote;
import com.ose.materialspm.entity.ReleaseNoteHead;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * service接口
 */
public interface ReleaseNoteInterface {

    Page<ReleaseNoteHead> getReleaseNoteHeadList(String spmProjectId, ReleaseNoteListDTO releaseNoteListDTO);

    ReleaseNoteHead getReleaseNote(String spmProjectId, String relnNumber);

    Page<ReleaseNote> getReleaseNoteItemsByPage(String spmProjectId, String relnNumber, PageDTO pageDTO);

    List<ReleaseNote> getReleaseNoteItems(String spmProjectId, String relnNumber);
}
