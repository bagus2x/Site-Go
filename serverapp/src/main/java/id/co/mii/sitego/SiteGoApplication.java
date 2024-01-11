package id.co.mii.sitego;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class SiteGoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SiteGoApplication.class, args);

        log.info("Server App\n\nSiteGo is running 🚀🔥\n\n");
    }
}
