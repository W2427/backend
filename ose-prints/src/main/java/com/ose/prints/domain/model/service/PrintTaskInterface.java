package com.ose.prints.domain.model.service;

import com.ose.prints.dto.*;

/**
 * 打印服务接口。
 */
public interface PrintTaskInterface {


    /**
     * 二维码打印任务。
     *
     * @param title         标题
     * @param printId       打印机ID
     * @param printTs24Path 打印机Lib文件路径
     */
    void printQrCode(String title, String printTs24Path, String printId, CuttingDetailDTO cuttingDTO,String type);

    /**
     * @param title         标题
     * @param printId       打印机ID
     * @param printTs24Path 打印机Lib文件路径
     * @param cuttingDTO    DTO
     */
    void printQrCodeR(String title, String printTs24Path, String printId, CuttingDetailDTO cuttingDTO);

    /**
     * 二维码打印任务Spool。
     *
     * @param title          标题
     * @param printId        打印机ID
     * @param printTs24Path  打印机Lib文件路径
     * @param spoolQrCodeDTO DTO
     */
    void printSpoolQrCode(String title, String printTs24Path, String printId, SpoolQrCodeDetailDTO spoolQrCodeDTO);

    /**
     * 二维码打印任务Spool(竖版)。
     *
     * @param title          标题
     * @param printId        打印机ID
     * @param printTs24Path  打印机Lib文件路径
     * @param spoolQrCodeDTO DTO
     */
    void printSpoolQrCodeR(String title, String printTs24Path, String printId, SpoolQrCodeDetailDTO spoolQrCodeDTO);

    /**
     * 二维码打印库位。
     *
     * @param title         标题
     * @param printTs24Path
     * @param printId       打印机Id
     */
    void printWLQrCode(String title, String printTs24Path, String printId, WarehouseLoactionDetailDTO warehouseLoactionDetailDTO);

    /**
     * 结构材料下料打印
     *
     * @param title         标题
     * @param printTs24Path
     * @param printId       打印机id
     * @param cuttingDTO    打印信息
     */
    void structureCuttingPrintQrCode(String title, String printTs24Path, String printId, StructureCuttingDetailListDTO cuttingDTO);

    /**
     * 结构配送单打印实体
     *
     * @param printTs24Path
     * @param printId       打印机id
     * @param partDTO    打印信息
     */
    void structurePartPrintQrCode(String printTs24Path, String printId, StructurePartDetailListDTO partDTO);

}
