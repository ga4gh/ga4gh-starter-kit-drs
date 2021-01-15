package org.ga4gh.drs.constant;

import java.util.Arrays;
import java.util.List;

import org.ga4gh.drs.configuration.DataSource;

public class DataSourceDefaults {

    public static final List<DataSource> REGISTRY = Arrays.asList(
        new DataSource("test.id", "./src/test/resources/data")
    );
    
}
