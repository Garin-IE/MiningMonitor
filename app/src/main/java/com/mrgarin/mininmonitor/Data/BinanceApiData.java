package com.mrgarin.mininmonitor.Data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BinanceApiData {

    public ArrayList<Price> prices = new ArrayList<>();

    public class Price{
        String symbol;
        double price;
    }

    public double getPrice(int i){
        return prices.get(i).price;
    }

    public int getSize(){
        return prices.size();
    }

    public String getPairName(int i){
        return prices.get(i).symbol;
    }
}
