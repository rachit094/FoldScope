package com.example.blackpearl.foldscope.Api;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.blackpearl.foldscope.Model.AllPostData;
import com.example.blackpearl.foldscope.R;
import com.example.blackpearl.foldscope.Utils.ApiResponse;
import com.example.blackpearl.foldscope.Utils.Const;
import com.example.blackpearl.foldscope.Utils.ResponseListener;
import com.example.blackpearl.foldscope.Utils.WebInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class RegistrationAPI extends AsyncTask<Void, Void, Integer> {

    public Context caller;
    public ResponseListener handler;
    public int result = -1;
    public String DisplayName, UserName, Nonce, Email, Password, Userid;
    public String Tag = "RegistrationAPI";
    private ArrayList<AllPostData> allPostDataArrayList;

    public RegistrationAPI(Context context, ResponseListener responseListener, String DN, String UN, String Nonce, String Email, String Password) {

        this.caller = context;
        this.handler = responseListener;
        this.DisplayName = DN;
        this.UserName = UN;
        this.Email = Email;
        this.Password = Password;
        this.Nonce = Nonce;
    }

    protected void onPreExecute() {

    }

    protected Integer doInBackground(Void... arg0) {

        ApiResponse apiResponse = null;
        try {
            Log.d(Tag, "Registration URL :----->" + Const.URL_REGISTRATION + "nonce=" + URLEncoder.encode(Nonce) + "&display_name=" +
                    URLEncoder.encode(DisplayName) + "&username=" + URLEncoder.encode(UserName) + "&email=" + Email +
                    "&password=" + URLEncoder.encode(Password) + "&insecure=cool");
            apiResponse = WebInterface.doGet(Const.URL_REGISTRATION + "nonce=" + URLEncoder.encode(Nonce) + "&display_name=" +
                    URLEncoder.encode(DisplayName) + "&username=" + URLEncoder.encode(UserName) + "&email=" + Email +
                    "&password=" + URLEncoder.encode(Password) + "&insecure=cool");
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
            Log.d(Tag, "Registration Json Response :----->" + apiResponse.response);
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
            this.handler.onResponse(Const.URL_REGISTRATION,
                    Const.API_RESULT.SUCCESS, Userid);
        } else {
            Toast.makeText(caller, R.string.server_error, Toast.LENGTH_LONG).show();
            this.handler.onResponse(Const.URL_REGISTRATION,
                    Const.API_RESULT.FAIL, Userid);
        }
    }

    public void Parse(String response) {

        try {
            JSONObject ResponseJson = new JSONObject(response);
            Userid = ResponseJson.optString("user_id");
        } catch (JSONException e) {
        }
    }
}



