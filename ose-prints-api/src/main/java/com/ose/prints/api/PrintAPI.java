package com.ose.prints.api;

import com.ose.prints.dto.*;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * 二维码打印接口。
 */
public interface PrintAPI {


    @RequestMapping(method = GET, value = "/prints/{printId}")
    JsonResponseBody getPrinter(@PathVariable("printId") String printId, @Parameter(description = "high") String high,
                                @Parameter(description = "cuttingRFlg") Boolean cuttingRFlg);

    @Operation(description = "cutting")
    @RequestMapping(method = POST, value = "/prints/{printId}/cutting")
    JsonObjectResponseBody<CuttingDetailDTO> cutting(@PathVariable @Parameter(description = "打印机ID") String printId,
                                                     @Parameter(description = "cuttingRFlg") Boolean cuttingRFlg, @RequestBody CuttingDetailDTO cuttingDetailDTO);

    @Operation(description = "product")
    @RequestMapping(method = POST, value = "/prints/{printId}/product")
    JsonObjectResponseBody<CuttingDetailDTO> product(@PathVariable @Parameter(description = "打印机ID") String printId,
                                                     @Parameter(description = "cuttingRFlg") Boolean cuttingRFlg, @RequestBody CuttingDetailDTO cuttingDetailDTO);

    @Operation(description = "cuttings")
    @RequestMapping(method = POST, value = "/prints/{printId}/cutting")
    JsonObjectResponseBody<CuttingDTO> cuttings(@PathVariable @Parameter(description = "打印机ID") String printId,
                                                @Parameter(description = "cuttingRFlg") Boolean cuttingRFlg, @RequestBody CuttingDTO cuttingDTO);

    @Operation(description = "products")
    @RequestMapping(method = POST, value = "/prints/{printId}/product")
    JsonObjectResponseBody<CuttingDTO> products(@PathVariable @Parameter(description = "打印机ID") String printId,
                                                @Parameter(description = "cuttingRFlg") Boolean cuttingRFlg, @RequestBody CuttingDTO cuttingDTO);

    @Operation(description = "spool")
    @RequestMapping(method = POST, value = "/prints/{printId}/spool")
    JsonObjectResponseBody<SpoolQrCodeDetailDTO> spool(@PathVariable @Parameter(description = "打印机ID") String printId,
                                                       @Parameter(description = "cuttingRFlg") Boolean cuttingRFlg, @RequestBody SpoolQrCodeDetailDTO spoolQrCodeDTO);

    @Operation(description = "spools")
    @RequestMapping(method = POST, value = "/prints/{printId}/spool")
    JsonObjectResponseBody<SpoolQrCodeDTO> spools(@PathVariable @Parameter(description = "打印机ID") String printId,
                                                  @Parameter(description = "cuttingRFlg") Boolean cuttingRFlg, @RequestBody SpoolQrCodeDTO spoolQrCodeDTO);

    @Operation(description = "product")
    @RequestMapping(method = POST, value = "/prints/{printId}/product/warehouse-location")
    JsonObjectResponseBody<WarehouseLocationDTO> warehouseLocationPrint(
        @PathVariable @Parameter(description = "打印机ID") String printId,
        @RequestBody WarehouseLocationDTO warehouseLocationDTO);

    /**
     * 结构下料打印wp05实体。
     *
     * @param printId
     * @param cuttingDetailDTO
     * @return
     */
    @Operation(description = "结构下料打印wp05实体")
    @RequestMapping(method = POST, value = "/prints/{printId}/structure-cutting")
    JsonObjectResponseBody<StructureCuttingDetailListDTO> structureCuttingPrint(
        @PathVariable @Parameter(description = "打印机ID") String printId,
        @RequestBody StructureCuttingDetailListDTO cuttingDetailDTO);

    @Operation(description = "结构配送单打印实体")
    @RequestMapping(method = POST, value = "/prints/{printId}/part")
    JsonObjectResponseBody<StructurePartDetailListDTO> structurePartPrint(
        @PathVariable @Parameter(description = "打印机ID") String printId,
        @RequestBody StructurePartDetailListDTO partDetailDTO);
}
