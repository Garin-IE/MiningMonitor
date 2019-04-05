package com.mrgarin.mininmonitor.Data;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
public class EthermineOrgElement extends BasicPoolElement {
    protected String walletAdress;
    @JsonProperty("HashrateAlert")
    protected float alert_MinCurrentHashrate;
    @JsonProperty("WorkersAlert")
    protected int alert_ActiveWorkers;
    protected ArrayList<Worker> workers = new ArrayList<>();

    EthermineOrgElement(){}

    public EthermineOrgElement(String walletAdress, String poolName){
        this.walletAdress = walletAdress;
        this.poolName = poolName;
    }

    public void init(float avgHashRate, float currentHashRate, int activeWorkers){
        setAvgHashRate(avgHashRate);
        setCurrentHashRate(currentHashRate);
        setActiveWorkers(activeWorkers);
    }

    public String getWalletAdress() {
        return walletAdress;
    }

    public void setWalletAdress(String walletAdress) {
        this.walletAdress = walletAdress;
    }

    public float getAlert_MinCurrentHashrate() {
        return alert_MinCurrentHashrate;
    }

    public void setAlert_MinCurrentHashrate(float alert_MinCurrentHashrate) {
        this.alert_MinCurrentHashrate = alert_MinCurrentHashrate;
    }

    public int getAlert_ActiveWorkers() {
        return alert_ActiveWorkers;
    }

    public void setAlert_ActiveWorkers(int alert_ActiveWorkers) {
        this.alert_ActiveWorkers = alert_ActiveWorkers;
    }

    protected class Worker{
        protected String workerName;
        protected float workerCurrentHashrate, workerAvgHashrate;
    }

    public ArrayList<Worker> getWorkers() {
        return workers;
    }

    public void setWorkers(ArrayList<Worker> workers) {
        this.workers.clear();
        this.workers.addAll(workers);
    }
}

