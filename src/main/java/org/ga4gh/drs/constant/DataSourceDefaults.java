package org.ga4gh.drs.constant;

import java.util.Arrays;
import java.util.List;
import org.ga4gh.drs.utils.datasource.LocalFileDataSource;
import org.ga4gh.drs.utils.datasource.S3DataSource;

public class DataSourceDefaults {

    public static final List<LocalFileDataSource> LOCAL = Arrays.asList(
        new LocalFileDataSource() {{
            setIdPrefix("TestDataHtsjdkSamtools.");
            setRootDir("./src/test/resources/data/htsjdk/samtools/");
        }},
        new LocalFileDataSource() {{
            setIdPrefix("TestDataPhenopackets.");
            setRootDir("./src/test/resources/data/phenopackets/");
        }}
    );

    public static final List<S3DataSource> S3 = Arrays.asList(
        new S3DataSource() {{
            setIdPrefix("S3DemoData.");
            setRootDir("/");
            setRegion("us-east-2");
            setBucket("ga4gh-demo-data");
        }},
        new S3DataSource() {{
            setIdPrefix("TabulaMuris.");
            setRootDir("/");
            setRegion("us-east-1");
            setBucket("czbiohub-tabula-muris");
        }}
    );
}
