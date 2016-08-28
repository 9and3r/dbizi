package com.aorbegozo005.dbizi;

import java.util.ArrayList;

/**
 * Created by ander on 2016/08/08.
 */
public interface EstazioProvider extends DBiziDataListener{

    void addEstazioListener(DBiziDataListener listener);

    void removeEstazioListener(DBiziDataListener listener);

    ArrayList<Estazioa> getEstazioak();

    Estazioa getSelected();

    void setSelected(Estazioa estazioa);
}
