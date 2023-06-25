package cloudclud.msa.userservice.client;

import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
@RequiredArgsConstructor
public class FeignErrorDecoder implements ErrorDecoder {
    private final Environment env;
    @Override
    public Exception decode(String methodKey, Response response) {
        switch (response.status()) {
            case 400:
                break;
            case 404:
                if (methodKey.contains("getOrders")) {
                    return new ResponseStatusException(HttpStatus.NOT_FOUND,
                            env.getProperty("order_service.exception.orders_empty"));
                }
                break;
            default:
                return new Exception(response.reason());
        }
        return null; // 예외가 발생하지 않으면 null 리턴
    }
}
