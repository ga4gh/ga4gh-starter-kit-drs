package org.ga4gh.drs.utils.requesthandler;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.ga4gh.drs.AppConfigConstants;
import org.ga4gh.drs.configuration.DrsConfigContainer;
import org.ga4gh.drs.exception.ResourceNotFoundException;
import org.ga4gh.drs.model.AccessMethod;
import org.ga4gh.drs.model.AccessType;
import org.ga4gh.drs.model.ContentsObject;
import org.ga4gh.drs.model.DrsObject;
import org.ga4gh.drs.utils.cache.AccessCache;
import org.ga4gh.drs.utils.cache.AccessCacheItem;
import org.ga4gh.drs.utils.hibernate.HibernateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class ObjectRequestHandler implements RequestHandler<DrsObject> {

    @Autowired
    @Qualifier(AppConfigConstants.MERGED_DRS_CONFIG_CONTAINER)
    DrsConfigContainer drsConfigContainer;

    // @Autowired
    // DataSourceLookup dataSourceLookup;

    @Autowired
    AccessCache accessCache;

    @Autowired
    HibernateUtil hibernateUtil;

    private String objectId;

    private boolean expand;

    public ObjectRequestHandler() {
        
    }

    public DrsObject handleRequest() {
        // Get DrsObject from db
        DrsObject drsObject = (DrsObject) hibernateUtil.loadDrsObject(getObjectId());
        if (drsObject == null) {
            throw new ResourceNotFoundException("no DrsObject found by id: " + getObjectId());
        }

        // post query prep of response
        drsObject.setSelfURI(prepareSelfURI(getObjectId()));
        drsObject.setContents(prepareContents(drsObject));

        return drsObject;
        
        /*
        DrsObjectLoader drsObjectLoader = dataSourceLookup.getDrsObjectLoaderFromId(getObjectId());
        if (drsObjectLoader == null) {
            throw new ResourceNotFoundException("Could not locate data source associated with requested object_id");
        }
        if (!drsObjectLoader.exists()) {
            throw new ResourceNotFoundException("No object found for the provided id");
        }
        DrsObject drsObject = drsObjectLoader.generateDrsObject();

        // register an access id in the cache for each returned AccessMethod with
        // an access id string
        for (AccessMethod accessMethod : drsObject.getAccessMethods()) {
            if (accessMethod.getAccessId() != null) {
                AccessCacheItem item = generateAccessCacheItem(
                    drsObject.getId(),
                    accessMethod.getAccessId(),
                    drsObjectLoader.getObjectPath(),
                    accessMethod.getType(),
                    drsObject.getMimeType()
                );
                accessCache.put(drsObject.getId(), accessMethod.getAccessId(), item);
            }
        }
        
        return drsObject;
        */
    }

    private URI prepareSelfURI(String id) {
        String hostname = drsConfigContainer.getDrsConfig().getServerProps().getHostname();
        return URI.create("drs://" + hostname + "/" + id);
    }

    private List<ContentsObject> prepareContents(DrsObject drsObject) {
        List<ContentsObject> contents = new ArrayList<>();
        for (int i = 0; i < drsObject.getDrsObjectChildren().size(); i++) {
            contents.add(convertDrsObjectToContentsObject(drsObject.getDrsObjectChildren().get(i)));
        }
        return contents;
    }

    private ContentsObject convertDrsObjectToContentsObject(DrsObject drsObject) {
        ContentsObject contentsObject = new ContentsObject();
        contentsObject.setId(drsObject.getId());
        contentsObject.setDrsUri(new ArrayList<URI>(){{
            add(prepareSelfURI(drsObject.getId()));
        }});
        contentsObject.setName(drsObject.getName());

        if (getExpand()) {
            List<ContentsObject> childContents = new ArrayList<>();
            for (int i = 0; i < drsObject.getDrsObjectChildren().size(); i++) {
                childContents.add(convertDrsObjectToContentsObject(drsObject.getDrsObjectChildren().get(i)));
            }
            contentsObject.setContents(childContents);
        }
        
        return contentsObject;
    }

    private AccessCacheItem generateAccessCacheItem(String objectId, String accessId, String objectPath, AccessType accessType, String mimeType) {
        AccessCacheItem item = new AccessCacheItem();
        item.setObjectId(objectId);
        item.setAccessId(accessId);
        item.setObjectPath(objectPath);
        item.setAccessType(accessType);
        item.setMimeType(mimeType);
        return item;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setExpand(boolean expand) {
        this.expand = expand;
    }

    public boolean getExpand() {
        return expand;
    }
}
