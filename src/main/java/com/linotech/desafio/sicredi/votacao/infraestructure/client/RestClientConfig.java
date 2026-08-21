package com.linotech.desafio.sicredi.votacao.infraestructure.client;

import com.linotech.desafio.sicredi.votacao.infraestructure.client.exception.CpfNaoEncontradoException;
import com.linotech.desafio.sicredi.votacao.infraestructure.client.exception.UserInfoServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
@RequiredArgsConstructor
public class RestClientConfig {

    private final UserInfoProperties properties;
    @Bean
    public RestClient userInfoRestClient(RestClient.Builder builder) {
        return builder
                .baseUrl(properties.baseUrl())
                .requestFactory(clientHttpRequestFactory())
                .defaultStatusHandler(
                        HttpStatusCode::isError,
                        (request, response) -> {

                            if (response.getStatusCode().value() == 404) {
                                throw new CpfNaoEncontradoException();
                            }

                            throw new UserInfoServiceException("elegibilidade.servico-indisponivel");
                        }
                )
                .build();
    }

    @Bean
    public UserInfoClient userInfoClient(RestClient userInfoRestClient) {
        RestClientAdapter adapter = RestClientAdapter.create(userInfoRestClient);
        HttpServiceProxyFactory proxyFactory = HttpServiceProxyFactory.builderFor(adapter).build();
        return proxyFactory.createClient(UserInfoClient.class);
    }

    @Bean
    public ClientHttpRequestFactory clientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();

        factory.setConnectTimeout(properties.connectTimeout());
        factory.setReadTimeout(properties.readTimeout());
        return factory;
    }

}