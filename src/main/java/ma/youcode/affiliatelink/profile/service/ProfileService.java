package ma.youcode.affiliatelink.profile.service;

import lombok.AllArgsConstructor;
import ma.youcode.affiliatelink.common.exceptions.ResourcesNotFoundException;
import ma.youcode.affiliatelink.profile.dto.request.UpdateProfileStatusRequestDTO;
import ma.youcode.affiliatelink.profile.dto.response.ProfileStatusResponseDTO;
import ma.youcode.affiliatelink.profile.entities.Profile;
import ma.youcode.affiliatelink.profile.enums.Status;
import ma.youcode.affiliatelink.profile.event.ProfileSuspended;
import ma.youcode.affiliatelink.profile.repository.ProfileRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final ApplicationEventPublisher eventPublisher;


    @Transactional
    public ProfileStatusResponseDTO updateStatus(Long id, UpdateProfileStatusRequestDTO dto) {
        Profile profile = profileRepository.findById(id)
                .orElseThrow(() -> new ResourcesNotFoundException("Profile not found with id: " + id));

        boolean becomingSuspended = profile.getStatus() != Status.SUSPENDED
                                    && dto.status() == Status.SUSPENDED;

        profile.setStatus(dto.status());

        if (becomingSuspended){
            eventPublisher.publishEvent(
                    new ProfileSuspended(profile.getId())
            );
        }

        return ProfileStatusResponseDTO.builder()
                .id(profile.getId())
                .email(profile.getEmail())
                .status(profile.getStatus())
                .createdAt(profile.getCreatedAt())
                .build();
    }
}