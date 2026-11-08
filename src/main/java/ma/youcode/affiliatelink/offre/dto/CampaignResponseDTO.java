package ma.youcode.affiliatelink.offre.dto;

import ma.youcode.affiliatelink.offre.enums.CampaignStatus;
import ma.youcode.affiliatelink.offre.enums.PayoutModel;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Builder;

@Builder
public record CampaignResponseDTO(
        Long id,
        Long offreId,
        String name,
        LocalDate startDate,
        LocalDate endDate,
        BigDecimal budget,
        PayoutModel payoutModel,
        BigDecimal payoutValue,
        CampaignStatus status
) {}