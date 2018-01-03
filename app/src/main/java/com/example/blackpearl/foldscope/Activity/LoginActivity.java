package com.example.blackpearl.foldscope.Activity;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blackpearl.foldscope.Api.GetTokenAPI;
import com.example.blackpearl.foldscope.Api.LoginAPI;
import com.example.blackpearl.foldscope.Database.DataBaseHelper;
import com.example.blackpearl.foldscope.Model.TokenData;
import com.example.blackpearl.foldscope.Model.UserData;
import com.example.blackpearl.foldscope.R;
import com.example.blackpearl.foldscope.Utils.Const;
import com.example.blackpearl.foldscope.Utils.ResponseListener;
import com.example.blackpearl.foldscope.Utils.SharedPreference;
import com.example.blackpearl.foldscope.Utils.Utility;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView ForgotPassword;
    private EditText ET_UserName, ET_Password;
    private String UserName, Password;
    private Button Login;
    private ProgressDialog myDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.layout_header);
        actionBar.setElevation(0);
        ImageView Back = (ImageView) findViewById(R.id.img_back);
        TextView Title = (TextView) findViewById(R.id.tv_title);
        TextView Publish = (TextView) findViewById(R.id.tv_publish);
        Back.setVisibility(View.VISIBLE);
        Title.setVisibility(View.VISIBLE);
        Title.setText("LOGIN");
        Title.setTypeface(Typeface.DEFAULT_BOLD);
        Back.setOnClickListener(this);
        ForgotPassword = (TextView) findViewById(R.id.tv_forgetpassword);
        ForgotPassword.setOnClickListener(this);
        ET_UserName = (EditText) findViewById(R.id.et_username);
        ET_Password = (EditText) findViewById(R.id.et_password);
        Login = (Button) findViewById(R.id.b_login);
        Login.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.tv_forgetpassword:
                Intent intent = new Intent(this, ForgotPasswordNew.class);
                startActivity(intent);
                finish();
                break;
            case R.id.b_login:
                Utility.HideSoftKeyboard(this);
                if (Utility.isOnline(this)) {
                    myDialog = Utility.ShowProgressDialog(this, "Please Wait");
                    if (isLoginFormValidated()) {
                    } else {
                        UserName = ET_UserName.getText().toString();
                        Password = ET_Password.getText().toString();
                        new GetTokenAPI(this, responseListener, UserName, Password).execute();
                    }
                } else {
                    Toast.makeText(this, R.string.connection_error, Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private Boolean isLoginFormValidated() {

        boolean flag = false;
        if (ET_UserName.getText().toString().trim().length() == 0) {
            Toast.makeText(this, R.string.enter_username, Toast.LENGTH_LONG).show();
            flag = true;
            myDialog.dismiss();
            return flag;
        } else if (ET_Password.getText().toString().trim().length() == 0) {
            Toast.makeText(this, R.string.enter_password, Toast.LENGTH_LONG).show();
            myDialog.dismiss();
            flag = true;
        } else if (ET_Password.getText().toString().trim().length() < 6) {
            Toast.makeText(this, R.string.enter_correct_password, Toast.LENGTH_LONG).show();
            myDialog.dismiss();
            flag = true;
        }
        return flag;
    }

    ResponseListener responseListener = new ResponseListener() {
        @Override
        public void onResponse(String api, Const.API_RESULT result, Object obj) {

            SharedPreference.SharedPreference(LoginActivity.this);
            myDialog.dismiss();
            if (api.equals(Const.URL_TOKEN)) {
                if (result == Const.API_RESULT.SUCCESS) {
                    ArrayList<TokenData> tokenDataArrayList;
                    tokenDataArrayList = (ArrayList<TokenData>) obj;
                    SharedPreference.setString(Const.PREF_USER_EMAIL, tokenDataArrayList.get(0).getEmail());
                    SharedPreference.setString(Const.PREF_TOKEN, tokenDataArrayList.get(0).getToken());
                    Calendar c = Calendar.getInstance();
                    System.out.println("Current time => " + c.getTime());
                    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                    String FromDate = df.format(c.getTime());
                    c.add(Calendar.DATE, 84);
                    String ToDate = df.format(c.getTime());
                    DataBaseHelper dataBaseHelper = new DataBaseHelper(LoginActivity.this);
                    dataBaseHelper.CreateDatabase();
                    dataBaseHelper.Open();
                    dataBaseHelper.DeleteAllDataFromTableName("Authentication");
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("Token", tokenDataArrayList.get(0).getToken());
                    contentValues.put("User_email", tokenDataArrayList.get(0).getEmail());
                    contentValues.put("User_nicename", tokenDataArrayList.get(0).getNiceName());
                    contentValues.put("User_display_name", tokenDataArrayList.get(0).getDisplayName());
                    contentValues.put("ToDate", ToDate);
                    contentValues.put("FromDate", FromDate);
                    dataBaseHelper.InsertIntoTable("Authentication", contentValues);
                    myDialog = Utility.ShowProgressDialog(LoginActivity.this, "Please Wait");
                    new LoginAPI(LoginActivity.this, responseListener, tokenDataArrayList.get(0).getToken()).execute();
                }
            }
            if (api.equals(Const.URL_LOGIN)) {
                if (result == Const.API_RESULT.SUCCESS) {
                    ArrayList<UserData> userDataArrayList;
                    userDataArrayList = (ArrayList<UserData>) obj;
                    SharedPreference.setString(Const.PREF_USERID, userDataArrayList.get(0).getUserId());
                    SharedPreference.setString(Const.PREF_USERNAME, UserName);
                    SharedPreference.setString(Const.PREF_PASSWORD, Password);
                    SharedPreference.setString(Const.PREF_PROFILE_PIC, userDataArrayList.get(0).getAvatar());
                    Intent intent = new Intent(LoginActivity.this, RegisterUser.class);
                    startActivity(intent);
                    finishAffinity();
                }
            }
        }
    };
}
