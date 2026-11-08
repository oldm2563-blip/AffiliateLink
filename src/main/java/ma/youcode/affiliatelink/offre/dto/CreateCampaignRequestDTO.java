
package ma.youcode.affiliatelink.offre.dto;

import jakarta.validation.constraints.*;
import ma.youcode.affiliatelink.offre.enums.PayoutModel;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateCampaignRequestDTO(

        @NotBlank
        @Size(max = 200)
        String name,

        @NotNull
        LocalDate startDate,

        @NotNull
        LocalDate endDate,

        @NotNull
        @DecimalMin("0.01")
        BigDecimal budget,

        @NotNull
        PayoutModel payoutModel,

        @NotNull
        @DecimalMin("0.01")
        BigDecimal payoutValue

) {}
