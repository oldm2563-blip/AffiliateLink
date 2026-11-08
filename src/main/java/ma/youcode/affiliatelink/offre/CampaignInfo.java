package ma.youcode.affiliatelink.offre;

import java.math.BigDecimal;
import java.time.LocalDate;


public record CampaignInfo(
        Long id,
        Long offreId,
        Long advertiserId,
        String name,
        LocalDate startDate,
        LocalDate endDate,
        BigDecimal budget,
        String payoutModel,
        BigDecimal payoutValue,
        String status
) {}
