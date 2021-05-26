package org.ga4gh.starterkit.drs.app;

/**
 * Top-level configuration container object for standalone deployments. To
 * be deserialized/loaded as part of a YAML config file specified on the command
 * line.
 */
public class DrsStandaloneYamlConfigContainer {

    /**
     * Nested configuration object
     */
    private DrsStandaloneYamlConfig drs;

    /**
     * Instantiates a new DrsStandaloneYamlConfigContainer object with default properties
     */
    public DrsStandaloneYamlConfigContainer() {
        drs = new DrsStandaloneYamlConfig();
    }

    /**
     * Instantiates a new DrsStandaloneYamlConfigContainer with a preconfigured DrsStandaloneYamlConfig object
     * @param drs preconfigured DrsStandaloneYamlConfig object
     */
    public DrsStandaloneYamlConfigContainer(DrsStandaloneYamlConfig drs) {
        this.drs = drs;
    }

    /**
     * Assign drs
     * @param drs DrsStandaloneYamlConfig object
     */
    public void setDrs(DrsStandaloneYamlConfig drs) {
        this.drs = drs;
    }

    /**
     * Retrieve drs
     * @return DrsStandaloneYamlConfig object
     */
    public DrsStandaloneYamlConfig getDrs() {
        return drs;
    }
}
