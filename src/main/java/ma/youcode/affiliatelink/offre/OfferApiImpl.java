package ma.youcode.affiliatelink.offre;

import lombok.RequiredArgsConstructor;
import ma.youcode.affiliatelink.offre.dto.CampaignMapper;
import ma.youcode.affiliatelink.offre.repository.CampaignRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
class OfferApiImpl implements OfferApi {

    private final CampaignRepository campaignRepository;
    private final CampaignMapper campaignMapper;

    @Override
    @Transactional(readOnly = true)
    public Optional<CampaignInfo> getCampaign(Long campaignId) {
        return campaignRepository.findById(campaignId).map(campaignMapper::toInfo);
    }
}
