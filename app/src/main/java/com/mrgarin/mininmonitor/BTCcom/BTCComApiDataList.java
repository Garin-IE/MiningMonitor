package com.mrgarin.mininmonitor.BTCcom;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BTCComApiDataList {

    @JsonProperty("err_no")
    protected String err;

    @JsonProperty("data")
    @JsonIgnoreProperties(ignoreUnknown = true)
    ArrayList<BTCComApiDataResponseList> dataResponseLists = new ArrayList<>();

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BTCComApiDataResponseList{

        BTCComApiDataResponseList(){}

        @JsonProperty("puid")

        public String puidCode;

        public void setPuidCode(String puidCode){
            this.puidCode = puidCode;
        }

        @JsonProperty("name")
        public String puidName;

        public void setPuidName(String puidName){
            this.puidName = puidName;
        }

        @JsonProperty("coin_type")
        public String coinName;

        @JsonProperty("worker_name")
        public String workerName;
    }

    public ArrayList<BTCComApiDataResponseList> getDataResponseLists(){
        return dataResponseLists;
    }

    public String getErr(){
        return err;
    }
}
