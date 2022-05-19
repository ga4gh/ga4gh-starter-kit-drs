package org.ga4gh.starterkit.drs.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
public class BulkResponse {

    private BulkSummary summary;
    private List<DrsObject> resolvedDrsObject;
    private Map<String, Integer> unresolvedDrsObject;

    public BulkResponse() {
        summary = new BulkSummary();
        resolvedDrsObject = new ArrayList<>();
        unresolvedDrsObject = new HashMap<>();
    }
}
