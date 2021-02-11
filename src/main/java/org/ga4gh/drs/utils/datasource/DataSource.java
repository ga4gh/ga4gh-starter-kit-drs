package org.ga4gh.drs.utils.datasource;

import org.ga4gh.drs.utils.objectloader.DrsObjectLoader;

public interface DataSource<T extends DrsObjectLoader> {

    public boolean objectIdMatches(String objectId);
}
