package org.ga4gh.starterkit.drs.app;

/**
 * Top-level configuration container object for standalone deployments. To
 * be deserialized/loaded as part of a YAML config file specified on the command
 * line.
 */
public class DrsServerYamlConfigContainer {

    /**
     * Nested configuration object
     */
    private DrsServerYamlConfig drs;

    /**
     * Instantiates a new DrsStandaloneYamlConfigContainer object with default properties
     */
    public DrsServerYamlConfigContainer() {
        drs = new DrsServerYamlConfig();
    }

    /**
     * Instantiates a new DrsStandaloneYamlConfigContainer with a preconfigured DrsStandaloneYamlConfig object
     * @param drs preconfigured DrsStandaloneYamlConfig object
     */
    public DrsServerYamlConfigContainer(DrsServerYamlConfig drs) {
        this.drs = drs;
    }

    /**
     * Assign drs
     * @param drs DrsStandaloneYamlConfig object
     */
    public void setDrs(DrsServerYamlConfig drs) {
        this.drs = drs;
    }

    /**
     * Retrieve drs
     * @return DrsStandaloneYamlConfig object
     */
    public DrsServerYamlConfig getDrs() {
        return drs;
    }
}
