package cn.benbenedu.sundial.account.model;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Reference: org.springframework.security.oauth2.provider.client.BaseClientDetails
 * TODO
 */
@Data
public class SundialClientDetails implements ClientDetails {

    private String clientId;
    private String clientSecret;
    private Set<String> scope;
    private Set<String> resourceIds;
    private Set<String> authorizedGrantTypes;
    private Set<String> registeredRedirectUri;
    private Set<String> autoApproveScopes;
    private List<GrantedAuthority> authorities;
    private Integer accessTokenValiditySeconds;
    private Integer refreshTokenValiditySeconds;
    private Map<String, Object> additionalInformation;

    /**
     * TODO
     *
     * @param client
     * @return
     */
    public static SundialClientDetails of(Client client) {

        final var clientDetails = new SundialClientDetails();
        clientDetails.setClientId(client.getClientId());
        clientDetails.setClientSecret(client.getClientSecret());
        clientDetails.setScope(client.getScope());
        clientDetails.setResourceIds(client.getResourceIds());
        clientDetails.setAuthorizedGrantTypes(client.getAuthorizedGrantTypes());
        clientDetails.setRegisteredRedirectUri(client.getRegisteredRedirectUris());
        clientDetails.setAutoApproveScopes(client.getAutoApproveScopes());
        clientDetails.setAuthorities(client.getAuthorities());
        clientDetails.setAccessTokenValiditySeconds(client.getAccessTokenValiditySeconds());
        clientDetails.setRefreshTokenValiditySeconds(client.getRefreshTokenValiditySeconds());

        return clientDetails;
    }

    @Override
    public boolean isAutoApprove(String scope) {

        return Optional.ofNullable(autoApproveScopes)
                .map(autos -> autos.contains(scope))
                .orElse(false);
    }

    @Override
    public boolean isSecretRequired() {
        return true;
    }

    @Override
    public boolean isScoped() {
        return this.scope != null &&
                !this.scope.isEmpty();
    }
}
