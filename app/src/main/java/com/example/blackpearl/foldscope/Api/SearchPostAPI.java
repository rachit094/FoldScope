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

import org.json.JSONArray;
import org.json.JSONException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchPostAPI extends AsyncTask<Void, Void, Integer> {

    public Context caller;
    public ResponseListener handler;
    public String keyword;
    public int result = -1;
    public String Tag = "SearchPostAPI";
    private ArrayList<AllPostData> allPostDataArrayList;

    public SearchPostAPI(Context context, ResponseListener responseListener, String keyword) {

        this.caller = context;
        this.handler = responseListener;
        this.keyword = keyword;
    }

    protected void onPreExecute() {

    }

    protected Integer doInBackground(Void... arg0) {

        ApiResponse apiResponse = null;
        try {
            apiResponse = WebInterface.doGet(Const.URL_SEARCH + keyword);
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
            Log.d(Tag, " Search Post Json Response :----->" + apiResponse.response);
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
            this.handler.onResponse(Const.URL_SEARCH, Const.API_RESULT.SUCCESS, allPostDataArrayList);
        } else {
            Toast.makeText(caller, R.string.server_error, Toast.LENGTH_LONG).show();
            this.handler.onResponse(Const.URL_SEARCH, Const.API_RESULT.FAIL, allPostDataArrayList);
        }
    }

    public void Parse(String response) {

        allPostDataArrayList = new ArrayList<AllPostData>();
        try {
            JSONArray PostArray = new JSONArray(response);
            for (int j = 0; j < PostArray.length(); j++) {
                AllPostData allPostData = new AllPostData();
                allPostData.setTitle(PostArray.getJSONObject(j).getJSONObject("title").optString("rendered"));
                allPostData.setName(PostArray.getJSONObject(j).optString("author"));
                allPostData.setTime(PostArray.getJSONObject(j).getString("date"));
                allPostData.setLink(PostArray.getJSONObject(j).getString("link"));
                allPostData.setAllDescription(PostArray.getJSONObject(j).getJSONObject("content").optString("rendered"));
                org.jsoup.nodes.Document doc = Jsoup.parse(PostArray.getJSONObject(j).getJSONObject("excerpt").optString("rendered"));
                Element p = doc.select("p").first();
                String text = doc.body().text();
                allPostData.setDescription(text);
                String imgRegex = "<[iI][mM][gG][^>]+[sS][rR][cC]\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>";
                Pattern p1 = Pattern.compile(imgRegex);
                Matcher m = p1.matcher(PostArray.getJSONObject(j).getJSONObject("content").optString("rendered"));
                String imgSrc = null;
                if (m.find()) {
                    imgSrc = m.group(1);
                }
                allPostData.setEventPic(imgSrc);
                allPostDataArrayList.add(allPostData);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}


