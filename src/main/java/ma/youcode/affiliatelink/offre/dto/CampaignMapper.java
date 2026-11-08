package ma.youcode.affiliatelink.offre.dto;

import ma.youcode.affiliatelink.offre.CampaignInfo;
import ma.youcode.affiliatelink.offre.entities.Campaign;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CampaignMapper {

    @Mapping(target = "offreId", source = "offre.id")
    CampaignResponseDTO toResponse(Campaign campaign);

    @Mapping(target = "offreId", source = "offre.id")
    @Mapping(target = "advertiserId", source = "offre.advertiserId")
    CampaignInfo toInfo(Campaign campaign);

    // status has no setter (the entity owns it) and defaults to DRAFT
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "offre", ignore = true)
    @Mapping(target = "name", expression = "java(request.name().trim())")
    Campaign toEntity(CreateCampaignRequestDTO request);
}
