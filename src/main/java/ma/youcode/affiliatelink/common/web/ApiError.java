package ma.youcode.affiliatelink.common.web;

import java.time.Instant;
import java.util.List;

public record ApiError(
        int status,
        String error,
        String message,
        List<String> details,
        Instant timestamp

) {}
