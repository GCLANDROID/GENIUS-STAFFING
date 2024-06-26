package io.cordova.myapp00d753.activity.metso.model;

import com.google.gson.annotations.SerializedName;

public class ApproverModel {
    @SerializedName("UserId")
    public int approverId;
    @SerializedName("UserName")
    public String approverName;

    public ApproverModel(int approverId, String approverName) {
        this.approverId = approverId;
        this.approverName = approverName;
    }

    public int getApproverId() {
        return approverId;
    }

    public void setApproverId(int approverId) {
        this.approverId = approverId;
    }

    public String getApproverName() {
        return approverName;
    }

    public void setApproverName(String approverName) {
        this.approverName = approverName;
    }
}
