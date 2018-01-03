package com.example.blackpearl.foldscope.Api;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.blackpearl.foldscope.Model.CategoryData;
import com.example.blackpearl.foldscope.R;
import com.example.blackpearl.foldscope.Utils.ApiResponse;
import com.example.blackpearl.foldscope.Utils.Const;
import com.example.blackpearl.foldscope.Utils.ResponseListener;
import com.example.blackpearl.foldscope.Utils.WebInterface;

import org.json.JSONArray;
import org.json.JSONException;

import java.net.UnknownHostException;
import java.util.ArrayList;

public class GetCategoriesAPI extends AsyncTask<Void, Void, Integer> {

    public Context caller;
    public ResponseListener handler;
    public int result = -1;
    ArrayList<CategoryData> allCategoriesArrayList;
    public String Tag = "GetCategoriesAPI";

    public GetCategoriesAPI(Context context, ResponseListener responseListener) {

        this.caller = context;
        this.handler = responseListener;
    }

    protected void onPreExecute() {

    }

    protected Integer doInBackground(Void... arg0) {

        ApiResponse apiResponse = null;
        try {
            apiResponse = WebInterface.doGet(Const.URL_GETCATEGOIES);
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
            Log.d(Tag, "Get Categories Json Response :----->" + apiResponse.response);
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
            this.handler.onResponse(Const.URL_GETCATEGOIES,
                    Const.API_RESULT.SUCCESS, allCategoriesArrayList);
        } else {
            Toast.makeText(caller, R.string.server_error, Toast.LENGTH_LONG).show();
            this.handler.onResponse(Const.URL_GETCATEGOIES,
                    Const.API_RESULT.FAIL, allCategoriesArrayList);
        }
    }

    public void Parse(String response) {

        allCategoriesArrayList = new ArrayList<CategoryData>();
        try {
            JSONArray CategoriesArray = new JSONArray(response);
            for (int j = 0; j < CategoriesArray.length(); j++) {
                CategoryData categoryData = new CategoryData();
                String id, title, slug;
                id = CategoriesArray.getJSONObject(j).optString("id");
                title = CategoriesArray.getJSONObject(j).optString("name");
                slug = CategoriesArray.getJSONObject(j).optString("slug");
                categoryData.setCategories_id(id);
                categoryData.setSlug(slug);
                categoryData.setCategories_title(title);
                allCategoriesArrayList.add(categoryData);
            }
        } catch (JSONException e) {
        }
    }
}


