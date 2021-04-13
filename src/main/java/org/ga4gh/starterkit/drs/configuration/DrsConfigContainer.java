package org.ga4gh.starterkit.drs.configuration;

public class DrsConfigContainer {

    private DrsConfig drsConfig;

    public DrsConfigContainer() {
        drsConfig = new DrsConfig();
    }

    public DrsConfigContainer(DrsConfig drsConfig) {
        this.drsConfig = drsConfig;
    }

    public void setDrsConfig(DrsConfig drsConfig) {
        this.drsConfig = drsConfig;
    }

    public DrsConfig getDrsConfig() {
        return drsConfig;
    }
}
