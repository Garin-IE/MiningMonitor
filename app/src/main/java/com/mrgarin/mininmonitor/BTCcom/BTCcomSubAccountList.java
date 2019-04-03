package com.mrgarin.mininmonitor.BTCcom;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mrgarin.mininmonitor.Adapters.PoolsViewOffsetDecoration;
import com.mrgarin.mininmonitor.Data.BTCcomElement;
import com.mrgarin.mininmonitor.Data.BTCcomLoader;
import com.mrgarin.mininmonitor.R;

import java.util.ArrayList;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BTCcomSubAccountList extends DialogFragment {

    Bundle bundle;
    String access_key;
    Adapter adapter;
    //ArrayList<String> puidCodes;
    //ArrayList<String> puidNames;
    ArrayList<Puid> puidList;
    RecyclerView recyclerView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().setTitle("Choice subaccount");
        bundle = this.getArguments();
        if (bundle != null){
            access_key = bundle.getString("access_key");
        }
        View v = inflater.inflate(R.layout.btc_com_subaccount_list, null);
        //v.setMinimumWidth(10000);
        //v.setMinimumHeight(1000);
        recyclerView = v.findViewById(R.id.simple_rv_list);
        Call<BTCComApiDataList> btcComApiDataCall = new BTCcomLoader().getBtcComApi()
                    .getSubAccountList(access_key);
        btcComApiDataCall.enqueue(new Callback<BTCComApiDataList>() {
            @Override
            public void onResponse(Call<BTCComApiDataList> call, Response<BTCComApiDataList> response) {
                if (response.body().getErr().equals("0")){
                    ArrayList<String> puidCodes = new ArrayList<>();
                    ArrayList<String> puidNames = new ArrayList<>();
                    ArrayList<String> coinNames = new ArrayList<>();
                    for (int i = 0; i < response.body().dataResponseLists.size(); i++) {
                        puidList.add(new Puid(response.body().dataResponseLists.get(i).puidCode,
                                response.body().dataResponseLists.get(i).puidName,
                                response.body().dataResponseLists.get(i).coinName));
                        //puidCodes.add(response.body().dataResponseLists.get(i).puidCode);
                        //puidNames.add(response.body().dataResponseLists.get(i).puidName);
                    }
                    adapter.setData(puidList);
                    //setPuids(puidCodes, puidNames);
                    //Log.d("myLogs", "Инфа загружена " + response.body().dataResponseLists.get(0).puidName);
                }
                else {
                    Log.d("myLogs", "Error " + response.body().getErr());
                }
            }

            @Override
            public void onFailure(Call<BTCComApiDataList> call, Throwable t) {
                Log.d("myLogs", "Ошибка Загрузки " + t.toString());
            }
        });
        //puidCodes = new ArrayList<>();
        //puidNames = new ArrayList<>();
        puidList = new ArrayList<>();
        adapter = new Adapter(getContext(), puidList);
        recyclerView.addItemDecoration(new PoolsViewOffsetDecoration(50));
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return v;
    }

    //public void setPuids(ArrayList<String> puidCode, ArrayList<String> puidName){
    //    this.puidCodes.clear();
    //    this.puidCodes.addAll(puidCode);
    //    this.puidNames.clear();
    //    this.puidNames.addAll(puidName);
    //    adapter.notifyDataSetChanged();
    //}

    private class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
        public ArrayList<String> puids;
        protected LayoutInflater inflater;
        protected ArrayList<Puid> puidList;

        //private Adapter(Context context, ArrayList<String> puids){
        //    this.inflater = LayoutInflater.from(context);
        //    this.puids = puids;
        //}

        private Adapter(Context context, ArrayList<Puid> puidList){
            this.inflater = LayoutInflater.from(context);
            this.puidList = new ArrayList<>();
            this.puidList.addAll(puidList);
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = inflater.inflate(R.layout.simple_list_item, viewGroup, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
            viewHolder.name.setText(puidList.get(i).getpUidName() + "   " + puidList.get(i).getCoinName());
        }

        @Override
        public int getItemCount() {
            return puidList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
            TextView name;
            ViewHolder(View view){
                super(view);
                view.setOnClickListener(this);
                name = view.findViewById(R.id.name);
            }

            @Override
            public void onClick(View v) {
                poolAdd(getAdapterPosition());
            }
        }

        //public void setData(ArrayList<String> puidNames){
        //    puids.addAll(puidNames);
        //    notifyDataSetChanged();
        //}

        public void setData(ArrayList<Puid> puidList){
            this.puidList.clear();
            this.puidList.addAll(puidList);
            notifyDataSetChanged();
        }
    }

    private BTCcomLoader.OnBTCComPoolAdd mPoolPasser;
    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        mPoolPasser = (BTCcomLoader.OnBTCComPoolAdd) context;
    }

    public void poolAdd(int i){
        BTCcomElement element = new BTCcomElement(access_key, puidList.get(i).getpUidCode(), "BTC.com", puidList.get(i).getpUidName(), puidList.get(i).getCoinName());
        Log.d("myLogs", puidList.get(i).getpUidCode());
        mPoolPasser.onBTCComPoolAdd(element);
        dismiss();
    }

}
