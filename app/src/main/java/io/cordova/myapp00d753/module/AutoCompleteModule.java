package io.cordova.myapp00d753.module;

public class AutoCompleteModule {
    String clientid,clientName;

    public AutoCompleteModule(String clientid, String clientName) {
        this.clientid = clientid;
        this.clientName = clientName;
    }

    public String getClientid() {
        return clientid;
    }

    public void setClientid(String clientid) {
        this.clientid = clientid;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }
}
