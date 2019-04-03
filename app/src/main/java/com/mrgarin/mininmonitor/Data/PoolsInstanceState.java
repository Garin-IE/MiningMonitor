package com.mrgarin.mininmonitor.Data;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mrgarin.mininmonitor.aplication.AppConfig;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;


public class PoolsInstanceState {
    ObjectMapper mapper = new ObjectMapper();
    Context context;
    final static String FILE_NAME = "PoolsInstanceState.txt";

    public PoolsInstanceState(Context context){
        this.context = context;
    }

    public void saveInstance(List<BasicPoolElement> pools){

        House house = new House();
        house.poolElements.addAll(pools);

        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(
                    context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE)));
            mapper.writeValue(bufferedWriter,house);
            bufferedWriter.close();
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(context, "Error: " + e.toString(),Toast.LENGTH_LONG).show();
            Log.d("myLogs", "Error: " + e.toString());
        }

        if (AppConfig.write_debug_save_on_sd) {
            File sdPath = Environment.getExternalStorageDirectory();
            sdPath = new File(sdPath.getAbsolutePath());
            sdPath.mkdirs();
            File sdFile = new File(sdPath, "test.txt");
            try {
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(sdFile));
                mapper.writeValue(bufferedWriter, house);
                bufferedWriter.close();
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("myLogs", "Error: " + e.toString());
            }
        }
    }

    public List<BasicPoolElement> loadInstance(){
        House house = new House();
        final List<BasicPoolElement> poolElements = new ArrayList<>();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader
                    (context.openFileInput(FILE_NAME)));
            //Log.d("myLogs", bufferedReader.readLine());
            house = mapper.readValue(bufferedReader, new TypeReference<House>() {});
            poolElements.addAll(house.poolElements);
            Log.d("myLogs", "size in loader: " + String.valueOf(poolElements.size()));
            return poolElements;
        } catch (Exception e){
            e.printStackTrace();
            Log.d("myLogs", e.toString());
        }

        return poolElements;
    }

    public List<BasicPoolElement> advLoadInstance(){
        return null;
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
class House {
    public ArrayList<BasicPoolElement> poolElements = new ArrayList<>();
}
