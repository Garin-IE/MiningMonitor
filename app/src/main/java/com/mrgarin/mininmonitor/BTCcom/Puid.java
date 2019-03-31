package com.mrgarin.mininmonitor.BTCcom;

public class Puid {
    protected String pUidCode, pUidName, coinName;

    Puid(String pUidCode, String pUidName, String coinName){
        this.pUidCode = pUidCode;
        this.pUidName = pUidName;
        this.coinName = coinName;
    }

    public String getpUidCode() {
        return pUidCode;
    }

    public String getpUidName() {
        return pUidName;
    }

    public String getCoinName() {
        return coinName;
    }
}
