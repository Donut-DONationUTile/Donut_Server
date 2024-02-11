package zero.eight.donut;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class DonutApplication {

    public static void main(String[] args) {
        SpringApplication.run(DonutApplication.class, args);
    }

}
