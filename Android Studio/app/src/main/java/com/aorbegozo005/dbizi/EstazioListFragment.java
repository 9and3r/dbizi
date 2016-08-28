package com.aorbegozo005.dbizi;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by ander on 2016/08/08.
 */
public class EstazioListFragment extends BaseListenerFragment {

    private RecyclerView recyclerView;
    private EstazioListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.list_layout, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        adapter = new EstazioListAdapter();
        recyclerView.setAdapter(adapter);
        return v;
    }

    @Override
    public void onEstazioakLoaded(ArrayList<Estazioa> estazioak) {
        adapter.setEstazioak(estazioak);
    }

    @Override
    public void onEstazioaSelected(Estazioa previous, Estazioa selected) {

    }



}
