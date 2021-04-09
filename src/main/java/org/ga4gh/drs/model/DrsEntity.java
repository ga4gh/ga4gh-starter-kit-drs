package org.ga4gh.drs.model;

public interface DrsEntity {

    public void setId(String id);
    public String getId();
    public void lazyLoad();
    
}
