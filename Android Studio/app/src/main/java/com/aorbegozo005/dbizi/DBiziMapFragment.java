package com.aorbegozo005.dbizi;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.StreetViewPanoramaFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.StreetViewPanoramaCamera;
import com.google.android.gms.maps.model.StreetViewPanoramaLocation;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class DBiziMapFragment extends BaseListenerFragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, DBiziDataListener, OnStreetViewPanoramaReadyCallback {

    private static final int paddingDP = 32;

    private int paddingPX;

    private Estazioa currentEstazioa;

    private GoogleMap map;
    private StreetViewPanorama streetViewPanorama;
    private BottomSheetBehavior mBottomSheetBehavior;
    private View bottomSheet;
    private View panoramaContainer;

    private EstazioItemHolder estazioItemHolder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.map_layout, container, false);

        //estazioItemHolder = new EstazioItemHolder(v.findViewById(R.id.base_item_id));


        bottomSheet = v.findViewById(R.id.bottom_sheet);
        estazioItemHolder = new EstazioItemHolder(bottomSheet);
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehavior.setPeekHeight(300);
        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN || newState == BottomSheetBehavior.STATE_COLLAPSED){
                    panoramaContainer.setAlpha(0);
                }else if ( newState == BottomSheetBehavior.STATE_EXPANDED){
                    if (map != null && currentEstazioa != null){
                        map.animateCamera(CameraUpdateFactory.newLatLng(currentEstazioa.getLocation()));
                    }
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                if (map != null){
                    map.setPadding(0, 0, 0, (int) (slideOffset * bottomSheet.getHeight()));
                }
            }
        });

        panoramaContainer = v.findViewById(R.id.panorama_container);

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        paddingPX = Math.round(paddingDP * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));


        MapFragment mapFragment = (MapFragment) getActivity().getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        StreetViewPanoramaFragment streetViewPanoramaFragment =
                (StreetViewPanoramaFragment) getActivity().getFragmentManager()
                        .findFragmentById(R.id.streetviewpanorama);
        streetViewPanoramaFragment.getStreetViewPanoramaAsync(this);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        currentEstazioa = (Estazioa) marker.getTag();
        marker.showInfoWindow();
        provider.setSelected(currentEstazioa);

        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setOnMarkerClickListener(this);
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                provider.setSelected(null);
            }
        });
        loadBidegorris();
        onEstazioakLoaded(getEstazioak());
    }

    private void loadBidegorris() {
        try {
            JSONObject jsonObject = new JSONObject(convertStreamToString(getResources().openRawResource(R.raw.bidegorri)));
            JSONArray array = jsonObject.getJSONArray("features");
            for (int i=0; i<array.length(); i++){
                PolylineOptions options = new PolylineOptions();
                options.color(Color.RED);
                options.width(12);
                JSONObject geometry = array.getJSONObject(i).getJSONObject("geometry");
                if (geometry.getString("type").equals("LineString")){
                    JSONArray points = geometry.getJSONArray("coordinates");
                    for (int z=0; z<points.length(); z++){
                        LatLng latLng = new LatLng(points.getJSONArray(z).getDouble(1), points.getJSONArray(z).getDouble(0));
                        options.add(latLng);
                    }
                    map.addPolyline(options);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }

    @Override
    public void onEstazioakLoaded(ArrayList<Estazioa> estazioak) {

        if (map != null){
            int size = estazioak.size();

            LatLngBounds.Builder builder = new LatLngBounds.Builder();

            for (int i = 0; i < size; i++) {
                Estazioa estazioa = estazioak.get(i);
                Marker marker = map.addMarker(new MarkerOptions()
                        .position(estazioa.getLocation())
                        .title(estazioa.name)
                        .icon(BitmapDescriptorFactory.defaultMarker(ColorManager.getColorHUE(getContext(), estazioa, true))));
                marker.setTag(estazioa);
                builder.include(estazioa.getLocation());
            }

            if (size > 0){
                // Move camera
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(builder.build(), paddingPX);
                map.animateCamera(cameraUpdate);
            }
        }
    }

    @Override
    public void onEstazioaSelected(Estazioa previous, Estazioa selected) {
        if (selected != null){
            estazioItemHolder.setEstazioa(currentEstazioa);
            panoramaContainer.setAlpha(0);
            streetViewPanorama.setOnStreetViewPanoramaChangeListener(new StreetViewPanorama.OnStreetViewPanoramaChangeListener() {
                @Override
                public void onStreetViewPanoramaChange(StreetViewPanoramaLocation streetViewPanoramaLocation) {
                    if (streetViewPanoramaLocation != null){
                        Location location1 = new Location("FAKE_GPS");
                        location1.setLatitude(currentEstazioa.latitude);
                        location1.setLongitude(currentEstazioa.longitude);

                        Location location2 = new Location("FAKE_GPS");
                        location2.setLatitude(streetViewPanoramaLocation.position.latitude);
                        location2.setLongitude(streetViewPanoramaLocation.position.longitude);

                        StreetViewPanoramaCamera camera = new StreetViewPanoramaCamera.Builder()
                                .zoom(streetViewPanorama.getPanoramaCamera().zoom)
                                .tilt(0)
                                .bearing(location2.bearingTo(location1))
                                .build();

                        streetViewPanorama.animateTo(camera, 0);
                        panoramaContainer.setAlpha(1);
                    }
                }
            });
            streetViewPanorama.setPosition(currentEstazioa.getLocation());
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }else{
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }

    @Override
    public void onStreetViewPanoramaReady(StreetViewPanorama pStreetViewPanorama) {
        streetViewPanorama = pStreetViewPanorama;
    }
}
