package cn.benbenedu.sundial.account.model;

import lombok.Data;

@Data
public class WechatInfo {

    private String openid;
    private String unionid;
    private WechatLogin latestLogin;

    @Data
    public static class WechatLogin {

        private Long timestamp;
        private String accessToken;
        private Long expiresAt;
        private String refreshToken;
    }
}
