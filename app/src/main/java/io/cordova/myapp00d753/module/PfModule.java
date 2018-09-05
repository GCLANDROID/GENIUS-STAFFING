package io.cordova.myapp00d753.module;

public class PfModule {
    String pfYear,pfUrl;

    public PfModule(String pfYear, String pfUrl) {
        this.pfYear = pfYear;
        this.pfUrl = pfUrl;
    }

    public String getPfYear() {
        return pfYear;
    }

    public void setPfYear(String pfYear) {
        this.pfYear = pfYear;
    }

    public String getPfUrl() {
        return pfUrl;
    }

    public void setPfUrl(String pfUrl) {
        this.pfUrl = pfUrl;
    }
}
