
package ma.youcode.affiliatelink.offre.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.youcode.affiliatelink.offre.enums.Niche;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "offres")
public class Offre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "advertiser_id", nullable = false)
    private Long advertiserId;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "landing_url", nullable = false, length = 2048)
    private String landingUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 100)
    private Niche niche;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToMany
    @JoinTable(
            name = "offre_tags",
            joinColumns = @JoinColumn(name = "offre_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"),
            uniqueConstraints = @UniqueConstraint(
                    name = "uk_offre_tag",
                    columnNames = {"offre_id", "tag_id"}
            )
    )
    private Set<Tag> tags = new HashSet<>();

    @OneToMany(
            mappedBy = "offre",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Campaign> campaigns = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    public void addCampaign(Campaign campaign) {
        campaigns.add(campaign);
        campaign.setOffre(this);
    }

    public void removeCampaign(Campaign campaign) {
        campaigns.remove(campaign);
        campaign.setOffre(null);
    }
}
