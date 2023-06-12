package cloudclud.msa.apigatewayservice.filter;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class LoggingFilter extends AbstractGatewayFilterFactory<LoggingFilter.Config> {

    public LoggingFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        // ServerWebExchange, GatewayFilterChain 매개 변수로 받아 GatewayFilter 구현체 생성
        // Logging Pre Filter
        GatewayFilter filter = new OrderedGatewayFilter((exchange, chain) -> { // 비동기 -> ServletRequest/Response가 아닌, ServerRequest/Response로부터 요청/응답 추출
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            log.info("LoggingFilter baseMessage: {}", config.getBaseMessage());
            if (config.isPreLogger()) { // preLogger 작동 필요(args로 true 전달 시)
                log.info("LoggingFilter Start: req id -> {}", request.getId());
            }

            // Logging Post Filter
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                if (config.isPostLogger()) { // postLogger 작동 필요(args로 true 전달 시)
                    log.info("LoggingFilter End:res code -> {}", response.getStatusCode());
                }
            }));
        }, Ordered.HIGHEST_PRECEDENCE); // pre filter중 가장 먼저, post filter 중 가장 마지막에 실행


        return filter;
    }

    @Data
    public static class Config { // configutaion properties
        private String baseMessage;
        private boolean preLogger;
        private boolean postLogger;
    }

}