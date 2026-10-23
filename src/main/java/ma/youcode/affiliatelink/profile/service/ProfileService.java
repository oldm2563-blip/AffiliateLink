package ma.youcode.affiliatelink.profile.service;

import lombok.AllArgsConstructor;
import ma.youcode.affiliatelink.common.exceptions.ResourcesNotFoundException;
import ma.youcode.affiliatelink.profile.dto.request.UpdateProfileStatusRequestDTO;
import ma.youcode.affiliatelink.profile.dto.response.ProfileStatusResponseDTO;
import ma.youcode.affiliatelink.profile.entities.Profile;
import ma.youcode.affiliatelink.profile.repository.ProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;

    @Transactional
    public ProfileStatusResponseDTO updateStatus(Long id, UpdateProfileStatusRequestDTO dto) {
        Profile profile = profileRepository.findById(id)
                .orElseThrow(() -> new ResourcesNotFoundException("Profile not found with id: " + id));

        profile.setStatus(dto.status());

        return ProfileStatusResponseDTO.builder()
                .id(profile.getId())
                .email(profile.getEmail())
                .status(profile.getStatus())
                .createdAt(profile.getCreatedAt())
                .build();
    }
}