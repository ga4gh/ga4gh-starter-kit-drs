package org.ga4gh.starterkit.drs.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.springframework.lang.NonNull;

import java.net.URI;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ContentsObject {
    // Required
    @NonNull
    private String name;

    // Optional
    private List<ContentsObject> contents;

    private List<URI> drsUri;

    private String id;

    public ContentsObject() {
        
    }

    public ContentsObject(String name) {
        this.name = name;
    }

    public List<ContentsObject> getContents() {
        return contents;
    }

    public void setContents(List<ContentsObject> contents) {
        this.contents = contents;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<URI> getDrsUri() {
        return drsUri;
    }

    public void setDrsUri(List<URI> drsUri) {
        this.drsUri = drsUri;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
