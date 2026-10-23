package ma.youcode.affiliatelink.profile.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import ma.youcode.affiliatelink.profile.enums.Status;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected long id;

    @Column(nullable = false, unique = true)
    protected String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    protected Status status = Status.PENDING;

    @Column(nullable = false, updatable = false)
    protected LocalDateTime createdAt;

    protected Profile(String email) {
        this.email = email;
    }

    
}
