package com.mrgarin.mininmonitor.Data;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
public class EthermineOrgElement extends BasicPoolElement {
    protected String walletAdress;

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
}

