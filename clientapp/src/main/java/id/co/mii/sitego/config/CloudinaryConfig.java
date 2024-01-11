package id.co.mii.sitego.config;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {
    @Value("${cloudinary.key}")
    private String key;

    @Value("${cloudinary.name}")
    private String name;

    @Value("${cloudinary.secret}")
    private String secret;

    @Bean
    public Cloudinary cloudinary() {
        // CLOUDINARY_URL=cloudinary://key:secret@name
        String url = String.format("cloudinary://%s:%s@%s", key, secret, name);
        return new Cloudinary(url);
    }
}
