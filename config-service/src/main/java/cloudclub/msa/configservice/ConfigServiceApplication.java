package cloudclub.msa.configservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@EnableConfigServer
@SpringBootApplication
public class ConfigServiceApplication {

    @PostConstruct
    public void started() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
    }

    public static void main(String[] args) {
        SpringApplication.run(ConfigServiceApplication.class, args);
    }
}