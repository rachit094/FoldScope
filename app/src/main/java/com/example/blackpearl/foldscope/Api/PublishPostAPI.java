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

public class PublishPostAPI extends AsyncTask<Void, Void, Integer> {

    public Context caller;
    public ResponseListener handler;
    public int result = -1;
    public String Title, Content, Status, Token;
    String EventId, Message;
    public String Tag = "PublishPostAPI";

    public PublishPostAPI(Context context, ResponseListener responseListener, String Title, String Content, String Status, String Token) {

        this.caller = context;
        this.handler = responseListener;
        this.Title = Title;
        this.Content = Content;
        this.Status = Status;
        this.Token = Token;
    }

    protected void onPreExecute() {

    }

    protected Integer doInBackground(Void... arg0) {

        ApiResponse apiResponse = null;
        try {
            apiResponse = WebInterface.doPostForPostBlog(Const.URL_POST_BLOG, "status=" + Status + "&title=" + Title + "&content=" + Content, Token);
            System.out.println();
            result = apiResponse.code;
//            if (apiResponse.code == 200) {
            if (apiResponse.response != null
                    && !apiResponse.response.equals("")) {
                this.Parse(apiResponse.response);
                result = 0;
            } else {
                result = -1;
            }
//            }
            Log.d(Tag, "Publish Post Json Response :----->" + apiResponse.response);
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
            this.handler.onResponse(Const.URL_POST_BLOG,
                    Const.API_RESULT.SUCCESS, EventId);
        } else {
            Toast.makeText(caller, "Error While Publish Your Post Please Try again.", Toast.LENGTH_LONG).show();
            this.handler.onResponse(Const.URL_POST_BLOG,
                    Const.API_RESULT.FAIL, Message);
        }
    }

    public void Parse(String response) {

        try {
            JSONObject Response = new JSONObject(response);
            EventId = Response.optString("id");
            if (EventId != null) {
            } else {
                Message = Response.optString("code");
                result = -1;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}


