package com.cresittel.android.ip2country.data;

import ru.johnlife.lifetools.data.JsonData;

/**
 * Created by Yan Yurkin
 * 26 April 2017
 */

public class CountryInfo extends JsonData {
    public static final class Response extends JsonData{
        private String status;
        private CountryInfo data;

        public CountryInfo getData() {
            return data;
        }
    }


    private String ipv4; // "8.8.8.8",
    private String continent_name; // "North America",
    private String country_name; // "United States",
    private String subdivision_1_name; // "California",
    private String subdivision_2_name; // ""
    private String city_name; // "Mountain View",
    private String latitude; // "37.38600",
    private String longitude; // "-122.08380"

    public String getContinentName() {
        return continent_name;
    }

    public String getCountryName() {
        return country_name;
    }

    public String getSubdivisionName() {
        return subdivision_1_name;
    }

    public String getCityName() {
        return city_name;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getIpv4() {
        return  ipv4;
    }

    public boolean isEmpty() {
        return
            null == getContinentName() &&
            null == getCountryName() &&
            null == getSubdivisionName() &&
            null == getCityName();

    }
}



