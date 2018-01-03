package com.example.blackpearl.foldscope.Activity;

import android.app.ProgressDialog;
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
import com.example.blackpearl.foldscope.Api.SearchPostAPI;
import com.example.blackpearl.foldscope.Api.UserDataAPI;
import com.example.blackpearl.foldscope.Model.AuthorData;
import com.example.blackpearl.foldscope.Model.AllPostData;
import com.example.blackpearl.foldscope.R;
import com.example.blackpearl.foldscope.Utils.Const;
import com.example.blackpearl.foldscope.Utils.ResponseListener;
import com.example.blackpearl.foldscope.Utils.Utility;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText Et_Search;
    private String Search;
    private ImageView Img_Search;
    private String TAG = "SearchActivity";
    private RecyclerView Rv_UserStory;
    private ArrayList<AuthorData> authorDataArrayList;
    private AllPostAdapter allPostAdapter;
    private ProgressDialog myDialog;
    private TextView Tv_Msg;
    private int pastVisiblesItems, visibleItemCount, totalItemCount, End = 10;
    private boolean loading = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        new UserDataAPI(this, responseListener).execute();
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
        myDialog = Utility.ShowProgressDialog(this, "Please Wait");
        Et_Search = (EditText) findViewById(R.id.et_search);
        Img_Search = (ImageView) findViewById(R.id.iv_search);
        Img_Search.setOnClickListener(this);
        Tv_Msg = (TextView) findViewById(R.id.tv_msg);
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
                    new SearchPostAPI(this, responseListener, Search).execute();
                }
                break;
        }
    }

    ResponseListener responseListener = new ResponseListener() {
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
                    myDialog.dismiss();
                    Tv_Msg.setVisibility(View.GONE);
                    Rv_UserStory.setVisibility(View.VISIBLE);
                    allPostDataArrayList = (ArrayList<AllPostData>) obj;
                    if (allPostDataArrayList.size() != 0) {
                        for (int i = 0; i < allPostDataArrayList.size(); i++) {
                            for (int j = 0; j < authorDataArrayList.size(); j++) {
                                if (allPostDataArrayList.get(i).getName() == authorDataArrayList.get(j).getUserId()) {
                                    allPostDataArrayList.get(i).setName(authorDataArrayList.get(j).getName());
                                    allPostDataArrayList.get(i).setProfilePic(authorDataArrayList.get(j).getPic_Url());
                                }
                            }
                        }
                        final ArrayList<AllPostData> allPostDataArrayList1;
                        if (allPostDataArrayList.size() > 5) {
                            allPostDataArrayList1 = new ArrayList<AllPostData>(allPostDataArrayList.subList(0, 5));
                        } else {
                            allPostDataArrayList1 = new ArrayList<AllPostData>(allPostDataArrayList);
                        }
                        Rv_UserStory.setHasFixedSize(true);
                        final LinearLayoutManager layoutManager = new LinearLayoutManager(SearchActivity.this);
                        Rv_UserStory.setLayoutManager(layoutManager);
                        Rv_UserStory.setItemAnimator(new DefaultItemAnimator());
                        allPostAdapter = new AllPostAdapter(SearchActivity.this, allPostDataArrayList1);
                        Rv_UserStory.setAdapter(allPostAdapter);
                        allPostAdapter.notifyDataSetChanged();
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
                                            ArrayList<AllPostData> allPostDataArrayList1;
                                            loading = false;
                                            Parcelable recyclerViewState;
                                            recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();
                                            recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
                                            if (allPostDataArrayList.size() > End) {
                                                allPostDataArrayList1 = new ArrayList<AllPostData>(allPostDataArrayList.subList(0, End));
                                            } else {
                                                allPostDataArrayList1 = new ArrayList<AllPostData>(allPostDataArrayList.subList(0, allPostDataArrayList.size()));
                                            }
                                            allPostAdapter = new AllPostAdapter(SearchActivity.this, allPostDataArrayList1);
                                            Rv_UserStory.setAdapter(allPostAdapter);
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
            if (api.equals(Const.URL_CHECK_USERNAME)) {
                myDialog.dismiss();
                if (result == Const.API_RESULT.SUCCESS) {
                    authorDataArrayList = (ArrayList<AuthorData>) obj;
                }
            }
        }
    };
}
