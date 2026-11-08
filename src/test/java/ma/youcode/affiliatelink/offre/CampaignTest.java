package ma.youcode.affiliatelink.offre;

import ma.youcode.affiliatelink.offre.entities.Campaign;
import ma.youcode.affiliatelink.offre.enums.CampaignStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CampaignTest {

    private Campaign campaign(LocalDate end) {
        Campaign c = new Campaign();
        c.setStartDate(LocalDate.now().minusDays(10));
        c.setEndDate(end);
        return c;
    }

    @Test
    void newCampaignIsDraft() {
        assertThat(campaign(LocalDate.now().plusDays(5)).getStatus()).isEqualTo(CampaignStatus.DRAFT);
    }

    @Test
    void draftCanBeActivatedThenPausedThenReactivatedThenEnded() {
        Campaign c = campaign(LocalDate.now().plusDays(5));
        c.activate(LocalDate.now());
        c.pause();
        c.activate(LocalDate.now());
        c.end();
        assertThat(c.getStatus()).isEqualTo(CampaignStatus.ENDED);
    }

    @Test
    void draftCannotBePausedOrEnded() {
        Campaign c = campaign(LocalDate.now().plusDays(5));
        assertThatThrownBy(c::pause).isInstanceOf(IllegalStateException.class);
        assertThatThrownBy(c::end).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void endedCampaignHasNoTransition() {
        Campaign c = campaign(LocalDate.now().plusDays(5));
        c.activate(LocalDate.now());
        c.end();
        assertThatThrownBy(() -> c.activate(LocalDate.now())).isInstanceOf(IllegalStateException.class);
        assertThatThrownBy(c::pause).isInstanceOf(IllegalStateException.class);
        assertThatThrownBy(c::end).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void expiredCampaignCannotBeActivated() {
        Campaign c = campaign(LocalDate.now().minusDays(1));
        assertThatThrownBy(() -> c.activate(LocalDate.now()))
                .isInstanceOf(IllegalStateException.class);
    }
}
