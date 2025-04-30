package com.ose.prints.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.List;

import com.ose.prints.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ose.controller.BaseController;
import com.ose.exception.NotFoundError;
import com.ose.prints.api.PrintAPI;
import com.ose.prints.common.ZplPrinter;
import com.ose.prints.domain.model.service.PrintTaskInterface;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

@Tag(name = "Print")
@RestController
public class PrintController extends BaseController implements PrintAPI {


    // 受保护文件路径
    @Value("${print.ts24.path}")
    private String printTs24Path;

    // 工作场地管理服务接口
    private final PrintTaskInterface printTaskService;

    /**
     * 构造方法。
     */
    @Autowired
    public PrintController(PrintTaskInterface printTaskService) {
        this.printTaskService = printTaskService;

    }

    @Override
    @Operation(description = "Print")
    @RequestMapping(method = GET, value = "/prints/{printId}")
    public JsonResponseBody getPrinter(@PathVariable @Parameter(description = "打印机ID") String printId,
                                       @Parameter(description = "height") String height, @Parameter(description = "cuttingRFlag") Boolean cuttingRFlag) {
        System.out.println("OK");
        try {
            // "ZDesigner ZT410-300dpi ZPL"
            ZplPrinter p = new ZplPrinter(printId, printTs24Path);
            CuttingDetailDTO cuttingDTO = new CuttingDetailDTO();
            cuttingDTO.setIdent("axx12");
            cuttingDTO.setProject("F196F");
            cuttingDTO.setHeatNo("9999xxxxxxx-8888888888");
            cuttingDTO.setSpec("xxxx9999");
            cuttingDTO.setTagNumber("xxxxxxxxxxxxxx9999");
            cuttingDTO.setUnit("xxxxxxxxxxxxx");
            cuttingDTO.setQrCode("M12345678");
            cuttingDTO.setQty("999");
            cuttingDTO.setName("x123456789012345678901234567890");
            cuttingDTO.setDesc(
                "轧制H形钢,Q345B,GB/T11263-2017,H400X400X13X21轧制H形钢,Q345B,GB/T11263-2017,H400X400X13X21轧制H形钢,Q345B,GB/T11263-2017,H400X400X13X21");
            cuttingDTO.setHeight(height);
            if (cuttingRFlag == null || cuttingRFlag == true) {
                printTaskService.printQrCode("测试打印", printTs24Path, printId, cuttingDTO,null);
            } else {
                printTaskService.printQrCodeR("测试打印", printTs24Path, printId, cuttingDTO);
            }
        } catch (Exception e) {
            throw new NotFoundError();
        }
        return new JsonResponseBody();
    }

    @Override
    @Operation(description = "cutting")
    @RequestMapping(method = POST, value = "/prints/{printId}/cutting")
    public JsonObjectResponseBody<CuttingDetailDTO> cutting(@PathVariable @Parameter(description = "打印机ID") String printId,
                                                            @Parameter(description = "cuttingRFlag") Boolean cuttingRFlag, @RequestBody CuttingDetailDTO detail) {
        if (cuttingRFlag == null || cuttingRFlag == true) {
            printTaskService.printQrCode("PIPE-PIECE", printTs24Path, printId, detail,"cutting");
        } else {
            printTaskService.printQrCodeR("PIPE-PIECE", printTs24Path, printId, detail);
        }
        return new JsonObjectResponseBody<>(detail);
    }

    @Override
    @Operation(description = "product")
    @RequestMapping(method = POST, value = "/prints/{printId}/product")
    public JsonObjectResponseBody<CuttingDetailDTO> product(@PathVariable @Parameter(description = "打印机ID") String printId,
                                                            @Parameter(description = "cuttingRFlag") Boolean cuttingRFlag, @RequestBody CuttingDetailDTO detail) {
        System.out.println("OK");
        if (cuttingRFlag == null || cuttingRFlag == true) {
            printTaskService.printQrCode("MATERIAL", printTs24Path, printId, detail,"product");
        } else {
            printTaskService.printQrCodeR("MATERIAL", printTs24Path, printId, detail);
        }
        return new JsonObjectResponseBody<>(detail);
    }

    @Override
    @Operation(description = "cuttings")
    @RequestMapping(method = POST, value = "/prints/{printId}/cuttings")
    public JsonObjectResponseBody<CuttingDTO> cuttings(@PathVariable @Parameter(description = "打印机ID") String printId,
                                                       @Parameter(description = "cuttingRFlag") Boolean cuttingRFlag, @RequestBody CuttingDTO cuttingDTO) {
        List<CuttingDetailDTO> details = cuttingDTO.getDatas();
        for (CuttingDetailDTO detail : details) {
            if (cuttingRFlag == null || cuttingRFlag == true) {
                printTaskService.printQrCode("PIPE-PIECE", printTs24Path, printId, detail,"cuttings");
            } else {
                printTaskService.printQrCodeR("PIPE-PIECE", printTs24Path, printId, detail);
            }
        }
        return new JsonObjectResponseBody<>(cuttingDTO);
    }

