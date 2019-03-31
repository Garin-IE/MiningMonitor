package com.mrgarin.mininmonitor;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.mrgarin.mininmonitor.Data.EthermineOrgElement;
import com.mrgarin.mininmonitor.Data.EthermineOrgLoader;

public class EthermineOrgAddDialog extends AppCompatDialogFragment implements View.OnClickListener {

    Button add;
    EditText walletAddress;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getDialog().setTitle("Ethermine.org");
        View v = inflater.inflate(R.layout.ethermine_org_add_dialog, container, false);
        v.setMinimumWidth(10000);

        add = v.findViewById(R.id.button_add);
        add.setOnClickListener(this);
        walletAddress = v.findViewById(R.id.et_wallet_address);
        return v;
    }

    @Override
    public void onClick(View v) {
        EthermineOrgElement element;
        if (!String.valueOf(walletAddress.getText()).isEmpty()){
            element = new EthermineOrgElement(String.valueOf(walletAddress.getText()), "Ethermine.org");
            mPoolPasser.onEthermineOrgPoolAdd(element);
            dismiss();
        }

    }

    private EthermineOrgLoader.OnEthermineOrgPoolAdd mPoolPasser;
    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        mPoolPasser = (EthermineOrgLoader.OnEthermineOrgPoolAdd) context;
    }
}
