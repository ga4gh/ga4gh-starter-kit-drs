package org.ga4gh.starterkit.drs.utils.requesthandler;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.ga4gh.starterkit.drs.AppConfigConstants;
import org.ga4gh.starterkit.drs.configuration.DrsConfigContainer;
import org.ga4gh.starterkit.common.exception.ResourceNotFoundException;
import org.ga4gh.starterkit.drs.model.AccessMethod;
import org.ga4gh.starterkit.drs.model.AccessType;
import org.ga4gh.starterkit.drs.model.AccessURL;
import org.ga4gh.starterkit.drs.model.AwsS3AccessObject;
import org.ga4gh.starterkit.drs.model.ContentsObject;
import org.ga4gh.starterkit.drs.model.DrsObject;
import org.ga4gh.starterkit.drs.model.FileAccessObject;
import org.ga4gh.starterkit.drs.utils.cache.AccessCache;
import org.ga4gh.starterkit.drs.utils.cache.AccessCacheItem;
import org.ga4gh.starterkit.drs.utils.hibernate.DrsHibernateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class ObjectRequestHandler implements RequestHandler<DrsObject> {

    @Autowired
    @Qualifier(AppConfigConstants.FINAL_DRS_CONFIG_CONTAINER)
    DrsConfigContainer drsConfigContainer;

    @Autowired
    AccessCache accessCache;

    @Autowired
    DrsHibernateUtil hibernateUtil;

    private String objectId;

    private boolean expand;

    /* Constructors */

    public ObjectRequestHandler() {
        
    }

    /* Custom API methods */

    public DrsObject handleRequest() {

        // Get DrsObject from db
        DrsObject drsObject = (DrsObject) hibernateUtil.loadDrsObject(getObjectId(), true);
        if (drsObject == null) {
            throw new ResourceNotFoundException("no DrsObject found by id: " + getObjectId());
        }

        // post query prep of response
        drsObject.setSelfURI(prepareSelfURI(getObjectId()));
        drsObject.setContents(prepareContents(drsObject));
        drsObject.setAccessMethods(prepareAccessMethods(drsObject));
        return drsObject;
    }

    private URI prepareSelfURI(String id) {
        String hostname = drsConfigContainer.getDrsConfig().getServerProps().getHostname();
        return URI.create("drs://" + hostname + "/" + id);
    }

    private List<ContentsObject> prepareContents(DrsObject drsObject) {
        List<ContentsObject> contents = new ArrayList<>();
        for (int i = 0; i < drsObject.getDrsObjectChildren().size(); i++) {
            contents.add(createContentsObject(drsObject.getDrsObjectChildren().get(i)));
        }
        return contents;
    }

    private ContentsObject createContentsObject(DrsObject drsObject) {
        ContentsObject contentsObject = new ContentsObject();
        contentsObject.setId(drsObject.getId());
        contentsObject.setDrsUri(new ArrayList<URI>(){{
            add(prepareSelfURI(drsObject.getId()));
        }});
        contentsObject.setName(drsObject.getName());

        if (getExpand()) {
            List<ContentsObject> childContents = new ArrayList<>();
            for (int i = 0; i < drsObject.getDrsObjectChildren().size(); i++) {
                childContents.add(createContentsObject(drsObject.getDrsObjectChildren().get(i)));
            }
            contentsObject.setContents(childContents);
        }
        
        return contentsObject;
    }

    private List<AccessMethod> prepareAccessMethods(DrsObject drsObject) {

        List<AccessMethod> accessMethods = new ArrayList<>();

        for (FileAccessObject fileAccessObject : drsObject.getFileAccessObjects()) {
            accessMethods.add(createAccessMethod(fileAccessObject));

        }

        for (AwsS3AccessObject awsS3AccessObject : drsObject.getAwsS3AccessObjects()) {
            accessMethods.add(createAccessMethod(awsS3AccessObject));
        }
        
        return accessMethods;
    }

    private AccessMethod createAccessMethod(FileAccessObject fileAccessObject) {
        AccessMethod accessMethod = new AccessMethod();
        accessMethod.setType(AccessType.https);

        String accessID = UUID.randomUUID().toString();
        AccessCacheItem accessCacheItem = generateAccessCacheItem(
            fileAccessObject.getDrsObject().getId(),
            accessMethod.getAccessId(),
            fileAccessObject.getPath(),
            accessMethod.getType(),
            fileAccessObject.getDrsObject().getMimeType());
        accessCache.put(fileAccessObject.getDrsObject().getId(), accessID, accessCacheItem);

        accessMethod.setAccessId(accessID);
        return accessMethod;
    }

    private AccessMethod createAccessMethod(AwsS3AccessObject awsS3AccessObject) {
        AccessMethod accessMethod = new AccessMethod();
        accessMethod.setType(AccessType.s3);
        accessMethod.setRegion(awsS3AccessObject.getRegion());

        AccessURL accessURL = new AccessURL(URI.create(
            "s3://" + awsS3AccessObject.getBucket() 
            + awsS3AccessObject.getKey()));
        accessMethod.setAccessUrl(accessURL);
        return accessMethod;

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

    /* Setters and Getters */

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
