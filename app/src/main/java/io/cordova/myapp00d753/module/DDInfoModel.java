package io.cordova.myapp00d753.module;

public class DDInfoModel {
    String documentType;
    String description;
    String status;
    boolean isHeader=false;

    public DDInfoModel(String documentType, String description, String status, boolean isHeader) {
        this.documentType = documentType;
        this.description = description;
        this.status = status;
        this.isHeader = isHeader;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isHeader() {
        return isHeader;
    }

    public void setHeader(boolean header) {
        isHeader = header;
    }
}
