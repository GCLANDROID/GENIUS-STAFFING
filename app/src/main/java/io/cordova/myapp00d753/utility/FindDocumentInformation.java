package io.cordova.myapp00d753.utility;

import android.util.Log;

public class FindDocumentInformation {
    private static final String TAG = "FindDocumentName";
    public static String FileNameFromURL(String fileURL){
        String NAME = "";
        String arrayURL[] = fileURL.split("/");
        Log.e(TAG, "FileNameFromURL: Number of Split: "+arrayURL.length);
        NAME = arrayURL[(arrayURL.length)-1];
        return NAME;
    }

    public static String FindFileTypeFromDocumentName(String fileName){
        String TYPE = "";
        String arrayType[] = fileName.split(".",2);
        Log.e(TAG, "FindFileTypeFromDocumentName: type: size: "+arrayType.length);
        Log.e(TAG, "FindFileTypeFromDocumentName: type: "+arrayType[(arrayType.length)-1]);
        TYPE = arrayType[1];
        return TYPE;
    }
}
