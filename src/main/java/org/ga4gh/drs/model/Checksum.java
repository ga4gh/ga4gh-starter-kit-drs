package org.ga4gh.drs.model;

import org.springframework.lang.NonNull;

public class Checksum {
    @NonNull
    private String checksum;

    @NonNull
    private String type;

    public Checksum() {
        
    }

    public Checksum(String checksum, String type) {
        this.checksum = checksum;
        this.type = type;
    }

    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
