package com.aorbegozo005.dbizi;

import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.ArrayList;

/**
 * Created by ander on 2016/08/08.
 */
public abstract class BaseListenerFragment extends Fragment implements  DBiziDataListener{



    @Override
    public void onStart() {
        super.onStart();
        EstazioProvider provider = (EstazioProvider) getActivity();
        provider.addEstazioListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EstazioProvider provider = (EstazioProvider) getActivity();
        provider.removeEstazioListener(this);
    }

    public ArrayList<Estazioa> getEstazioak(){
        EstazioProvider provider = (EstazioProvider) getActivity();
        return provider.getEstazioak();
    }


}
