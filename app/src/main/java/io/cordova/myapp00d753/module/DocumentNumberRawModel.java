package io.cordova.myapp00d753.module;

public class DocumentNumberRawModel {
    String docName,docNumber;
    int d;

    public DocumentNumberRawModel(String docName, String docNumber) {
        this.docName = docName;
        this.docNumber = docNumber;
    }

    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }

    public int getD() {
        return d;
    }

    public void setD(int d) {
        this.d = d;
    }

    public String getDocNumber() {
        return docNumber;
    }

    public void setDocNumber(String docNumber) {
        this.docNumber = docNumber;
    }
}
