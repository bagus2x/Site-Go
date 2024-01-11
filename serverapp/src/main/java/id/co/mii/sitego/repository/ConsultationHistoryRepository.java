package id.co.mii.sitego.repository;

import id.co.mii.sitego.model.ConsultationHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsultationHistoryRepository extends JpaRepository<ConsultationHistory, Integer> {
}
