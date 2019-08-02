package cn.benbenedu.sundial.account.configuration;

import cn.benbenedu.sundial.account.model.Account;
import cn.benbenedu.sundial.account.model.AccountRole;
import cn.benbenedu.sundial.account.model.AccountState;
import cn.benbenedu.sundial.account.repository.AccountRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Configuration
public class WebSecurityConfigurer
        extends WebSecurityConfigurerAdapter {

    private AccountRepository accountRepository;
    private UserDetailsService userDetailsService;

    public WebSecurityConfigurer(
            AccountRepository accountRepository,
            UserDetailsService userDetailsService) {

        this.accountRepository = accountRepository;
        this.userDetailsService = userDetailsService;
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean()
            throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth)
            throws Exception {

        initGodAccount();

        auth.userDetailsService(userDetailsService);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private void initGodAccount() {

        final var godMobile = "13810454607";    // TODO

        if (accountRepository.existsByMobile(godMobile)) {
            return;
        }

        final var godAccount = new Account();
        godAccount.setState(AccountState.Active);
        godAccount.setMobile(godMobile);
        /**
         * TODO External the configuration of God-secret.
         */
        godAccount.setPassword(passwordEncoder().encode("AND_THERE_WAS_LIGHT"));
        godAccount.setRoles(List.of(AccountRole.God));

        accountRepository.save(godAccount);
    }
}
