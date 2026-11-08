
package ma.youcode.affiliatelink.offre.repository;

import ma.youcode.affiliatelink.offre.entities.Offre;
import ma.youcode.affiliatelink.offre.enums.Niche;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OffreRepository extends JpaRepository<Offre, Long> {

    @Query("""
        SELECT DISTINCT o FROM Offre o
        LEFT JOIN o.tags t
        WHERE (:niche IS NULL OR o.niche = :niche)
          AND (:advertiserId IS NULL OR o.advertiserId = :advertiserId)
          AND (:tag IS NULL OR LOWER(t.name) = LOWER(:tag))
          AND (:keyword IS NULL OR LOWER(o.title) LIKE LOWER(CONCAT('%', :keyword, '%')))
        """)
    Page<Offre> searchOffres(
            @Param("niche") Niche niche,
            @Param("advertiserId") Long advertiserId,
            @Param("tag") String tag,
            @Param("keyword") String keyword,
            Pageable pageable);
}
