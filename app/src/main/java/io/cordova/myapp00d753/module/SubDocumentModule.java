package io.cordova.myapp00d753.module;

public class SubDocumentModule {
    String DocumentID,Document;

    public SubDocumentModule(String documentID, String document) {
        DocumentID = documentID;
        Document = document;
    }

    public String getDocumentID() {
        return DocumentID;
    }

    public void setDocumentID(String documentID) {
        DocumentID = documentID;
    }

    public String getDocument() {
        return Document;
    }

    public void setDocument(String document) {
        Document = document;
    }
}
