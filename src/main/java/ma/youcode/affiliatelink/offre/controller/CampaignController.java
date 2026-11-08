package ma.youcode.affiliatelink.offre.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.youcode.affiliatelink.offre.dto.CampaignResponseDTO;
import ma.youcode.affiliatelink.offre.dto.UpdateCampaignStatusRequestDTO;
import ma.youcode.affiliatelink.offre.enums.CampaignStatus;
import ma.youcode.affiliatelink.offre.service.CampaignService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/campaigns")
@RequiredArgsConstructor
public class CampaignController {

    private final CampaignService campaignService;

    @GetMapping
    public Page<CampaignResponseDTO> getCampaigns(
            @RequestParam(required = false) CampaignStatus status,
            Pageable pageable) {
        return campaignService.getCampaigns(status, pageable);
    }

    @PatchMapping("/{id}/status")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateCampaignStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCampaignStatusRequestDTO request) {
        campaignService.updateCampaignStatus(id, request.status());
    }
}
