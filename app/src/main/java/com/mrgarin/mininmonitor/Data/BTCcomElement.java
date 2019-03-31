package com.mrgarin.mininmonitor.Data;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
public class BTCcomElement extends BasicPoolElement {

    protected String access_key;
    protected String puid;
    protected String coinName;
    protected String subAccountName;

    public BTCcomElement(){}


    public BTCcomElement(String access_key, String puid, String poolName) {
        setAccess_key(access_key);
        setPuid(puid);
        setPoolName(poolName);
    }

    public BTCcomElement(String access_key, String puid, String poolName, String subAccountName, String coinName){
        this(access_key,puid,poolName);
        this.subAccountName = subAccountName;
        this.coinName = coinName;
    }

    public String getAccess_key() {
        return access_key;
    }

    @JsonProperty("access_key")
    public void setAccess_key(String access_key) {
        this.access_key = access_key;
    }

    public String getPuid() {
        return puid;
    }

    @JsonProperty("puid")
    public void setPuid(String puid) {
        this.puid = puid;
    }

    @Override
    public void setActiveWorkers(int activeWorkers){
        super.setActiveWorkers(activeWorkers);
    }

    @Override
    public void setInActiveWorkers(int inActiveWorkers){
        super.setInActiveWorkers(inActiveWorkers);
    }

    public void initData(int activeWorkers, int inActiveWorkers, float avgHashRate, float currentHashRate){
        setActiveWorkers(activeWorkers);
        setInActiveWorkers(inActiveWorkers);
        setAvgHashRate(avgHashRate);
        setCurrentHashRate(currentHashRate);
    }

    public void setCoinName(String coinName) {
        this.coinName = coinName;
    }

    public String getCoinName() {
        return coinName;
    }

    public String getSubAccountName(){
        return subAccountName;
    }
}
