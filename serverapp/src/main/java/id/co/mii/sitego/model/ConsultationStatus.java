package id.co.mii.sitego.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "consultation_status")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ConsultationStatus {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "id.consultationStatus")
    @OrderBy("createdAt ASC")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<ConsultationHistory> histories;

    @OneToMany(mappedBy = "status")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Consultation> consultations;
}
