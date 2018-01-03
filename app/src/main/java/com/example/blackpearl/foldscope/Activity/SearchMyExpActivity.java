package com.example.blackpearl.foldscope.Activity;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blackpearl.foldscope.Adapter.AllPostAdapter;
import com.example.blackpearl.foldscope.Adapter.MyExperimentAdapter;
import com.example.blackpearl.foldscope.Api.SearchPostAPI;
import com.example.blackpearl.foldscope.Database.DataBaseHelper;
import com.example.blackpearl.foldscope.Model.AllPostData;
import com.example.blackpearl.foldscope.Model.MyExperimentData;
import com.example.blackpearl.foldscope.R;
import com.example.blackpearl.foldscope.Utils.Const;
import com.example.blackpearl.foldscope.Utils.ResponseListener;
import com.example.blackpearl.foldscope.Utils.SharedPreference;
import com.example.blackpearl.foldscope.Utils.Utility;

import java.util.ArrayList;

public class SearchMyExpActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText Et_Search;
    private String Search;
    private ImageView Img_Search;
    private String TAG = "SearchMyExpActivity";
    private RecyclerView Rv_UserStory;
    private MyExperimentAdapter myExperimentAdapter;
    private ProgressDialog myDialog;
    private TextView Tv_Msg;
    private int pastVisiblesItems, visibleItemCount, totalItemCount, End = 10;
    private boolean loading = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.layout_header);
        actionBar.setElevation(0);
        ImageView Back = (ImageView) findViewById(R.id.img_back);
        TextView Title = (TextView) findViewById(R.id.tv_title);
        TextView Publish = (TextView) findViewById(R.id.tv_publish);
        Back.setVisibility(View.VISIBLE);
        Title.setVisibility(View.VISIBLE);
        Title.setText("SEARCH");
        Title.setTypeface(Typeface.DEFAULT_BOLD);
        Back.setOnClickListener(this);
        Rv_UserStory = (RecyclerView) findViewById(R.id.rv_user_story_search);
        Et_Search = (EditText) findViewById(R.id.et_search);
        Img_Search = (ImageView) findViewById(R.id.iv_search);
        Img_Search.setOnClickListener(this);
        Tv_Msg = (TextView) findViewById(R.id.tv_msg);
        SharedPreference.SharedPreference(SearchMyExpActivity.this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.iv_search:
                Utility.HideSoftKeyboard(this);
                if (Et_Search.getText().toString().trim().length() == 0) {
                    Toast.makeText(this, R.string.search, Toast.LENGTH_LONG).show();
                } else {
                    myDialog = Utility.ShowProgressDialog(this, "Please Wait");
                    Search = Et_Search.getText().toString();
                    Utility.LogD(TAG, "Search String :----->" + Search);
                    Et_Search.setText("");
                    new SearchPostAPI(this, responseListener, Search).execute();
                }
                break;
        }
    }

    ResponseListener responseListener = new ResponseListener() {
        ArrayList<MyExperimentData> myExperimentDataArrayList;
        ArrayList<MyExperimentData> myExperimentDataArrayList1;
        ArrayList<AllPostData> allPostDataArrayList;

        @Override
        public void onResponse(String api, Const.API_RESULT result, Object obj) {

            End = 10;
            loading = true;
            pastVisiblesItems = 0;
            visibleItemCount = 0;
            totalItemCount = 0;
            if (api.equals(Const.URL_SEARCH)) {
                if (result == Const.API_RESULT.SUCCESS) {
                    myExperimentDataArrayList = new ArrayList<MyExperimentData>();
                    myExperimentDataArrayList1 = new ArrayList<MyExperimentData>();
                    myDialog.dismiss();
                    Tv_Msg.setVisibility(View.GONE);
                    Rv_UserStory.setVisibility(View.VISIBLE);
                    allPostDataArrayList = (ArrayList<AllPostData>) obj;
                    for (int j = 0; j < allPostDataArrayList.size(); j++) {
                        MyExperimentData myExperimentData = new MyExperimentData();
                        myExperimentData.setTitle(allPostDataArrayList.get(j).getTitle());
                        myExperimentData.setDescription(allPostDataArrayList.get(j).getDescription());
                        myExperimentData.setTime(allPostDataArrayList.get(j).getTime());
                        myExperimentData.setEventPic(allPostDataArrayList.get(j).getEventPic());
                        myExperimentData.setLink(allPostDataArrayList.get(j).getLink());
                        myExperimentData.setName(allPostDataArrayList.get(j).getName());
                        myExperimentDataArrayList.add(myExperimentData);
                    }
                    if (myExperimentDataArrayList.size() != 0) {
                        for (int i = 0; i < myExperimentDataArrayList.size(); i++) {
                            if (myExperimentDataArrayList.get(i).getName().equals(SharedPreference.getString(Const.PREF_USERID))) {
                                MyExperimentData myExperimentData = new MyExperimentData();
                                myExperimentData.setTitle(myExperimentDataArrayList.get(i).getTitle());
                                myExperimentData.setDescription(myExperimentDataArrayList.get(i).getDescription());
                                myExperimentData.setTime(myExperimentDataArrayList.get(i).getTime());
                                myExperimentData.setEventPic(myExperimentDataArrayList.get(i).getEventPic());
                                myExperimentData.setLink(myExperimentDataArrayList.get(i).getLink());
                                myExperimentData.setName(myExperimentDataArrayList.get(i).getName());
                                myExperimentDataArrayList1.add(myExperimentData);
                            }
                        }
                        final ArrayList<MyExperimentData> myExperimentDataArrayList2;
                        if (myExperimentDataArrayList1.size() > 5) {
                            myExperimentDataArrayList2 = new ArrayList<MyExperimentData>(myExperimentDataArrayList1.subList(0, 5));
                        } else {
                            myExperimentDataArrayList2 = new ArrayList<MyExperimentData>(myExperimentDataArrayList1);
                        }
                        Rv_UserStory.setHasFixedSize(true);
                        final LinearLayoutManager layoutManager = new LinearLayoutManager(SearchMyExpActivity.this);
                        Rv_UserStory.setLayoutManager(layoutManager);
                        Rv_UserStory.setItemAnimator(new DefaultItemAnimator());
                        myExperimentAdapter = new MyExperimentAdapter(SearchMyExpActivity.this, myExperimentDataArrayList2);
                        Rv_UserStory.setAdapter(myExperimentAdapter);
                        myExperimentAdapter.notifyDataSetChanged();
                        Rv_UserStory.addOnScrollListener(new RecyclerView.OnScrollListener() {
                            @Override
                            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                                super.onScrolled(recyclerView, dx, dy);
                                if (dy > 0) //check for scroll down
                                {
                                    visibleItemCount = layoutManager.getChildCount();
                                    totalItemCount = layoutManager.getItemCount();
                                    pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();
                                    if (loading) {
                                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                                            ArrayList<MyExperimentData> myExperimentDataArrayList2;
                                            loading = false;
                                            Parcelable recyclerViewState;
                                            recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();
                                            recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
                                            if (myExperimentDataArrayList1.size() > End) {
                                                myExperimentDataArrayList2 = new ArrayList<MyExperimentData>(myExperimentDataArrayList1.subList(0, End));
                                            } else {
                                                myExperimentDataArrayList2 = new ArrayList<MyExperimentData>(myExperimentDataArrayList1.subList(0, myExperimentDataArrayList1.size()));
                                            }
                                            myExperimentAdapter = new MyExperimentAdapter(SearchMyExpActivity.this, myExperimentDataArrayList2);
                                            Rv_UserStory.setAdapter(myExperimentAdapter);
                                            myExperimentAdapter.notifyDataSetChanged();
                                            End = End + 5;
                                        }
                                    }
                                }
                            }
                        });
                    } else {
                        Tv_Msg.setVisibility(View.VISIBLE);
                        Rv_UserStory.setVisibility(View.GONE);
                    }
                } else {
                    Tv_Msg.setVisibility(View.VISIBLE);
                    Rv_UserStory.setVisibility(View.GONE);
                }
            }
        }
    };
}
