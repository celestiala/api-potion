package com.celestiala.apipotion.core.version;

public class BuildInfo {
    private String title;
    private String version;
    private String vendor;
    private String buildJDK;
    private String buildBy;
    private String gitShaHash;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getBuildJDK() {
        return buildJDK;
    }

    public void setBuildJDK(String buildJDK) {
        this.buildJDK = buildJDK;
    }

    public String getBuildBy() {
        return buildBy;
    }

    public void setBuildBy(String buildBy) {
        this.buildBy = buildBy;
    }

    public String getGitShaHash() {
        return gitShaHash;
    }

    public void setGitShaHash(String gitShaHash) {
        this.gitShaHash = gitShaHash;
    }
}
