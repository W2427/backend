package com.ose.tasks.controller.bpm;

import com.ose.auth.annotation.SetUserInfo;
import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.dto.BaseDTO;
import com.ose.response.JsonObjectResponseBody;
import com.ose.tasks.api.bpm.QRCodeSearchAPI;
import com.ose.tasks.domain.model.repository.drawing.DrawingRepository;
import com.ose.tasks.domain.model.repository.drawing.SubDrawingRepository;
import com.ose.tasks.domain.model.service.drawing.SubDrawingInterface;
import com.ose.tasks.dto.QRCodeSearchResultDTO;
import com.ose.tasks.entity.drawing.SubDrawing;
import com.ose.vo.QrcodePrefixType;
import io.swagger.annotations.Api;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Api(description = "交接单接口")
@RestController
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/")
public class QRCodeSearchController extends BaseController implements QRCodeSearchAPI {

    private static final int QRCODE_LENGTH = 9;

    private final SubDrawingInterface subDrawingService;
    /**
     * 构造方法
     */
    @Autowired
    public QRCodeSearchController(SubDrawingInterface subDrawingService) {
        this.subDrawingService = subDrawingService;
    }

    @Override
    @Operation(
        summary = "二维码查询",
        description = "二维码查询"
    )
    @RequestMapping(
        method = GET,
        value = "qrcode-search/{qrcode}"
    )
    @WithPrivilege
    @SetUserInfo
    @ResponseStatus(OK)
    public JsonObjectResponseBody<QRCodeSearchResultDTO> getEntityList(
        @PathVariable("orgId") @Parameter(description = "orgId") Long orgId,
        @PathVariable("projectId") @Parameter(description = "projectId") Long projectId,
        @PathVariable("qrcode") @Parameter(description = "qrcode") String qrcode) {

        QRCodeSearchResultDTO resultDTO = new QRCodeSearchResultDTO();

        QrcodePrefixType type = null;
        BaseDTO result = null;

        if (qrcode.length() == QRCODE_LENGTH) {

            String typeWord = qrcode.substring(0, 1);

            if (typeWord.equals(QrcodePrefixType.DRAWING.getCode())) {

                type = QrcodePrefixType.DRAWING;
                result = subDrawingService.getDrawingByQrcode(orgId, projectId, qrcode);

            }
//            else if (typeWord.equals(QrcodePrefixType.MERTIAL.getCode())) {
//
//                type = QrcodePrefixType.MERTIAL;
//                result = qrCodeInterface.getQrCodeFItemInfo(orgId, projectId, qrcode);
//
//            }

        }

        resultDTO.setResult(result);
        resultDTO.setType(type);

        return new JsonObjectResponseBody<>(resultDTO);
    }


}
