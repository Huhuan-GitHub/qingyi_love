package com.neusoft.qingyi.pojo;

public class Cos {
    private String secretId;
    private String secretKey;
    private String bucketName;
    private String url;
    private static Cos cos = new Cos("AKIDud5GAnAG4brrb5IFwMVN1OTHp65nFQKx", "NTJtCF0UheKipgGYZJn02qQmvra7RXS8", "qingyi-1302213380", "https://qingyi-1302213380.cos.ap-chengdu.myqcloud.com/");

    private Cos(String secretId, String secretKey, String bucketName, String url) {
        this.secretId = secretId;
        this.secretKey = secretKey;
        this.bucketName = bucketName;
        this.url = url;
    }

    public String getSecretId() {
        return secretId;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public String getBucketName() {
        return bucketName;
    }

    public static Cos getCos() {
        return cos;
    }

    public String getUrl() {
        return url;
    }
}
