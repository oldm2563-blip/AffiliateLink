package ma.youcode.affiliatelink.offre.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.youcode.affiliatelink.common.exceptions.BusinessRuleException;
import ma.youcode.affiliatelink.common.exceptions.ResourcesNotFoundException;
import ma.youcode.affiliatelink.common.exceptions.ValidationException;
import ma.youcode.affiliatelink.offre.dto.CampaignMapper;
import ma.youcode.affiliatelink.offre.dto.CampaignResponseDTO;
import ma.youcode.affiliatelink.offre.dto.CreateCampaignRequestDTO;
import ma.youcode.affiliatelink.offre.entities.Campaign;
import ma.youcode.affiliatelink.offre.entities.Offre;
import ma.youcode.affiliatelink.offre.enums.CampaignStatus;
import ma.youcode.affiliatelink.offre.enums.PayoutModel;
import ma.youcode.affiliatelink.offre.repository.CampaignRepository;
import ma.youcode.affiliatelink.offre.repository.OffreRepository;
import ma.youcode.affiliatelink.profile.ProfileApi;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class CampaignService {

    private static final BigDecimal HUNDRED = BigDecimal.valueOf(100);

    private final CampaignRepository campaignRepository;
    private final OffreRepository offreRepository;
    private final ProfileApi profileApi;
    private final CampaignMapper campaignMapper;

    @Transactional
    public CampaignResponseDTO createCampaign(Long offreId, CreateCampaignRequestDTO request) {
        Offre offre = offreRepository.findById(offreId)
                .orElseThrow(() -> new ResourcesNotFoundException("Offer not found with id: " + offreId));

        Long advertiserId = offre.getAdvertiserId();
        if (!profileApi.advertiserExists(advertiserId)) {
            throw new ResourcesNotFoundException("Advertiser not found with id: " + advertiserId);
        }
        if (!profileApi.isActiveAdvertiser(advertiserId)) {
            log.warn("Campaign creation refused: advertiser {} is not active", advertiserId);
            throw new BusinessRuleException("Advertiser is not active");
        }

        // input errors -> 400
        if (!request.endDate().isAfter(request.startDate())) {
            throw new ValidationException("Campaign end date must be after start date");
        }
        if (request.payoutModel() == PayoutModel.CPA_PERCENT
                && request.payoutValue().compareTo(HUNDRED) > 0) {
            throw new ValidationException("Percentage payout must be between 0 and 100");
        }

        Campaign campaign = campaignMapper.toEntity(request);
        offre.addCampaign(campaign);               // keeps both sides of the relation in sync

        Campaign saved = campaignRepository.save(campaign);
        log.info("Campaign {} created for offer {}", saved.getId(), offreId);
        return campaignMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public Page<CampaignResponseDTO> getCampaigns(CampaignStatus status, Pageable pageable) {
        Page<Campaign> page = (status == null)
                ? campaignRepository.findAll(pageable)
                : campaignRepository.findByStatus(status, pageable);
        return page.map(campaignMapper::toResponse);
    }

    /**
     * Atomic: if activating fails, nothing (including the pause of the previous
     * active campaign) is committed.
     */
    @Transactional
    public void updateCampaignStatus(Long campaignId, CampaignStatus newStatus) {
        Campaign campaign = campaignRepository.findById(campaignId)
                .orElseThrow(() -> new ResourcesNotFoundException("Campaign not found with id: " + campaignId));

        CampaignStatus previous = campaign.getStatus();

        try {
            switch (newStatus) {
                case ACTIVE -> {
                    campaign.activate(LocalDate.now());   // validates transition + expiry first
                    pauseOtherActiveCampaigns(campaign);
                }
                case PAUSED -> campaign.pause();
                case ENDED -> campaign.end();
                case DRAFT -> throw new IllegalStateException("A campaign cannot return to DRAFT");
            }
        } catch (IllegalStateException e) {
            log.warn("Transition refused for campaign {}: {} -> {} ({})",
                    campaignId, previous, newStatus, e.getMessage());
            throw new BusinessRuleException(e.getMessage());
        }

        campaignRepository.save(campaign);
        log.info("Campaign {} status changed: {} -> {}", campaignId, previous, newStatus);
    }

    private void pauseOtherActiveCampaigns(Campaign activated) {
        campaignRepository
                .findByOffre_IdAndStatus(activated.getOffre().getId(), CampaignStatus.ACTIVE)
                .stream()
                .filter(other -> !other.getId().equals(activated.getId()))
                .forEach(other -> {
                    other.pause();
                    log.info("Campaign {} paused: campaign {} activated on the same offer",
                            other.getId(), activated.getId());
                });
    }
}
