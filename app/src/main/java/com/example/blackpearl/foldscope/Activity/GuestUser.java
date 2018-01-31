package com.example.blackpearl.foldscope.Activity;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Typeface;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blackpearl.foldscope.Adapter.AllPostAdapter;
import com.example.blackpearl.foldscope.Api.AllPostAPI;
import com.example.blackpearl.foldscope.Api.CategorywisePostAPI;
import com.example.blackpearl.foldscope.Api.GetCategoriesAPI;
import com.example.blackpearl.foldscope.Api.GetTokenAPI;
import com.example.blackpearl.foldscope.Api.LoginAPI;
import com.example.blackpearl.foldscope.Api.UserDataAPI;
import com.example.blackpearl.foldscope.Database.DataBaseHelper;
import com.example.blackpearl.foldscope.Model.AllPostData;
import com.example.blackpearl.foldscope.Model.AuthorData;
import com.example.blackpearl.foldscope.Model.CategoryData;
import com.example.blackpearl.foldscope.Model.TokenData;
import com.example.blackpearl.foldscope.Model.UserData;
import com.example.blackpearl.foldscope.R;
import com.example.blackpearl.foldscope.Utils.Const;
import com.example.blackpearl.foldscope.Utils.DownloadImage;
import com.example.blackpearl.foldscope.Utils.ResponseListener;
import com.example.blackpearl.foldscope.Utils.SharedPreference;
import com.example.blackpearl.foldscope.Utils.Utility;
import com.otaliastudios.cameraview.CameraException;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraOptions;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.Gesture;
import com.otaliastudios.cameraview.GestureAction;
import com.otaliastudios.cameraview.SessionType;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class GuestUser extends AppCompatActivity implements View.OnClickListener {

    private android.support.v4.app.FragmentTransaction transaction;
    private ActionBar actionBar;
    private ImageView Back;
    private TextView Title, Publish;
    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 110;
    private View CameraLayout;
    private View LoginLayout;
    private View HomeLayout;
    private View Profile_GU_Layout;
    private String TAG = "GuestUser";
    //Elements of Home Layout
    private RecyclerView Rv_UserStory;
    private AllPostAdapter allPostAdapter;
    private ArrayList<String> Categories;
    private TextView Tv_Msg;
    private Spinner Sp_Category;
    private ImageView Img_Search;
    private ProgressDialog myDialog;
    private ArrayList<AuthorData> authorDataArrayList;
    private ArrayList<CategoryData> categoryDataArrayList;
    private ArrayList<AllPostData> allPostDataArrayList;
    private int End = 10;
    private boolean loading = true;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    private SwipeRefreshLayout SwipeRefreshLayout;
    private CameraView cameraView;
    private Button VideoIcon, CameraScreen, RecordVideo, CameraIcon;
    private TextView ZoomScale, ZoomScaleMM;
    private LinearLayout LayoutText;
    private RelativeLayout LayoutScale;
    private Bitmap ImageScale, MainImage, ImageText, NewImage = null;
    private File root, dir;
    private boolean Recording = false;
    private double ScreeSize, ActualScreenSize, ScaleSize, ActualScaleSize, MaxZoom;
    private List<Integer> ZoomRatios;
    private Camera camera;
    private Camera.Parameters p;
    private TextView ForgotPassword;
    private EditText ET_UserName, ET_Password;
    private String UserName, Password;
    private Button Login;
    private Button Registration, BTNBuyFoldScope;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_user_story:
                    Title.setText("HOME");
                    HomeLayout.setVisibility(View.VISIBLE);
                    CameraLayout.setVisibility(View.GONE);
                    LoginLayout.setVisibility(View.GONE);
                    Profile_GU_Layout.setVisibility(View.GONE);
                    Rv_UserStory.scrollToPosition(0);
                    cameraView.stop();
                    return true;
                case R.id.navigation_camera:
                    Title.setText("CAMERA");
                    HomeLayout.setVisibility(View.GONE);
                    CameraLayout.setVisibility(View.VISIBLE);
                    LoginLayout.setVisibility(View.GONE);
                    Profile_GU_Layout.setVisibility(View.GONE);
                    CameraLayout();
                    return true;
                case R.id.navigation_user:
                    Title.setText("LOGIN");
                    HomeLayout.setVisibility(View.GONE);
                    CameraLayout.setVisibility(View.GONE);
                    LoginLayout.setVisibility(View.VISIBLE);
                    Profile_GU_Layout.setVisibility(View.GONE);
                    cameraView.stop();
                    return true;
                case R.id.navigation_profile:
                    Title.setText("MENU");
                    HomeLayout.setVisibility(View.GONE);
                    CameraLayout.setVisibility(View.GONE);
                    LoginLayout.setVisibility(View.GONE);
                    Profile_GU_Layout.setVisibility(View.VISIBLE);
                    cameraView.stop();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_user);
        Utility.CheckAndRequestPermissions(this, this);
        actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.layout_header);
        actionBar.setElevation(0);
        Back = (ImageView) findViewById(R.id.img_back);
        Title = (TextView) findViewById(R.id.tv_title);
        Publish = (TextView) findViewById(R.id.tv_publish);
        Title.setVisibility(View.VISIBLE);
        Title.setText("HOME");
        Title.setTypeface(Typeface.DEFAULT_BOLD);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        HomeLayout = (View) findViewById(R.id.home_layout);
        CameraLayout = (View) findViewById(R.id.camera_layout);
        LoginLayout = (View) findViewById(R.id.login_layout);
        Profile_GU_Layout = (View) findViewById(R.id.profile_gu__layout);
        HomeLayout();
        LoginLayout();
        GuestUser_Layout();
        cameraView = (CameraView) findViewById(R.id.cameraview);
    }

    @Override
    public void onPause() {

        cameraView.stop();
        super.onPause();
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
        cameraView.destroy();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.img_search:
                Intent intent = new Intent(this, SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.captureimage:
                cameraView.capturePicture();
                break;
            case R.id.video_capture:
                VideoIcon.setVisibility(View.INVISIBLE);
                CameraIcon.setVisibility(View.INVISIBLE);
                RecordVideo.setVisibility(View.VISIBLE);
                CameraScreen.setVisibility(View.VISIBLE);
                cameraView.setSessionType(SessionType.VIDEO);
                break;
            case R.id.camera:
                if (Recording) {
                    Toast.makeText(GuestUser.this, "Please Stop Video Capture.", Toast.LENGTH_LONG).show();
                } else {
                    cameraView.setSessionType(SessionType.PICTURE);
                    VideoIcon.setVisibility(View.VISIBLE);
                    CameraScreen.setVisibility(View.INVISIBLE);
                    CameraIcon.setVisibility(View.VISIBLE);
                    RecordVideo.setVisibility(View.INVISIBLE);
                }
                break;
            case R.id.record_Start:
                if (!Recording) {
                    RecordVideo.setBackgroundResource(R.drawable.recordred);
                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                    File file = new File(dir.getPath() + File.separator + "VID_" + timeStamp + ".mp4");
                    cameraView.startCapturingVideo(file);
                    Recording = true;
                } else {
                    RecordVideo.setBackgroundResource(R.drawable.recordblack);
                    cameraView.stopCapturingVideo();
                    Recording = false;
                }
                break;
            case R.id.tv_forgetpassword:
                Intent intent1 = new Intent(GuestUser.this, com.example.blackpearl.foldscope.Activity.ForgotPassword.class);
                startActivity(intent1);
                break;
            case R.id.b_login:
                if (Utility.isOnline(GuestUser.this)) {
                    myDialog = Utility.ShowProgressDialog(GuestUser.this, "Please Wait");
                    if (isLoginFormValidated()) {
                    } else {
                        UserName = ET_UserName.getText().toString();
                        Password = ET_Password.getText().toString();
                        new GetTokenAPI(GuestUser.this, responseListener, UserName, Password).execute();
                    }
                } else {
                    Toast.makeText(GuestUser.this, R.string.connection_error, Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.b_login_frg:
                if (Utility.isOnline(GuestUser.this)) {
                    Intent intent2 = new Intent(GuestUser.this, LoginActivity.class);
                    startActivity(intent2);
                } else {
                    Toast.makeText(GuestUser.this, R.string.connection_error, Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.b_register_frg:
                if (Utility.isOnline(GuestUser.this)) {
                    Intent intent3 = new Intent(GuestUser.this, Registration.class);
                    startActivity(intent3);
                } else {
                    Toast.makeText(GuestUser.this, R.string.connection_error, Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.b_buy_foldscope:
                if (Utility.isOnline(GuestUser.this)) {
                    Intent intent4 = new Intent(GuestUser.this, BuyFoldScope.class);
                    startActivity(intent4);
                } else {
                    Toast.makeText(GuestUser.this, R.string.connection_error, Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private void HomeLayout() {

        Tv_Msg = (TextView) findViewById(R.id.tv_msg1);
        Rv_UserStory = (RecyclerView) findViewById(R.id.rv_user_story);
        Sp_Category = (Spinner) findViewById(R.id.sp_category);
        Img_Search = (ImageView) findViewById(R.id.img_search);
        Img_Search.setOnClickListener(this);
        Img_Search.setVisibility(View.GONE);
        SwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        categoryDataArrayList = new ArrayList<CategoryData>();
        Categories = new ArrayList<String>();
        Categories.add(0, "ALL");
        ArrayList<AllPostData> allPostDataArrayList = new ArrayList<AllPostData>();
        DataBaseHelper dataBaseHelper = new DataBaseHelper(GuestUser.this);
        dataBaseHelper.CreateDatabase();
        dataBaseHelper.Open();
        Cursor cursor = dataBaseHelper.SelectAllDataFromTable("UserPost");
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                AllPostData allPostData = new AllPostData();
                allPostData.setTitle(cursor.getString(cursor.getColumnIndex("Title")));
                allPostData.setDescription(cursor.getString(cursor.getColumnIndex("Description")));
                allPostData.setName(cursor.getString(cursor.getColumnIndex("UserName")));
                allPostData.setTime(cursor.getString(cursor.getColumnIndex("Date")));
                allPostData.setProfilePic(cursor.getString(cursor.getColumnIndex("ProfilePic")));
                allPostData.setEventPic(cursor.getString(cursor.getColumnIndex("PostPic")));
                allPostData.setAllDescription(cursor.getString(cursor.getColumnIndex("DescriptionAll")));
                allPostDataArrayList.add(allPostData);
            } while (cursor.moveToNext());
            Img_Search.setVisibility(View.GONE);
            Rv_UserStory.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(GuestUser.this);
            Rv_UserStory.setLayoutManager(layoutManager);
            Rv_UserStory.setItemAnimator(new DefaultItemAnimator());
            allPostAdapter = new AllPostAdapter(GuestUser.this, allPostDataArrayList, true);
            Rv_UserStory.setAdapter(allPostAdapter);
            allPostAdapter.notifyDataSetChanged();
        } else {
            Rv_UserStory.setVisibility(View.INVISIBLE);
            Tv_Msg.setVisibility(View.VISIBLE);
        }
        SwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                SwipeRefreshLayout.setRefreshing(false);
                if (Utility.isOnline(GuestUser.this)) {
                    myDialog = Utility.ShowProgressDialog(GuestUser.this, "Please Wait");
                    Categories.clear();
                    Categories.add(0, "ALL");
                    new UserDataAPI(GuestUser.this, responseListener).execute();
                    new GetCategoriesAPI(GuestUser.this, responseListener).execute();
                    Sp_Category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                            String Name = Sp_Category.getSelectedItem().toString();
                            if (Name.equals("ALL")) {
                                new AllPostAPI(GuestUser.this, responseListener).execute();
                            } else {
                                myDialog = Utility.ShowProgressDialog(GuestUser.this, "Please Wait");
                                String CategoryId = null;
                                for (int j = 0; j < categoryDataArrayList.size(); j++) {
                                    if (Sp_Category.getSelectedItem().toString().equals(categoryDataArrayList.get(j).getCategories_title())) {
                                        CategoryId = categoryDataArrayList.get(j).getCategories_id();
                                    }
                                }
                                new CategorywisePostAPI(GuestUser.this, responseListener, CategoryId).execute();
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                } else {
                    SwipeRefreshLayout.setRefreshing(false);
                    ArrayList<AllPostData> allPostDataArrayList = new ArrayList<AllPostData>();
                    DataBaseHelper dataBaseHelper = new DataBaseHelper(GuestUser.this);
                    dataBaseHelper.CreateDatabase();
                    dataBaseHelper.Open();
                    Cursor cursor = dataBaseHelper.SelectAllDataFromTable("UserPost");
                    if (cursor != null && cursor.getCount() > 0) {
                        cursor.moveToFirst();
                        do {
                            AllPostData allPostData = new AllPostData();
                            allPostData.setTitle(cursor.getString(cursor.getColumnIndex("Title")));
                            allPostData.setDescription(cursor.getString(cursor.getColumnIndex("Description")));
                            allPostData.setName(cursor.getString(cursor.getColumnIndex("UserName")));
                            allPostData.setTime(cursor.getString(cursor.getColumnIndex("Date")));
                            allPostData.setProfilePic(cursor.getString(cursor.getColumnIndex("ProfilePic")));
                            allPostData.setEventPic(cursor.getString(cursor.getColumnIndex("PostPic")));
                            allPostData.setAllDescription(cursor.getString(cursor.getColumnIndex("DescriptionAll")));
                            allPostDataArrayList.add(allPostData);
                        } while (cursor.moveToNext());
                        Img_Search.setVisibility(View.GONE);
                        Rv_UserStory.setHasFixedSize(true);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(GuestUser.this);
                        Rv_UserStory.setLayoutManager(layoutManager);
                        Rv_UserStory.setItemAnimator(new DefaultItemAnimator());
                        allPostAdapter = new AllPostAdapter(GuestUser.this, allPostDataArrayList, false);
                        Rv_UserStory.setAdapter(allPostAdapter);
                        allPostAdapter.notifyDataSetChanged();
                    } else {
                        Rv_UserStory.setVisibility(View.INVISIBLE);
                        Tv_Msg.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    private void CameraLayout() {

        ZoomScale = (TextView) findViewById(R.id.tv_zoom);
        ZoomScaleMM = (TextView) findViewById(R.id.tv_zoom_mm);
        Display display = GuestUser.this.getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        float density = getResources().getDisplayMetrics().density;
        float dpHeight = outMetrics.heightPixels / density;
        float dpWidth = outMetrics.widthPixels / density;
        ScreeSize = dpWidth;
        ScaleSize = ScreeSize / 5.00;
        ActualScreenSize = ScreeSize * 0.2 / 40;
        ActualScaleSize = (ScaleSize * ActualScreenSize) / ScreeSize;
        Utility.LogD(TAG, "Actual Screen Size----->" + ActualScreenSize);
        Utility.LogD(TAG, "Screen Size----->" + ScreeSize);
        Utility.LogD(TAG, "Actual Scale Size----->" + ActualScaleSize);
        cameraView = (CameraView) findViewById(R.id.cameraview);
        CameraIcon = (Button) findViewById(R.id.captureimage);
        VideoIcon = (Button) findViewById(R.id.video_capture);
        RecordVideo = (Button) findViewById(R.id.record_Start);
        CameraScreen = (Button) findViewById(R.id.camera);
        camera = Camera.open();
        p = camera.getParameters();
        MaxZoom = p.getMaxZoom();
        ZoomRatios = new ArrayList<Integer>();
        ZoomRatios = p.getZoomRatios();
        Utility.LogD(TAG, "Maximum Zoom----->" + MaxZoom);
        Utility.LogD(TAG, "Zoom Ratio----->" + ZoomRatios);
        camera.release();
        LayoutScale = (RelativeLayout) findViewById(R.id.rl_camera);
        LayoutText = (LinearLayout) findViewById(R.id.rl_textview);
        CameraScreen.setOnClickListener(this);
        VideoIcon.setOnClickListener(this);
        CameraIcon.setOnClickListener(this);
        RecordVideo.setOnClickListener(this);
        dir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "Foldscope");
        ZoomScale.setText((new DecimalFormat("#.#").format(ActualScaleSize * 1000)));
        ZoomScaleMM.setText((new DecimalFormat("#.###").format(ActualScaleSize)));
        cameraView.start();
        cameraView.setSessionType(SessionType.PICTURE);
        cameraView.mapGesture(Gesture.PINCH, GestureAction.ZOOM); // Pinch to zoom!
        cameraView.mapGesture(Gesture.TAP, GestureAction.FOCUS_WITH_MARKER); // Tap to focus!
        cameraView.mapGesture(Gesture.LONG_TAP, GestureAction.CAPTURE); // Long tap to shoot!
        cameraView.setJpegQuality(50);
        cameraView.addCameraListener(new CameraListener() {
            /**
             * Notifies that the camera was opened.
             * The options object collects all supported options by the current camera.
             */
            @Override
            public void onCameraOpened(CameraOptions options) {

            }

            /**
             * Notifies that the camera session was closed.
             */
            @Override
            public void onCameraClosed() {

            }

            /**
             * Notifies about an error during the camera setup or configuration.
             * At the moment, errors that are passed here are unrecoverable. When this is called,
             * the camera has been released and is presumably showing a black preview.
             *
             * This is the right moment to show an error dialog to the user.
             */
            @Override
            public void onCameraError(CameraException error) {

            }

            /**
             * Notifies that a picture previously captured with capturePicture()
             * or captureSnapshot() is ready to be shown or saved.
             *
             * If planning to get a bitmap, you can use CameraUtils.decodeBitmap()
             * to decode the byte array taking care about orientation.
             */
            @Override
            public void onPictureTaken(byte[] picture) {

                MainImage = BitmapFactory.decodeByteArray(picture, 0, picture.length);
                String deviceMan = android.os.Build.MANUFACTURER;
                if (deviceMan.matches("samsung") || deviceMan.matches("Genymotion")) {
                    Matrix matrix = new Matrix();
                    matrix.postRotate(90);
                    NewImage = Bitmap.createBitmap(MainImage, 0, 0, MainImage.getWidth(), MainImage.getHeight(), matrix, true);
                } else {
                    NewImage = MainImage;
                }
                LayoutScale.setDrawingCacheEnabled(true);
                LayoutScale.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                //   LayoutScale.layout(0, 0, LayoutScale.getMeasuredWidth(), LayoutScale.getMeasuredHeight());
                LayoutScale.buildDrawingCache(true);
                Bitmap b = Bitmap.createBitmap(LayoutScale.getDrawingCache());
                LayoutScale.setDrawingCacheEnabled(false);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                b.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                LayoutText.setDrawingCacheEnabled(true);
                LayoutText.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                //   LayoutText.layout(0, 0, LayoutText.getMeasuredWidth(), LayoutText.getMeasuredHeight());
                LayoutText.buildDrawingCache(true);
                Bitmap b1 = Bitmap.createBitmap(LayoutText.getDrawingCache());
                ImageScale = Bitmap.createScaledBitmap(b, (int) getResources().getDimension(R.dimen.dp200), (int) getResources().getDimension(R.dimen.dp120), false);
                ImageText = Bitmap.createScaledBitmap(b1, (int) getResources().getDimension(R.dimen.dp100), (int) getResources().getDimension(R.dimen.dp60), false);
                Bitmap OriginalImage = OverlayImage(NewImage, ImageScale, ImageText);
                SaveImage(OriginalImage);
            }

            /**
             * Notifies that a video capture has just ended. The file parameter is the one that
             * was passed to startCapturingVideo(File), or a fallback video file.
             */
            @Override
            public void onVideoTaken(File video) {

            }

            /**
             * Notifies that the device was tilted or the window offset changed.
             * The orientation passed can be used to align views (e.g. buttons) to the current
             * camera viewport so they will appear correctly oriented to the user.
             */
            @Override
            public void onOrientationChanged(int orientation) {

            }

            /**
             * Notifies that user interacted with the screen and started focus with a gesture,
             * and the autofocus is trying to focus around that area.
             * This can be used to draw things on screen.
             */
            @Override
            public void onFocusStart(PointF point) {

            }

            /**
             * Notifies that a gesture focus event just ended, and the camera converged
             * to a new focus (and possibly exposure and white balance).
             */
            @Override
            public void onFocusEnd(boolean successful, PointF point) {

            }

            /**
             * Noitifies that a finger gesture just caused the camera zoom
             * to be changed. This can be used, for example, to draw a seek bar.
             */
            @Override
            public void onZoomChanged(float newValue, float[] bounds, PointF[] fingers) {

                double ZoomLevel = (double) (ZoomRatios.get((int) (MaxZoom * cameraView.getZoom()))) / 100;
                Utility.LogD(TAG, "On Zoom Change----->" + (new DecimalFormat("#.#").format(ZoomLevel)));
                double Zoom = cameraView.getZoom();
                double Factor = (MaxZoom - (MaxZoom * Zoom) + 1) / (MaxZoom * Zoom);
                double ActualScaleSize1 = ActualScaleSize / (ZoomLevel);
                ZoomScale.setText((new DecimalFormat("#.#").format(ActualScaleSize1 * 1000)));
                ZoomScaleMM.setText((new DecimalFormat("#.###").format(ActualScaleSize1)));
            }

            /**
             * Noitifies that a finger gesture just caused the camera exposure correction
             * to be changed. This can be used, for example, to draw a seek bar.
             */
            @Override
            public void onExposureCorrectionChanged(float newValue, float[] bounds, PointF[] fingers) {

            }
        });
    }

    private void LoginLayout() {

        ET_UserName = (EditText) findViewById(R.id.et_username);
        ET_Password = (EditText) findViewById(R.id.et_password);
        ForgotPassword = (TextView) findViewById(R.id.tv_forgetpassword);
        ForgotPassword.setOnClickListener(this);
        Login = (Button) findViewById(R.id.b_login);
        Login.setOnClickListener(this);
    }

    private void GuestUser_Layout() {

        Login = (Button) findViewById(R.id.b_login_frg);
        Login.setOnClickListener(this);
        Registration = (Button) findViewById(R.id.b_register_frg);
        Registration.setOnClickListener(this);
        BTNBuyFoldScope = (Button) findViewById(R.id.b_buy_foldscope);
        BTNBuyFoldScope.setOnClickListener(this);
    }

    ResponseListener responseListener = new ResponseListener() {
        @Override
        public void onResponse(String api, Const.API_RESULT result, Object obj) {

            End = 10;
            loading = true;
            pastVisiblesItems = 0;
            visibleItemCount = 0;
            totalItemCount = 0;
            SharedPreference.SharedPreference(GuestUser.this);
            if (api.equals(Const.URL_POST)) {
                Img_Search.setVisibility(View.VISIBLE);
                if (result == Const.API_RESULT.SUCCESS) {
                    Rv_UserStory.invalidate();
                    if (myDialog.isShowing())
                        myDialog.dismiss();
                    allPostDataArrayList = (ArrayList<AllPostData>) obj;
                    if (allPostDataArrayList.size() > 0) {
                        Tv_Msg.setVisibility(View.INVISIBLE);
                        Rv_UserStory.setVisibility(View.VISIBLE);
                        for (int i = 0; i < allPostDataArrayList.size(); i++) {
                            for (int j = 0; j < authorDataArrayList.size(); j++) {
                                if (allPostDataArrayList.get(i).getName() == authorDataArrayList.get(j).getUserId()) {
                                    allPostDataArrayList.get(i).setName(authorDataArrayList.get(j).getName());
                                    allPostDataArrayList.get(i).setProfilePic(authorDataArrayList.get(j).getPic_Url());
                                }
                            }
                        }
                        new DownloadImage(GuestUser.this, allPostDataArrayList).execute();
                        Rv_UserStory.setHasFixedSize(true);
                        final LinearLayoutManager layoutManager = new LinearLayoutManager(GuestUser.this);
                        Rv_UserStory.setLayoutManager(layoutManager);
                        Rv_UserStory.setItemAnimator(new DefaultItemAnimator());
                        ArrayList<AllPostData> allPostDataArrayList1;
                        if (allPostDataArrayList.size() > 5) {
                            allPostDataArrayList1 = new ArrayList<AllPostData>(allPostDataArrayList.subList(0, 5));
                        } else {
                            allPostDataArrayList1 = new ArrayList<AllPostData>(allPostDataArrayList);
                        }
                        allPostAdapter = new AllPostAdapter(GuestUser.this, allPostDataArrayList1, false);
                        Rv_UserStory.setAdapter(allPostAdapter);
                        allPostAdapter.notifyDataSetChanged();
//                        if (myDialog.isShowing()) {
//                            myDialog.dismiss();
//                        }
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
                                            Parcelable recyclerViewState;
                                            recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();
                                            recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
                                            ArrayList<AllPostData> allPostDataArrayList2;
                                            loading = false;
                                            if (allPostDataArrayList.size() > End) {
                                                allPostDataArrayList2 = new ArrayList<AllPostData>(allPostDataArrayList.subList(0, End));
                                                End = End + 5;
                                                loading = true;
                                            } else {
                                                allPostDataArrayList2 = new ArrayList<AllPostData>(allPostDataArrayList.subList(0, allPostDataArrayList.size()));
                                            }
                                            allPostAdapter = new AllPostAdapter(GuestUser.this, allPostDataArrayList2, false);
                                            Rv_UserStory.setAdapter(allPostAdapter);
                                        } else {
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
                if (result == Const.API_RESULT.SUCCESS) {
                    authorDataArrayList = (ArrayList<AuthorData>) obj;
                }
            }
            if (api.equals(Const.URL_GETCATEGOIES)) {
//                if (myDialog.isShowing()) {
//                    myDialog.dismiss();
//                }
                if (result == Const.API_RESULT.SUCCESS) {
                    categoryDataArrayList = (ArrayList<CategoryData>) obj;
                    for (int i = 0; i < categoryDataArrayList.size(); i++) {
                        Categories.add(categoryDataArrayList.get(i).getCategories_title());
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(GuestUser.this,
                                R.layout.layout_spinner, Categories);
                        dataAdapter.setDropDownViewResource(R.layout.layout_spinner_selected);
                        dataAdapter.notifyDataSetChanged();
                        Sp_Category.setAdapter(dataAdapter);
                    }
                }
            }
            if (api.equals(Const.URL_GETCATEGOIESWISEPOST)) {
                myDialog.dismiss();
                if (result == Const.API_RESULT.SUCCESS) {
                    allPostDataArrayList = (ArrayList<AllPostData>) obj;
                    if (allPostDataArrayList.size() > 0) {
                        Tv_Msg.setVisibility(View.INVISIBLE);
                        Rv_UserStory.setVisibility(View.VISIBLE);
                        for (int i = 0; i < allPostDataArrayList.size(); i++) {
                            for (int j = 0; j < authorDataArrayList.size(); j++) {
                                if (allPostDataArrayList.get(i).getName() == authorDataArrayList.get(j).getUserId()) {
                                    allPostDataArrayList.get(i).setName(authorDataArrayList.get(j).getName());
                                    allPostDataArrayList.get(i).setProfilePic(authorDataArrayList.get(j).getPic_Url());
                                }
                            }
                        }
                        Rv_UserStory.setHasFixedSize(true);
                        final LinearLayoutManager layoutManager = new LinearLayoutManager(GuestUser.this);
                        Rv_UserStory.setLayoutManager(layoutManager);
                        Rv_UserStory.setItemAnimator(new DefaultItemAnimator());
                        final ArrayList<AllPostData> allPostDataArrayList1;
                        if (allPostDataArrayList.size() > 5) {
                            allPostDataArrayList1 = new ArrayList<AllPostData>(allPostDataArrayList.subList(0, 5));
                        } else {
                            allPostDataArrayList1 = new ArrayList<AllPostData>(allPostDataArrayList);
                        }
                        allPostAdapter = new AllPostAdapter(GuestUser.this, allPostDataArrayList1, false);
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
                                            loading = false;
                                            Parcelable recyclerViewState;
                                            recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();
                                            recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
                                            ArrayList<AllPostData> allPostDataArrayList1;
                                            if (allPostDataArrayList.size() > End) {
                                                allPostDataArrayList1 = new ArrayList<AllPostData>(allPostDataArrayList.subList(0, End));
                                            } else {
                                                allPostDataArrayList1 = new ArrayList<AllPostData>(allPostDataArrayList.subList(0, allPostDataArrayList.size()));
                                            }
                                            allPostAdapter = new AllPostAdapter(GuestUser.this, allPostDataArrayList1, false);
                                            Rv_UserStory.setAdapter(allPostAdapter);
                                            allPostAdapter.notifyItemChanged(visibleItemCount);
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
            if (api.equals(Const.URL_TOKEN)) {
                myDialog.dismiss();
                if (result == Const.API_RESULT.SUCCESS) {
                    ArrayList<TokenData> tokenDataArrayList;
                    tokenDataArrayList = (ArrayList<TokenData>) obj;
                    SharedPreference.setString(Const.PREF_USER_EMAIL, tokenDataArrayList.get(0).getEmail());
                    SharedPreference.setString(Const.PREF_TOKEN, tokenDataArrayList.get(0).getToken());
                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                    String FromDate = df.format(c.getTime());
                    c.add(Calendar.DATE, 84);
                    String ToDate = df.format(c.getTime());
                    DataBaseHelper dataBaseHelper = new DataBaseHelper(GuestUser.this);
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
                    myDialog = Utility.ShowProgressDialog(GuestUser.this, "Please Wait");
                    new LoginAPI(GuestUser.this, responseListener, tokenDataArrayList.get(0).getToken()).execute();
                }
            }
            if (api.equals(Const.URL_LOGIN)) {
                myDialog.dismiss();
                if (result == Const.API_RESULT.SUCCESS) {
                    ArrayList<UserData> userDataArrayList;
                    userDataArrayList = (ArrayList<UserData>) obj;
                    SharedPreference.setString(Const.PREF_USERID, userDataArrayList.get(0).getUserId());
                    SharedPreference.setString(Const.PREF_USERNAME, UserName);
                    SharedPreference.setString(Const.PREF_PASSWORD, Password);
                    SharedPreference.setString(Const.PREF_PROFILE_PIC, userDataArrayList.get(0).getAvatar());
                    Intent intent = new Intent(GuestUser.this, RegisterUser.class);
                    startActivity(intent);
                    GuestUser.this.finishAffinity();
                }
            }
        }
    };

    private void SaveImage(Bitmap finalBitmap) {

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "Foldscope");
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fname = "IMG_" + timeStamp + ".jpg";
        File file = new File(mediaStorageDir, fname);
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"
//                + Environment.getExternalStorageDirectory())));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            final Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            final Uri contentUri = Uri.fromFile(file);
            scanIntent.setData(contentUri);
            sendBroadcast(scanIntent);
        } else {
            final Intent intent = new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory()));
            sendBroadcast(intent);
        }
    }

    public Bitmap OverlayImage(Bitmap bitmap1, Bitmap bitmap2, Bitmap bitmap3) {

        Bitmap overlayBitmap = Bitmap.createBitmap(bitmap1.getWidth(), bitmap1.getHeight(), bitmap1.getConfig());
        Canvas canvas = new Canvas(overlayBitmap);
        canvas.drawBitmap(bitmap1, new Matrix(), null);
        canvas.drawBitmap(bitmap2, (int) (canvas.getWidth() - bitmap2.getWidth() * 1.6), (int) (canvas.getHeight() - bitmap2.getHeight() * 1.6), null);
        return overlayBitmap;
    }

    private Boolean isLoginFormValidated() {

        boolean flag = false;
        if (ET_UserName.getText().toString().trim().length() == 0) {
            Toast.makeText(GuestUser.this, R.string.enter_username, Toast.LENGTH_LONG).show();
            flag = true;
            myDialog.dismiss();
            return flag;
        } else if (ET_Password.getText().toString().trim().length() == 0) {
            Toast.makeText(GuestUser.this, R.string.enter_password, Toast.LENGTH_LONG).show();
            myDialog.dismiss();
            flag = true;
        } else if (ET_Password.getText().toString().trim().length() < 6) {
            Toast.makeText(GuestUser.this, R.string.enter_correct_password, Toast.LENGTH_LONG).show();
            myDialog.dismiss();
            flag = true;
        }
        return flag;
    }
}



