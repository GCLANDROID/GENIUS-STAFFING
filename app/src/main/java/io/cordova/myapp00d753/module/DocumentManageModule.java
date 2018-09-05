package io.cordova.myapp00d753.module;

public class DocumentManageModule {
    String documentName,documentType,approvalRemarks,createdOn,aEMStatusName,docLink;

    public DocumentManageModule(String documentName, String documentType, String approvalRemarks, String createdOn, String aEMStatusName,String docLink) {
        this.documentName = documentName;
        this.documentType = documentType;
        this.approvalRemarks = approvalRemarks;
        this.createdOn = createdOn;
        this.aEMStatusName = aEMStatusName;
        this.docLink=docLink;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getApprovalRemarks() {
        return approvalRemarks;
    }

    public void setApprovalRemarks(String approvalRemarks) {
        this.approvalRemarks = approvalRemarks;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getaEMStatusName() {
        return aEMStatusName;
    }

    public void setaEMStatusName(String aEMStatusName) {
        this.aEMStatusName = aEMStatusName;
    }

    public String getDocLink() {
        return docLink;
    }

    public void setDocLink(String docLink) {
        this.docLink = docLink;
    }
}
