package com.corpogas.corpoapp.Entities.UpdateApp;

public class ApplicationUpdateDetail {
    public long applicationUpdateId;
    public ApplicationUpdate applicationUpdate;
    public long applicationId;
//    public Application Application;
    public String version;
    public String minimunVersionSDK;
    public String minimunVersionAndroid;
    public String deviceModel;
    public String fileName;

    public long getApplicationUpdateId() {
        return applicationUpdateId;
    }

    public void setApplicationUpdateId(long applicationUpdateId) {
        this.applicationUpdateId = applicationUpdateId;
    }

    public ApplicationUpdate getApplicationUpdate() {
        return applicationUpdate;
    }

    public void setApplicationUpdate(ApplicationUpdate applicationUpdate) {
        this.applicationUpdate = applicationUpdate;
    }

    public long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(long applicationId) {
        this.applicationId = applicationId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getMinimunVersionSDK() {
        return minimunVersionSDK;
    }

    public void setMinimunVersionSDK(String minimunVersionSDK) {
        this.minimunVersionSDK = minimunVersionSDK;
    }

    public String getMinimunVersionAndroid() {
        return minimunVersionAndroid;
    }

    public void setMinimunVersionAndroid(String minimunVersionAndroid) {
        this.minimunVersionAndroid = minimunVersionAndroid;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
