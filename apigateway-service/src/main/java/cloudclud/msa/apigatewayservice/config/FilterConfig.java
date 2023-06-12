package cloudclud.msa.apigatewayservice.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes() // 특정 path 들어오면 uri로 route, 이 때 요청/응답 전달 시 거칠 filter 추가
                .route(r -> r.path("/first-service/**")
                        .filters(f -> f.addRequestHeader("first-req", "first-req-header")
                                .addResponseHeader("first-res", "first-res-header"))
                        .uri("http://localhost:8081")) // route정보
                .route(r -> r.path("/second-service/**")
                        .filters(f -> f.addRequestHeader("second-req", "second-req-header")
                                .addResponseHeader("second-req", "second-res-header"))
                        .uri("http://localhost:8082")) // route정보
                .build(); // 정의한 내용 memory에 반영
    }
}