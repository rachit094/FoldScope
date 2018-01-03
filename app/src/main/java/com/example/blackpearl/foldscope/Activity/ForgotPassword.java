package com.example.blackpearl.foldscope.Activity;

import android.app.ProgressDialog;
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

import com.example.blackpearl.foldscope.Api.ForgotPasswordAPI;
import com.example.blackpearl.foldscope.R;
import com.example.blackpearl.foldscope.Utils.Const;
import com.example.blackpearl.foldscope.Utils.ResponseListener;
import com.example.blackpearl.foldscope.Utils.Utility;

public class ForgotPassword extends AppCompatActivity implements View.OnClickListener {

    private EditText Et_UserName;
    private String UserName;
    private Button GetPassword;
    private TextView Login, Register;
    private ProgressDialog myDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.layout_header);
        actionBar.setElevation(0);
        ImageView Back = (ImageView) findViewById(R.id.img_back);
        TextView Title = (TextView) findViewById(R.id.tv_title);
        TextView Publish = (TextView) findViewById(R.id.tv_publish);
        Back.setVisibility(View.VISIBLE);
        Title.setVisibility(View.VISIBLE);
        Title.setText("FORGOT PASSWORD");
        Title.setTypeface(Typeface.DEFAULT_BOLD);
        Back.setOnClickListener(this);
        Et_UserName = (EditText) findViewById(R.id.et_username_or_email);
        Login = (TextView) findViewById(R.id.tv_login);
        Register = (TextView) findViewById(R.id.tv_register);
        GetPassword = (Button) findViewById(R.id.b_get_password);
        GetPassword.setOnClickListener(this);
        Login.setOnClickListener(this);
        Register.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.tv_login:
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.tv_register:
                Intent intent1 = new Intent(this, Registration.class);
                startActivity(intent1);
                finish();
                break;
            case R.id.b_get_password:
                myDialog = Utility.ShowProgressDialog(this, "Please Wait...");
                if (isValidate()) {
                } else {
                    myDialog.dismiss();
                    UserName = Et_UserName.getText().toString();
                    new ForgotPasswordAPI(this, responseListener, UserName).execute();
                }
        }
    }

    private Boolean isValidate() {

        boolean flag = false;
        if (Et_UserName.getText().toString().trim().length() == 0) {
            Toast.makeText(this, R.string.enter_username, Toast.LENGTH_LONG).show();
            flag = true;
            myDialog.dismiss();
            return flag;
        }
        return flag;
    }

    ResponseListener responseListener = new ResponseListener() {
        @Override
        public void onResponse(String api, Const.API_RESULT result, Object obj) {

            if (api.equals(Const.URL_FORGOT_PASSWORD)) {
                String Message = (String) obj;
                if (Message == null) {
                    Toast.makeText(ForgotPassword.this, R.string.user_not_exist, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(ForgotPassword.this, Message, Toast.LENGTH_LONG).show();
                }
            }
        }
    };
}
