package id.co.mii.sitego.repository;

import id.co.mii.sitego.model.ConsultationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConsultationStatusRepository extends JpaRepository<ConsultationStatus, Integer> {

    boolean existsByNameIgnoreCase(String name);

    Optional<ConsultationStatus> findByName(String name);
}
