package com.example.blackpearl.foldscope.Api;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.blackpearl.foldscope.R;
import com.example.blackpearl.foldscope.Utils.ApiResponse;
import com.example.blackpearl.foldscope.Utils.Const;
import com.example.blackpearl.foldscope.Utils.ResponseListener;
import com.example.blackpearl.foldscope.Utils.WebInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.net.UnknownHostException;

public class ForgotPasswordAPI extends AsyncTask<Void, Void, Integer> {

    public Context caller;
    public ResponseListener handler;
    public int result = -1;
    public String UserName;
    public String Message;
    public String Tag = "ForgotPasswordAPI";

    public ForgotPasswordAPI(Context context, ResponseListener responseListener, String UN) {

        this.caller = context;
        this.handler = responseListener;
        this.UserName = UN;
    }

    protected void onPreExecute() {

    }

    protected Integer doInBackground(Void... arg0) {

        ApiResponse apiResponse = null;
        try {
            apiResponse = WebInterface.doGet(Const.URL_FORGOT_PASSWORD + URLEncoder.encode(UserName) + "&insecure=cool");
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
            Log.d(Tag, "Forgot Password Json Response :----->" + apiResponse.response);
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
            this.handler.onResponse(Const.URL_FORGOT_PASSWORD,
                    Const.API_RESULT.SUCCESS, Message);
        } else {
            Toast.makeText(caller, R.string.server_error, Toast.LENGTH_LONG).show();
            this.handler.onResponse(Const.URL_FORGOT_PASSWORD,
                    Const.API_RESULT.FAIL, Message);
        }
    }

    public void Parse(String response) {

        try {
            JSONObject ResponseJson = new JSONObject(response);
            Message = ResponseJson.optString("msg");
        } catch (JSONException e) {
        }
    }
}


