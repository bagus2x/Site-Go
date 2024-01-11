package id.co.mii.sitego.repository;

import id.co.mii.sitego.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Integer> {

    List<Profile> findDistinctByUserRolesNameIn(Set<String> roleNames);

    Optional<Profile> findByUserToken(String token);
}
