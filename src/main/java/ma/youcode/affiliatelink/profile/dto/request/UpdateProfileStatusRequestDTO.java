package ma.youcode.affiliatelink.profile.dto.request;

import jakarta.validation.constraints.NotNull;
import ma.youcode.affiliatelink.profile.enums.Status;

public record UpdateProfileStatusRequestDTO(
        @NotNull(message = "Status is required")
        Status status
) {}