package id.co.mii.sitego.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "profile")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Profile {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "gender", length = 8)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "name", nullable = false, length = 127)
    private String name;

    @Column(name = "cv", length = 511)
    private String cv;

    @Column(name = "photo", length = 511)
    private String photo;

    @Column(name = "phone", length = 16, unique = true)
    private String phone;

    @OneToOne(mappedBy = "profile", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private User user;

    @ManyToOne
    @JoinColumn(
        name = "status_id",
        referencedColumnName = "id"
    )
    private ProfileStatus status;

    @OneToMany(mappedBy = "counselee")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Consultation> counseleeConsultations;

    @OneToMany(mappedBy = "consultant")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Consultation> consultantConsultations;
}
