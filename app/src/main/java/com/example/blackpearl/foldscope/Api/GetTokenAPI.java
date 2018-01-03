package com.example.blackpearl.foldscope.Api;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.blackpearl.foldscope.Model.TokenData;
import com.example.blackpearl.foldscope.R;
import com.example.blackpearl.foldscope.Utils.ApiResponse;
import com.example.blackpearl.foldscope.Utils.Const;
import com.example.blackpearl.foldscope.Utils.ResponseListener;
import com.example.blackpearl.foldscope.Utils.WebInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.UnknownHostException;
import java.util.ArrayList;

public class GetTokenAPI extends AsyncTask<Void, Void, Integer> {

    public Context caller;
    public ResponseListener handler;
    public int result = -1;
    public String UserName, Password;
    public String Tag = "GetTokenAPI";
    private ArrayList<TokenData> tokenDataArrayList;

    public GetTokenAPI(Context context, ResponseListener responseListener, String UserName, String Password) {

        this.caller = context;
        this.handler = responseListener;
        this.UserName = UserName;
        this.Password = Password;
    }

    protected void onPreExecute() {

    }

    protected Integer doInBackground(Void... arg0) {

        ApiResponse apiResponse = null;
        try {
            apiResponse = WebInterface.doPost(Const.URL_TOKEN, "username=" + UserName + "&password=" + Password);
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
            Log.d(Tag, "Get Token Json Response :----->" + apiResponse.response);
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
            this.handler.onResponse(Const.URL_TOKEN,
                    Const.API_RESULT.SUCCESS, tokenDataArrayList);
        } else {
            Toast.makeText(caller, R.string.error_login, Toast.LENGTH_LONG).show();
            this.handler.onResponse(Const.URL_TOKEN,
                    Const.API_RESULT.FAIL, tokenDataArrayList);
        }
    }

    public void Parse(String response) {

        try {
            tokenDataArrayList = new ArrayList<TokenData>();
            JSONObject ResponseJson = new JSONObject(response);
            TokenData tokenData = new TokenData();
            tokenData.setToken(ResponseJson.optString("token"));
            tokenData.setNiceName(ResponseJson.optString("user_nicename"));
            tokenData.setEmail(ResponseJson.optString("user_email"));
            tokenData.setDisplayName(ResponseJson.optString("user_display_name"));
            tokenDataArrayList.add(tokenData);
        } catch (JSONException e) {
        }
    }
}


