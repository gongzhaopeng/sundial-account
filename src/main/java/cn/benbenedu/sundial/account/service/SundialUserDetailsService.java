package cn.benbenedu.sundial.account.service;

import cn.benbenedu.sundial.account.model.SundialUserDetails;
import cn.benbenedu.sundial.account.repository.AccountRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Primary
public class SundialUserDetailsService
        implements UserDetailsService {

    private AccountRepository accountRepository;

    public SundialUserDetailsService(
            AccountRepository accountRepository) {

        this.accountRepository = accountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        final var account = accountRepository
                .findByIdNumberOrMobileOrEmail(username, username, username);   // TODO

        return account
                .map(SundialUserDetails::of)
                .orElseThrow(() ->
                        new UsernameNotFoundException("No Account with requested username: " + username));
    }
}
