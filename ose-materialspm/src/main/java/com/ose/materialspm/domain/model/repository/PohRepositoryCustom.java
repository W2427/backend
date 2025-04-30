package com.ose.materialspm.domain.model.repository;

import com.ose.materialspm.dto.PohDTO;
import com.ose.materialspm.entity.PoDetail;
import com.ose.materialspm.entity.ViewMxjValidPohEntity;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PohRepositoryCustom {

    Page<ViewMxjValidPohEntity> getPohs(PohDTO pohDTO);

    Page<PoDetail> getDetail(PohDTO pohDTO);

    List<PoDetail> getDetailNoPage(PohDTO pohDTO);
}
