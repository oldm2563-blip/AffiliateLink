
package ma.youcode.affiliatelink.offre.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.youcode.affiliatelink.offre.enums.CampaignStatus;
import ma.youcode.affiliatelink.offre.enums.PayoutModel;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "campaigns")
public class Campaign {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal budget;

    @Enumerated(EnumType.STRING)
    @Column(name = "payout_model", nullable = false, length = 30)
    private PayoutModel payoutModel;

    @Column(name = "payout_value", nullable = false, precision = 12, scale = 2)
    private BigDecimal payoutValue;

    @Setter(AccessLevel.NONE)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CampaignStatus status = CampaignStatus.DRAFT;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "offre_id", nullable = false)
    private Offre offre;

    public void activate(LocalDate today) {
        if (status != CampaignStatus.DRAFT
                && status != CampaignStatus.PAUSED) {
            throw new IllegalStateException(
                    "Only DRAFT or PAUSED campaigns can be activated."
            );
        }

        if (endDate.isBefore(today)) {
            throw new IllegalStateException(
                    "An expired campaign cannot be activated."
            );
        }

        status = CampaignStatus.ACTIVE;
    }

    public void pause() {
        if (status != CampaignStatus.ACTIVE) {
            throw new IllegalStateException(
                    "Only ACTIVE campaigns can be paused."
            );
        }

        status = CampaignStatus.PAUSED;
    }

    public void end() {
        if (status != CampaignStatus.ACTIVE
                && status != CampaignStatus.PAUSED) {
            throw new IllegalStateException(
                    "Only ACTIVE or PAUSED campaigns can be ended."
            );
        }

        status = CampaignStatus.ENDED;
    }
}
