package com.trans.api;

import org.json.JSONObject;

public interface APIResponse {

    void onSuccess(JSONObject object);

    void onFailure(String error);

}
