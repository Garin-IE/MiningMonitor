package com.mrgarin.mininmonitor.BTCcom;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonIgnoreProperties(ignoreUnknown = true)
public class BTCComApiData {

    @JsonProperty("err_no")
    protected String err;

    @JsonProperty("data")
    BTCComDataResponce dataResponce;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public class BTCComDataResponce {
        @JsonProperty("workers_active")
        protected int workersActive;

        @JsonProperty("workers_inactive")
        protected int workersInActive;

        @JsonProperty("shares_1d")
        protected HashRate avgHashRate;

        @JsonProperty("shares_1h")
        protected HashRate currentHashRate;

        @JsonIgnoreProperties(ignoreUnknown = true)
        public class HashRate {
            @JsonProperty("size")
            protected float hashRate;
        }

    }

    public float getCurrentHashRate(){
        return dataResponce.currentHashRate.hashRate;
    }

    public float getAvgHashRate() {
        return dataResponce.avgHashRate.hashRate;
    }

    public int getWorkersActive(){
        return dataResponce.workersActive;
    }

    public int getWorkersInActive() {
        return dataResponce.workersInActive;
    }

    public String getErr(){
        return err;
    }

}


