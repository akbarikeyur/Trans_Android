package com.trans.api;

import org.json.JSONArray;

public interface APIResponseArray {
    void onSuccessArray(JSONArray array);

    void onFailureArray(String error);
}
