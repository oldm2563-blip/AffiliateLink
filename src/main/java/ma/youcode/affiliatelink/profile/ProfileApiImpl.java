package ma.youcode.affiliatelink.profile;

import lombok.RequiredArgsConstructor;
import ma.youcode.affiliatelink.profile.entities.Advertiser;
import ma.youcode.affiliatelink.profile.entities.Publisher;
import ma.youcode.affiliatelink.profile.enums.Status;
import ma.youcode.affiliatelink.profile.repository.AdvertiserRepository;
import ma.youcode.affiliatelink.profile.repository.PublisherRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
class ProfileApiImpl implements ProfileApi {

    private final AdvertiserRepository advertiserRepository;
    private final PublisherRepository publisherRepository;

    @Override
    @Transactional(readOnly = true)
    public boolean advertiserExists(Long advertiserId) {
        return advertiserRepository.existsById(advertiserId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isActiveAdvertiser(Long advertiserId) {
        return advertiserRepository.findById(advertiserId)
                .map(Advertiser::getStatus)
                .filter(Status.ACTIVE::equals)
                .isPresent();
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> findActivePublisherEmails() {
        return publisherRepository.findByStatus(Status.ACTIVE).stream()
                .map(Publisher::getEmail)
                .toList();
    }
}