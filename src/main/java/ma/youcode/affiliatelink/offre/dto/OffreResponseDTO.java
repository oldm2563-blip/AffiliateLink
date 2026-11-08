package ma.youcode.affiliatelink.offre.dto;

import lombok.Builder;
import ma.youcode.affiliatelink.offre.enums.Niche;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Builder
public record OffreResponseDTO(
        Long id,
        Long advertiserId,
        String title,
        String description,
        String landingUrl,
        Niche niche,
        LocalDateTime createdAt,
        Set<String> tags,
        List<CampaignResponseDTO> campaigns   // null in search results, filled on GET /{id}
) {}
