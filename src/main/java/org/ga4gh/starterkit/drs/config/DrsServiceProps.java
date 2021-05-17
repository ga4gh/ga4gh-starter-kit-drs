package org.ga4gh.starterkit.drs.config;

public class DrsServiceProps {

    private boolean serveFileURLForFileObjects;
    private boolean serveStreamURLForFileObjects;

    public DrsServiceProps() {
        setAllDefaults();
    }

    public void setServeFileURLForFileObjects(boolean serveFileURLForFileObjects) {
        this.serveFileURLForFileObjects = serveFileURLForFileObjects;
    }

    public boolean getServeFileURLForFileObjects() {
        return serveFileURLForFileObjects;
    }

    public void setServeStreamURLForFileObjects(boolean serveStreamURLForFileObjects) {
        this.serveStreamURLForFileObjects = serveStreamURLForFileObjects;
    }

    public boolean getServeStreamURLForFileObjects() {
        return serveStreamURLForFileObjects;
    }

    private void setAllDefaults() {
        serveFileURLForFileObjects = false;
        serveStreamURLForFileObjects = true;
    }
    
}
