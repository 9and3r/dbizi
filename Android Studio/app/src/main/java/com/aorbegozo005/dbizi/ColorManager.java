package com.aorbegozo005.dbizi;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;

/**
 * Created by ander on 2016/08/19.
 */
public class ColorManager {

    private ColorManager(){

    }

    public static int getColor(Context c, Estazioa estazioa, boolean base){
        int color = ContextCompat.getColor(c, R.color.availabilityDisabled);
        if (estazioa.active == 0){
            int available;
            if (base){
                available = estazioa.free;
            }else{
                available = estazioa.lock;
            }
            color = getColorFromValue(c, available);
        }
        return color;
    }

    public static int getColorFromValue(Context c, int pValue){
        int color;
        if (pValue < 1){
            color = ContextCompat.getColor(c, R.color.availabilityNone);
        }else if (pValue < 3){
            color = ContextCompat.getColor(c, R.color.availabilityLow);
        }else if (pValue < 5){
            color = ContextCompat.getColor(c, R.color.availabilityMedium);
        }else if (pValue < 8){
            color = ContextCompat.getColor(c, R.color.availabilityHigh);
        }else{
            color = ContextCompat.getColor(c, R.color.availabilityVeryHigh);
        }
        return color;
    }

    public static float getColorHUE(Context c, Estazioa estazioa, boolean base){
        float color = 0;
        if (estazioa.active == 0){
            int available;
            if (base){
                available = estazioa.free;
            }else{
                available = estazioa.lock;
            }

            if (available < 1){
                color = BitmapDescriptorFactory.HUE_RED;
            }else if (available < 3){
                color = BitmapDescriptorFactory.HUE_ORANGE;
            }else if (available < 5){
                color = BitmapDescriptorFactory.HUE_YELLOW;
            }else if (available < 8){
                color = BitmapDescriptorFactory.HUE_GREEN;
            }else{
                color = BitmapDescriptorFactory.HUE_GREEN;
            }

        }
        return color;
    }

}
