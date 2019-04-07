package com.mrgarin.mininmonitor.Data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EthermineOrgApiDataList {

    @JsonProperty("status")
    protected String status;

    @JsonProperty("data")
    public ArrayList<DataResponse> dataResponseList = new ArrayList<>();

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DataResponse{

        DataResponse(){}

        @JsonProperty("worker")
        public String worker;
        @JsonProperty("reportedHashrate")
        public float reportedHashrate;
        @JsonProperty("currentHashrate")
        public float currentHashrate;
        @JsonProperty("averageHashrate")
        public float avgHashrate;
    }

    public String getStatus() {
        return status;
    }

    public ArrayList<DataResponse> getDataResponseList() {
        return dataResponseList;
    }
}
