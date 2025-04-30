package com.ose.materialspm.domain.model.repository;

import com.ose.dto.PageDTO;
import com.ose.materialspm.dto.ReleaseNoteListDTO;
import com.ose.materialspm.entity.ReleaseNote;
import com.ose.materialspm.entity.ReleaseNoteHead;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ReleaseNoteRepositoryCustom {

    Page<ReleaseNoteHead> getReleaseNoteHeadList(String spmProjectId, ReleaseNoteListDTO releaseNoteListDTO);

    ReleaseNoteHead getReleaseNoteHead(String spmProjId, String relnNumber);

    Page<ReleaseNote> getReleaseNoteItemsByPage(String spmProjId, String relnNumber, PageDTO pageDTO);

    List<ReleaseNote> getReleaseNoteItems(String spmProjId, String relnNumber);
}
