package com.ose.baiduTranslate;

public class Main {

    // 在平台申请的APP_ID 详见 http://api.fanyi.baidu.com/api/trans/product/desktop?req=developer
    private static final String APP_ID = "20210604000853885";
    private static final String SECURITY_KEY = "314Cd5xAmPOqc1LZVgAy";

    public static void main(String[] args) {
        TransApi api = new TransApi(APP_ID, SECURITY_KEY);

        String query = "高度600米";
        String domain = "medicine";
        System.out.println(api.getTransResult(query, "auto", "en", domain));
    }

}
