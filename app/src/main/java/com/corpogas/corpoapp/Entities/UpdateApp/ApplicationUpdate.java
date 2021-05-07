package com.corpogas.corpoapp.Entities.UpdateApp;

import java.io.Serializable;
import java.util.List;

public class ApplicationUpdate implements Serializable {
    public long stationId;
//    public Station Station;
    public String reason;
    public String applicationDate;
    public List<ApplicationUpdateDetail> applicationUpdateDetails;

    public long getStationId() {
        return stationId;
    }

    public void setStationId(long stationId) {
        this.stationId = stationId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(String applicationDate) {
        this.applicationDate = applicationDate;
    }

    public List<ApplicationUpdateDetail> getApplicationUpdateDetails() {
        return applicationUpdateDetails;
    }

    public void setApplicationUpdateDetails(List<ApplicationUpdateDetail> applicationUpdateDetails) {
        this.applicationUpdateDetails = applicationUpdateDetails;
    }
}
