package com.ose.materialspm.domain.model.service.impl;

import com.ose.dto.PageDTO;
import com.ose.materialspm.domain.model.repository.ReleaseNoteRepository;
import com.ose.materialspm.domain.model.service.ReleaseNoteInterface;
import com.ose.materialspm.dto.ReleaseNoteListDTO;
import com.ose.materialspm.entity.ReleaseNote;
import com.ose.materialspm.entity.ReleaseNoteHead;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ReleaseNoteService implements ReleaseNoteInterface {


    /**
     * 放行单  操作仓库
     */
    private final ReleaseNoteRepository releaseNoteRepository;

    /**
     * 构造方法
     */
    @Autowired
    public ReleaseNoteService(ReleaseNoteRepository releaseNoteRepository) {
        this.releaseNoteRepository = releaseNoteRepository;
    }

    @Override
    public Page<ReleaseNoteHead> getReleaseNoteHeadList(String spmProjectId, ReleaseNoteListDTO releaseNoteListDTO) {
        return releaseNoteRepository.getReleaseNoteHeadList(spmProjectId, releaseNoteListDTO);
    }

    @Override
    public ReleaseNoteHead getReleaseNote(String spmProjectId, String relnNumber) {
        return releaseNoteRepository.getReleaseNoteHead(spmProjectId, relnNumber);
    }

    @Override
    public Page<ReleaseNote> getReleaseNoteItemsByPage(String spmProjectId, String relnNumber, PageDTO pageDTO) {
        return releaseNoteRepository.getReleaseNoteItemsByPage(spmProjectId, relnNumber, pageDTO);
    }

    @Override
    public List<ReleaseNote> getReleaseNoteItems(String spmProjectId, String relnNumber) {
        return releaseNoteRepository.getReleaseNoteItems(spmProjectId, relnNumber);
    }
}
