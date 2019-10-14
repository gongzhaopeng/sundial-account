package cn.benbenedu.sundial.account.controller;

import cn.benbenedu.sundial.account.model.*;
import cn.benbenedu.sundial.account.repository.AccountRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/genesis/accounts")
public class AccountController {

    private PasswordEncoder passwordEncoder;
    private AccountRepository accountRepository;

    public AccountController(
            PasswordEncoder passwordEncoder,
            AccountRepository accountRepository) {

        this.passwordEncoder = passwordEncoder;
        this.accountRepository = accountRepository;
    }

    /**
     * TODO
     *
     * @param accountCreatingReq
     * @return
     */
    @PostMapping(
            consumes = "application/json",
            produces = "application/json"
    )
    @ResponseStatus(HttpStatus.CREATED)
    public AccountCreatedResp createAccount(
            @RequestBody AccountCreatingReq accountCreatingReq,
            OAuth2Authentication auth) {

        if (accountRepository.existsByIdNumberOrMobileOrEmail(
                Optional.ofNullable(accountCreatingReq.idNumber).orElse("__INVALID_ID_NUMBER__"),
                Optional.ofNullable(accountCreatingReq.mobile).orElse("__INVALID_MOBILE__"),
                Optional.ofNullable(accountCreatingReq.email).orElse("__INVALID_EMAIL__"))) {
            throw new IllegalArgumentException(
                    "Exclusive properties have been occupied.");
        }

        final var newAccount = new Account();

        final var now = System.currentTimeMillis();
        newAccount.setCreateTime(now);
        newAccount.setLUTime(now);

        final var authUserDetails = (SundialUserDetails) auth.getPrincipal();
        newAccount.setCreator(Creator.of(authUserDetails));

        newAccount.setState(AccountState.Unactivated);

        newAccount.setRoles(List.of(AccountRole.Normal));

        newAccount.setAffiliation(accountCreatingReq.affiliation);
        newAccount.setIdNumber(accountCreatingReq.idNumber);
        newAccount.setMobile(accountCreatingReq.mobile);
        newAccount.setEmail(accountCreatingReq.email);
        newAccount.setPassword(
                passwordEncoder.encode(accountCreatingReq.password));
        newAccount.setType(accountCreatingReq.type);
        newAccount.setName(accountCreatingReq.name);
        newAccount.setNickname(accountCreatingReq.nickname);

        final var createdAccount = accountRepository.save(newAccount);

        return AccountCreatedResp.of(createdAccount);
    }

    @GetMapping("/{account-id}")
    public Account getAccountById(
            @PathVariable("account-id") String accountId) {

        return accountRepository
                .findById(accountId)
                .map(acc -> {
                    acc.eraseCredentials();
                    return acc;
                })
                .orElseThrow();
    }

    @Setter
    private static class AccountCreatingReq {

        private AccountAffiliation affiliation;
        private String idNumber;
        private String mobile;
        private String email;
        private String password;
        private AccountType type;
        private String name;
        private String nickname;
    }

    @Getter
    private static class AccountCreatedResp {

        public static AccountCreatedResp of(Account createdAccount) {

            final var resp = new AccountCreatedResp();
            resp.id = createdAccount.getId();

            return resp;
        }

        private String id;
    }
}
