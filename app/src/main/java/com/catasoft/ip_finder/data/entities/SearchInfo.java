package com.catasoft.ip_finder.data.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.catasoft.ip_finder.data.room.RoomConfig;

import java.io.Serializable;

@Entity(tableName = RoomConfig.SEARCHES_TABLE)
public class SearchInfo implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private long searchId;

    private String country;
    private String regionName;
    private String city;
    private double lat;
    private double lon;
    private String timezone;
    private String isp;
    private String query;
    private String createdAt;

    private long userId;
    private int previousUserSearchInfo;

    public SearchInfo(String country, String regionName, String city, double lat, double lon,
                      String timezone, String isp, String query, String createdAt, long userId,
                      int previousUserSearchInfo) {
        this.country = country;
        this.regionName = regionName;
        this.city = city;
        this.lat = lat;
        this.lon = lon;
        this.timezone = timezone;
        this.isp = isp;
        this.query = query;
        this.createdAt = createdAt;
        this.userId = userId;
        this.previousUserSearchInfo = previousUserSearchInfo;
    }

    public long getSearchId() {
        return searchId;
    }

    public void setSearchId(long searchId) {
        this.searchId = searchId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getIsp() {
        return isp;
    }

    public void setIsp(String isp) {
        this.isp = isp;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getPreviousUserSearchInfo() {
        return previousUserSearchInfo;
    }

    public void setPreviousUserSearchInfo(int previousUserSearchInfo) {
        this.previousUserSearchInfo = previousUserSearchInfo;
    }
}
