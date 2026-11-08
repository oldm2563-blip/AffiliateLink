package ma.youcode.affiliatelink.offre.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PublisherNotifier {

    private final JavaMailSender mailSender;

    public void notifyStopSharing(List<String> publisherEmails) {
        for (String email : publisherEmails) {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("AffiliateLink - advertiser suspended");
            message.setText("An advertiser has been suspended. "
                    + "Please stop sharing its links on your social networks.");
            mailSender.send(message);
        }
    }
}