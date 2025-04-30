package com.ose.baiduNormalTranslate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.ose.util.StringUtils;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

public class Main {

    // 在平台申请的APP_ID 详见 http://api.fanyi.baidu.com/api/trans/product/desktop?req=developer
    @Value("${baidu.translate.appid}")
    private static String APP_ID;
    @Value("${baidu.translate.key}")
    private static String SECURITY_KEY;

    public static void main(String[] args) {
        TransApi api = new TransApi(APP_ID, SECURITY_KEY);

        String query = "Height : 600m";
        String result = api.getTransResult(query, "en", "zh");
        result = result.replaceAll("trans_result", "transResult");
        NormalTranslateResult ntr = StringUtils.decode(result, new TypeReference<NormalTranslateResult>(){});
        List<TranslateDetail> tds = ntr.getJsonTransResult();
        System.out.println(tds.get(0).getDst());

    }

}
