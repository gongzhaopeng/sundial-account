package cn.benbenedu.sundial.account.service;

import cn.benbenedu.sundial.account.model.SundialClientDetails;
import cn.benbenedu.sundial.account.repository.ClientRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import org.springframework.stereotype.Service;

@Service
@Primary
public class SundialClientDetailsService
        implements ClientDetailsService {

    private ClientRepository clientRepository;

    public SundialClientDetailsService(
            ClientRepository clientRepository) {

        this.clientRepository = clientRepository;
    }

    @Override
    public ClientDetails loadClientByClientId(String clientId)
            throws ClientRegistrationException {

        return clientRepository.findById(clientId)
                .map(SundialClientDetails::of)
                .orElseThrow(() ->
                        new NoSuchClientException("No client with requested id: " + clientId));
    }
}
