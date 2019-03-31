package com.mrgarin.mininmonitor;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class FabAddDialog extends AppCompatDialogFragment {

    String[] pools = {"btc.com", "ethermine.org"};
    DialogFragment btcCom_add_dialog;
    DialogFragment ethermineOrg_add_dialog;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().setTitle("Title!");
        View v = inflater.inflate(R.layout.fab_add_dialog, null);
        ListView lw = v.findViewById(R.id.lv_add_dialog);
        lw.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1,
                pools));
        lw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                add(position);
                dismiss();
            }
        });

        btcCom_add_dialog = new BtcComAddDialog();
        ethermineOrg_add_dialog = new EthermineOrgAddDialog();

        return v;
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    private void add(int position) {
        switch (position) {
            case 0:
                btcCom_add_dialog.show(getFragmentManager(), "btcCom_add_dialog");
                Log.d("myLogs", "item Clicked: " + pools[position]);
                dismiss();
                break;
            case 1:
                ethermineOrg_add_dialog.show(getFragmentManager(), "Ethermine.org Dialog");
                Log.d("myLogs", "item clicked: " + pools[position]);
                dismiss();
                break;
            default:
                break;
        }
    }
}
