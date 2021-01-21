package org.ga4gh.drs.constant;

import java.util.Arrays;
import java.util.List;

import org.ga4gh.drs.configuration.DataSource;
import org.ga4gh.drs.model.AccessType;

public class DataSourceDefaults {

    public static final List<DataSource> REGISTRY = Arrays.asList(
        new DataSource(
            "test.id",
            AccessType.FILE,
            "./src/test/resources/data"
        )
    );
    
}
