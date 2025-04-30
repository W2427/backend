package com.ose.materialspm.domain.model.service.impl;

import com.ose.materialspm.domain.model.repository.TransRepository;
import com.ose.materialspm.domain.model.repository.WRtiItemRepository;
import com.ose.materialspm.domain.model.repository.WRtiRepository;
import com.ose.materialspm.domain.model.service.ReturnReceiptInterface;
import com.ose.materialspm.dto.FMaterialReturnReceiptDTO;
import com.ose.materialspm.dto.FMaterialReturnReceiptDetailDTO;
import com.ose.materialspm.dto.ReceiveReceiptResultDTO;
import com.ose.materialspm.entity.WRtiEntity;
import com.ose.materialspm.entity.WRtiItemEntity;
import com.ose.util.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
public class ReturnReceiptService implements ReturnReceiptInterface {

    private final WRtiRepository wRtiRepository;

    private final WRtiItemRepository wRtiItemRepository;

    private final TransRepository transRepository;

    /**
     * 构造方法
     *
     * @param wRtiRepository
     * @param wRtiItemRepository
     * @param transRepository
     */
    @Autowired
    public ReturnReceiptService(
        WRtiRepository wRtiRepository,
        WRtiItemRepository wRtiItemRepository,
        TransRepository transRepository
    ) {
        this.wRtiRepository = wRtiRepository;
        this.wRtiItemRepository = wRtiItemRepository;
        this.transRepository = transRepository;
    }

    @Override
    @Transactional
    public ReceiveReceiptResultDTO saveRti(String projId, FMaterialReturnReceiptDTO returnReceiptDTO) {

        // main
        wRtiRepository.deleteByProjIdAndRtiNumber(projId, returnReceiptDTO.getRtiNumber());

        WRtiEntity entity = new WRtiEntity();
        entity = BeanUtils.copyProperties(returnReceiptDTO, entity);
//        entity.setRtiNumber(returnReceiptDTO.getRtiNumber());
//        entity.setEsiStatus(returnReceiptDTO.getEsiStatus());
        entity.setRevisionId(Integer.valueOf(returnReceiptDTO.getRevisionId()));
//        entity.setRtiCreateDate(returnReceiptDTO.getRtiCreateDate());
//        entity.setRtiPostDate(returnReceiptDTO.getRtiPostDate());
        entity.setPoplIsHByProc(returnReceiptDTO.getPoplIshByProc());
        entity.setProjId(projId);
//        entity.setShortDesc(returnReceiptDTO.getShortDesc());
//        entity.setDescription(returnReceiptDTO.getDescription());
        entity.setCompanyId(Integer.valueOf(returnReceiptDTO.getCompanyId()));
        wRtiRepository.save(entity);

        // detail
        wRtiItemRepository.deleteByProjIdAndRtiNumber(projId, returnReceiptDTO.getRtiNumber());

        List<FMaterialReturnReceiptDetailDTO> details = returnReceiptDTO.getDetails();
        if (details != null) {
            List<WRtiItemEntity> wRtiItemEntityList = new ArrayList<>();

            for (FMaterialReturnReceiptDetailDTO dto : details) {
                WRtiItemEntity item = new WRtiItemEntity();
//                BeanUtils.copyProperties(dto, item);
//                item.setIisId(Integer.valueOf(dto.getIisId()));
                item.setEsiStatus(dto.getEsiStatus());
                item.setIvprId(Integer.valueOf(dto.getIvprId()));
                item.setLocId(Integer.valueOf(dto.getLocId()));
                item.setWhId(Integer.valueOf(dto.getWhId()));
                item.setMirNumber(dto.getMirNumber());
                item.setProjId(projId);
                item.setReturnQty(dto.getReturnQty());
                item.setRtiNumber(dto.getRtiNumber());
                item.setSmstId(Integer.valueOf(dto.getSmstId()));
                item.setUnitId(Integer.valueOf(dto.getUnitId()));
//                item.setMirtiId();

                wRtiItemEntityList.add(item);
            }
            wRtiItemRepository.saveAll(wRtiItemEntityList);
        }

        return new ReceiveReceiptResultDTO("1", "");
    }

    @Override
    public ReceiveReceiptResultDTO runRti(String projId, FMaterialReturnReceiptDTO returnReceiptDTO) {
        return transRepository.callProcedure(returnReceiptDTO.getRtiNumber(), projId, "RTI");
    }

}
