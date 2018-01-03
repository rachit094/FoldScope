package com.example.blackpearl.foldscope.Api;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.blackpearl.foldscope.Utils.ApiResponse;
import com.example.blackpearl.foldscope.Utils.Const;
import com.example.blackpearl.foldscope.Utils.ResponseListener;
import com.example.blackpearl.foldscope.Utils.WebInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.UnknownHostException;

public class UploadFileAPI extends AsyncTask<Void, Void, Integer> {

    public Context caller;
    public ResponseListener handler;
    public int result = -1;
    public String FilePath, Token, Type;
    String SourceUrl, Message;
    public String Tag = "UploadFileAPI";

    public UploadFileAPI(Context context, ResponseListener responseListener, String FilePath, String Token, String Type) {

        this.caller = context;
        this.handler = responseListener;
        this.FilePath = FilePath;
        this.Token = Token;
        this.Type = Type;
    }

    protected void onPreExecute() {

    }

    protected Integer doInBackground(Void... arg0) {

        ApiResponse apiResponse = null;
        try {
            apiResponse = WebInterface.doPostForUploadFile(Const.URL_UPLOAD_MEDIA, Type, Token, FilePath);
            result = apiResponse.code;
            if (apiResponse.response != null
                    && !apiResponse.response.equals("")) {
                this.Parse(apiResponse.response);
                result = 0;
            } else {
                result = -1;
            }
            Log.d(Tag, "File Upload Json Response :----->" + apiResponse.response);
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
            this.handler.onResponse(Const.URL_UPLOAD_MEDIA,
                    Const.API_RESULT.SUCCESS, SourceUrl);
        } else {
            Toast.makeText(caller, "Error While Uploading Media File Please Try again.", Toast.LENGTH_LONG).show();
            this.handler.onResponse(Const.URL_UPLOAD_MEDIA,
                    Const.API_RESULT.FAIL, Message);
        }
    }

    public void Parse(String response) {

        try {
            JSONObject Response = new JSONObject(response);
            SourceUrl = Response.optString("source_url");
            if (SourceUrl != null) {
            } else {
                Message = Response.optString("code");
                result = -1;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}


