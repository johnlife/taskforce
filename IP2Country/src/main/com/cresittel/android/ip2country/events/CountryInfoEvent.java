package com.cresittel.android.ip2country.events;

import com.cresittel.android.ip2country.data.CountryInfo;

/**
 * Created by Yan Yurkin
 * 26 April 2017
 */

public class CountryInfoEvent {
    private CountryInfo data;

    public CountryInfoEvent(CountryInfo data) {
        this.data = data;
    }

    public CountryInfo getData() {
        return data;
    }
}
