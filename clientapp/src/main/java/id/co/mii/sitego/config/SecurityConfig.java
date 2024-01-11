package id.co.mii.sitego.config;


import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableMethodSecurity
@AllArgsConstructor
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                .antMatchers("/css/**", "/js/**", "/img/**", "/vendor/**").permitAll()
                .antMatchers("/").permitAll()
                .antMatchers("/registration", "/login", "/verification").permitAll()
                .anyRequest().authenticated()
            )
            .formLogin()
            .loginPage("/login")
            .loginProcessingUrl("/login")
            .successForwardUrl("/dashboard")
            .failureForwardUrl("/login?error=true");

        return http.build();
    }
}
