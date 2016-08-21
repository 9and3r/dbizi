package com.aorbegozo005.dbizi;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by ander on 2016/08/07.
 */
public class Estazioa {

    public int id;
    public double latitude;
    public double longitude;
    public String name;
    public int free;
    public int lock;
    public int light;
    public int active;
    public int number;
    public String address;

    private LatLng location;


    @Override
    public boolean equals(Object obj) {
        if (obj instanceof  Estazioa){
            Estazioa estazioa = (Estazioa) obj;
            return (estazioa.number == this.number);
        }else{
            return super.equals(obj);
        }
    }

    public LatLng getLocation(){
        if (location == null){
            location = new LatLng(latitude, longitude);
        }
        return location;
    }

}
