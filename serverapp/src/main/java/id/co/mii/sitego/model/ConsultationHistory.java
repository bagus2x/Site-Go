package id.co.mii.sitego.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "consultation_history")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ConsultationHistory {
    @EmbeddedId
    private Id id;

    @Column(name = "description")
    @Lob
    private String description;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(
        name = "editor_id",
        referencedColumnName = "id"
    )
    private Profile editor;

    @Embeddable
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class Id implements Serializable {
        private static final long serialVersionUID = -1674575896565639073L;

        @ManyToOne
        @JoinColumn(
            name = "consultation_id",
            referencedColumnName = "id"
        )
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        private Consultation consultation;

        @ManyToOne
        @JoinColumn(
            name = "consultation_status_id",
            referencedColumnName = "id"
        )
        private ConsultationStatus consultationStatus;
    }
}
