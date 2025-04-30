package com.ose.materialspm.domain.model.service.impl;


import com.ose.materialspm.domain.model.repository.TransRepository;
import com.ose.materialspm.domain.model.repository.VMxjMatlRecvRptsEntityRepository;
import com.ose.materialspm.domain.model.repository.WMrrItemRepository;
import com.ose.materialspm.domain.model.repository.WMrrRepository;
import com.ose.materialspm.domain.model.service.ReceiveReceiptInterface;
import com.ose.materialspm.dto.*;
import com.ose.materialspm.entity.WMrrEntity;
import com.ose.materialspm.entity.WMrrItemEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class ReceiveReceiptService implements ReceiveReceiptInterface {


    private final VMxjMatlRecvRptsEntityRepository vMxjMatlRecvRptsEntityRepository;

    private final WMrrItemRepository wMrrItemRepository;

    private final WMrrRepository wMrrRepository;

    private final TransRepository transRepository;

    /**
     * 构造方法
     *
     * @param vMxjMatlRecvRptsEntityRepository
     * @param wMrrItemRepository
     * @param wMrrRepository
     * @param transRepository
     */
    @Autowired
    public ReceiveReceiptService(
        VMxjMatlRecvRptsEntityRepository vMxjMatlRecvRptsEntityRepository,
        WMrrItemRepository wMrrItemRepository,
        WMrrRepository wMrrRepository,
        TransRepository transRepository
    ) {
        this.vMxjMatlRecvRptsEntityRepository = vMxjMatlRecvRptsEntityRepository;
        this.wMrrItemRepository = wMrrItemRepository;
        this.wMrrRepository = wMrrRepository;
        this.transRepository = transRepository;
    }

    @Override
    public Page<ReceiveReceiptListResultsDTO> getReceiveReceipt(ReceiveReceiptDTO receiveReceiptDTO) {

        return vMxjMatlRecvRptsEntityRepository.getReceiveReceiptList(receiveReceiptDTO);
    }

    @Override
    @Transactional
    public ReceiveReceiptResultDTO saveMrr(String projId, FMaterialReceiveReceiptDTO receiveReceiptDTO) {

        System.out.println("材料入库反写信息：" + receiveReceiptDTO);
        // main
//        List<WMrrEntity> wMrrEntityList = wMrrRepository.findByProjIdAndMrrNumber(projId, receiveReceiptDTO.getMrrNumber());
//        if (!wMrrEntityList.isEmpty()) {
//            for (int i = 0; i < wMrrEntityList.size(); i++) {
//                wMrrRepository.delete(wMrrEntityList.get(i));
//            }
//        }

        // 材料放行单主表的信息存储过程
        // 先删除原来对应的主表信息
        wMrrRepository.deleteByProjIdAndMrrNumber(projId, receiveReceiptDTO.getMrrNumber());

        // 存储新的主表信息
        WMrrEntity entity = new WMrrEntity();
        entity.setDescription(receiveReceiptDTO.getDescription());
        entity.setLocId(Integer.parseInt(receiveReceiptDTO.getLocId()));
        entity.setMatlRecvDate(receiveReceiptDTO.getMatlRecvDate());
        entity.setMrrCreateDate(new Date());
        entity.setMrrNumber(receiveReceiptDTO.getMrrNumber());
        entity.setPohId(Integer.parseInt(receiveReceiptDTO.getSpmPohId()));
        entity.setPoplIshByProc(receiveReceiptDTO.getPoplIshByProc());
        entity.setProjId(projId);
        // 特殊处理 反写SPM数据库的接收人为保管员。
        if (receiveReceiptDTO.getDetails() != null && receiveReceiptDTO.getDetails().size() > 0) {
            entity.setRecvBy(receiveReceiptDTO.getDetails().get(0).getStorekeeper());
        } else {
            entity.setRecvBy(receiveReceiptDTO.getRecvBy());
        }

        entity.setRecvType(receiveReceiptDTO.getRecvType());
        entity.setRelnId(Integer.parseInt(receiveReceiptDTO.getSpmRelnId()));
        entity.setShipper(receiveReceiptDTO.getShipper());
        entity.setShipperRefNo(receiveReceiptDTO.getShipperRefNo());
        entity.setShortDesc(receiveReceiptDTO.getShortDesc());
        entity.setSmstId(Integer.valueOf(receiveReceiptDTO.getSmstId()));
        entity.setWhId(Integer.valueOf(receiveReceiptDTO.getWhId()));
        entity.setEsiStatus(receiveReceiptDTO.getEsiStatus());
        wMrrRepository.save(entity);

        // detail
//        List<WMrrItemEntity> wMrrItemEntityList = wMrrItemRepository.findByProjIdAndMrrNumber(projId, receiveReceiptDTO.getMrrNumber());
//        if (!wMrrItemEntityList.isEmpty()) {
//            for (int i = 0; i < wMrrItemEntityList.size(); i++) {
//                wMrrItemRepository.delete(wMrrItemEntityList.get(i));
//            }
//        }

        // 删除原来子表的信息
        wMrrItemRepository.deleteByProjIdAndMrrNumber(projId, receiveReceiptDTO.getMrrNumber());

        // 储存新的子表信息
        List<FMaterialReceiveReceiptDetailDTO> details = receiveReceiptDTO.getDetails();
        if (details != null) {
            List<WMrrItemEntity> wMrrItemEntities = new ArrayList<>();

            for (FMaterialReceiveReceiptDetailDTO dto : details) {
                WMrrItemEntity item = new WMrrItemEntity();
                item.setCertificateNumber(dto.getCertificateNumber());
                item.setEsTagDescription(dto.getEsTagDescription());
                item.setEsTagShortDesc(dto.getEsTagShortDesc());
                item.setFileLocation(dto.getFileLocation());
//                item.setHeatNumber(dto.getHeatNumber());
                item.setIdent(Integer.valueOf(dto.getIdent()));
                item.setItemShipId(Integer.valueOf(dto.getItemShipId()));
                item.setItyCode(dto.getItyCode());
                item.setLocId(Integer.valueOf(dto.getLocId()));
                item.setManufacturer(dto.getManufacturer());
                item.setMrrNumber(receiveReceiptDTO.getMrrNumber());
                item.setProjId(projId);
                item.setRecvDate(dto.getRecvDate());
                item.setRecvQty(dto.getRecvQty());
                item.setSmstId(Integer.valueOf(dto.getSmstId()));
                item.setTagNumber(dto.getTagNumber());
                item.setUnitId(Integer.valueOf(dto.getUnitId()));
                item.setWhId(Integer.valueOf(dto.getWhId()));
                item.setEsiStatus(dto.getEsiStatus());

                wMrrItemEntities.add(item);
            }
            wMrrItemRepository.saveAll(wMrrItemEntities);
        }

        return new ReceiveReceiptResultDTO("1", "");
    }

    @Override
    public ReceiveReceiptResultDTO runProcedure(String projId, FMaterialReceiveReceiptDTO receiveReceiptDTO) {
        return transRepository.callProcedure(receiveReceiptDTO.getMrrNumber(), projId, "MRR");
    }

}
