package io.cordova.myapp00d753.module;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NeedToActModel {
    @JsonProperty("LetterID")
    public int letterID;
    @JsonProperty("MasterID")
    public String masterID;
    @JsonProperty("Domain")
    public String domain;
    @JsonProperty("DocName")
    public String docName;
    @JsonProperty("Category")
    public String category;
    @JsonProperty("ExpDate")
    public String expDate;
    @JsonProperty("AcceptanceType")
    public int acceptanceType;
    @JsonProperty("IsMandatoryPopup")
    public int isMandatoryPopup;
    @JsonProperty("ActUrl")
    public String actUrl;

    public NeedToActModel(int letterID, String masterID, String domain, String docName, String category, String expDate, int acceptanceType, int isMandatoryPopup, String actUrl) {
        this.letterID = letterID;
        this.masterID = masterID;
        this.domain = domain;
        this.docName = docName;
        this.category = category;
        this.expDate = expDate;
        this.acceptanceType = acceptanceType;
        this.isMandatoryPopup = isMandatoryPopup;
        this.actUrl = actUrl;
    }

    public int getLetterID() {
        return letterID;
    }

    public void setLetterID(int letterID) {
        this.letterID = letterID;
    }

    public String getMasterID() {
        return masterID;
    }

    public void setMasterID(String masterID) {
        this.masterID = masterID;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getExpDate() {
        return expDate;
    }

    public void setExpDate(String expDate) {
        this.expDate = expDate;
    }

    public int getAcceptanceType() {
        return acceptanceType;
    }

    public void setAcceptanceType(int acceptanceType) {
        this.acceptanceType = acceptanceType;
    }

    public int getIsMandatoryPopup() {
        return isMandatoryPopup;
    }

    public void setIsMandatoryPopup(int isMandatoryPopup) {
        this.isMandatoryPopup = isMandatoryPopup;
    }

    public String getActUrl() {
        return actUrl;
    }

    public void setActUrl(String actUrl) {
        this.actUrl = actUrl;
    }
}
