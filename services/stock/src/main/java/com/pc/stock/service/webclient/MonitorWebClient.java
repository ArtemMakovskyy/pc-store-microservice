package com.pc.stock.service.webclient;

import com.pc.stock.dto.CreateMonitorDto;
import com.pc.stock.dto.MonitorDto;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class MonitorWebClient {
    private final WebClient.Builder webClientBuilder;
    private static final String BASE_URL = "http://PRODUCT-SERVICE/api/monitors";

    private WebClient getClient() {
        return webClientBuilder.baseUrl(BASE_URL).build();
    }

    public Mono<MonitorDto> saveMonitor(CreateMonitorDto createMonitorDto) {
        return getClient().post()
                .bodyValue(createMonitorDto)
                .retrieve()
                .bodyToMono(MonitorDto.class);

    }

    public Mono<MonitorDto> getMonitorById(Long id) {
        return getClient().get()
                .uri("/{id}", id)
                .retrieve()
                .bodyToMono(MonitorDto.class);
    }

    public Mono<List<MonitorDto>> getAllMonitors() {
        return getClient().get()
                .retrieve()
                .bodyToFlux(MonitorDto.class)
                .collectList();
    }

    public Mono<ResponseEntity<Void>> deleteMonitor(Long id) {
        return getClient().delete()
                .uri("/{id}", id)
                .retrieve()
                .onStatus(status -> status.isError(), response ->
                        Mono.error(new RuntimeException("Failed to delete monitor")))
                .toBodilessEntity();
    }


    public Mono<MonitorDto> updateMonitor(Long id, CreateMonitorDto createMonitorDto) {
        return getClient().put()
                .uri("/{id}", id)
                .bodyValue(createMonitorDto)
                .retrieve()
                .bodyToMono(MonitorDto.class);
    }

    public Mono<MonitorDto> updateMonitorSellingPrice(Long id, BigDecimal newSellingPrice) {
        return getClient().patch()
                .uri(uriBuilder -> uriBuilder
                        .path("/{id}/selling-price")
                        .queryParam("newSellingPrice", newSellingPrice)
                        .build(id))
                .retrieve()
                .bodyToMono(MonitorDto.class);
    }
}
