package io.cordova.myapp00d753.module;

public class NewPFLinkModel {
    String documentName;
    int image;

    public NewPFLinkModel(String documentName, int image) {
        this.documentName = documentName;
        this.image = image;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
