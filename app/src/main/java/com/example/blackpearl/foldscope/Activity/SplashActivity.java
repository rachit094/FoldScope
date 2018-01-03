package com.example.blackpearl.foldscope.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.example.blackpearl.foldscope.Database.DataBaseHelper;
import com.example.blackpearl.foldscope.R;
import com.example.blackpearl.foldscope.Utils.SharedPreference;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 3000;
    private String Token, CurrentDate, TokenDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        SharedPreference.SharedPreference(SplashActivity.this);
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        CurrentDate = df.format(c.getTime());
        DataBaseHelper dataBaseHelper = new DataBaseHelper(this);
        dataBaseHelper.CreateDatabase();
        dataBaseHelper.Open();
        Cursor cursor = dataBaseHelper.SelectAllDataFromTable("Authentication");
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                Token = cursor.getString(cursor.getColumnIndex("Token"));
                TokenDate = cursor.getString(cursor.getColumnIndex("ToDate"));
                System.out.println("Token"+Token+TokenDate);
                System.out.println("Current Date"+CurrentDate);
                if (Token != null && CurrentDate.compareTo(TokenDate) < 0) {

                    new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

                        @Override
                        public void run() {

                            System.out.println("In Register");

                            Intent i = new Intent(SplashActivity.this, RegisterUser.class);
                            startActivity(i);
                            finish();
                        }
                    }, SPLASH_TIME_OUT);
                } else {
                    new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

                        @Override
                        public void run() {
                            System.out.println("In Guest");
                            Intent i = new Intent(SplashActivity.this, GuestUser.class);
                            startActivity(i);
                            finish();
                        }
                    }, SPLASH_TIME_OUT);
                }
            } while (cursor.moveToNext());
        } else {
            new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

                @Override
                public void run() {

                    Intent i = new Intent(SplashActivity.this, GuestUser.class);
                    startActivity(i);
                    finish();
                }
            }, SPLASH_TIME_OUT);
        }
    }
}



