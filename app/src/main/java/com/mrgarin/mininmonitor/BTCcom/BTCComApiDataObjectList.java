package com.mrgarin.mininmonitor.BTCcom;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BTCComApiDataObjectList {
    @JsonProperty("err_no")
    String err;

    @JsonProperty("data")
    DataResponseObjectList objectList;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DataResponseObjectList{
        @JsonProperty("data")
        ArrayList<DataList> dataLists;

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class DataList{
            @JsonProperty("worker_name")
            String workerName;

            @JsonProperty("shares_15m")
            float currentHashrate;

            @JsonProperty("shares_1d")
            float avgHashrate;

            public String getWorkerName() {
                return workerName;
            }

            public float getCurrentHashrate() {
                return currentHashrate;
            }

            public float getAvgHashrate() {
                return avgHashrate;
            }
        }

        public ArrayList<DataList> getDataLists() {
            return dataLists;
        }
    }

    public DataResponseObjectList getObjectList() {
        return objectList;
    }

    public String getErr() {
        return err;
    }
}
