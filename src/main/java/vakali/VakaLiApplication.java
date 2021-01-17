package vakali;

import java.util.TimeZone;
import javax.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class VakaLiApplication {

    //Aikavyöhyke
    @PostConstruct
    public void started() {
        TimeZone.setDefault(TimeZone.getTimeZone("EET"));
    }

    public static void main(String[] args) {
        SpringApplication.run(VakaLiApplication.class, args);
    }
}
