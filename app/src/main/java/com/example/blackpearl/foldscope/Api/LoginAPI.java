package com.example.blackpearl.foldscope.Api;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.blackpearl.foldscope.Model.UserData;
import com.example.blackpearl.foldscope.R;
import com.example.blackpearl.foldscope.Utils.ApiResponse;
import com.example.blackpearl.foldscope.Utils.Const;
import com.example.blackpearl.foldscope.Utils.ResponseListener;
import com.example.blackpearl.foldscope.Utils.WebInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.UnknownHostException;
import java.util.ArrayList;

public class LoginAPI extends AsyncTask<Void, Void, Integer> {

    public Context caller;
    public ResponseListener handler;
    public int result = -1;
    public String Token;
    public String Tag = "LoginAPI";
    private ArrayList<UserData> userDataArrayList;

    public LoginAPI(Context context, ResponseListener responseListener, String Token) {

        this.caller = context;
        this.handler = responseListener;
        this.Token = Token;
    }

    protected void onPreExecute() {

    }

    protected Integer doInBackground(Void... arg0) {

        ApiResponse apiResponse = null;
        try {
            apiResponse = WebInterface.doGetLogin(Const.URL_LOGIN, Token);
            result = apiResponse.code;
            if (apiResponse.code == 200) {
                if (apiResponse.response != null
                        && !apiResponse.response.equals("")) {
                    this.Parse(apiResponse.response);
                    result = 0;
                } else {
                    result = -1;
                }
            }
            Log.d(Tag, "Login Json Response :----->" + apiResponse.response);
        } catch (UnknownHostException ue) {
            result = -2;
            ue.printStackTrace();
        } catch (Exception e) {
            result = -3;
            e.printStackTrace();
        }
        return result;
    }

    protected void onPostExecute(Integer result) {

        if (result == 0) {
            this.handler.onResponse(Const.URL_LOGIN,
                    Const.API_RESULT.SUCCESS, userDataArrayList);
        } else {
            Toast.makeText(caller, R.string.server_error, Toast.LENGTH_LONG).show();
            this.handler.onResponse(Const.URL_LOGIN,
                    Const.API_RESULT.FAIL, userDataArrayList);
        }
    }

    public void Parse(String response) {

        try {
            userDataArrayList = new ArrayList<UserData>();
            JSONObject ResponseJson = new JSONObject(response);
            UserData userData = new UserData();
            userData.setUserId(ResponseJson.optString("id"));
            userData.setUserName(ResponseJson.optString("slug"));
            userData.setSlug(ResponseJson.optString("slug"));
            userData.setAvatar(ResponseJson.getJSONObject("avatar_urls").optString("96"));
            userDataArrayList.add(userData);
        } catch (JSONException e) {
        }
    }
}


