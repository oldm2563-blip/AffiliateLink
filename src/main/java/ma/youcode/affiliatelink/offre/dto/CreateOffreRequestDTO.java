package ma.youcode.affiliatelink.offre.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import ma.youcode.affiliatelink.offre.enums.Niche;

import java.util.Set;

public record CreateOffreRequestDTO(
        @NotBlank(message = "Title is required")
        @Size(max = 200, message = "Title must not exceed 200 characters")
        String title,

        @NotBlank(message = "Description is required")
        String description,

        @NotBlank(message = "Landing URL is required")
        @Size(max = 2048, message = "Landing URL must not exceed 2048 characters")
        String landingUrl,

        @NotNull(message = "Niche is required")
        Niche niche,

        Set<@NotBlank(message = "Tag cannot be blank") String> tags
) {}