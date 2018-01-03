package com.example.blackpearl.foldscope.Activity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.blackpearl.foldscope.R;

import java.io.IOException;
import java.net.URL;

public class ViewBlog extends AppCompatActivity implements View.OnClickListener {

    private ImageView Img_Profile;
    private TextView Tv_Title, Tv_Username, Tv_Time, Tv_Desc;
    private WebView ViewBlog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_blog);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.layout_header);
        actionBar.setElevation(0);
        ImageView Back = (ImageView) findViewById(R.id.img_back);
        TextView Title = (TextView) findViewById(R.id.tv_title);
        TextView Publish = (TextView) findViewById(R.id.tv_publish);
        Back.setVisibility(View.VISIBLE);
        Title.setVisibility(View.VISIBLE);
        Title.setText("VIEW BLOG");
        Title.setTypeface(Typeface.DEFAULT_BOLD);
        Back.setOnClickListener(this);
        Img_Profile = (ImageView) findViewById(R.id.img_ad_profile_bv);
        Tv_Title = (TextView) findViewById(R.id.tv_ad_title_bv);
        Tv_Username = (TextView) findViewById(R.id.tv_ad_name_bv);
        Tv_Time = (TextView) findViewById(R.id.tv_ad_time_bv);
        ViewBlog = (WebView) findViewById(R.id.wv_view_blog);
        ViewBlog.getSettings().setLoadWithOverviewMode(true);
        ViewBlog.getSettings().setUseWideViewPort(true);
        ViewBlog.getSettings().setJavaScriptEnabled(true);
        Intent intent = getIntent();
        String Link = intent.getStringExtra("Link");
        String Title1 = intent.getStringExtra("Title");
        String Name = intent.getStringExtra("Name");
        String Time = intent.getStringExtra("Time");
        String Pic = intent.getStringExtra("Pic");
        String Desc = intent.getStringExtra("Desc");
        Pic = "http:" + Pic;
        Bitmap bmpProfile = null;
        try {
            URL urlProfile = new URL(Pic);
            bmpProfile = BitmapFactory.decodeStream(urlProfile.openConnection().getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Tv_Title.setText(Title1);
        Tv_Username.setText(Name);
        Tv_Time.setText(Time);
        Img_Profile.setImageBitmap(bmpProfile);
        ViewBlog.loadDataWithBaseURL("", getHtmlData(Desc), "", "", "");
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
        }
    }

    private String getHtmlData(String bodyHTML) {
        String head = "<head><style>img{max-width: 100% !important; width:100% !important; height: auto !important;}" +
                "div{max-width: 100% !important; width:100% !important; height: auto !important;}" +
                "video{max-width: 100% !important; width:100% !important; height: auto !important;}" +
                "p{font-family: Calibri !important;font-size: 50px !important;text-decoration: none !important;}" +
                "}a{text-decoration: none !important;color:black !important;}></style></head>";
        return "<html>" + head + "<body>" + bodyHTML + "</body></html>";
    }
}


