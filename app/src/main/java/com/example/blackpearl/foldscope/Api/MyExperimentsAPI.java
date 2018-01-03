package com.example.blackpearl.foldscope.Api;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.blackpearl.foldscope.Model.MyExperimentData;
import com.example.blackpearl.foldscope.R;
import com.example.blackpearl.foldscope.Utils.ApiResponse;
import com.example.blackpearl.foldscope.Utils.Const;
import com.example.blackpearl.foldscope.Utils.ResponseListener;
import com.example.blackpearl.foldscope.Utils.WebInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyExperimentsAPI extends AsyncTask<Void, Void, Integer> {

    public Context caller;
    public ResponseListener handler;
    public int result = -1;
    public String Userid;
    public String Tag = "MyExperimentsAPI";
    private ArrayList<MyExperimentData> myExperimentDataArrayList;

    public MyExperimentsAPI(Context context, ResponseListener responseListener, String Userid) {

        this.caller = context;
        this.handler = responseListener;
        this.Userid = Userid;
    }

    protected void onPreExecute() {

    }

    protected Integer doInBackground(Void... arg0) {

        ApiResponse apiResponse = null;
        try {
            apiResponse = WebInterface.doGet(Const.URL_MY_EXPERIMENTS + Userid);
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
            Log.d(Tag, "My Experiments Json Response :----->" + apiResponse.response);
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
            this.handler.onResponse(Const.URL_MY_EXPERIMENTS,
                    Const.API_RESULT.SUCCESS, myExperimentDataArrayList);
        } else {
            Toast.makeText(caller, R.string.server_error, Toast.LENGTH_LONG).show();
            this.handler.onResponse(Const.URL_MY_EXPERIMENTS,
                    Const.API_RESULT.FAIL, myExperimentDataArrayList);
        }
    }

    public void Parse(String response) {

        myExperimentDataArrayList = new ArrayList<MyExperimentData>();
        try {
            // JSONObject ResponseJson = new JSONObject(response);
            JSONArray PostArray = new JSONArray(response);
            //  PostArray = ResponseJson.getJSONArray("posts");
            for (int j = 0; j < PostArray.length(); j++) {
                MyExperimentData myExperimentData = new MyExperimentData();
                myExperimentData.setTitle(PostArray.getJSONObject(j).getJSONObject("title").optString("rendered"));
                myExperimentData.setName(PostArray.getJSONObject(j).optString("author"));
                myExperimentData.setTime(PostArray.getJSONObject(j).getString("date"));
                myExperimentData.setLink(PostArray.getJSONObject(j).getString("link"));
                myExperimentData.setAllDescription(PostArray.getJSONObject(j).getJSONObject("content").optString("rendered"));
                org.jsoup.nodes.Document doc = Jsoup.parse(PostArray.getJSONObject(j).getJSONObject("excerpt").optString("rendered"));
                Element p = doc.select("p").first();
                String text = doc.body().text();
                myExperimentData.setDescription(text);
                String imgRegex = "<[iI][mM][gG][^>]+[sS][rR][cC]\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>";
                Pattern p1 = Pattern.compile(imgRegex);
                Matcher m = p1.matcher(PostArray.getJSONObject(j).getJSONObject("content").optString("rendered"));
                String imgSrc = null;
                if (m.find()) {
                    imgSrc = m.group(1);
                }
                myExperimentData.setEventPic(imgSrc);
                myExperimentDataArrayList.add(myExperimentData);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        result = 0;
    }
}


