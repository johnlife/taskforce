package com.cresittel.android.ip2country.data;

import com.fasterxml.jackson.annotation.JsonProperty;

import ru.johnlife.lifetools.data.JsonData;

/**
 * Created by Yan Yurkin
 * 26 April 2017
 */

public class CountryInfo extends JsonData {
    private String status;
    private String ip;
    private Continent continent;
    private Country country;
    private Region region;

    public String getStatus() {
        return status;
    }

    public String getIp() {
        return ip;
    }

    public Continent getContinent() {
        return continent;
    }

    public Country getCountry() {
        return country;
    }

    public Region getRegion() {
        return region;
    }

    public class Country{
        @JsonProperty(value="alpha-2")
        private String alpha2;

        @JsonProperty(value="alpha-3")
        private String alpha3;

        private String name;

        private String phone;

        public String getAlpha2() {
            return alpha2;
        }

        public String getPhone() {
            return phone;
        }

        public String getAlpha3() {
            return alpha3;
        }

        public String getName() {
            return name;
        }
    }
    public class Continent{
        private String code;
        private String name;

        public String getCode() {
            return code;
        }

        public String getName() {
            return name;
        }
    }

    public class Region{
        private String city;
        private String state;
        private String postal;
        private double latitude;
        private double longitude;

        public String getCity() {
            return city;
        }

        public String getState() {
            return state;
        }

        public String getPostal() {
            return postal;
        }

        public double getLatitude() {
            return latitude;
        }

        public double getLongitude() {
            return longitude;
        }
    }
}