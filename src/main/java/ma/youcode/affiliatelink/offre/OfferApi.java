package ma.youcode.affiliatelink.offre;

import java.util.Optional;

/** Public API of the offre module (will be used by the future affiliate module). */
public interface OfferApi {

    Optional<CampaignInfo> getCampaign(Long campaignId);
}

