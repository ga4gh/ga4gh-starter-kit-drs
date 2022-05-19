package org.ga4gh.starterkit.drs.model;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.ga4gh.starterkit.drs.utils.SerializeView;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonView(SerializeView.Public.class)
public class BulkAuthInfoResponse {

    private BulkSummary summary;
    private Map<String, AuthInfo> resolvedDrsObjectAuthInfo;
    private Map<String, Integer> unresolvedDrsObjectAuthInfo;

    public BulkAuthInfoResponse() {
        summary = new BulkSummary();
        resolvedDrsObjectAuthInfo = new HashMap<>();
        unresolvedDrsObjectAuthInfo = new HashMap<>();
    }
}
