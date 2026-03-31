package lk.ijse.minitoonseduhub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class MIniToonsEduHubBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(MIniToonsEduHubBackendApplication.class, args);
    }

}
