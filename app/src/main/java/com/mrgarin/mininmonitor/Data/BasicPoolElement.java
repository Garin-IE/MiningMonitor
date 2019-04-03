package com.mrgarin.mininmonitor.Data;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonAutoDetect
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = BTCcomElement.class, name = "BTC.com"),
        @JsonSubTypes.Type(value = EthermineOrgElement.class, name = "Ethermine.org")
})
public class BasicPoolElement {

    @JsonIgnore
    protected float currentHashRate;
    @JsonIgnore
    protected float avgHashRate;
    @JsonIgnore
    protected float balance;
    protected String poolName;
    @JsonIgnore
    protected int activeWorkers;
    @JsonIgnore
    protected int inActiveWorkers;

    public BasicPoolElement(){}

    public Float getCurrentHashRate() {
        return currentHashRate;
    }

    public void setCurrentHashRate(float currentHashRate) {
        if (currentHashRate >= 0) {
            this.currentHashRate = currentHashRate;
        }
    }

    public Float getAvgHashRate() {
        return avgHashRate;
    }

    public void setAvgHashRate(float avgHashRate) {
        if (avgHashRate >= 0) {
            this.avgHashRate = avgHashRate;
        }
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        if (balance >= 0) {
            this.balance = balance;
        }
    }

    public String getPoolName() {
        return poolName;
    }

    @JsonProperty("poolName")
    public void setPoolName(String poolName) {
        if (poolName != null && !poolName.isEmpty()) {
            this.poolName = poolName;
        }
    }

    public int getActiveWorkers() {
        return activeWorkers;
    }

    public void setActiveWorkers(int activeWorkers) {
        if (activeWorkers >= 0) {
            this.activeWorkers = activeWorkers;
        }
    }

    public int getInActiveWorkers() {
        return inActiveWorkers;
    }

    public void setInActiveWorkers(int inActiveWorkers) {
        if (inActiveWorkers >= 0) {
            this.inActiveWorkers = inActiveWorkers;
        }
    }
}
