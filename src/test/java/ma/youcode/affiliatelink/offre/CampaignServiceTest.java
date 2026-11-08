package ma.youcode.affiliatelink.offre;

import ma.youcode.affiliatelink.common.exceptions.BusinessRuleException;
import ma.youcode.affiliatelink.offre.dto.CampaignMapper;
import ma.youcode.affiliatelink.offre.entities.Campaign;
import ma.youcode.affiliatelink.offre.entities.Offre;
import ma.youcode.affiliatelink.offre.enums.CampaignStatus;
import ma.youcode.affiliatelink.offre.repository.CampaignRepository;
import ma.youcode.affiliatelink.offre.repository.OffreRepository;
import ma.youcode.affiliatelink.offre.service.CampaignService;
import ma.youcode.affiliatelink.profile.ProfileApi;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CampaignServiceTest {

    @Mock CampaignRepository campaignRepository;
    @Mock OffreRepository offreRepository;
    @Mock ProfileApi profileApi;
    @Mock CampaignMapper campaignMapper;
    @InjectMocks CampaignService service;

    private Campaign campaign(long id, Offre offre, CampaignStatus target) {
        Campaign c = new Campaign();
        c.setId(id);
        c.setOffre(offre);
        c.setStartDate(LocalDate.now().minusDays(1));
        c.setEndDate(LocalDate.now().plusDays(30));
        if (target == CampaignStatus.ACTIVE) c.activate(LocalDate.now());
        return c;
    }

    @Test
    void activatingPausesTheCampaignAlreadyActiveOnTheSameOffer() {
        Offre offre = new Offre();
        offre.setId(1L);
        Campaign old = campaign(10, offre, CampaignStatus.ACTIVE);
        Campaign fresh = campaign(11, offre, CampaignStatus.DRAFT);

        when(campaignRepository.findById(11L)).thenReturn(Optional.of(fresh));
        when(campaignRepository.findByOffre_IdAndStatus(1L, CampaignStatus.ACTIVE))
                .thenReturn(List.of(old, fresh));

        service.updateCampaignStatus(11L, CampaignStatus.ACTIVE);

        assertThat(fresh.getStatus()).isEqualTo(CampaignStatus.ACTIVE);
        assertThat(old.getStatus()).isEqualTo(CampaignStatus.PAUSED);
    }

    @Test
    void refusedTransitionBecomes409AndPausesNothing() {
        Offre offre = new Offre();
        offre.setId(1L);
        Campaign draft = campaign(11, offre, CampaignStatus.DRAFT);
        when(campaignRepository.findById(11L)).thenReturn(Optional.of(draft));

        assertThatThrownBy(() -> service.updateCampaignStatus(11L, CampaignStatus.PAUSED))
                .isInstanceOf(BusinessRuleException.class);
    }
}
