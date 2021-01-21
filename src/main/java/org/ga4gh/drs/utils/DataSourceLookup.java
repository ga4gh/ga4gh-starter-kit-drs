package org.ga4gh.drs.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import org.ga4gh.drs.AppConfigConstants;
import org.ga4gh.drs.configuration.DataSource;
import org.ga4gh.drs.configuration.DrsConfigContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class DataSourceLookup {

    @Autowired
    @Qualifier(AppConfigConstants.MERGED_DRS_CONFIG_CONTAINER)
    DrsConfigContainer drsConfigContainer;

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

    public int findMatch(String id) {
        int match = -1; // -1 indicates no match
        for (int i = 0; i < dataSources.size(); i++) {
            System.out.println("pattern " + i);
            Pattern pattern = patterns.get(i);
            System.out.println(pattern.toString());
            Matcher matcher = pattern.matcher(id);
            if (matcher.find()) {
                match = i;
                break;
            }
        }
        return match;
    }

    public void renderPath(int match) {
        Pattern pattern = patterns.get(match);
        DataSource dataSource = dataSources.get(match);


    }
}
