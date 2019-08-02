package cn.benbenedu.sundial.account.configuration;

import cn.benbenedu.sundial.account.model.Client;
import cn.benbenedu.sundial.account.model.ClientState;
import cn.benbenedu.sundial.account.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Set;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfigurer
        extends AuthorizationServerConfigurerAdapter {

    private ClientRepository clientRepository;
    private ClientDetailsService clientDetailsService;
    private AuthenticationManager authenticationManager;
    private UserDetailsService userDetailsService;
    private RedisConnectionFactory redisConnectionFactory;

    @Autowired
    public AuthorizationServerConfigurer(
            ClientRepository clientRepository,
            ClientDetailsService clientDetailsService,
            AuthenticationManager authenticationManager,
            UserDetailsService userDetailsService,
            RedisConnectionFactory redisConnectionFactory) {

        this.clientRepository = clientRepository;
        this.clientDetailsService = clientDetailsService;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.redisConnectionFactory = redisConnectionFactory;
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients)
            throws Exception {

        initSundialCoreClient();

        clients.withClientDetails(clientDetailsService);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints)
            throws Exception {

        endpoints.authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService)
                .tokenStore(tokenStore());
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security)
            throws Exception {

        security.passwordEncoder(NoOpPasswordEncoder.getInstance());

        security.tokenKeyAccess("permitAll()");
        security.addTokenEndpointAuthenticationFilter(customCorsFilter());
    }

    @Bean
    public TokenStore tokenStore() {
        return new RedisTokenStore(redisConnectionFactory);
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public Filter customCorsFilter() {

        return (ServletRequest servletRequest,
                ServletResponse servletResponse,
                FilterChain filterChain) -> {

            final var response = (HttpServletResponse) servletResponse;
            final var request = (HttpServletRequest) servletRequest;

            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Methods", "*");
            response.setHeader("Access-Control-Max-Age", "3600");
            response.setHeader("Access-Control-Allow-Headers",
                    "x-requested-with, authorization, Content-Type, Authorization, credential, X-XSRF-TOKEN");

            if (HttpMethod.OPTIONS.name()
                    .equalsIgnoreCase(request.getMethod())) {

                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                filterChain.doFilter(servletRequest, servletResponse);
            }
        };
    }

    private void initSundialCoreClient() {

        final var coreClientId = "__SUNDIAL_CORE_CLIENT__";

        if (clientRepository.existsById(coreClientId)) {
            return;
        }

        final var coreClient = new Client();
        coreClient.setClientId(coreClientId);
        /**
         * TODO External the configuration of Sundial-core-client-secret.
         */
        coreClient.setClientSecret("LET_THERE_BE_LIGHT");
        coreClient.setAuthorizedGrantTypes(Set.of("password"));
        coreClient.setScope(Set.of("client:inner"));
        coreClient.setState(ClientState.Active);

        clientRepository.save(coreClient);
    }
}
