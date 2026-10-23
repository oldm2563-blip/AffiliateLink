package ma.youcode.affiliatelink.profile.dto.response;

import lombok.Builder;
import ma.youcode.affiliatelink.profile.enums.Status;

import java.time.LocalDateTime;

@Builder
public record ProfileStatusResponseDTO(
        Long id,
        String email,
        Status status,
        LocalDateTime createdAt
) {}