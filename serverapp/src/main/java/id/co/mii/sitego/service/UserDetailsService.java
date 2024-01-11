package id.co.mii.sitego.service;

import id.co.mii.sitego.model.User;
import id.co.mii.sitego.model.UserDetails;
import id.co.mii.sitego.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@AllArgsConstructor
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository
            .findByUsernameOrEmail(username, username)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Username or email is not found"));

        return new UserDetails(user);
    }
}
