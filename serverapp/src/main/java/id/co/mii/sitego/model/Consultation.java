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
@Table(name = "consultation")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Consultation {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "venue", length = 511)
    private String venue;

    @Column(name = "time")
    private LocalDateTime time;

    @Column(name = "duration")
    private Long duration;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(
        name = "counselee_id",
        referencedColumnName = "id"
    )
    private Profile counselee;

    @ManyToOne
    @JoinColumn(
        name = "consultant_id",
        referencedColumnName = "id"
    )
    private Profile consultant;

    @ManyToOne
    @JoinColumn(
        name = "status_id",
        referencedColumnName = "id"
    )
    private ConsultationStatus status;

    @OneToMany(mappedBy = "id.consultation", fetch = FetchType.EAGER)
    private List<ConsultationHistory> histories;

    @OneToOne(mappedBy = "consultation", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Report report;
}
