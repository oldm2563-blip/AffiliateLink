package ma.youcode.affiliatelink.profile;

import java.util.List;

public interface ProfileApi {

    boolean advertiserExists(Long advertiserId);

    boolean isActiveAdvertiser(Long advertiserId);

    List<String> findActivePublisherEmails();
}
