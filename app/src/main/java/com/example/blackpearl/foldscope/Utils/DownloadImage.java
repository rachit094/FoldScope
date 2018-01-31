package com.example.blackpearl.foldscope.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;

import com.example.blackpearl.foldscope.Database.DataBaseHelper;
import com.example.blackpearl.foldscope.Model.AllPostData;

import java.io.File;
import java.util.ArrayList;

public class DownloadImage extends AsyncTask<Void, Void, Integer> {

    public Context caller;
    public int result = -1;
    private File root,dir;
    private ArrayList<AllPostData> allPostDataArrayList;
    DataBaseHelper dataBaseHelper;

    public DownloadImage(Context context, ArrayList<AllPostData> allPostDataArrayList) {

        this.caller = context;
        this.allPostDataArrayList = allPostDataArrayList;
        root = android.os.Environment.getExternalStorageDirectory();
        dir = new File(root.getAbsolutePath() + "/Foldscope/foldscope/");
        if (dir.exists() == false) {
            dir.mkdirs();
        }
        else
        {
            Utility.DeleteFile(dir.getPath());
            dir.mkdirs();
        }

        dataBaseHelper = new DataBaseHelper(caller);
        dataBaseHelper.CreateDatabase();
        dataBaseHelper.Open();
        dataBaseHelper.DeleteAllDataFromTableName("UserPost");
    }

    protected void onPreExecute() {

    }

    protected Integer doInBackground(Void... arg0) {
        if (dir.exists() == false) {
            dir.mkdirs();
        }

        for (int j = 0; j < 5; j++) {
            String PathPost=null,PathProfile=null;
            PathPost = Utility.DownloadImage(allPostDataArrayList.get(j).getEventPic(),Utility.Filename(dir,""));
            PathProfile = Utility.DownloadImage("http:" + allPostDataArrayList.get(j).getProfilePic(),Utility.Filename(dir,""));
            ContentValues contentValues = new ContentValues();
            contentValues.put("Title", allPostDataArrayList.get(j).getTitle());
            contentValues.put("Description", allPostDataArrayList.get(j).getDescription());
            contentValues.put("UserName", allPostDataArrayList.get(j).getName());
            contentValues.put("Date", allPostDataArrayList.get(j).getTime());
            contentValues.put("ProfilePic", PathProfile);
            contentValues.put("PostPic", PathPost);
            contentValues.put("DescriptionAll", allPostDataArrayList.get(j).getAllDescription());
            dataBaseHelper.InsertIntoTable("UserPost", contentValues);

        }
        return result;
    }

    protected void onPostExecute(Integer result) {
        dataBaseHelper.close();
    }
}


