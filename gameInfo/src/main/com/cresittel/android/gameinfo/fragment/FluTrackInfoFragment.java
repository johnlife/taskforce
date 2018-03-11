package com.cresittel.android.gameinfo.fragment;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cresittel.android.gameinfo.Constants;
import com.cresittel.android.gameinfo.R;
import com.cresittel.android.gameinfo.events.FluTrackListEvent;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.constants.Style;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

import ru.johnlife.lifetools.fragment.BaseAbstractFragment;

import static com.mapbox.mapboxsdk.Mapbox.getApplicationContext;

/**
 * Created by Encore on 2/27/2018.
 */

public class FluTrackInfoFragment extends BaseAbstractFragment implements Constants{
    @Override
    protected String getTitle(Resources r) {
        return "Location";
    }

    @Override
    protected AppBarLayout getToolbar(LayoutInflater inflater, ViewGroup container) {
        return defaultToolbar();
    }

    private Double lat;
    private Double lng;
    private MapView mapView;

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View    view = inflater.inflate(R.layout.item_info, container, false);
        Bundle   params = getParams();
       // final MapView mapView;
        lat= Double.parseDouble(params.getString(PARAM_LATITUDE));
        lng= Double.parseDouble(params.getString(PARAM_LONGITUDE));
        super.onCreate(savedInstanceState);
        mapView = view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.setStyleUrl(Style.MAPBOX_STREETS);

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                mapboxMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng))
                        .title( params.getString(PARAM_USERNAME)).snippet(params.getString(PARAM_TWEET_TEXT)));
                CameraPosition position = new CameraPosition.Builder().target(new LatLng(lat, lng))
                        .zoom(7)
                        .tilt(0)
                        .build();
                mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position),5000);
            }
        });

        return view;

    }

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    //    mapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
     //   mapView.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
   // mapView.onResume();
    }

   @Override
    public void onDestroy() {
        super.onDestroy();
     //   mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
       //  mapView.onLowMemory();
    }

    @Override
    public void onSaveInstanceState( Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
}
