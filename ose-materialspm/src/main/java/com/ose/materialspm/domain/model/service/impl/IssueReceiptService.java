package com.ose.materialspm.domain.model.service.impl;


import com.ose.materialspm.domain.model.repository.TransRepository;
import com.ose.materialspm.domain.model.repository.VMxjValidIssueReptEntityRepository;
import com.ose.materialspm.domain.model.repository.WMirItemRepository;
import com.ose.materialspm.domain.model.repository.WMirRepository;
import com.ose.materialspm.domain.model.service.IssueReceiptInterface;
import com.ose.materialspm.dto.*;
import com.ose.materialspm.entity.WMirEntity;
import com.ose.materialspm.entity.WMirItemEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class IssueReceiptService implements IssueReceiptInterface {

    private final VMxjValidIssueReptEntityRepository vMxjValidIssueReptEntityRepository;

    private final WMirRepository wMirRepository;

    private final WMirItemRepository wMirItemRepository;

    private final TransRepository transRepository;

    /**
     * 构造方法
     *
     * @param vMxjValidIssueReptEntityRepository
     * @param wMirRepository
     * @param wMirItemRepository
     * @param transRepository
     */
    @Autowired
    public IssueReceiptService(
        VMxjValidIssueReptEntityRepository vMxjValidIssueReptEntityRepository,
        WMirRepository wMirRepository,
        WMirItemRepository wMirItemRepository,
        TransRepository transRepository
    ) {
        this.vMxjValidIssueReptEntityRepository = vMxjValidIssueReptEntityRepository;
        this.wMirRepository = wMirRepository;
        this.wMirItemRepository = wMirItemRepository;
        this.transRepository = transRepository;
    }

    @Override
    public Page<IssueReceiptListResultsDTO> getIssueReceipt(IssueReceiptDTO issueReceiptDTO) {

        return vMxjValidIssueReptEntityRepository.getIssueReceiptList(issueReceiptDTO);
    }

    @Override
    @Transactional
    public ReceiveReceiptResultDTO saveMir(String projId, FMaterialIssueReceiptDTO issueReceiptDTO) {

        // main
//        List<WMirEntity> wMirEntityList = wMirRepository.findByProjIdAndMirNumber(projId, issueReceiptDTO.getMirNumber());
//        if (!wMirEntityList.isEmpty()) {
//            for (int i = 0; i < wMirEntityList.size(); i++) {
//                wMirRepository.delete(wMirEntityList.get(i));
//            }
//        }
        wMirRepository.deleteByProjIdAndMirNumber(projId, issueReceiptDTO.getMirNumber());

        WMirEntity entity = new WMirEntity();
        entity.setEsiStatus(issueReceiptDTO.getEsiStatus());
        entity.setRevisionId(Integer.valueOf(issueReceiptDTO.getRevisionId()));
        entity.setFahId(Integer.valueOf(issueReceiptDTO.getFahId()));
        entity.setMirNumber(issueReceiptDTO.getMirNumber());
        entity.setMirCreateDate(new Date());
        entity.setIssueDate(issueReceiptDTO.getIssueDate());
        entity.setIssueBy(issueReceiptDTO.getIssueBy());
        entity.setCompanyId(Integer.parseInt(issueReceiptDTO.getCompanyId()));
        entity.setWhId(issueReceiptDTO.getWhId());
        entity.setLocId(issueReceiptDTO.getLocId());
        entity.setMirType(issueReceiptDTO.getMirType());
        entity.setIssueType(issueReceiptDTO.getIssueType());
        entity.setPoplIssByProc(issueReceiptDTO.getPoplIssByProc());
        entity.setSiteStatInd(issueReceiptDTO.getSiteStatInd());
        // entity.setBnlId();
        entity.setProjId(projId);
        entity.setShortDesc(issueReceiptDTO.getShortDesc());
        entity.setDescription(issueReceiptDTO.getDescription());

        wMirRepository.save(entity);

        // detail
//        List<WMirItemEntity> wMirItemEntityList = wMirItemRepository.findByProjIdAndMirNumber(projId, issueReceiptDTO.getMirNumber());
//        if (!wMirItemEntityList.isEmpty()) {
//            for (int i = 0; i < wMirItemEntityList.size(); i++) {
//                wMirItemRepository.delete(wMirItemEntityList.get(i));
//            }
//        }
        wMirItemRepository.deleteByProjIdAndMirNumber(projId, issueReceiptDTO.getMirNumber());

        List<FMaterialIssueReceiptDetailDTO> details = issueReceiptDTO.getDetails();
        if (details != null) {
            List<WMirItemEntity> wMirItemEntities = new ArrayList<>();

            for (FMaterialIssueReceiptDetailDTO dto : details) {
                WMirItemEntity item = new WMirItemEntity();
                item.setIdent(Integer.parseInt(dto.getIdent()));
                item.setEsiStatus(dto.getEsiStatus());
                item.setIviId(Integer.valueOf(dto.getIviId()));
                // item.setMirId();
                item.setLpId(Integer.valueOf(dto.getLpId()));
                item.setIvprId(Integer.valueOf(dto.getIvprId()));
                item.setIssueQty(dto.getIssueQty());
                item.setIssueDate(dto.getIssueDate());
                item.setWhId(Integer.valueOf(dto.getWhId()));
                item.setLocId(Integer.valueOf(dto.getLocId()));
                item.setSmstId(Integer.valueOf(dto.getSmstId()));
                item.setUnitId(Integer.valueOf(dto.getUnitId()));
                item.setTagNumber(dto.getTagNumber());
                item.setHeatId(dto.getHeatId() == null ? null : Integer.valueOf(dto.getHeatId()));
                //item.setPlateId();
                item.setIdentDeviation(dto.getIdentDeviation());
                // item.setSasId();
                item.setSiteStatInd(dto.getSiteStatInd());
                item.setMirNumber(dto.getMirNumber());
                item.setProjId(projId);

                item.setMirNumber(dto.getMirNumber());
                item.setMirId(0);

                wMirItemEntities.add(item);
            }
            wMirItemRepository.saveAll(wMirItemEntities);
        }

        return new ReceiveReceiptResultDTO("1", "");
    }

    @Override
    public ReceiveReceiptResultDTO runMir(String projId, FMaterialIssueReceiptDTO issueReceiptDTO) {
        return transRepository.callProcedure(issueReceiptDTO.getMirNumber(), projId, "MIR");
    }

}
