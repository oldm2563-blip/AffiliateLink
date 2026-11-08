
package ma.youcode.affiliatelink.offre.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "tags", uniqueConstraints = @UniqueConstraint(name = "uk_tag_name", columnNames = "name")
)
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @ManyToMany(mappedBy = "tags")
    private Set<Offre> offres = new HashSet<>();
}
