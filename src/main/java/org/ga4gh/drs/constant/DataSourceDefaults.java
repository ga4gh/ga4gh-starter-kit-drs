package org.ga4gh.drs.constant;

import java.util.Arrays;
import java.util.List;

import org.ga4gh.drs.configuration.DataSource;
import org.ga4gh.drs.model.AccessType;

public class DataSourceDefaults {

    public static final List<DataSource> REGISTRY = Arrays.asList(
        new DataSource(
            "^test\\.htsjdk\\.samtools\\.(?<subdir>.+?)\\.(?<filename>.+)\\.(?<ext>.+)$",
            AccessType.FILE,
            "./src/test/resources/data/htsjdk/samtools/{subdir}/{filename}.{ext}"
        ),
        new DataSource(
            "^test\\.htsjdk\\.samtools\\.(?<filename>.+)\\.(?<ext>.+)$",
            AccessType.FILE,
            "./src/test/resources/data/htsjdk/samtools/{filename}.{ext}"
        ),
        new DataSource(
            "^test.phenopackets.(?<lastName>.+?)\\.(?<patientNumber>\\d)$",
            AccessType.FILE,
            "./src/test/resources/data/phenopackets/{lastName}/{lastName}-Patient-{patientNumber}.json"
        ),
        new DataSource(
            "^test.phenopackets.(?<lastName>.+?)$",
            AccessType.FILE,
            "./src/test/resources/data/phenopackets/{lastName}"
        )
    );
}
