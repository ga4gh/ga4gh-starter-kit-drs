package org.ga4gh.drs.utils;

import org.ga4gh.drs.AppConfigConstants;
import org.ga4gh.drs.configuration.DataSource;
import org.ga4gh.drs.configuration.DrsConfigContainer;
import org.ga4gh.drs.model.AccessType;
import org.ga4gh.drs.utils.objectloader.DrsObjectLoader;
import org.ga4gh.drs.utils.objectloader.DrsObjectLoaderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataSourceLookup {

    @Autowired
    @Qualifier(AppConfigConstants.MERGED_DRS_CONFIG_CONTAINER)
    DrsConfigContainer drsConfigContainer;

    @Autowired
    DrsObjectLoaderFactory drsObjectLoaderFactory;

    List<DataSource> dataSources;

    private static final Pattern pathTemplateSlotPattern = Pattern.compile("\\{[a-zA-Z][a-zA-Z0-9]*}");
    public DataSourceLookup() {

    }

    @PostConstruct
    private void postConstruct() {
        dataSources = drsConfigContainer.getDrsConfig().getDataSourceRegistry().getDataSources();
    }

    public DrsObjectLoader getDrsObjectLoaderFromId(String objectId) {
        DataSource source = findDataSourceMatch(objectId);

        if (source == null) {
            return null;
        }

        String objectPath = renderObjectPath(source, objectId);
        return newDrsObjectLoader(source, objectPath);
    }

    private DataSource findDataSourceMatch(String objectId) {
        for (DataSource source : dataSources) {
            if (source.getDrsIdPattern().matcher(objectId).find()) {
                return source;
            }
        }
        return null;
    }

    @SuppressWarnings("unused")
    private String renderObjectPath(DataSource source, String objectId) {
        Pattern drsIdPattern = source.getDrsIdPattern();

        Matcher drsIdPatternMatcher = drsIdPattern.matcher(objectId);

        // Find cannot fail because the provided source was selected as its pattern matched
        drsIdPatternMatcher.find();

        String objectPath = source.getObjectPathTemplate();

        // render the object path, ie. by substituting capture group names with specified values
        Matcher replacementMatcher = pathTemplateSlotPattern.matcher(objectPath);
        return replacementMatcher.replaceAll(result -> {
            // Get name of group with surrounding {}
            String matched = result.group();
            // Trim {}
            String subseq = matched.substring(1, matched.length() - 1);
            return drsIdPatternMatcher.group(subseq);
        });
    }

    private DrsObjectLoader newDrsObjectLoader(DataSource source, String objectPath) {
        AccessType accessType = source.getProtocol();
        DrsObjectLoader drsObjectLoader = drsObjectLoaderFactory.createDrsObjectLoader(accessType);
        drsObjectLoader.setObjectPath(objectPath);
        return drsObjectLoader;
    }
}
