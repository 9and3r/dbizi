package com.aorbegozo005.dbizi;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by ander on 2016/08/08.
 */
public class EstazioItemHolder extends RecyclerView.ViewHolder {

    private TextView nombreTextView;
    private TextView direccionTextView;
    private CircularValueView biciLibresTextView;
    private CircularValueView basesLibresTextView;


    public EstazioItemHolder(View v) {
        super(v);
        nombreTextView = (TextView) v.findViewById(R.id.textViewNombre);
        direccionTextView = (TextView) v.findViewById(R.id.textViewDireccion);
        biciLibresTextView = (CircularValueView) v.findViewById(R.id.textViewBicicletasLibres);
        basesLibresTextView = (CircularValueView) v.findViewById(R.id.textViewBasesLibres);
        basesLibresTextView.setReversed(true);
    }

    public void setEstazioa(Estazioa estazioa){
        nombreTextView.setText(estazioa.name);
        direccionTextView.setText(estazioa.address);

        biciLibresTextView.setEnabled(estazioa.active == 0);
        basesLibresTextView.setEnabled(estazioa.active == 0);
        biciLibresTextView.setValue(estazioa.lock);
        basesLibresTextView.setValue(estazioa.free);
        biciLibresTextView.setMaxValue(estazioa.lock + estazioa.free);
        basesLibresTextView.setMaxValue(estazioa.lock + estazioa.free);
    }
}
