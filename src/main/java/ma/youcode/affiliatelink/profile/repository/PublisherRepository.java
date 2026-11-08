package ma.youcode.affiliatelink.profile.repository;

import ma.youcode.affiliatelink.profile.entities.Publisher;
import ma.youcode.affiliatelink.profile.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PublisherRepository extends JpaRepository<Publisher, Long> {
    boolean existsByEmail(String email);

    List<Publisher> findByStatus(Status status);
}
