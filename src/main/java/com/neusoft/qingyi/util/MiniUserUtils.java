package com.neusoft.qingyi.util;

import com.alibaba.fastjson2.JSONObject;
import com.neusoft.qingyi.common.ErrorCode;
import com.neusoft.qingyi.qingyiexception.QingYiException;
import org.springframework.web.client.RestTemplate;

public class MiniUserUtils {
    /**
     * 根据jsCode换取openid
     *
     * @param jsCode 小程序用户登录时获取的code
     * @return 小程序用户唯一标识
     */
    public static String getOpenid(String codeUrl, String appid, String secret, String jsCode) {
        String url = codeUrl + "?appid=" + appid + "&secret=" + secret + "&js_code=" + jsCode + "&grant_type=authorization_code";
        String res = new RestTemplate().getForObject(url, String.class);
        JSONObject jsonObject = JSONObject.parseObject(res);

        if (jsonObject != null) {
            String openid = jsonObject.getString("openid");
            if (openid == null) {
                throw new QingYiException(ErrorCode.OPERATION_ERROR);
            }
            return openid;
        } else {
            return res;
        }
    }
}
