package org.ga4gh.starterkit.drs.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.ga4gh.starterkit.drs.utils.SerializeView;
import org.springframework.lang.NonNull;
import java.net.URI;
import java.util.List;

/**
 * Directly from DRS specification, references the nested contents of a DRS
 * bundle 
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonView(SerializeView.Public.class)
public class ContentsObject {

    /**
     * Name of ContentsObject
     */
    @NonNull
    private String name;

    /**
     * Sub-contents of this ContentsObject, indicating that there is further
     * nesting. The objects within 'contents' may also contain further contents
     */
    private List<ContentsObject> contents;

    /**
     * Full DRS URL/URI that will enable the loading of this ContentsObject as
     * a full DRSObject
     */
    private List<URI> drsUri;

    /**
     * DRS id
     */
    private String id;

    /**
     * Instantiates a new ContentsObject
     */
    public ContentsObject() {
        
    }

    /**
     * Instantiates a new ContentsObject with preset name
     * @param name name of ContentsObject
     */
    public ContentsObject(String name) {
        this.name = name;
    }

    /**
     * Retrieve contents
     * @return list of nested contents objects
     */
    public List<ContentsObject> getContents() {
        return contents;
    }

    /**
     * Assign contents
     * @param contents list of nested contents objects
     */
    public void setContents(List<ContentsObject> contents) {
        this.contents = contents;
    }

    /**
     * Retrieve name
     * @return name of contents object
     */
    public String getName() {
        return name;
    }

    /**
     * Assign name
     * @param name name of contents object
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retrieve DRS URI
     * @return DRS URI enabling access to DRSObject outlined by this contents object
     */
    public List<URI> getDrsUri() {
        return drsUri;
    }

    /**
     * Assign DRS URI
     * @param drsUri DRS URI enabling access to DRSObject outlined by this contents object
     */
    public void setDrsUri(List<URI> drsUri) {
        this.drsUri = drsUri;
    }

    /**
     * Retrieve id
     * @return DRS identifier
     */
    public String getId() {
        return id;
    }

    /**
     * Assign id
     * @param id DRS identifier
     */
    public void setId(String id) {
        this.id = id;
    }
}
