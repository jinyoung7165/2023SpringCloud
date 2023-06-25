package cloudclud.msa.userservice;

import feign.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@EnableWebSecurity
@EnableFeignClients
@EnableDiscoveryClient // discovery에 등록할 수 있는 service라고 정의
@SpringBootApplication
public class UserServiceApplication {

    @PostConstruct
    public void started() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
    }

    @Bean
    @LoadBalanced
    public RestTemplate getRestTemplate() { return new RestTemplate(); }

    @Bean // FEIGN CLIENT 호출 시 LOG
    public Logger.Level feignLoggerLevel() { return Logger.Level.FULL; }

//    @Bean
//    public FeignErrorDecoder getFeignErrorDecoder() { return new FeignErrorDecoder(); }

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }
}