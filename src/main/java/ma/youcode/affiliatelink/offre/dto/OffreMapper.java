package ma.youcode.affiliatelink.offre.dto;

import ma.youcode.affiliatelink.offre.entities.Offre;
import ma.youcode.affiliatelink.offre.entities.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = CampaignMapper.class)
public interface OffreMapper {

    /** Search / create / update: campaigns are not loaded. */
    @Mapping(target = "campaigns", ignore = true)
    OffreResponseDTO toResponse(Offre offre);

    /** GET /api/offres/{id}: the offer with its campaigns. */
    OffreResponseDTO toDetailResponse(Offre offre);

    default Set<String> mapTags(Set<Tag> tags) {
        return tags.stream().map(Tag::getName).collect(Collectors.toSet());
    }
}
