package cloudclud.msa.apigatewayservice.filter;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class GlobalFilter extends AbstractGatewayFilterFactory<GlobalFilter.Config> {

    public GlobalFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        // Global Pre Filter
        return (exchange, chain) -> { // 비동기 -> ServletRequest/Response가 아닌, ServerRequest/Response로부터 요청/응답 추출
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            log.info("GlobalFilter baseMessage: {}", config.getBaseMessage());
            if (config.isPreLogger()) { // preLogger 작동 필요(args로 true 전달 시)
                log.info("GlobalFilter Start: req id -> {}", request.getId());
            }

            // Global Post Filter
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                if (config.isPostLogger()) { // postLogger 작동 필요(args로 true 전달 시)
                    log.info("GlobalFilter End:res code -> {}", response.getStatusCode());
                }
            }));
        };
    }

    @Data
    public static class Config { // configutaion properties
        private String baseMessage;
        private boolean preLogger;
        private boolean postLogger;
    }

}