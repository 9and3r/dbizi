package com.aorbegozo005.dbizi;

import android.os.Handler;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by ander on 2016/08/08.
 */
public class DBiziDataProvider implements EstazioProvider, Callback {

    private static final String startString = "json = JSON.parse('{\"stations\":";
    private static final String endString = "}');";

    private ArrayList<DBiziDataListener> listeners;
    private ArrayList<Estazioa> estazioak;

    private Estazioa selected;

    private OkHttpClient client;
    private Gson gson;
    private Handler handler;
    private Call call;



    public DBiziDataProvider(){
        estazioak = new ArrayList<>();
        listeners = new ArrayList<>();

        client = new OkHttpClient();
        gson = new Gson();
        handler = new Handler();
    }

    public void onStart(){
        for (int i=0; i<listeners.size(); i++){
            listeners.get(i).onEstazioakLoaded(estazioak);
        }
        updateData();
    }

    public void onStop(){
        listeners.clear();
        if (call != null && !call.isExecuted() && !call.isCanceled()){
            call.cancel();
            call = null;
        }
    }

    @Override
    public void onFailure(Call call, IOException e) {
        Log.d("Proba", "Error");
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        String result = response.body().string();
        result = result.substring(result.indexOf(startString) + startString.length());
        result = result.substring(0, result.indexOf(endString));

        final String finalResult = result;
        handler.post(new Runnable() {
            @Override
            public void run() {
                Estazioa[] estazioakList = gson.fromJson(finalResult, Estazioa[].class);
                estazioak = new ArrayList<Estazioa>(Arrays.asList(estazioakList));

                // Quitar nave
                Estazioa nave = new Estazioa();
                nave.id = 2008;
                nave.number = 2008;
                estazioak.remove(nave);

                Collections.sort(estazioak, new Comparator<Estazioa>() {
                    @Override
                    public int compare(Estazioa estazioa, Estazioa t1) {
                        return estazioa.name.compareTo(t1.name);
                    }
                });
                onEstazioakLoaded(estazioak);
            }
        });
    }

    @Override
    public void addEstazioListener(DBiziDataListener listener) {
        listeners.add(listener);
        listener.onEstazioakLoaded(estazioak);
    }

    public void updateData(){
        if (call != null && !call.isExecuted() && !call.isCanceled()){
            call.cancel();
            call = null;
        }
        Request request = new Request.Builder()
                .url("http://www.dbizi.com/map/")
                .build();
        call = client.newCall(request);
        call.enqueue(this);
    }

    @Override
    public void removeEstazioListener(DBiziDataListener listener) {
        listeners.remove(listener);
    }

    @Override
    public ArrayList<Estazioa> getEstazioak() {
        return estazioak;
    }

    @Override
    public Estazioa getSelected() {
        return selected;
    }

    @Override
    public void setSelected(Estazioa estazioBerria) {
        onEstazioaSelected(selected, estazioBerria);
        selected = estazioBerria;
    }

    @Override
    public void onEstazioakLoaded(ArrayList<Estazioa> pEstazioak) {
        estazioak = pEstazioak;
        for (int i=0; i<listeners.size(); i++){
            listeners.get(i).onEstazioakLoaded(estazioak);
        }
    }

    @Override
    public void onEstazioaSelected(Estazioa previous, Estazioa pSelected) {
        for (int i=0; i<listeners.size(); i++){
            listeners.get(i).onEstazioaSelected(selected, pSelected);
        }
    }
}