    @Override
    @Operation(description = "products")
    @RequestMapping(method = POST, value = "/prints/{printId}/products")
    public JsonObjectResponseBody<CuttingDTO> products(@PathVariable @Parameter(description = "打印机ID") String printId,
                                                       @Parameter(description = "cuttingRFlag") Boolean cuttingRFlag, @RequestBody CuttingDTO cuttingDTO) {
        System.out.println("OK");
        List<CuttingDetailDTO> details = cuttingDTO.getDatas();
        for (CuttingDetailDTO detail : details) {
            if (cuttingRFlag == null || cuttingRFlag == true) {
                printTaskService.printQrCode("MATERIAL", printTs24Path, printId, detail,"products");
            } else {
                printTaskService.printQrCodeR("MATERIAL", printTs24Path, printId, detail);
            }
        }
        return new JsonObjectResponseBody<>(cuttingDTO);
    }

    @Override
    @Operation(description = "spool")
    @RequestMapping(method = POST, value = "/prints/{printId}/spool")
    public JsonObjectResponseBody<SpoolQrCodeDetailDTO> spool(@PathVariable @Parameter(description = "打印机ID") String printId,
                                                              @Parameter(description = "cuttingRFlag") Boolean cuttingRFlag, @RequestBody SpoolQrCodeDetailDTO detail) {
        System.out.println("OK");
        if (cuttingRFlag == null || cuttingRFlag == true) {
            printTaskService.printSpoolQrCode("PIPE-SPOOL", printTs24Path, printId, detail);
        } else {
            printTaskService.printSpoolQrCodeR("PIPE-SPOOL", printTs24Path, printId, detail);
        }
        return new JsonObjectResponseBody<>(detail);
    }

    @Override
    @Operation(description = "spools")
    @RequestMapping(method = POST, value = "/prints/{printId}/spools")
    public JsonObjectResponseBody<SpoolQrCodeDTO> spools(@PathVariable @Parameter(description = "打印机ID") String printId,
                                                         @Parameter(description = "cuttingRFlag") Boolean cuttingRFlag, @RequestBody SpoolQrCodeDTO spoolQrCodeDTO) {
        System.out.println("OK");
        List<SpoolQrCodeDetailDTO> datas = spoolQrCodeDTO.getDatas();
        for (SpoolQrCodeDetailDTO detail : datas) {
            if (cuttingRFlag == null || cuttingRFlag == true) {
                printTaskService.printSpoolQrCode("PIPE-SPOOL", printTs24Path, printId, detail);
            } else {
                printTaskService.printSpoolQrCodeR("PIPE-SPOOL", printTs24Path, printId, detail);
            }
        }
        return new JsonObjectResponseBody<>(spoolQrCodeDTO);
    }


    @Override
    @Operation(description = "product")
    @RequestMapping(method = POST, value = "/prints/{printId}/product/warehouse-location")
    public JsonObjectResponseBody<WarehouseLocationDTO> warehouseLocationPrint(
        @PathVariable @Parameter(description = "打印机ID") String printId,
        @RequestBody WarehouseLocationDTO warehouseLocationDTO
    ) {
        System.out.println("OK");
        List<WarehouseLoactionDetailDTO> datas = warehouseLocationDTO.getDatas();
        for (WarehouseLoactionDetailDTO detail : datas) {
            printTaskService.printWLQrCode("WH-LOC-GS", printTs24Path, printId, detail);
        }
        return new JsonObjectResponseBody<>(warehouseLocationDTO);
    }

    /**
     * 结构下料打印wp05实体。
     *
     * @param printId
     * @param detail
     * @return
     */
    @Override
    @Operation(description = "结构下料打印wp05实体")
    @RequestMapping(method = POST, value = "/prints/{printId}/structure-cutting")
    public JsonObjectResponseBody<StructureCuttingDetailListDTO> structureCuttingPrint(
        @PathVariable @Parameter(description = "打印机ID") String printId,
        @RequestBody StructureCuttingDetailListDTO detail) {
        printTaskService.structureCuttingPrintQrCode("WP05", printTs24Path, printId, detail);
        return new JsonObjectResponseBody<>(detail);
    }

    /**
     * 结构配送单打印实体。
     *
     * @param printId
     * @param detail
     * @return
     */
    @Override
    @Operation(description = "结构配送单打印实体")
    @RequestMapping(method = POST, value = "/prints/{printId}/part")
    public JsonObjectResponseBody<StructurePartDetailListDTO> structurePartPrint(
        @PathVariable @Parameter(description = "打印机ID") String printId,
        @RequestBody StructurePartDetailListDTO detail) {
        printTaskService.structurePartPrintQrCode(printTs24Path, printId, detail);
        return new JsonObjectResponseBody<>(detail);
    }

}
