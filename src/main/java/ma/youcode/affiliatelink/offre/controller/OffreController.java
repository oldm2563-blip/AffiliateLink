
package ma.youcode.affiliatelink.offre.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.youcode.affiliatelink.offre.dto.CampaignResponseDTO;
import ma.youcode.affiliatelink.offre.dto.CreateCampaignRequestDTO;
import ma.youcode.affiliatelink.offre.dto.CreateOffreRequestDTO;
import ma.youcode.affiliatelink.offre.dto.OffreResponseDTO;
import ma.youcode.affiliatelink.offre.enums.Niche;
import ma.youcode.affiliatelink.offre.service.CampaignService;
import ma.youcode.affiliatelink.offre.service.OffreService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/offres")
@RequiredArgsConstructor
public class OffreController {

    private final OffreService offreService;
    private final CampaignService campaignService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OffreResponseDTO createOffre(
            @RequestParam Long advertiserId,
            @Valid @RequestBody CreateOffreRequestDTO request) {
        return offreService.createOffre(advertiserId, request);
    }

    @GetMapping("/{id}")
    public OffreResponseDTO getOffreById(@PathVariable Long id) {
        return offreService.getOffreById(id);
    }

    @PutMapping("/{id}")
    public OffreResponseDTO updateOffre(
            @PathVariable Long id,
            @RequestParam Long advertiserId,
            @Valid @RequestBody CreateOffreRequestDTO request) {

        return offreService.updateOffre(id, advertiserId, request);
    }

    @GetMapping
    public Page<OffreResponseDTO> searchOffres(
            @RequestParam(required = false) Niche niche,
            @RequestParam(required = false) Long advertiserId,
            @RequestParam(required = false) String tag,
            @RequestParam(required = false) String keyword,
            @PageableDefault(
                    size = 10,
                    sort = "createdAt",
                    direction = Sort.Direction.DESC
            ) Pageable pageable) {

        return offreService.searchOffres(
                niche, advertiserId, tag, keyword, pageable);
    }

    @PostMapping("/{id}/campaigns")
    @ResponseStatus(HttpStatus.CREATED)
    public CampaignResponseDTO createCampaign(
            @PathVariable Long id,
            @Valid @RequestBody CreateCampaignRequestDTO request) {

        return campaignService.createCampaign(id, request);
    }



}
