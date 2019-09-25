package cn.benbenedu.sundial.account.controller;

import cn.benbenedu.sundial.account.model.Client;
import cn.benbenedu.sundial.account.model.ClientState;
import cn.benbenedu.sundial.account.model.Creator;
import cn.benbenedu.sundial.account.model.Owner;
import cn.benbenedu.sundial.account.repository.AccountRepository;
import cn.benbenedu.sundial.account.repository.ClientRepository;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/genesis/clients")
public class ClientController {

    private ClientRepository clientRepository;
    private AccountRepository accountRepository;

    public ClientController(
            ClientRepository clientRepository,
            AccountRepository accountRepository) {

        this.clientRepository = clientRepository;
        this.accountRepository = accountRepository;
    }

    /**
     * TODO
     *
     * @param clientCreatingReq
     * @return
     */
    @PostMapping(
            consumes = "application/json",
            produces = "application/json"
    )
    @ResponseStatus(HttpStatus.CREATED)
    public ClientCreatedResp createClient(
            @RequestBody ClientCreatingReq clientCreatingReq,
            OAuth2Authentication auth) {

        final var ownerAccount = accountRepository
                .findById(clientCreatingReq.ownerId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "The specified owner dose not exist."));

        final var newClient = new Client();

        final var now = System.currentTimeMillis();
        newClient.setCreateTime(now);
        newClient.setLUTime(now);

        newClient.setCreator(
                Creator.of((Map) auth.getPrincipal()));

        newClient.setState(ClientState.Unactivated);

        newClient.setOwner(Owner.of(ownerAccount));

        newClient.setClientSecret(
                RandomStringUtils.randomAlphanumeric(64));

        newClient.setScope(clientCreatingReq.scope);
        newClient.setResourceIds(clientCreatingReq.resourceIds);
        newClient.setAuthorizedGrantTypes(
                clientCreatingReq.authorizedGrantTypes);
        newClient.setRegisteredRedirectUris(
                clientCreatingReq.registeredRedirectUris);
        newClient.setAutoApproveScopes(
                clientCreatingReq.autoApproveScopes);
        newClient.setAuthorities(clientCreatingReq.authorities);
        newClient.setAccessTokenValiditySeconds(
                clientCreatingReq.accessTokenValiditySeconds);
        newClient.setRefreshTokenValiditySeconds(
                clientCreatingReq.refreshTokenValiditySeconds);

        final var createdClient = clientRepository.save(newClient);

        return ClientCreatedResp.of(createdClient);
    }

    @GetMapping("/{client-id}")
    public Client getClientById(
            @PathVariable("client-id") String clientId) {

        return clientRepository.findById(clientId).orElseThrow();
    }

    @Setter
    private static class ClientCreatingReq {

        private String ownerId;
        private Set<String> scope;
        private Set<String> resourceIds;
        private Set<String> authorizedGrantTypes;
        private Set<String> registeredRedirectUris;
        private Set<String> autoApproveScopes;
        private List<GrantedAuthority> authorities;
        private Integer accessTokenValiditySeconds;
        private Integer refreshTokenValiditySeconds;

        @JsonIgnore
        public void setAuthorities(
                List<? extends GrantedAuthority> authorities) {
            this.authorities = List.copyOf(authorities);
        }

        @JsonProperty("authorities")
        private void setAuthoritiesAsStrings(Set<String> values) {
            setAuthorities(AuthorityUtils.createAuthorityList(values
                    .toArray(new String[0])));
        }
    }

    @Getter
    private static class ClientCreatedResp {

        public static ClientCreatedResp of(Client createdClient) {

            final var resp = new ClientCreatedResp();
            resp.id = createdClient.getClientId();

            return resp;
        }

        private String id;
    }
}
