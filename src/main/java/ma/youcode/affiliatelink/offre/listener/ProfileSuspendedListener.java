package ma.youcode.affiliatelink.offre.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.youcode.affiliatelink.offre.entities.Campaign;
import ma.youcode.affiliatelink.offre.enums.CampaignStatus;
import ma.youcode.affiliatelink.offre.notification.PublisherNotifier;
import ma.youcode.affiliatelink.offre.repository.CampaignRepository;
import ma.youcode.affiliatelink.profile.ProfileApi;
import ma.youcode.affiliatelink.profile.event.ProfileSuspended;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Runs AFTER the profile transaction commits, asynchronously and in its own
 * transaction (that is what @ApplicationModuleListener does). The event is kept
 * in the event_publication table until this method completes.
 */
@Slf4j
@Component
@RequiredArgsConstructor
class ProfileSuspendedListener {

    private final CampaignRepository campaignRepository;
    private final ProfileApi profileApi;
    private final PublisherNotifier publisherNotifier;

    @ApplicationModuleListener
    void on(ProfileSuspended event) {
        Long profileId = event.profileId();
        log.info("ProfileSuspended received for profile {}", profileId);

        List<Campaign> active = campaignRepository
                .findByOffre_AdvertiserIdAndStatus(profileId, CampaignStatus.ACTIVE);

        if (active.isEmpty()) {
            // not an advertiser, or no active campaign: nothing to pause, nobody to notify
            log.debug("No active campaign for profile {}", profileId);
            return;
        }

        active.forEach(Campaign::pause);
        campaignRepository.saveAll(active);
        log.info("{} campaign(s) paused for suspended advertiser {}", active.size(), profileId);

        publisherNotifier.notifyStopSharing(profileApi.findActivePublisherEmails());
        log.info("Suspension notification sent for advertiser {}", profileId);
    }
}
