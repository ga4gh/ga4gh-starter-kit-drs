package org.ga4gh.drs.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import org.ga4gh.drs.AppConfigConstants;
import org.ga4gh.drs.configuration.DataSource;
import org.ga4gh.drs.configuration.DrsConfigContainer;
import org.ga4gh.drs.model.AccessType;
import org.ga4gh.drs.utils.objectloader.DrsObjectLoader;
import org.ga4gh.drs.utils.objectloader.DrsObjectLoaderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class DataSourceLookup {

    @Autowired
    @Qualifier(AppConfigConstants.MERGED_DRS_CONFIG_CONTAINER)
    DrsConfigContainer drsConfigContainer;

    @Autowired
    DrsObjectLoaderFactory drsObjectLoaderFactory;

    List<DataSource> dataSources;
    List<Pattern> patterns;

    public DataSourceLookup() {
        dataSources = new ArrayList<>();
        patterns = new ArrayList<>();
    }

    @PostConstruct
    private void postConstruct() {
        dataSources = drsConfigContainer.getDrsConfig().getDataSourceRegistry().getDataSources();
        for (DataSource dataSource: dataSources) {
            Pattern pattern = Pattern.compile(dataSource.getDrsIdPattern());
            patterns.add(pattern);
        }
    }

    public DrsObjectLoader getDrsObjectLoaderFromId(String objectId) {
        DrsObjectLoader drsObjectLoader = null;
        int match = findDataSourceMatch(objectId);
        if (match == -1) {
            return drsObjectLoader;
        }

        String objectPath = renderObjectPath(match, objectId);
        return newDrsObjectLoader(match, objectPath);
    }

    private int findDataSourceMatch(String objectId) {
        int match = -1; // -1 indicates no match
        for (int i = 0; i < dataSources.size(); i++) {
            Pattern pattern = patterns.get(i);
            Matcher matcher = pattern.matcher(objectId);
            if (matcher.find()) {
                match = i;
                break;
            }
        }
        return match;
    }

    private String renderObjectPath(int match, String objectId) {
        DataSource dataSource = dataSources.get(match);
        String drsIdPatternString = dataSource.getDrsIdPattern();

        // parse out the regex capture group names (ie. keys)
        Pattern captureGroupNamePattern = Pattern.compile("\\(\\?<([a-zA-Z][a-zA-Z0-9]*)>");
        Matcher captureGroupNameMatcher = captureGroupNamePattern.matcher(drsIdPatternString);
        List<String> captureGroupNames = new ArrayList<>();

        while (captureGroupNameMatcher.find()) {
            String captureGroupName = captureGroupNameMatcher.group(1);
            captureGroupNames.add(captureGroupName);
        }

        // parse out the regex capture group values
        Pattern drsIdPattern = patterns.get(match);
        Matcher drsIdPatternMatcher = drsIdPattern.matcher(objectId);
        drsIdPatternMatcher.find();
        HashMap<String, String> regexMap = new HashMap<>();
        for (String captureGroupName : captureGroupNames) {
            String value = drsIdPatternMatcher.group(captureGroupName);
            regexMap.put(captureGroupName, value);
        }
        
        // render the object path, ie. by substituting capture group names with specified values
        String objectPath = dataSource.getObjectPathTemplate();
        for (String captureGroupName : captureGroupNames) {
            String toReplace = "{" + captureGroupName + "}";
            String replacement = regexMap.get(captureGroupName);
            objectPath = objectPath.replace(toReplace, replacement);
        }
        return objectPath;
    }

    private DrsObjectLoader newDrsObjectLoader(int match, String objectPath) {
        AccessType accessType = dataSources.get(match).getProtocol();
        DrsObjectLoader drsObjectLoader = drsObjectLoaderFactory.createDrsObjectLoader(accessType);
        drsObjectLoader.setObjectPath(objectPath);
        return drsObjectLoader;
    }
}
