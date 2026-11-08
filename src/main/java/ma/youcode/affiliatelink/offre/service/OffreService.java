
package ma.youcode.affiliatelink.offre.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.youcode.affiliatelink.common.exceptions.BusinessRuleException;
import ma.youcode.affiliatelink.common.exceptions.ResourcesNotFoundException;
import ma.youcode.affiliatelink.offre.dto.CreateOffreRequestDTO;
import ma.youcode.affiliatelink.offre.dto.OffreMapper;
import ma.youcode.affiliatelink.offre.dto.OffreResponseDTO;
import ma.youcode.affiliatelink.offre.entities.Offre;
import ma.youcode.affiliatelink.offre.entities.Tag;
import ma.youcode.affiliatelink.offre.enums.Niche;
import ma.youcode.affiliatelink.offre.repository.OffreRepository;
import ma.youcode.affiliatelink.offre.repository.TagRepository;
import ma.youcode.affiliatelink.profile.ProfileApi;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OffreService {

    private final OffreRepository offreRepository;
    private final TagRepository tagRepository;
    private final ProfileApi profileApi;
    private final OffreMapper offreMapper;

    @Transactional
    public OffreResponseDTO createOffre(
            Long advertiserId,
            CreateOffreRequestDTO request) {

        if (!profileApi.advertiserExists(advertiserId)) {
            throw new ResourcesNotFoundException(
                    "Advertiser not found with id: " + advertiserId);          // 404
        }
        if (!profileApi.isActiveAdvertiser(advertiserId)) {
            log.warn("Offer creation refused: advertiser {} is not active", advertiserId);
            throw new BusinessRuleException("Advertiser is not active");       // 409
        }

        Offre offre = new Offre();
        offre.setAdvertiserId(advertiserId);
        offre.setTitle(request.title().trim());
        offre.setDescription(request.description().trim());
        offre.setLandingUrl(request.landingUrl().trim());
        offre.setNiche(request.niche());

        Set<Tag> tags = request.tags() == null
                ? Set.of()
                : request.tags().stream()
                .map(String::trim)
                .map(tag -> tag.toLowerCase(Locale.ROOT))
                .filter(tag -> !tag.isEmpty())
                .distinct()
                .map(this::findOrCreateTag)
                .collect(Collectors.toSet());

        offre.setTags(tags);

        Offre saved = offreRepository.save(offre);
        log.info("Offer {} created by advertiser {}", saved.getId(), advertiserId);
        return offreMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public OffreResponseDTO getOffreById(Long id) {
        Offre offre = offreRepository.findById(id)
                .orElseThrow(() -> new ResourcesNotFoundException(
                        "Offer not found with id: " + id));

        return offreMapper.toDetailResponse(offre);   // includes campaigns
    }

    @Transactional
    public OffreResponseDTO updateOffre(
            Long id,
            Long advertiserId,
            CreateOffreRequestDTO request) {

        Offre offre = offreRepository.findById(id)
                .orElseThrow(() -> new ResourcesNotFoundException(
                        "Offer not found with id: " + id));

        if (!offre.getAdvertiserId().equals(advertiserId)) {
            log.warn("Offer {} update refused: advertiser {} is not the owner", id, advertiserId);
            throw new BusinessRuleException(
                    "You can only update your own offers.");
        }

        offre.setTitle(request.title().trim());
        offre.setDescription(request.description().trim());
        offre.setLandingUrl(request.landingUrl().trim());
        offre.setNiche(request.niche());

        Set<Tag> tags = new HashSet<>();

        if (request.tags() != null) {
            Set<String> names = new HashSet<>();

            for (String name : request.tags()) {
                name = name.trim().toLowerCase(Locale.ROOT);

                if (!name.isEmpty() && names.add(name)) {
                    tags.add(findOrCreateTag(name));
                }
            }
        }

        offre.setTags(tags);

        Offre saved = offreRepository.save(offre);
        log.info("Offer {} updated", saved.getId());
        return offreMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public Page<OffreResponseDTO> searchOffres(
            Niche niche,
            Long advertiserId,
            String tag,
            String keyword,
            Pageable pageable) {

        log.debug("Searching offers niche={} advertiserId={} tag={} keyword={}",
                niche, advertiserId, tag, keyword);
        return offreRepository
                .searchOffres(niche, advertiserId, tag, keyword, pageable)
                .map(offreMapper::toResponse);
    }


    private Tag findOrCreateTag(String name) {
        return tagRepository.findByNameIgnoreCase(name)
                .orElseGet(() -> {
                    Tag tag = new Tag();
                    tag.setName(name);
                    return tagRepository.save(tag);
                });
    }
}
