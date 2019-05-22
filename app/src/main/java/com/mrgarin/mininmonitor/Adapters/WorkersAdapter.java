package com.mrgarin.mininmonitor.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mrgarin.mininmonitor.Data.Worker;
import com.mrgarin.mininmonitor.R;

import java.util.ArrayList;

public class WorkersAdapter extends RecyclerView.Adapter<WorkersAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Worker> workers;
    private LayoutInflater inflater;

    public WorkersAdapter(Context context, ArrayList<Worker> workers) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.workers = workers;
        //this.workers = new ArrayList<>();
        //this.workers.addAll(workers);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.worker_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkersAdapter.ViewHolder viewHolder, int i) {
        if (workers.get(i).getReportedHashrate() == 0){
            viewHolder.reportHashrate.setVisibility(View.GONE);
        }
        viewHolder.name.setText(workers.get(i).getWorkerName());
        viewHolder.reportHashrate.setText(String.format("%.2f", workers.get(i).getReportedHashrate()));
        viewHolder.currentHashrate.setText(String.format("%.2f", workers.get(i).getCurrentHashrate()));
        viewHolder.avgHashrate.setText(String.format("%.2f", workers.get(i).getAvgHashrate()));
        //Log.d("myLogs", String.valueOf(workers.get(i).getAvgHashrate()));
    }

    @Override
    public int getItemCount() {
        return workers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView name;
        TextView reportHashrate;
        TextView currentHashrate;
        TextView avgHashrate;

        public ViewHolder(@NonNull View view) {
            super(view);
            name = view.findViewById(R.id.workerview_name);
            reportHashrate = view.findViewById(R.id.workerview_reporthashrate);
            currentHashrate = view.findViewById(R.id.workerview_currenthashrate);
            avgHashrate = view.findViewById(R.id.workerview_avghashrate);
        }
    }

    public void setData(ArrayList<Worker> workers){
        this.workers.clear();
        this.workers.addAll(workers);
        notifyDataSetChanged();
    }

}
