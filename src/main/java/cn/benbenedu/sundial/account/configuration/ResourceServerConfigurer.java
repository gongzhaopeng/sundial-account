package cn.benbenedu.sundial.account.configuration;

import cn.benbenedu.sundial.account.model.AccountRole;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

@Configuration
@EnableResourceServer
public class ResourceServerConfigurer
        extends ResourceServerConfigurerAdapter {

    private static final String RESOURCE_ID = "ACCOUNT_API";

    @Override
    public void configure(ResourceServerSecurityConfigurer resources)
            throws Exception {

        resources.resourceId(RESOURCE_ID);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS).permitAll();

        http.authorizeRequests()
                .antMatchers(HttpMethod.POST,
                        "/genesis/clients", "/genesis/accounts")
                .hasAnyRole(
                        AccountRole.God.name(),
                        AccountRole.SuperAdmin.name(),
                        AccountRole.Admin.name());

        http.authorizeRequests()
                .anyRequest().authenticated();
    }
}
