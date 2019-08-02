package cn.benbenedu.sundial.account.controller;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/genesis/oauth")
public class OauthController {

    @GetMapping(value = "/userinfo", produces = "application/json")
    public Map<String, Object> getUserInformation(OAuth2Authentication user) {

        return Map.of(
                "user", user.getUserAuthentication().getPrincipal(),
                "authorities", AuthorityUtils.authorityListToSet(
                        user.getUserAuthentication().getAuthorities())
        );
    }
}
