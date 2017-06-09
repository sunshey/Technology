package com.wl.technology.bean;

import java.util.List;

/**
 * Created by wanglin  on 2017/6/5 08:47.
 */

public class DataInfo {

    private Long id;

    private boolean error;


    private List<DataItem> results;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<DataItem> getResults() {
        return results;
    }

    public void setResults(List<DataItem> results) {
        this.results = results;
    }

    public boolean getError() {
        return this.error;
    }



}
