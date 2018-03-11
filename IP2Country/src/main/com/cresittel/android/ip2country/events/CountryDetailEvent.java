package com.cresittel.android.ip2country.events;

import com.cresittel.android.ip2country.data.CountryDetail;

/**
 * Created by Hengly on 09-Mar-18.
 */

public class CountryDetailEvent {
    private CountryDetail data;

    public CountryDetail getData() {
        return data;
    }

    public CountryDetailEvent(CountryDetail data) {
        this.data = data;
    }
}
