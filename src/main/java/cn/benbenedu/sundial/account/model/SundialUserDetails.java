package cn.benbenedu.sundial.account.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
        userDetails.setId(account.getId());
        userDetails.setName(account.getName());
        userDetails.setNickname(account.getNickname());
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
    private String id;
    @JsonIgnore
    private Set<GrantedAuthority> authorities;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;

    private String name;
    private String nickname;

    @Override
    @JsonIgnore
    public String getUsername() {
        return id;
    }

    @Override
    @JsonIgnore
    public String getPassword() {
        return password;
    }

    public void eraseCredentials() {
        password = null;
    }
}
