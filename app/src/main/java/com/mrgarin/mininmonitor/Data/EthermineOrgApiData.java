package com.mrgarin.mininmonitor.Data;

import android.util.Log;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonIgnoreProperties(ignoreUnknown = true)
public class EthermineOrgApiData {


    @JsonProperty("status")
    protected String status;

    @JsonProperty("data")
    private EthermineOrgDataResponse dataResponse;

    @JsonIgnoreProperties(ignoreUnknown = true)
    protected class EthermineOrgDataResponse{

        public EthermineOrgDataResponse(){}

        @JsonProperty("currentHashrate")
        protected double currentHashRate;

        @JsonProperty("averageHashrate")
        protected double avgHashRate;

        @JsonProperty("activeWorkers")
        protected int activeWorkers;

        @JsonProperty("unpaid")
        protected long unpaid;

    }

    public float getCurrentHashRate(){
        return (float) dataResponse.currentHashRate/1000000;
    }

    public float getAvgHashRate(){
        return (float) dataResponse.avgHashRate/1000000;
    }

    public int getActiveWorkers(){
        return dataResponse.activeWorkers;
    }

    public String getStatus(){
        return status;
    }

    public float getUnpaid(){
        long tempLong  = dataResponse.unpaid/1000000000;
        float temp = (float) tempLong/1000000000;
        return temp;
    }
}
