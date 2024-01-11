package id.co.mii.sitego.repository;

import id.co.mii.sitego.model.Consultation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsultationRepository extends JpaRepository<Consultation, Integer> {

    List<Consultation> findAllByCounseleeIdOrderByCreatedAtDesc(Integer counseleeId);

    List<Consultation> findAllByConsultantIdOrderByCreatedAtDesc(Integer consultantId);

    List<Consultation> findAllByOrderByCreatedAtDesc();
}
