package com.example.blackpearl.foldscope.Activity;

import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blackpearl.foldscope.Api.UserDataAPI;
import com.example.blackpearl.foldscope.Model.AuthorData;
import com.example.blackpearl.foldscope.R;
import com.example.blackpearl.foldscope.Utils.Const;
import com.example.blackpearl.foldscope.Utils.ResponseListener;
import com.example.blackpearl.foldscope.Utils.SharedPreference;
import com.example.blackpearl.foldscope.Utils.Utility;

import java.util.ArrayList;

public class Registration extends AppCompatActivity implements View.OnClickListener {

    private EditText Et_UserName, ET_Email, ET_FirstName, ET_LastName, ET_Code, ET_Password, ET_Confirm_Password;
    private String UserName, Email, FirstName, LastName, Code, Password;
    private Button Register;
    private CheckBox Licence;
    private ProgressDialog myDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.layout_header);
        actionBar.setElevation(0);
        ImageView Back = (ImageView) findViewById(R.id.img_back);
        TextView Title = (TextView) findViewById(R.id.tv_title);
        TextView Publish = (TextView) findViewById(R.id.tv_publish);
        Back.setVisibility(View.VISIBLE);
        Title.setVisibility(View.VISIBLE);
        Title.setText("REGISTRATION");
        Title.setTypeface(Typeface.DEFAULT_BOLD);
        Back.setOnClickListener(this);
        Et_UserName = (EditText) findViewById(R.id.reg__username);
        ET_Email = (EditText) findViewById(R.id.reg__email);
        ET_FirstName = (EditText) findViewById(R.id.reg__firstname);
        ET_LastName = (EditText) findViewById(R.id.reg__lastname);
        ET_Code = (EditText) findViewById(R.id.req_code);
        ET_Password = (EditText) findViewById(R.id.reg__password);
        ET_Confirm_Password = (EditText) findViewById(R.id.reg_confrimpassword);
        Licence = (CheckBox) findViewById(R.id.reg_licence);
        Register = (Button) findViewById(R.id.b_registration);
        Register.setOnClickListener(this);
//        Et_UserName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean b) {
//
//                if (b) {
//                } else {
//                    myDialog = Utility.ShowProgressDialog(Registration.this, "Please Wait");
//                    new UserDataAPI(Registration.this, responseListener).execute();
//                }
//            }
//        });
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.b_registration:
                Utility.HideSoftKeyboard(this);
//                myDialog = Utility.ShowProgressDialog(this, "Please Wait");
//                if (isRegistrationFormValidated()) {
//                } else {
//                    myDialog.dismiss();
//                    UserName = Et_UserName.getText().toString();
//                    Email = ET_Email.getText().toString();
//                    FirstName = ET_FirstName.getText().toString();
//                    LastName = ET_LastName.getText().toString();
//                    Code = ET_Code.getText().toString();
//                    Password = ET_Password.getText().toString();
//                }
//                break;
        }
    }

    private boolean isRegistrationFormValidated() {

        boolean flag = false;
        if (Et_UserName.getText().toString().trim().length() == 0) {
            myDialog.dismiss();
            Toast.makeText(getApplicationContext(), R.string.enter_username, Toast.LENGTH_LONG).show();
            flag = true;
        } else if (ET_Email.getText().toString().trim().length() == 0) {
            myDialog.dismiss();
            Toast.makeText(getApplicationContext(), R.string.enter_email, Toast.LENGTH_LONG).show();
            flag = true;
            return flag;
        } else if (Utility.ValidateEmail(ET_Email.getText().toString().trim()) == false) {
            myDialog.dismiss();
            Toast.makeText(getApplicationContext(), R.string.enter_valid_email, Toast.LENGTH_LONG).show();
            flag = true;
        } else if (ET_FirstName.getText().toString().trim().length() == 0) {
            myDialog.dismiss();
            Toast.makeText(getApplicationContext(), R.string.enter_firstname, Toast.LENGTH_LONG).show();
            flag = true;
        } else if (ET_LastName.getText().toString().trim().length() == 0) {
            myDialog.dismiss();
            Toast.makeText(getApplicationContext(), R.string.enter_lastname, Toast.LENGTH_LONG).show();
            flag = true;
        } else if (ET_Code.getText().toString().trim().length() == 0) {
            myDialog.dismiss();
            Toast.makeText(getApplicationContext(), R.string.enter_code, Toast.LENGTH_LONG).show();
            flag = true;
        } else if (Utility.IsNumericValid(ET_Code.getText().toString().trim()) == false) {
            myDialog.dismiss();
            Toast.makeText(getApplicationContext(), R.string.enter_valid_code, Toast.LENGTH_LONG).show();
            flag = true;
        } else if (ET_Password.getText().toString().trim().length() == 0) {
            myDialog.dismiss();
            Toast.makeText(getApplicationContext(), R.string.enter_password, Toast.LENGTH_LONG).show();
            flag = true;
        } else if (!ET_Password.getText().toString().trim().equals(ET_Confirm_Password.getText().toString().trim())) {
            myDialog.dismiss();
            Toast.makeText(getApplicationContext(), R.string.match_password, Toast.LENGTH_LONG).show();
            flag = true;
        } else if (Licence.isChecked() == false) {
            myDialog.dismiss();
            Toast.makeText(getApplicationContext(), R.string.check_licence, Toast.LENGTH_LONG).show();
            flag = true;
        }
        return flag;
    }

    ResponseListener responseListener = new ResponseListener() {
        @Override
        public void onResponse(String api, Const.API_RESULT result, Object obj) {

            if (api.equals(Const.URL_REGISTRATION)) {
                if (result == Const.API_RESULT.SUCCESS) {
                    String Userid;
                    Userid = (String) obj;
                    SharedPreference.SharedPreference(Registration.this);
                    SharedPreference.setString(Const.PREF_USERID, Userid);
                    Toast.makeText(Registration.this, R.string.registration_completed, Toast.LENGTH_LONG).show();
                    finish();
                }
            }
            if (api.equals(Const.URL_CHECK_USERNAME)) {
                ArrayList<AuthorData> authorDataArrayList;
                if (result == Const.API_RESULT.SUCCESS) {
                    if (myDialog.isShowing()) {
                        myDialog.dismiss();
                    }
                    authorDataArrayList = (ArrayList<AuthorData>) obj;
                    for (int i = 0; i < authorDataArrayList.size(); i++) {
                        if (Et_UserName.getText().toString().equals(authorDataArrayList.get(i).getName())) {
                            Et_UserName.setText("");
                            Toast.makeText(Registration.this, R.string.user_exist, Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        }
    };
}
