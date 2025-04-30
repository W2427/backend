package com.ose.materialspm.domain.model.service;

import com.ose.docs.api.UploadFeignAPI;
import com.ose.materialspm.dto.ExportFileDTO;
import com.ose.materialspm.dto.PohDTO;
import com.ose.materialspm.entity.PoDetail;
import com.ose.materialspm.entity.ViewMxjValidPohEntity;
import org.springframework.data.domain.Page;

/**
 * Demoservice接口
 */
public interface PoInterface {

    Page<ViewMxjValidPohEntity> getPohs(PohDTO pohDTO);

    Page<PoDetail> getDetail(PohDTO pohDTO);

    ViewMxjValidPohEntity getPoh(PohDTO pohDTO);

    ExportFileDTO exportPoh(Long orgId, Long projectId, PohDTO pohDTO, UploadFeignAPI uploadFeignAPI);

}
