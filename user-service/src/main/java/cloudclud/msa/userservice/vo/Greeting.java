package cloudclud.msa.userservice.vo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

// VO(Value Object)? DTO와 다르게, Setter없음. Read-Only
@Component // Spring Bean으로 등록하고 싶은데 용도가 Controller, Config 등 명확한 게 아닐 때
public class Greeting {
    @Value("${greeting.message}")
    private String message;

    public String getMessage() {
        return this.message;
    }
}
