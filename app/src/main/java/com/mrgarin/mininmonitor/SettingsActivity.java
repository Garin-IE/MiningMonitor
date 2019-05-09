package com.mrgarin.mininmonitor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.mrgarin.mininmonitor.aplication.AppConfig;

public class SettingsActivity extends AppCompatActivity {

    EditText updTimeValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        updTimeValue = findViewById(R.id.settings_autoUPDValue);
        updTimeValue.setText(String.valueOf(AppConfig.userAutoUpdateTime));
        updTimeValue.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER){
                    if (updTimeValue.getText().length() > 0){
                        if (Integer.valueOf(updTimeValue.getText().toString()) > 0){
                            AppConfig.setUserAutoUpdateTime(Integer.valueOf(updTimeValue.getText().toString()));
                            Toast.makeText(getBaseContext(), "Value is updated", Toast.LENGTH_SHORT).show();
                        }
                        else Toast.makeText(getBaseContext(), "Value must be bigger than 0", Toast.LENGTH_SHORT).show();
                    }
                    else Toast.makeText(getBaseContext(), "Enter the value of auto update timer", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
        updTimeValue.setOnFocusChangeListener(onFocusChangeValidator);
    }

    @Override
    protected void onStop() {
        super.onStop();
        onStopValidator();
    }

    protected View.OnFocusChangeListener onFocusChangeValidator = new View.OnFocusChangeListener(){

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            switch (v.getId()){
                default:
                    break;
                case R.id.settings_autoUPDValue:
                    if (!hasFocus){
                        if (updTimeValue.getText().length() > 0){
                            if (Integer.valueOf(updTimeValue.getText().toString()) > 0){
                                Log.d("myLogs", "Updated");
                                AppConfig.setUserAutoUpdateTime(Integer.valueOf(updTimeValue.getText().toString()));
                                Toast.makeText(getBaseContext(), "Value is updated", Toast.LENGTH_SHORT).show();
                            }
                            else Toast.makeText(getBaseContext(), "Value must be bigger than 0", Toast.LENGTH_SHORT).show();
                        }
                        else Toast.makeText(getBaseContext(), "Enter the value of auto update timer", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    protected void onStopValidator(){
        boolean error = false;
        if (updTimeValue.getText().length() > 0){
            if (Integer.valueOf(updTimeValue.getText().toString()) != AppConfig.userAutoUpdateTime){
                if (Integer.valueOf(updTimeValue.getText().toString()) > 0){
                    AppConfig.setUserAutoUpdateTime(Integer.valueOf(updTimeValue.getText().toString()));
                }
                else error = true;
            }
        }
        else error = true;

        if (error) {
            Toast.makeText(getApplicationContext(), "Errors in settings", Toast.LENGTH_SHORT).show();
        }
    }
}
