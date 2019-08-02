package cn.benbenedu.sundial.account.model;

import lombok.Data;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class SundialUserDetails
        implements UserDetails, CredentialsContainer {

    public static SundialUserDetails of(Account account) {

        final var userDetails = new SundialUserDetails();

        userDetails.setPassword(account.getPassword());
        userDetails.setUsername(account.getId());
        userDetails.setAuthorities(
                Optional.ofNullable(account.getRoles())
                        .map(roles -> roles.stream().map(
                                role -> (GrantedAuthority) new SimpleGrantedAuthority("ROLE_" + role))
                                .collect(Collectors.toSet()))
                        .orElse(Set.of()));
        userDetails.setAccountNonExpired(account.getState() != AccountState.Abandoned);
        userDetails.setAccountNonLocked(account.getState() != AccountState.Locked);
        userDetails.setCredentialsNonExpired(true);
        userDetails.setEnabled(
                account.getState() != AccountState.Unactivated &&
                        account.getState() != AccountState.Disabled);

        return userDetails;
    }

    private String password;
    private String username;
    private Set<GrantedAuthority> authorities;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;

    public void eraseCredentials() {
        password = null;
    }
}
