package com.drcita.user.models.home;

import androidx.annotation.NonNull;

public class SearchRequest
{
    private String searchStr;

    public String getSearchStr() {
        return searchStr;
    }

    public void setSearchStr(String searchStr) {
        this.searchStr = searchStr;
    }

    public SearchRequest(String searchStr) {
        this.searchStr = searchStr;
    }

    @NonNull
    @Override
    public String toString() {
        return "SearchRequest{" +
                "searchStr='" + searchStr + '\'' +
                '}';
    }
}
