package org.ga4gh.drs.configuration;

public class DataSource {

    private String pattern;
    private String path;

    public DataSource() {

    }

    public DataSource(String pattern, String path) {
        this.pattern = pattern;
        this.path = path;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
