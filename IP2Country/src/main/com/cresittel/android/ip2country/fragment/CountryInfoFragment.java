package com.cresittel.android.ip2country.fragment;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.design.widget.AppBarLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import ru.johnlife.lifetools.fragment.BaseAbstractFragment;
import ru.johnlife.lifetools.tools.StyledStringBuilder;

import com.cresittel.android.ip2country.Constants;
import com.cresittel.android.ip2country.R;
import com.cresittel.android.ip2country.data.CountryInfo;
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
    private TextView tvState;
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

    private String default_ipaddress = "114.134.184.117";
    private String alpha3code;
    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ip2country, container, false);
        EventBus.getDefault().register(this);
        Mapbox.getInstance(this.getContext(), "pk.eyJ1IjoiaGVuZ2x5IiwiYSI6ImNqZTYxcnU3YTAwcWwycXBqajZiaG1iOWsifQ.-meBY3zSlHAjdIIelQPiUg");
        mapView = (MapView) view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        tvContinentName = view.findViewById(R.id.continentName);
        tvCountryName = view.findViewById(R.id.countryName);
        tvCountryName.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                changeFragment(new CountryDetailFragment()
                        .addParam(Constants.COUNTRY_CODE, alpha3code)
                        .addParam(Constants.COUNTRY_NAME, tvCountryName.getText().toString().substring(14)), true);
            }
        });
        tvState = view.findViewById(R.id.subdivisionName);
        tvCityName = view.findViewById(R.id.cityName);
        tvIpAddress = view.findViewById(R.id.ipaddress);
        progress = view.findViewById(R.id.progress);
        submit = view.findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestCountryInfo(tvIpAddress.getText().toString());
            }
        });
        tvIpAddress.setText(default_ipaddress);
        requestCountryInfo(default_ipaddress);
        return view;
    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }

    private void requestCountryInfo(String search) {
        requestService(new BackgroundService.Requester() {
            @Override
            public void requestService(BackgroundService service) {
                progress.setVisibility(View.VISIBLE);
                submit.setVisibility(View.GONE);
                service.requestCountryInfo(search);
            }
        });
    }

    private static String normalize(Context context, String value) {
        return (null == value) ? context.getString(R.string.unknown) : value;
    }

    public static void setText(TextView field, @StringRes int label, String value) {
        Context context = field.getContext();
        StyledStringBuilder b = new StyledStringBuilder(context);
        b   .append(label, R.style.LabelText)
            .append(" " + normalize(context, value), R.style.ImportantText);
        field.setText(b.build());
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onRequestCountryInfo(CountryInfoEvent event) {
        CountryInfo data = event.getData();
        CountryInfo.Region region = data.getRegion();
        setText(tvContinentName, R.string.continent_name, data.getContinent().getName());
        setText(tvCountryName, R.string.country_name, data.getCountry().getName());
        setText(tvState, R.string.state, region.getState());
        setText(tvCityName, R.string.city_name, region.getCity());
        alpha3code = data.getCountry().getAlpha3();
        default_ipaddress = data.getIp();
        mapView.getMapAsync(new OnMapReadyCallback() {
            double Lat = region.getLatitude();
            double Long = region.getLongitude();
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                for (Marker marker : mapboxMap.getMarkers()) {
                    mapboxMap.removeMarker(marker);
                }
                mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(Lat, Long),10));
                mapboxMap.addMarker(new MarkerOptions()
                        .position(new LatLng(Lat, Long))
                        .title(tvCountryName.getText().toString())
                        .snippet(data.getIp())
                        );
                /*if(!mapboxMap.getMarkers().get(0).isInfoWindowShown()){
                    mapboxMap.getMarkers().get(0).showInfoWindow(mapboxMap, mapView);
                }*/
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
        tvState.setText("");
        tvCityName.setText("");
        alpha3code = "";
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                for (Marker marker : mapboxMap.getMarkers()) {
                    mapboxMap.removeMarker(marker);
                }
                mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(0,0), 0));
            }
        });
        progress.setVisibility(View.GONE);
        submit.setVisibility(View.VISIBLE);
    }

}
