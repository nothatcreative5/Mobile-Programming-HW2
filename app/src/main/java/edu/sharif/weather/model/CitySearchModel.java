package edu.sharif.weather.model;

import ir.mirrajabi.searchdialog.core.Searchable;

public class CitySearchModel implements Searchable {
    private String mTitle;

    public CitySearchModel(String title) {
        mTitle = title;
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    public CitySearchModel setTitle(String title) {
        mTitle = title;
        return this;
    }
}
