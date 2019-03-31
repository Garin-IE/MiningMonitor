package com.mrgarin.mininmonitor.Data;

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
}
