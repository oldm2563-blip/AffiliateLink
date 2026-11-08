package ma.youcode.affiliatelink.offre.repository;

import ma.youcode.affiliatelink.offre.entities.Campaign;
import ma.youcode.affiliatelink.offre.enums.CampaignStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CampaignRepository extends JpaRepository<Campaign, Long> {

    Page<Campaign> findByStatus(CampaignStatus status, Pageable pageable);

    List<Campaign> findByOffre_IdAndStatus(Long offreId, CampaignStatus status);

    List<Campaign> findByOffre_AdvertiserIdAndStatus(Long advertiserId, CampaignStatus status);
}
