package com.mrgarin.mininmonitor;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mrgarin.mininmonitor.BTCcom.BTCcomSubAccountList;
import com.mrgarin.mininmonitor.Data.BTCcomElement;
import com.mrgarin.mininmonitor.Data.BTCcomLoader;

public class BtcComAddDialog extends AppCompatDialogFragment implements View.OnClickListener {

    Button add;
    EditText accessKey, puid;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().setTitle("BTC.com");
        View v = inflater.inflate(R.layout.btc_com_add_item, container, false);
        v.setMinimumWidth(10000);

        add = v.findViewById(R.id.button_add);
        add.setOnClickListener(this);
        accessKey = v.findViewById(R.id.et_access_key);
        puid = v.findViewById(R.id.et_puid);

        return v;
    }

    @Override
    public void onClick(View v) {
        BTCcomElement pool;
        if (!String.valueOf(accessKey.getText()).isEmpty()) {
            if (!String.valueOf(puid.getText()).isEmpty()) {
                pool = new BTCcomElement(String.valueOf(accessKey.getText()), String.valueOf(puid.getText()), "BTC.com");
                mPoolPasser.onBTCComPoolAdd(pool);
                dismiss();
            }else {
                DialogFragment subAccountList = new BTCcomSubAccountList();
                Bundle bundle = new Bundle();
                bundle.putString("access_key", String.valueOf(accessKey.getText()));
                subAccountList.setArguments(bundle);
                subAccountList.show(getFragmentManager(), "SubAccountList");
                dismiss();
            }
        }
        else {
            Toast.makeText(getContext(), "Введите данные учетной записи", Toast.LENGTH_LONG).show();
        }
    }

    private BTCcomLoader.OnBTCComPoolAdd mPoolPasser;
    @Override
    public void onAttach(Activity a){
        super.onAttach(a);
        mPoolPasser = (BTCcomLoader.OnBTCComPoolAdd) a;
    }
}
