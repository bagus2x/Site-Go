package id.co.mii.sitego.config;

import id.co.mii.sitego.model.*;
import id.co.mii.sitego.repository.ConsultationStatusRepository;
import id.co.mii.sitego.repository.ProfileRepository;
import id.co.mii.sitego.repository.ProfileStatusRepository;
import id.co.mii.sitego.repository.RoleRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Configuration
@AllArgsConstructor
@Slf4j
public class InitialDataConfig implements CommandLineRunner {
    private final RoleRepository roleRepository;
    private final ProfileRepository profileRepository;
    private final ConsultationStatusRepository consultationStatusRepository;
    private final ProfileStatusRepository profileStatusRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // Initialize roles
        List<String> roleNames = Arrays.asList("ADMIN", "CONSULTANT", "EMPLOYEE");
        roleNames.forEach(roleName -> {
            if (!roleRepository.existsByNameIgnoreCase(roleName)) {
                Role role = new Role();
                role.setName(roleName);

                roleRepository.save(role);
            }
        });

        // Initialize profile status
        List<String> profileStatuses = Arrays.asList("IDLE", "ONSITE");
        profileStatuses.forEach(profileStatusName -> {
            if (!profileStatusRepository.existsByNameIgnoreCase(profileStatusName)) {
                ProfileStatus profileStatus = new ProfileStatus();
                profileStatus.setName(profileStatusName);

                profileStatusRepository.save(profileStatus);
            }
        });

        // Initialize first admin
        Role adminRole = roleRepository.findByName("ADMIN")
            .orElseThrow(() -> new Exception("Roles failed to initialize"));

        if (adminRole.getUsers().isEmpty()) {
            String username = "system";
            String password = "system123";

            Role employeeRole = roleRepository.findByName("EMPLOYEE").orElseThrow(() -> new Exception("Failed to load employee role"));
            Role consultantRole = roleRepository.findByName("CONSULTANT").orElseThrow(() -> new Exception("Failed to load consultant role"));

            User admin = new User();
            admin.setEmail("system@yopmail.com");
            admin.setIsEnabled(true);
            admin.setUsername(username);
            admin.setPassword(passwordEncoder.encode(password));
            admin.setRoles(new HashSet<>(Arrays.asList(adminRole, employeeRole, consultantRole)));

            Profile profile = new Profile();
            profile.setName(username);
            profile.setUser(admin);
            admin.setProfile(profile);

            ProfileStatus profileStatus = profileStatusRepository.findByName("ONSITE")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile is status not found"));
            profile.setStatus(profileStatus);

            profileRepository.save(profile);

            log.info("Initialize the first admin account:\n\nUsername: {}\nPassword: {}\nNote: Please change the username and password for security reasons.\n\n", username, password);
        }

        // Initialize consultation status
        List<String> consultationStatuses = Arrays.asList("WAITING", "VALIDATED", "REJECTED", "SCHEDULED", "DONE");
        consultationStatuses.forEach(consultationStatusName -> {
            if (!consultationStatusRepository.existsByNameIgnoreCase(consultationStatusName)) {
                ConsultationStatus consultationStatus = new ConsultationStatus();
                consultationStatus.setName(consultationStatusName);

                consultationStatusRepository.save(consultationStatus);
            }
        });
    }
}
