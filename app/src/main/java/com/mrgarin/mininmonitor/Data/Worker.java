package com.mrgarin.mininmonitor.Data;

public class Worker {
    String workerName;
    float reportedHashrate;
    float currentHashrate;
    float avgHashrate;

    public String getWorkerName() {
        return workerName;
    }

    public void setWorkerName(String workerName) {
        this.workerName = workerName;
    }

    public float getReportedHashrate() {
        return reportedHashrate;
    }

    public void setReportedHashrate(float reportedHashrate) {
        this.reportedHashrate = reportedHashrate;
    }

    public float getCurrentHashrate() {
        return currentHashrate;
    }

    public void setCurrentHashrate(float currentHashrate) {
        this.currentHashrate = currentHashrate;
    }

    public float getAvgHashrate() {
        return avgHashrate;
    }

    public void setAvgHashrate(float avgHashrate) {
        this.avgHashrate = avgHashrate;
    }
}
