package cloudclud.msa.userservice.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordEncoder {
    @Bean // component와 bean annotation 둘 다 필요
    public BCryptPasswordEncoder bCryptEncoder() {
        return new BCryptPasswordEncoder();
    } // RandomSalt 부여->여러번 hash 적용하는 암호화 방식
}
