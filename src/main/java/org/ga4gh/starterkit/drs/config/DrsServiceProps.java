package org.ga4gh.starterkit.drs.config;

/**
 * Configuration properties modifying application behaviour that are unique
 * only to DRS. To be deserialized/loaded from YAML config file
 */
public class DrsServiceProps {

    /**
     * Indicates whether the DRS service should provide file URLs of DRSObject
     * file location back to the client. If true, assumes that client has 
     * access to the same filesystem as the service (e.g. HPC environment).
     * Applies only to 'file' based objects (ie not https, s3, etc.)
     */
    private boolean serveFileURLForFileObjects;

    /**
     * Indicates whether the DRS service should provide the streaming endpoint
     * URL to the client, for files that the server can access but the client
     * cannot. If true, assumes that client cannot access the file by its path.
     * Applies only to 'file' based objects (ie not https, s3, etc.)
     */
    private boolean serveStreamURLForFileObjects;

    /**
     * Instantiates a new DrsServiceProps object with all defaults
     */
    public DrsServiceProps() {
        setAllDefaults();
    }

    /**
     * Assign boolean, indicating whether to serve file paths
     * @param serveFileURLForFileObjects boolean indicator
     */
    public void setServeFileURLForFileObjects(boolean serveFileURLForFileObjects) {
        this.serveFileURLForFileObjects = serveFileURLForFileObjects;
    }

    /**
     * Retrieve boolean indicating whether to serve file paths
     * @return boolean indicator
     */
    public boolean getServeFileURLForFileObjects() {
        return serveFileURLForFileObjects;
    }

    /**
     * Assign boolean, indicating whether to serve files via stream endpoint
     * @param serveStreamURLForFileObjects boolean indicator
     */
    public void setServeStreamURLForFileObjects(boolean serveStreamURLForFileObjects) {
        this.serveStreamURLForFileObjects = serveStreamURLForFileObjects;
    }

    /**
     * Retrieve boolean, indicating whether to serve files via stream endpoint
     * @return boolean indicator
     */
    public boolean getServeStreamURLForFileObjects() {
        return serveStreamURLForFileObjects;
    }

    /**
     * Initialize DRS service props with defaults
     */
    private void setAllDefaults() {
        serveFileURLForFileObjects = true;
        serveStreamURLForFileObjects = false;
    }
}
