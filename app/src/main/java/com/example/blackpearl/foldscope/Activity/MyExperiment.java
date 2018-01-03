package com.example.blackpearl.foldscope.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.blackpearl.foldscope.Adapter.AllPostAdapter;
import com.example.blackpearl.foldscope.Adapter.MyExperimentAdapter;
import com.example.blackpearl.foldscope.Api.MyExperimentsAPI;
import com.example.blackpearl.foldscope.Database.DataBaseHelper;
import com.example.blackpearl.foldscope.Model.AllPostData;
import com.example.blackpearl.foldscope.Model.MyExperimentData;
import com.example.blackpearl.foldscope.R;
import com.example.blackpearl.foldscope.Utils.Const;
import com.example.blackpearl.foldscope.Utils.ResponseListener;
import com.example.blackpearl.foldscope.Utils.SharedPreference;
import com.example.blackpearl.foldscope.Utils.Utility;

import java.util.ArrayList;

public class MyExperiment extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView Rv_My_Experience;
    private MyExperimentAdapter myExperimentAdapter;
    private TextView Tv_Msg;
    private Spinner Sp_Category;
    private ImageView Img_Search;
    private ProgressDialog myDialog;
    private boolean loading = true;
    private int pastVisiblesItems, visibleItemCount, totalItemCount, End = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_experience);
        SharedPreference.SharedPreference(this);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.layout_header);
        actionBar.setElevation(0);
        ImageView Back = (ImageView) findViewById(R.id.img_back);
        TextView Title = (TextView) findViewById(R.id.tv_title);
        TextView Publish = (TextView) findViewById(R.id.tv_publish);
        Back.setVisibility(View.VISIBLE);
        Title.setVisibility(View.VISIBLE);
        Title.setText("MY EXPERIMENTS");
        Title.setTypeface(Typeface.DEFAULT_BOLD);
        Back.setOnClickListener(this);
        myDialog = Utility.ShowProgressDialog(this, "Please Wait");
        Tv_Msg = (TextView) findViewById(R.id.tv_msg);
        Rv_My_Experience = (RecyclerView) findViewById(R.id.rv_my_experience);
        Img_Search = (ImageView) findViewById(R.id.img_search);
        Img_Search.setOnClickListener(this);
        Sp_Category = (Spinner) findViewById(R.id.sp_category);
        Sp_Category.setVisibility(View.GONE);
        new MyExperimentsAPI(this, responseListener, SharedPreference.getString(Const.PREF_USERID)).execute();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.img_search:
                Intent intent = new Intent(this, SearchMyExpActivity.class);
                startActivity(intent);
                break;
        }
    }

    ResponseListener responseListener = new ResponseListener() {
        ArrayList<MyExperimentData> myExperimentDataArrayList;

        @Override
        public void onResponse(String api, Const.API_RESULT result, Object obj) {

            End = 10;
            loading = true;
            pastVisiblesItems = 0;
            visibleItemCount = 0;
            totalItemCount = 0;
            if (api.equals(Const.URL_MY_EXPERIMENTS)) {
                myDialog.dismiss();
                if (result == Const.API_RESULT.SUCCESS) {
                    myExperimentDataArrayList = (ArrayList<MyExperimentData>) obj;
                    Rv_My_Experience.setHasFixedSize(true);
                    final LinearLayoutManager layoutManager = new LinearLayoutManager(MyExperiment.this);
                    Rv_My_Experience.setLayoutManager(layoutManager);
                    Rv_My_Experience.setItemAnimator(new DefaultItemAnimator());
                    final ArrayList<MyExperimentData> myExperimentDataArrayList1;
                    if (myExperimentDataArrayList.size() > 5) {
                        myExperimentDataArrayList1 = new ArrayList<MyExperimentData>(myExperimentDataArrayList.subList(0, 5));
                    } else {
                        myExperimentDataArrayList1 = new ArrayList<MyExperimentData>(myExperimentDataArrayList);
                    }
                    myExperimentAdapter = new MyExperimentAdapter(MyExperiment.this, myExperimentDataArrayList1);
                    Rv_My_Experience.setAdapter(myExperimentAdapter);
                    myExperimentAdapter.notifyDataSetChanged();
                    Rv_My_Experience.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                                        ArrayList<MyExperimentData> myExperimentDataArrayList1;
                                        loading = false;
                                        Parcelable recyclerViewState;
                                        recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();
                                        recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
                                        if (myExperimentDataArrayList.size() > End) {
                                            myExperimentDataArrayList1 = new ArrayList<MyExperimentData>(myExperimentDataArrayList.subList(0, End));
                                        } else {
                                            myExperimentDataArrayList1 = new ArrayList<MyExperimentData>(myExperimentDataArrayList.subList(0, myExperimentDataArrayList.size()));
                                        }
                                        myExperimentAdapter = new MyExperimentAdapter(MyExperiment.this, myExperimentDataArrayList1);
                                        Rv_My_Experience.setAdapter(myExperimentAdapter);
                                        End = End + 5;
                                    }
                                }
                            }
                        }
                    });
                } else {
                    Tv_Msg.setVisibility(View.VISIBLE);
                    Rv_My_Experience.setVisibility(View.GONE);
                }
            }
        }
    };
}
