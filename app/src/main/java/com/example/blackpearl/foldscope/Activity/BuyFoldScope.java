package com.example.blackpearl.foldscope.Activity;

import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.blackpearl.foldscope.R;
import com.example.blackpearl.foldscope.Utils.Utility;

public class BuyFoldScope extends AppCompatActivity implements View.OnClickListener {

    private WebView WVBuyFoldscope;
    private ProgressDialog myDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_fold_scope);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.layout_header);
        actionBar.setElevation(0);
        ImageView Back = (ImageView) findViewById(R.id.img_back);
        TextView Title = (TextView) findViewById(R.id.tv_title);
        TextView Publish = (TextView) findViewById(R.id.tv_publish);
        Back.setVisibility(View.VISIBLE);
        Title.setVisibility(View.VISIBLE);
        Title.setText("BUY FOLDSCOPE");
        Title.setTypeface(Typeface.DEFAULT_BOLD);
        Back.setOnClickListener(this);
        myDialog = Utility.ShowProgressDialog(BuyFoldScope.this, "Please Wait");
        WVBuyFoldscope = (WebView) findViewById(R.id.wv_buy_foldscope);
//        WVBuyFoldscope.loadUrl("https://foldscope.backerkit.com/overlay_preorders");
        WVBuyFoldscope.loadUrl("https://foldscope.backerkit.com");
        WVBuyFoldscope.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {

                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public void onPageFinished(WebView view, String url) {

                super.onPageFinished(view, url);
                myDialog.dismiss();
            }

            @Override
            public void onLoadResource(WebView view, String url) {

                super.onLoadResource(view, url);
            }
        });
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
        }
    }
}
