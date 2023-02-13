package com.neusoft.qingyi.util;


import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Base64;

public class Base64Util {

    /**
     * 对字节数组字符串进行Base64编码并生成图片
     *
     * @param base64Str base64字符编码
     * @param fileName  文件名
     */
    public static File Base64ToFile(String base64Str, String fileName) {
        File file = new File(fileName);
        if (base64Str == null) // 图像数据为空
            return null;
        try {
            // Base64解码
            byte[] bytes = Base64.getDecoder().decode(base64Str);
            for (int i = 0; i < bytes.length; ++i) {
                if (bytes[i] < 0) {// 调整异常数据
                    bytes[i] += 256;
                }
            }
            // 生成jpeg图片
            OutputStream out = new FileOutputStream(file);
            out.write(bytes);
            out.flush();
            out.close();
            //====
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * 字节数组转Base64字符串
     *
     * @param bytes 字节数组
     * @return Base64字符串
     */
    public static String byteToBase64(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

}
