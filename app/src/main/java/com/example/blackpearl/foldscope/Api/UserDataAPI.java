package com.example.blackpearl.foldscope.Api;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.blackpearl.foldscope.Model.AuthorData;
import com.example.blackpearl.foldscope.R;
import com.example.blackpearl.foldscope.Utils.ApiResponse;
import com.example.blackpearl.foldscope.Utils.Const;
import com.example.blackpearl.foldscope.Utils.ResponseListener;
import com.example.blackpearl.foldscope.Utils.WebInterface;

import org.json.JSONArray;
import org.json.JSONException;

import java.net.UnknownHostException;
import java.util.ArrayList;

public class UserDataAPI extends AsyncTask<Void, Void, Integer> {

    public Context caller;
    public ResponseListener handler;
    public int result = -1;
    public String Tag = "UserDataAPI";
    private ArrayList<AuthorData> authorDataArrayList;

    public UserDataAPI(Context context, ResponseListener responseListener) {

        this.caller = context;
        this.handler = responseListener;
    }

    protected void onPreExecute() {

    }

    protected Integer doInBackground(Void... arg0) {

        ApiResponse apiResponse = null;
        try {
            apiResponse = WebInterface.doGet(Const.URL_CHECK_USERNAME);
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
            Log.d(Tag, "User Data API Json Response :----->" + apiResponse.response);
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
            this.handler.onResponse(Const.URL_CHECK_USERNAME,
                    Const.API_RESULT.SUCCESS, authorDataArrayList);
        } else {
            Toast.makeText(caller, R.string.server_error, Toast.LENGTH_LONG).show();
            this.handler.onResponse(Const.URL_CHECK_USERNAME,
                    Const.API_RESULT.FAIL, authorDataArrayList);
        }
    }

    public void Parse(String response) {

        authorDataArrayList = new ArrayList<AuthorData>();
        try {
            JSONArray jsonArray = null;
            jsonArray = new JSONArray(response);
            for (int j = 0; j < jsonArray.length(); j++) {
                AuthorData authorData = new AuthorData();
                authorData.setName(jsonArray.getJSONObject(j).optString("name"));
                authorData.setPic_Url(jsonArray.getJSONObject(j).getJSONObject("avatar_urls").optString("96"));
                authorData.setUserId(jsonArray.getJSONObject(j).optString("id"));
                authorData.setSlug(jsonArray.getJSONObject(j).optString("slug"));
                authorDataArrayList.add(authorData);
            }
        } catch (JSONException e) {
        }
    }
}


