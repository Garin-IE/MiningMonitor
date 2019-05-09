package com.mrgarin.mininmonitor;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mrgarin.mininmonitor.aplication.AppConfig;

public class AboutActivity extends AppCompatActivity implements View.OnClickListener {

    TextView buildVersion, ethAddress, btcAddress;
    ClipboardManager clipboardManager;
    ClipData clipData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        buildVersion = findViewById(R.id.about_version);
        buildVersion.setText("version: " + String.valueOf(AppConfig.buildVersion));
        ethAddress = findViewById(R.id.about_ethAddress);
        ethAddress.setOnClickListener(this);
        btcAddress = findViewById(R.id.about_btcAddress);
        btcAddress.setOnClickListener(this);
        clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
    }

    @Override
    public void onClick(View v) {
        TextView view;
        switch (v.getId()){
            default:
                break;
            case R.id.about_ethAddress:
                view = (TextView) v;
                Toast.makeText(this, "ETH Address copied to clipboard", Toast.LENGTH_SHORT).show();
                clipData = ClipData.newPlainText("ETH Address", view.getText());
                clipboardManager.setPrimaryClip(clipData);
                break;
            case R.id.about_btcAddress:
                view = (TextView) v;
                Toast.makeText(this, "BTC Address copied to clipboard", Toast.LENGTH_SHORT).show();
                clipData = ClipData.newPlainText("BTC Address", view.getText());
                clipboardManager.setPrimaryClip(clipData);
                break;
        }
    }
}
