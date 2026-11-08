package ma.youcode.affiliatelink.offre.dto;

import jakarta.validation.constraints.NotNull;
import ma.youcode.affiliatelink.offre.enums.CampaignStatus;

public record UpdateCampaignStatusRequestDTO(
        @NotNull CampaignStatus status
) {}