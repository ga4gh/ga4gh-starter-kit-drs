package org.ga4gh.starterkit.drs.app;

public class DrsStandaloneYamlConfigContainer {

    private DrsStandaloneYamlConfig drs;

    public DrsStandaloneYamlConfigContainer() {
        drs = new DrsStandaloneYamlConfig();
    }

    public DrsStandaloneYamlConfigContainer(DrsStandaloneYamlConfig drs) {
        this.drs = drs;
    }

    public void setDrs(DrsStandaloneYamlConfig drs) {
        this.drs = drs;
    }

    public DrsStandaloneYamlConfig getDrs() {
        return drs;
    }
}
