package com.cresittel.android.ip2country.fragment;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cresittel.android.ip2country.R;
import com.cresittel.android.ip2country.data.CountryDetail;
import com.cresittel.android.ip2country.events.CountryDetailEvent;
import com.cresittel.android.ip2country.events.InvalidRequestEvent;
import com.cresittel.android.ip2country.service.BackgroundService;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import ru.johnlife.lifetools.fragment.BaseAbstractFragment;

import static com.cresittel.android.ip2country.Constants.COUNTRY_CODE;
import static com.cresittel.android.ip2country.Constants.COUNTRY_NAME;


/**
 * Created by Hengly on 09-Mar-18.
 */

public class CountryDetailFragment extends BaseAbstractFragment {

    private ImageView flag;
    private TextView tvCountryName;
    private TextView tvAlpha3code;
    private TextView tvCapital;
    private TextView tvPopulation;
    private TextView tvArea;
    private TextView tvBorder;
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
        View view = inflater.inflate(R.layout.fragment_ip2country_country_detail, container, false);
        EventBus.getDefault().register(this);
        flag = view.findViewById(R.id.flag);
        tvCountryName = view.findViewById(R.id.countryName);
        tvCountryName.setText(getParams().getString(COUNTRY_NAME));
        tvAlpha3code = view.findViewById(R.id.alpha3code);
        tvCapital = view.findViewById(R.id.capital);
        tvPopulation = view.findViewById(R.id.population);
        tvArea = view.findViewById(R.id.area);
        tvBorder = view.findViewById(R.id.border);
        requestCountryDetail(getParams().getString(COUNTRY_CODE));
        return view;
    }

    private void requestCountryDetail(String alpha3code) {
        requestService(new BackgroundService.Requester() {
            @Override
            public void requestService(BackgroundService service) {
                service.requestCountryDetail(alpha3code);
            }
        });
    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }

    private static String normalize(String content){
        return (content.isEmpty()) ? "None":content;
    }
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onRequestCountryDetail(CountryDetailEvent event) {
        CountryDetail data = event.getData();
        Picasso.with(flag.getContext()).load(data.getPngFlag()).into(flag);
        CountryInfoFragment.setText(tvAlpha3code, R.string.alpha3code, getParams().getString(COUNTRY_CODE));
        CountryInfoFragment.setText(tvCapital, R.string.capital, data.getCapital());
        CountryInfoFragment.setText(tvPopulation, R.string.population, data.getPopulation());
        CountryInfoFragment.setText(tvArea, R.string.area, data.getArea() + " km²");
        CountryInfoFragment.setText(tvBorder, R.string.border, normalize(data.getOneLineBorder()));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onInvalidRequest(InvalidRequestEvent event)
    {
        Picasso.with(flag.getContext()).load("https://fthmb.tqn.com/4qMvIZ33Qlf6S9rdbLA-0JZF6rU=/768x0/filters:no_upscale()/shutterstock_325494917-5a68d8403418c600190a3e1f.jpg").into(flag);
        tvCountryName.setText("Error Loading Data");
        tvAlpha3code.setText("");
        tvCapital.setText("");
        tvPopulation.setText("");
        tvArea.setText("");
        tvBorder.setText("");
    }
}
