package com.trans.sharedPrefrence;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by acer on 30-06-2017.
 */
public class AppPrefrece {
    public static AppPrefrece appPrefrece;
    SharedPreferences.Editor edt;
    String AppPrefrece = "AppPrefrece";
    String UserData = "UserData";
    String ISLOGIN = "isLogin";
    String USER_ID = "user_id";
    String EMAIL = "email";
    String ACCESS_TOKEN = "access_token";
    String PROFILE_PIC = "profiloe_pic";
    String NAME = "name";
    SharedPreferences sp;

    public AppPrefrece(Context context) {
        sp = context.getSharedPreferences(AppPrefrece, Context.MODE_PRIVATE);
        edt = sp.edit();
        appPrefrece = this;
    }

    public static AppPrefrece getInstance() {
        return appPrefrece;
    }

    public void remove() {
        edt.remove(ISLOGIN);
        edt.remove(USER_ID);
        edt.remove(ACCESS_TOKEN);
        edt.remove(EMAIL);
        edt.remove(NAME);
        edt.remove(PROFILE_PIC);
        edt.commit();
    }

    public JSONObject getProfile() throws JSONException {
        String strJson = sp.getString(UserData, "0");//second parameter is necessary ie.,Value to return if this preference does not exist.
        JSONObject jsonData = new JSONObject(strJson);
        return jsonData;
    }

    public void setProfile(JSONObject profile) {
        edt.putString(UserData, profile.toString());
        edt.commit();
    }

    public String getUserId() {
        return sp.getString(USER_ID, "");
    }

    public void setUserId(String user_id) {
        edt.putString(USER_ID, user_id);
        edt.commit();
    }

    public String getEmail() {
        return sp.getString(EMAIL, "");
    }

    public void setEmail(String email) {
        edt.putString(EMAIL, email);
        edt.commit();
    }

    public String getName() {
        return sp.getString(NAME, "");
    }

    public void setName(String name) {
        edt.putString(NAME, name);
        edt.commit();
    }

    public String getProfilePic() {
        return sp.getString(PROFILE_PIC, "");
    }

    public void setProfilePic(String url) {
        edt.putString(PROFILE_PIC, url);
        edt.commit();
    }

    public Boolean getLogin() {
        return sp.getBoolean(ISLOGIN, false);
    }

    public void setLogin(Boolean login) {
        edt.putBoolean(ISLOGIN, login);
        edt.commit();
    }

    public String getAccessToken() {
        return sp.getString(ACCESS_TOKEN, "");
    }

    public void setAccessToken(String siteToken) {
        edt.putString(ACCESS_TOKEN, siteToken);
        edt.commit();

    }

}
