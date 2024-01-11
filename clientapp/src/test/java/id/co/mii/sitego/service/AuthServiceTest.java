package id.co.mii.sitego.service;

import id.co.mii.sitego.model.request.RegistrationRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AuthServiceTest {
    @Autowired
    public AuthService authService;

    @Test
    void testRegister() {
        authService.register(new RegistrationRequest("tubagus.sflh", "tubagus.sflh@gmail.com"));
    }
}
