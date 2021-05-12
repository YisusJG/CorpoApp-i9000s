package com.corpogas.corpoapp.Entities.Sucursales;



import java.io.Serializable;

public class UpdateDetail implements Serializable
{
    public long BranchId;
//    public virtual Branch Branch;
    public long UpdateId;
//    public virtual Update Update;
    public long ApplicationId;
    public Application Application;
    public String Version;
    public String FileName;
    public String Url;
    public String MinimunVersionSDK;
    public String MinimunVersionAndroid;
    public String DeviceModel;

    public long getBranchId() {
        return BranchId;
    }

    public void setBranchId(long branchId) {
        BranchId = branchId;
    }

    public long getUpdateId() {
        return UpdateId;
    }

    public void setUpdateId(long updateId) {
        UpdateId = updateId;
    }

    public long getApplicationId() {
        return ApplicationId;
    }

    public void setApplicationId(long applicationId) {
        ApplicationId = applicationId;
    }

    public Application getApplication() {
        return Application;
    }

    public void setApplication(Application application) {
        Application = application;
    }

    public String getVersion() {
        return Version;
    }

    public void setVersion(String version) {
        Version = version;
    }

    public String getFileName() {
        return FileName;
    }

    public void setFileName(String fileName) {
        FileName = fileName;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public String getMinimunVersionSDK() {
        return MinimunVersionSDK;
    }

    public void setMinimunVersionSDK(String minimunVersionSDK) {
        MinimunVersionSDK = minimunVersionSDK;
    }

    public String getMinimunVersionAndroid() {
        return MinimunVersionAndroid;
    }

    public void setMinimunVersionAndroid(String minimunVersionAndroid) {
        MinimunVersionAndroid = minimunVersionAndroid;
    }

    public String getDeviceModel() {
        return DeviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        DeviceModel = deviceModel;
    }
}
