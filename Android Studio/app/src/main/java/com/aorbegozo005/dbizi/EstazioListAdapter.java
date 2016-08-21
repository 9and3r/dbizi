package com.aorbegozo005.dbizi;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;

public class EstazioListAdapter extends RecyclerView.Adapter<EstazioItemHolder> {

    private ArrayList<Estazioa> estazioak;

    public EstazioListAdapter(){
        estazioak = new ArrayList<>(0);
        setHasStableIds(true);
    }

    @Override
    public long getItemId(int position) {
        return estazioak.get(position).number;
    }

    public void setEstazioak(ArrayList<Estazioa> pEstazioak){
        estazioak = pEstazioak;
        notifyDataSetChanged();
    }

    @Override
    public EstazioItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_layout, parent, false);
        return new EstazioItemHolder(v);
    }

    @Override
    public void onBindViewHolder(EstazioItemHolder holder, int position) {
        holder.setEstazioa(estazioak.get(position));
    }

    @Override
    public int getItemCount() {
        return estazioak.size();
    }
}
