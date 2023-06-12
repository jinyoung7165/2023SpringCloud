package cloudclud.msa.apigatewayservice.filter;

import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
//@RequiredArgsConstructor
public class AuthHeaderFilter extends AbstractGatewayFilterFactory<AuthHeaderFilter.Config> {
    Environment env;

    public AuthHeaderFilter(Environment env) {
        super(AuthHeaderFilter.Config.class);
        this.env = env;
    }

    public static class Config {

    }

    // 비동기 -> ServletRequest/Response가 아닌, ServerRequest/Response로부터 요청/응답 추출
    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest(); // requestHeader에 Authorization 정보 있는지 확인
            if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) { // header에 첨부x 에러
                return onError(exchange, "No Authorization Header", HttpStatus.UNAUTHORIZED);
            }
            String authorizationHeader = request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            String jwt = authorizationHeader.replace("Bearer ", "");
            if (!isJwtValid(jwt)) { // jwt 유효성 검사 실패
                return onError(exchange, "JWT token is not valid", HttpStatus.UNAUTHORIZED);
            }
            return chain.filter(exchange); // 무사히 통과 후, request를 넘김
        };
    }

    private boolean isJwtValid(String jwt) { // 정상적으로 발급된 jwt 토큰인지
        boolean returnValue = true;
        String subject = null;
        try {
            subject = Jwts.parser()
                    .setSigningKey(env.getProperty("token.secret"))
                    .parseClaimsJws(jwt).getBody() // 문자열로 복호화 후 body의 subject뽑기
                    .getSubject();
        } catch (Exception ex) { // 복호화 중 오류
            returnValue = false;
        }

        if (subject == null || subject.isEmpty()) {
            returnValue = false;
        }

        return returnValue;
    }

    // SpringWebFlux(비동기 처리)에서 단일 값 반환 시 Mono, 다중 값 반환 시 Flux
    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        log.error(err);
        return response.setComplete();
    }
}
