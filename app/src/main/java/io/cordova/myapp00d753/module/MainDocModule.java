package io.cordova.myapp00d753.module;

public  class MainDocModule {
    String DocID,DocumentType;

    public MainDocModule(String docID, String documentType) {
        DocID = docID;
        DocumentType = documentType;
    }

    public String getDocID() {

        return DocID;
    }

    public void setDocID(String docID) {
        DocID = docID;
    }

    public String getDocumentType() {
        return DocumentType;
    }

    public void setDocumentType(String documentType) {
        DocumentType = documentType;
    }
}
