package com.cresittel.android.ip2country.fragment;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import ru.johnlife.lifetools.fragment.BaseAbstractFragment;
import com.cresittel.android.ip2country.R;
import com.cresittel.android.ip2country.events.CountryInfoEvent;
import com.cresittel.android.ip2country.events.InvalidRequestEvent;
import com.cresittel.android.ip2country.service.BackgroundService;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

/**
 * Created by Yan Yurkin
 * 05 June 2016
 */
public class CountryInfoFragment extends BaseAbstractFragment{
    private MapView mapView;
    private TextView tvContinentName;
    private TextView tvCountryName;
    private TextView tvSubdivisonName;
    private TextView tvCityName;
    private TextView tvIpAddress;
    private TextView submit;
    private View progress;
    @Override
    protected String getTitle(Resources res) {
        return res.getString(R.string.app_name);
    }

    @Override
    protected AppBarLayout getToolbar(LayoutInflater inflater, ViewGroup container) {
        return defaultToolbar();
    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ip2country, container, false);
        EventBus.getDefault().register(this);
        Mapbox.getInstance(this.getContext(), "pk.eyJ1IjoiaGVuZ2x5IiwiYSI6ImNqZTYxcnU3YTAwcWwycXBqajZiaG1iOWsifQ.-meBY3zSlHAjdIIelQPiUg");
        mapView = (MapView) view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        tvContinentName = view.findViewById(R.id.continentName);
        tvCountryName = view.findViewById(R.id.countryName);
        tvSubdivisonName = view.findViewById(R.id.subdivisionName);
        tvCityName = view.findViewById(R.id.cityName);
        tvIpAddress = view.findViewById(R.id.ipaddress);
        progress = view.findViewById(R.id.progress);
        submit = view.findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestCountryName(tvIpAddress.getText().toString());
            }
        });
        return view;
    }

    private void requestCountryName(String search) {
        requestService(new BackgroundService.Requester() {
            @Override
            public void requestService(BackgroundService service) {
                progress.setVisibility(View.VISIBLE);
                submit.setVisibility(View.GONE);
                service.requestCountryName(search);
            }
        });
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onRequestCountryInfo(CountryInfoEvent event) {

        tvContinentName.setText(event.getData().getData().getContinent_name());
        tvCountryName.setText(event.getData().getData().getCountry_name());
        tvSubdivisonName.setText(event.getData().getData().getSubdivision_1_name());
        tvCityName.setText(event.getData().getData().getCity_name());
        mapView.getMapAsync(new OnMapReadyCallback() {
            double Lat = Double.parseDouble(event.getData().getData().getLatitude());
            double Long = Double.parseDouble(event.getData().getData().getLongitude());
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(Lat, Long),10));
                for (Marker marker : mapboxMap.getMarkers()
                     ) {
                    mapboxMap.removeMarker(marker);
                }
                mapboxMap.addMarker(new MarkerOptions()
                        .position(new LatLng(Lat, Long))
                        .title(tvCountryName.getText().toString())
                        .snippet(event.getData().getData().getIpv4()));
            }
        });
        progress.setVisibility(View.GONE);
        submit.setVisibility(View.VISIBLE);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onInvalidRequest(InvalidRequestEvent event)
    {
        tvContinentName.setText("Invalid Request or No Internet connection");
        tvCountryName.setText("");
        tvSubdivisonName.setText("");
        tvCityName.setText("");
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(0,0), 0));
                for (Marker marker : mapboxMap.getMarkers()
                        ) {
                    mapboxMap.removeMarker(marker);
                }
            }
        });
        progress.setVisibility(View.GONE);
        submit.setVisibility(View.VISIBLE);
    }

}
