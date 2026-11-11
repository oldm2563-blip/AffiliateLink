package ma.youcode.affiliatelink.offre.entities;

import ma.youcode.affiliatelink.offre.enums.CampaignStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CampaignTest {

    Campaign campaign = new Campaign();
    LocalDate today = LocalDate.of(2026, 11, 10);


    @Test
    void statusTest(){
        campaign.setEndDate(today.plusDays(30));
        campaign.activate(today);

        assertEquals(CampaignStatus.ACTIVE, campaign.getStatus());
    }
}
