package cn.benbenedu.sundial.account.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.List;
import java.util.Set;

/**
 * Reference: org.springframework.security.oauth2.provider.client.BaseClientDetails
 */
@Data
@Document("Client")
public class Client {

    @Id
    private String clientId;
    private Long createTime;
    @JsonProperty("lUTime")
    private Long lUTime;
    private Creator creator;

    private ClientState state;

    private Owner owner;
    private String clientSecret;
    private Set<String> scope = Set.of();
    private Set<String> resourceIds = Set.of();
    private Set<String> authorizedGrantTypes = Set.of();
    private Set<String> registeredRedirectUris;
    private Set<String> autoApproveScopes;
    private List<GrantedAuthority> authorities = List.of();
    private Integer accessTokenValiditySeconds;
    private Integer refreshTokenValiditySeconds;


    @JsonIgnore
    public List<GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @JsonIgnore
    public void setAuthorities(
            List<? extends GrantedAuthority> authorities) {

        if (authorities == null) {
            return;
        }

        this.authorities = List.copyOf(authorities);
    }

    @JsonProperty("authorities")
    private List<String> getAuthoritiesAsStrings() {
        return List.copyOf(
                AuthorityUtils.authorityListToSet(authorities));
    }

    @JsonProperty("authorities")
    private void setAuthoritiesAsStrings(Set<String> values) {
        setAuthorities(AuthorityUtils.createAuthorityList(values
                .toArray(new String[0])));
    }
}
