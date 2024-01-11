package id.co.mii.sitego.repository;

import id.co.mii.sitego.model.ProfileStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileStatusRepository extends JpaRepository<ProfileStatus, Integer> {

    Optional<ProfileStatus> findByName(String name);

    boolean existsByNameIgnoreCase(String name);
}
