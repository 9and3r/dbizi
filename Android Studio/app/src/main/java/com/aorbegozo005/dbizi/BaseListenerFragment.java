package com.aorbegozo005.dbizi;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by ander on 2016/08/08.
 */
public abstract class BaseListenerFragment extends Fragment implements  DBiziDataListener{


    protected EstazioProvider provider;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof EstazioProvider){
            provider = (EstazioProvider) context;
        }else{
            Log.e(getString(R.string.app_name), "Error: Context must implement " + DBiziDataProvider.class.getName());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        provider = null;
    }

    @Override
    public void onStart() {
        super.onStart();
        provider.addEstazioListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        provider.removeEstazioListener(this);
    }

    public ArrayList<Estazioa> getEstazioak(){
        return provider.getEstazioak();
    }




}
