package com.mrgarin.mininmonitor.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mrgarin.mininmonitor.Data.EthermineOrgElement;
import com.mrgarin.mininmonitor.Interfaces.PoolTouchHelperAdapter;
import com.mrgarin.mininmonitor.R;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;

import com.mrgarin.mininmonitor.Data.BTCcomElement;
import com.mrgarin.mininmonitor.Data.BasicPoolElement;

public class PoolsAdapter extends RecyclerView.Adapter<PoolsAdapter.ViewHolder> implements PoolTouchHelperAdapter {

    private List<BasicPoolElement> pools;
    private LayoutInflater inflater;

    DecimalFormat decimalFormat = new DecimalFormat("#.00");

    public PoolsAdapter(Context context, List<BasicPoolElement> pools){
        this.inflater = LayoutInflater.from(context);
        this.pools = pools;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.pool_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PoolsAdapter.ViewHolder viewHolder, int i) {
        BasicPoolElement element = pools.get(i);
        if (!element.getPoolName().isEmpty()) {
            viewHolder.tv_pool_name.setText(element.getPoolName());
        }
        switch (element.getPoolName()){
            case "BTC.com":
                BTCcomElement btCcomElement = (BTCcomElement) element;
                //viewHolder.tv_pool_name.setText(element.getPoolName() + " subaccount: " + btCcomElement.getSubAccountName() + " coin: " + btCcomElement.getCoinName());
                viewHolder.tv_current_hashrate.setText(String.valueOf(btCcomElement.getCurrentHashRate()));
                viewHolder.tv_avg_hashrate.setText(String.valueOf(btCcomElement.getAvgHashRate()));
                viewHolder.tv_active_workers.setText(String.valueOf(btCcomElement.getActiveWorkers()));
                viewHolder.tv_inactive_workers.setText(String.valueOf(btCcomElement.getInActiveWorkers()));
                break;

            case "Ethermine.org":
                EthermineOrgElement ethermineOrgElement = (EthermineOrgElement) element;
                viewHolder.tv_current_hashrate.setText(decimalFormat.format(ethermineOrgElement.getCurrentHashRate()));
                viewHolder.tv_avg_hashrate.setText(decimalFormat.format(ethermineOrgElement.getAvgHashRate()));
                viewHolder.tv_active_workers.setText(String.valueOf(ethermineOrgElement.getActiveWorkers()));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return pools.size();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++){
                Collections.swap(pools, i, i+ 1);
            }
        }
        else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(pools, i, i-1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismis(int position) {
        pools.remove(position);
        notifyItemRemoved(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv_current_hashrate;
        TextView tv_avg_hashrate;
        TextView tv_active_workers;
        TextView tv_inactive_workers;
        TextView tv_pool_name;

        ViewHolder(View view){
            super(view);
            tv_current_hashrate = view.findViewById(R.id.tv_rth);
            tv_avg_hashrate = view.findViewById(R.id.tv_avgh);
            tv_active_workers = view.findViewById(R.id.tv_act_m);
            tv_inactive_workers = view.findViewById(R.id.tv_inact_m);
            tv_pool_name = view.findViewById(R.id.pool_name);
        }
    }

    public void setPools(List<BasicPoolElement> pools){
        this.pools.clear();
        this.pools.addAll(pools);
        notifyDataSetChanged();
    }
}
