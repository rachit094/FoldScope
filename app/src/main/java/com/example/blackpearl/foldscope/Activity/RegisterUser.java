package com.example.blackpearl.foldscope.Activity;

import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Camera;
import android.net.Uri;
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
import android.view.ContextMenu;
import android.view.Display;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.example.blackpearl.foldscope.Api.PublishPostAPI;
import com.example.blackpearl.foldscope.Api.UploadFileAPI;
import com.example.blackpearl.foldscope.Api.UserDataAPI;
import com.example.blackpearl.foldscope.Database.DataBaseHelper;
import com.example.blackpearl.foldscope.Model.AllPostData;
import com.example.blackpearl.foldscope.Model.AuthorData;
import com.example.blackpearl.foldscope.Model.CategoryData;
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
import com.squareup.picasso.Picasso;

import org.wordpress.android.editor.EditorFragment;
import org.wordpress.android.editor.EditorFragmentAbstract;
import org.wordpress.android.editor.EditorMediaUploadListener;
import org.wordpress.android.editor.ImageSettingsDialogFragment;
import org.wordpress.android.util.AppLog;
import org.wordpress.android.util.helpers.MediaFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisterUser extends AppCompatActivity implements EditorFragmentAbstract.EditorFragmentListener, EditorFragmentAbstract.EditorDragAndDropListener, View.OnClickListener {

    private android.support.v4.app.FragmentTransaction transaction;
    private ActionBar actionBar;
    private ImageView Back;
    private TextView Title, Publish, Draft;
    private String TAG = "RegisterUser";
    public static final String EDITOR_PARAM = "EDITOR_PARAM";
    public static final String TITLE_PARAM = "TITLE_PARAM";
    public static final String CONTENT_PARAM = "CONTENT_PARAM";
    public static final String DRAFT_PARAM = "DRAFT_PARAM";
    public static final String TITLE_PLACEHOLDER_PARAM = "TITLE_PLACEHOLDER_PARAM";
    public static final String CONTENT_PLACEHOLDER_PARAM = "CONTENT_PLACEHOLDER_PARAM";
    public static final int USE_NEW_EDITOR = 1;
    public static final int USE_LEGACY_EDITOR = 2;
    public static final int ADD_MEDIA_ACTIVITY_REQUEST_CODE = 1111;
    public static final int ADD_MEDIA_FAIL_ACTIVITY_REQUEST_CODE = 1112;
    public static final int ADD_MEDIA_SLOW_NETWORK_REQUEST_CODE = 1113;
    public static final String MEDIA_REMOTE_ID_SAMPLE = "123";
    private static final int SELECT_IMAGE_MENU_POSITION = 0;
    private static final int SELECT_IMAGE_FAIL_MENU_POSITION = 1;
    private static final int SELECT_VIDEO_MENU_POSITION = 2;
    private static final int SELECT_VIDEO_FAIL_MENU_POSITION = 3;
    private static final int SELECT_IMAGE_SLOW_MENU_POSITION = 4;
    private EditorFragmentAbstract mEditorFragment;
    private Map<String, String> mFailedUploads;
    private String TitleBlog, ContentBlog, MediaUrl;
    private ProgressDialog myDialog;
    private View CameraLayout;
    private View CreateBlog_Layout;
    private View HomeLayout;
    private View Profile_RU_Layout;
    private RecyclerView Rv_UserStory;
    private AllPostAdapter allPostAdapter;
    private ArrayList<String> Categories;
    private TextView Tv_Msg;
    private Spinner Sp_Category;
    private ImageView Img_Search;
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
    private Button MyExperience, BuyFoldscope, Logout;
    private TextView Name, Email;
    private String ProfilePath;
    private ImageView ProfileImage;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_user_story_ru:
                    Title.setText("HOME");
                    Publish.setVisibility(View.INVISIBLE);
                    Draft.setVisibility(View.INVISIBLE);
                    HomeLayout.setVisibility(View.VISIBLE);
                    CameraLayout.setVisibility(View.GONE);
                    CreateBlog_Layout.setVisibility(View.GONE);
                    Profile_RU_Layout.setVisibility(View.GONE);
                    Rv_UserStory.scrollToPosition(0);
                    cameraView.stop();
                    return true;
                case R.id.navigation_camera_ru:
                    Title.setText("CAMERA");
                    Publish.setVisibility(View.INVISIBLE);
                    Draft.setVisibility(View.INVISIBLE);
                    HomeLayout.setVisibility(View.GONE);
                    CameraLayout.setVisibility(View.VISIBLE);
                    CreateBlog_Layout.setVisibility(View.GONE);
                    Profile_RU_Layout.setVisibility(View.GONE);
                    CameraLayout();
                    cameraView.start();
                    return true;
                case R.id.navigation_create_blog_ru:
                    Title.setText("CREATE BLOG");
                    Publish.setVisibility(View.INVISIBLE);
                    Publish.setVisibility(View.VISIBLE);
                    Publish.setText("PUBLISH");
                    Publish.setTypeface(Typeface.DEFAULT_BOLD);
                    Draft.setVisibility(View.INVISIBLE);
                    Draft.setText("DRAFT");
                    Draft.setTypeface(Typeface.DEFAULT_BOLD);
                    HomeLayout.setVisibility(View.GONE);
                    CameraLayout.setVisibility(View.GONE);
                    CreateBlog_Layout.setVisibility(View.VISIBLE);
                    Profile_RU_Layout.setVisibility(View.GONE);
                    cameraView.stop();
                    return true;
                case R.id.navigation_profile_ru:
                    Title.setText("MENU");
                    Publish.setVisibility(View.INVISIBLE);
                    Draft.setVisibility(View.INVISIBLE);
                    HomeLayout.setVisibility(View.GONE);
                    CameraLayout.setVisibility(View.GONE);
                    CreateBlog_Layout.setVisibility(View.GONE);
                    Profile_RU_Layout.setVisibility(View.VISIBLE);
                    cameraView.stop();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
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
        Publish.setOnClickListener(this);
        Draft = (TextView) findViewById(R.id.tv_draft);
        Draft.setOnClickListener(this);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation_ru);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        mFailedUploads = new HashMap<>();
        HomeLayout = (View) findViewById(R.id.home_layout);
        CameraLayout = (View) findViewById(R.id.camera_layout);
        CreateBlog_Layout = (View) findViewById(R.id.create_blog_layout);
        Profile_RU_Layout = (View) findViewById(R.id.profile_ru_layout);
        HomeLayout();
        Profile_RU_Layout();
        cameraView = (CameraView) findViewById(R.id.cameraview);
    }

    @Override
    public void onAttachFragment(Fragment fragment) {

        super.onAttachFragment(fragment);
        if (fragment instanceof EditorFragmentAbstract) {
            mEditorFragment = (EditorFragmentAbstract) fragment;
        }
    }

    @Override
    public void onBackPressed() {

        Fragment fragment = getFragmentManager()
                .findFragmentByTag(ImageSettingsDialogFragment.IMAGE_SETTINGS_DIALOG_TAG);
        if (fragment != null && fragment.isVisible()) {
            ((ImageSettingsDialogFragment) fragment).dismissFragment();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        menu.add(0, SELECT_IMAGE_MENU_POSITION, 0, getString(R.string.select_image));
        //  menu.add(0, SELECT_IMAGE_FAIL_MENU_POSITION, 0, getString(R.string.select_image_fail));
        menu.add(0, SELECT_VIDEO_MENU_POSITION, 0, getString(R.string.select_video));
        //   menu.add(0, SELECT_VIDEO_FAIL_MENU_POSITION, 0, getString(R.string.select_video_fail));
        //   menu.add(0, SELECT_IMAGE_SLOW_MENU_POSITION, 0, getString(R.string.select_image_slow_network));
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        Intent intent = new Intent(Intent.ACTION_PICK);
        switch (item.getItemId()) {
            case SELECT_IMAGE_MENU_POSITION:
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent = Intent.createChooser(intent, getString(R.string.select_image));
                startActivityForResult(intent, ADD_MEDIA_ACTIVITY_REQUEST_CODE);
                return true;
            case SELECT_IMAGE_FAIL_MENU_POSITION:
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent = Intent.createChooser(intent, getString(R.string.select_image_fail));
                startActivityForResult(intent, ADD_MEDIA_FAIL_ACTIVITY_REQUEST_CODE);
                return true;
            case SELECT_VIDEO_MENU_POSITION:
                intent.setType("video/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent = Intent.createChooser(intent, getString(R.string.select_video));
                startActivityForResult(intent, ADD_MEDIA_ACTIVITY_REQUEST_CODE);
                return true;
            case SELECT_VIDEO_FAIL_MENU_POSITION:
                intent.setType("video/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent = Intent.createChooser(intent, getString(R.string.select_video_fail));
                startActivityForResult(intent, ADD_MEDIA_FAIL_ACTIVITY_REQUEST_CODE);
                return true;
            case SELECT_IMAGE_SLOW_MENU_POSITION:
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent = Intent.createChooser(intent, getString(R.string.select_image_slow_network));
                startActivityForResult(intent, ADD_MEDIA_SLOW_NETWORK_REQUEST_CODE);
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        Uri mediaUri = data.getData();
        MediaFile mediaFile = new MediaFile();
        String mediaId = String.valueOf(System.currentTimeMillis());
        mediaFile.setMediaId(mediaId);
        mediaFile.setVideo(mediaUri.toString().contains("video"));
        System.out.println("Media Url--->" + mediaUri.getPath());
        switch (requestCode) {
            case ADD_MEDIA_ACTIVITY_REQUEST_CODE:
                myDialog = Utility.ShowProgressDialog(RegisterUser.this, "Please Wait");
                if (mediaUri.getPath().contains("mp4")) {
                    new UploadFileAPI(RegisterUser.this, responseListener1, mediaUri.getPath(), SharedPreference.getString(Const.PREF_TOKEN), "video").execute();
                } else {
                    new UploadFileAPI(RegisterUser.this, responseListener1, mediaUri.getPath(), SharedPreference.getString(Const.PREF_TOKEN), "image").execute();
                }
//                mEditorFragment.appendMediaFile(mediaFile, mediaUri.toString(), null);
//
//                if (mEditorFragment instanceof EditorMediaUploadListener) {
//                    simulateFileUpload(mediaId, mediaUri.toString());
//                }
                break;
            case ADD_MEDIA_FAIL_ACTIVITY_REQUEST_CODE:
                mEditorFragment.appendMediaFile(mediaFile, mediaUri.toString(), null);
                if (mEditorFragment instanceof EditorMediaUploadListener) {
                    simulateFileUploadFail(mediaId, mediaUri.toString());
                }
                break;
            case ADD_MEDIA_SLOW_NETWORK_REQUEST_CODE:
                mEditorFragment.appendMediaFile(mediaFile, mediaUri.toString(), null);
                if (mEditorFragment instanceof EditorMediaUploadListener) {
                    simulateSlowFileUpload(mediaId, mediaUri.toString());
                }
                break;
        }
    }

    @Override
    public void onSettingsClicked() {
        // TODO
    }

    @Override
    public void onAddMediaClicked() {
        // TODO
    }

    @Override
    public void onMediaRetryClicked(String mediaId) {

        if (mFailedUploads.containsKey(mediaId)) {
            simulateFileUpload(mediaId, mFailedUploads.get(mediaId));
        }
    }

    @Override
    public void onMediaUploadCancelClicked(String mediaId, boolean delete) {

    }

    @Override
    public void onFeaturedImageChanged(long mediaId) {

    }

    @Override
    public void onVideoPressInfoRequested(String videoId) {

    }

    @Override
    public String onAuthHeaderRequested(String url) {

        return "";
    }

    @Override
    public void onEditorFragmentInitialized() {
        // arbitrary setup
        mEditorFragment.setFeaturedImageSupported(true);
        mEditorFragment.setBlogSettingMaxImageWidth("600");
        mEditorFragment.setDebugModeEnabled(true);
        // get title and content and draft switch
        boolean isLocalDraft = getIntent().getBooleanExtra(DRAFT_PARAM, true);
        mEditorFragment.setTitlePlaceholder("Post Title");
        mEditorFragment.setContentPlaceholder("Share your Story here...");
        mEditorFragment.setLocalDraft(true);
    }

    @Override
    public void saveMediaFile(MediaFile mediaFile) {
        // TODO
    }

    @Override
    public void onTrackableEvent(EditorFragmentAbstract.TrackableEvent event) {

        AppLog.d(AppLog.T.EDITOR, "Trackable event: " + event);
    }

    private void simulateFileUpload(final String mediaId, final String mediaUrl) {

        Thread thread = new Thread() {
            @Override
            public void run() {

                try {
                    float count = (float) 0.1;
                    while (count < 1.1) {
                        sleep(500);
                        ((EditorMediaUploadListener) mEditorFragment).onMediaUploadProgress(mediaId, count);
                        count += 0.1;
                    }
                    MediaFile mediaFile = new MediaFile();
                    mediaFile.setMediaId(MEDIA_REMOTE_ID_SAMPLE);
                    mediaFile.setFileURL(MediaUrl);
                    ((EditorMediaUploadListener) mEditorFragment).onMediaUploadSucceeded(mediaId, mediaFile);
                    if (mFailedUploads.containsKey(mediaId)) {
                        mFailedUploads.remove(mediaId);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }

    private void simulateFileUploadFail(final String mediaId, final String mediaUrl) {

        Thread thread = new Thread() {
            @Override
            public void run() {

                try {
                    float count = (float) 0.1;
                    while (count < 0.6) {
                        sleep(500);
                        ((EditorMediaUploadListener) mEditorFragment).onMediaUploadProgress(mediaId, count);
                        count += 0.1;
                    }
                    ((EditorMediaUploadListener) mEditorFragment).onMediaUploadFailed(mediaId,
                            getString(R.string.tap_to_try_again));
                    mFailedUploads.put(mediaId, mediaUrl);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }

    private void simulateSlowFileUpload(final String mediaId, final String mediaUrl) {

        Thread thread = new Thread() {
            @Override
            public void run() {

                try {
                    sleep(5000);
                    float count = (float) 0.1;
                    while (count < 1.1) {
                        sleep(2000);
                        ((EditorMediaUploadListener) mEditorFragment).onMediaUploadProgress(mediaId, count);
                        count += 0.1;
                    }
                    MediaFile mediaFile = new MediaFile();
                    mediaFile.setMediaId(MEDIA_REMOTE_ID_SAMPLE);
                    mediaFile.setFileURL(mediaUrl);
                    ((EditorMediaUploadListener) mEditorFragment).onMediaUploadSucceeded(mediaId, mediaFile);
                    if (mFailedUploads.containsKey(mediaId)) {
                        mFailedUploads.remove(mediaId);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }

    @Override
    public void onMediaDropped(ArrayList<Uri> mediaUri) {
        // TODO
    }

    @Override
    public void onRequestDragAndDropPermissions(DragEvent dragEvent) {
        // TODO
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.tv_publish:
                Utility.HideSoftKeyboard(RegisterUser.this);
                if (Utility.isOnline(RegisterUser.this)) {
                    try {
                        TitleBlog = mEditorFragment.getTitle().toString();
                        ContentBlog = mEditorFragment.getContent().toString();
                        if (TitleBlog.length() == 0) {
                            Toast.makeText(this, R.string.enter_title, Toast.LENGTH_LONG).show();
                        } else if (ContentBlog.length() == 0) {
                            Toast.makeText(this, R.string.enter_content, Toast.LENGTH_LONG).show();
                        } else {
                            TitleBlog = mEditorFragment.getTitle().toString();
                            ContentBlog = mEditorFragment.getContent().toString();
                            myDialog = Utility.ShowProgressDialog(RegisterUser.this, "Please Wait");
                            new PublishPostAPI(RegisterUser.this, responseListener1, TitleBlog, ContentBlog, "publish", SharedPreference.getString(Const.PREF_TOKEN)).execute();
                        }
                    } catch (EditorFragment.IllegalEditorStateException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(this, R.string.connection_error, Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.tv_draft:
                DataBaseHelper dataBaseHelper = new DataBaseHelper(RegisterUser.this);
                dataBaseHelper.CreateDatabase();
                dataBaseHelper.Open();
                ContentValues contentValues = new ContentValues();
                String TitleDraft, ContentDraft;
                try {
                    TitleDraft = mEditorFragment.getTitle().toString();
                    ContentDraft = mEditorFragment.getContent().toString();
                    if (TitleDraft.length() == 0) {
                        Toast.makeText(this, "Please Enter Title", Toast.LENGTH_LONG).show();
                    } else if (ContentDraft.length() == 0) {
                        Toast.makeText(this, "Please Enter Content", Toast.LENGTH_LONG).show();
                    } else {
                        TitleDraft = mEditorFragment.getTitle().toString();
                        ContentDraft = mEditorFragment.getContent().toString();
                        SharedPreference.SharedPreference(RegisterUser.this);
                        contentValues.put("UserId", SharedPreference.getString(Const.PREF_USERID));
                        contentValues.put("Title", TitleDraft);
                        contentValues.put("Content", ContentDraft);
                        if (dataBaseHelper.InsertIntoTable("CreatePost", contentValues)) {
                        } else {
                        }
                    }
                } catch (EditorFragment.IllegalEditorStateException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.b_my_experience:
                Intent intent = new Intent(RegisterUser.this, MyExperiment.class);
                startActivity(intent);
                break;
            case R.id.b_buy_foldscope:
                if (Utility.isOnline(RegisterUser.this)) {
                    Intent intent2 = new Intent(RegisterUser.this, BuyFoldScope.class);
                    startActivity(intent2);
                } else {
                    Toast.makeText(RegisterUser.this, R.string.connection_error, Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.b_logout:
                LogoutDialog();
                break;
            case R.id.img_search:
                Intent intent1 = new Intent(this, SearchActivity.class);
                startActivity(intent1);
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
                    Toast.makeText(RegisterUser.this, "Please Stop Video Capture.", Toast.LENGTH_LONG).show();
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
//        if (Utility.isOnline(RegisterUser.this)) {
//            myDialog = Utility.ShowProgressDialog(RegisterUser.this, "Please Wait");
//            new UserDataAPI(RegisterUser.this, responseListener).execute();
//            new GetCategoriesAPI(RegisterUser.this, responseListener).execute();
//            Sp_Category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                @Override
//                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//
//                    String Name = Sp_Category.getSelectedItem().toString();
//                    if (Name.equals("ALL")) {
//                        new AllPostAPI(RegisterUser.this, responseListener).execute();
//                    } else {
//                        myDialog = Utility.ShowProgressDialog(RegisterUser.this, "Please Wait");
//                        String CategoryId = null;
//                        for (int j = 0; j < categoryDataArrayList.size(); j++) {
//                            if (Sp_Category.getSelectedItem().toString().equals(categoryDataArrayList.get(j).getCategories_title())) {
//                                CategoryId = categoryDataArrayList.get(j).getCategories_id();
//                            }
//                        }
//                        new CategorywisePostAPI(RegisterUser.this, responseListener, CategoryId).execute();
//                    }
//                }
//
//                @Override
//                public void onNothingSelected(AdapterView<?> adapterView) {
//
//                }
//            });
//        } else {
            ArrayList<AllPostData> allPostDataArrayList = new ArrayList<AllPostData>();
            DataBaseHelper dataBaseHelper = new DataBaseHelper(RegisterUser.this);
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
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(RegisterUser.this);
                Rv_UserStory.setLayoutManager(layoutManager);
                Rv_UserStory.setItemAnimator(new DefaultItemAnimator());
                allPostAdapter = new AllPostAdapter(RegisterUser.this, allPostDataArrayList);
                Rv_UserStory.setAdapter(allPostAdapter);
                allPostAdapter.notifyDataSetChanged();
            } else {
                Rv_UserStory.setVisibility(View.INVISIBLE);
                Tv_Msg.setVisibility(View.VISIBLE);
            }
//        }
        SwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                SwipeRefreshLayout.setRefreshing(false);
                if (Utility.isOnline(RegisterUser.this)) {
                    myDialog = Utility.ShowProgressDialog(RegisterUser.this, "Please Wait");
                    Categories.clear();
                    Categories.add(0, "ALL");
                    new UserDataAPI(RegisterUser.this, responseListener).execute();
                    new GetCategoriesAPI(RegisterUser.this, responseListener).execute();
                    Sp_Category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                            String Name = Sp_Category.getSelectedItem().toString();
                            if (Name.equals("ALL")) {
                                new AllPostAPI(RegisterUser.this, responseListener).execute();
                            } else {
                                myDialog = Utility.ShowProgressDialog(RegisterUser.this, "Please Wait");
                                String CategoryId = null;
                                for (int j = 0; j < categoryDataArrayList.size(); j++) {
                                    if (Sp_Category.getSelectedItem().toString().equals(categoryDataArrayList.get(j).getCategories_title())) {
                                        CategoryId = categoryDataArrayList.get(j).getCategories_id();
                                    }
                                }
                                new CategorywisePostAPI(RegisterUser.this, responseListener, CategoryId).execute();
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                } else {
                    SwipeRefreshLayout.setRefreshing(false);
                    ArrayList<AllPostData> allPostDataArrayList = new ArrayList<AllPostData>();
                    DataBaseHelper dataBaseHelper = new DataBaseHelper(RegisterUser.this);
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
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(RegisterUser.this);
                        Rv_UserStory.setLayoutManager(layoutManager);
                        Rv_UserStory.setItemAnimator(new DefaultItemAnimator());
                        allPostAdapter = new AllPostAdapter(RegisterUser.this, allPostDataArrayList);
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
        Display display = RegisterUser.this.getWindowManager().getDefaultDisplay();
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
//                LayoutScale.layout(0, 0, LayoutScale.getMeasuredWidth(), LayoutScale.getMeasuredHeight());
                LayoutScale.buildDrawingCache(true);
                Bitmap b = Bitmap.createBitmap(LayoutScale.getDrawingCache());
                LayoutScale.setDrawingCacheEnabled(false);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                b.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                LayoutText.setDrawingCacheEnabled(true);
                LayoutText.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
//                LayoutText.layout(0, 0, LayoutText.getMeasuredWidth(), LayoutText.getMeasuredHeight());
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

    private void Profile_RU_Layout() {

        SharedPreference.SharedPreference(RegisterUser.this);
        MyExperience = (Button) findViewById(R.id.b_my_experience);
        MyExperience.setOnClickListener(this);
        BuyFoldscope = (Button) findViewById(R.id.b_buy_foldscope);
        BuyFoldscope.setOnClickListener(this);
        Logout = (Button) findViewById(R.id.b_logout);
        Logout.setOnClickListener(this);
        Name = (TextView) findViewById(R.id.tv_ru_name);
        Email = (TextView) findViewById(R.id.tv_ru_email);
        ProfileImage = (ImageView) findViewById(R.id.img_ru_profile);
        ProfilePath = "http:" + SharedPreference.getString(Const.PREF_PROFILE_PIC);
        Picasso.with(RegisterUser.this)
                .load(ProfilePath)
                .placeholder(R.drawable.test)
                .error(R.drawable.test)
                .into(ProfileImage);
        Name.setText(SharedPreference.getString(Const.PREF_USERNAME));
        Email.setText(SharedPreference.getString(Const.PREF_USER_EMAIL));
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

    ResponseListener responseListener = new ResponseListener() {
        @Override
        public void onResponse(String api, Const.API_RESULT result, Object obj) {

            End = 10;
            loading = true;
            pastVisiblesItems = 0;
            visibleItemCount = 0;
            totalItemCount = 0;
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
                        new DownloadImage(RegisterUser.this, allPostDataArrayList).execute();
                        Rv_UserStory.setHasFixedSize(true);
                        final LinearLayoutManager layoutManager = new LinearLayoutManager(RegisterUser.this);
                        Rv_UserStory.setLayoutManager(layoutManager);
                        Rv_UserStory.setItemAnimator(new DefaultItemAnimator());
                        ArrayList<AllPostData> allPostDataArrayList1;
                        if (allPostDataArrayList.size() > 5) {
                            allPostDataArrayList1 = new ArrayList<AllPostData>(allPostDataArrayList.subList(0, 5));
                        } else {
                            allPostDataArrayList1 = new ArrayList<AllPostData>(allPostDataArrayList);
                        }
                        allPostAdapter = new AllPostAdapter(RegisterUser.this, allPostDataArrayList1);
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
                                            allPostAdapter = new AllPostAdapter(RegisterUser.this, allPostDataArrayList2);
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
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(RegisterUser.this,
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
                        final LinearLayoutManager layoutManager = new LinearLayoutManager(RegisterUser.this);
                        Rv_UserStory.setLayoutManager(layoutManager);
                        Rv_UserStory.setItemAnimator(new DefaultItemAnimator());
                        final ArrayList<AllPostData> allPostDataArrayList1;
                        if (allPostDataArrayList.size() > 5) {
                            allPostDataArrayList1 = new ArrayList<AllPostData>(allPostDataArrayList.subList(0, 5));
                        } else {
                            allPostDataArrayList1 = new ArrayList<AllPostData>(allPostDataArrayList);
                        }
                        allPostAdapter = new AllPostAdapter(RegisterUser.this, allPostDataArrayList1);
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
                                            allPostAdapter = new AllPostAdapter(RegisterUser.this, allPostDataArrayList1);
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
        }
    };
    ResponseListener responseListener1 = new ResponseListener() {
        @Override
        public void onResponse(String api, Const.API_RESULT result, Object obj) {

            if (api.equals(Const.URL_POST_BLOG)) {
                myDialog.dismiss();
                if (result == Const.API_RESULT.SUCCESS) {
                    String EventId = (String) obj;
                    if (EventId == null) {
                    } else {
                        Toast.makeText(RegisterUser.this, R.string.upload_post, Toast.LENGTH_LONG).show();
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                    }
                }
            }
            if (api.equals(Const.URL_UPLOAD_MEDIA)) {
                if (result == Const.API_RESULT.SUCCESS) {
                    myDialog.dismiss();
                    MediaUrl = (String) obj;
                    if (MediaUrl == null) {
                    } else {
                        Toast.makeText(RegisterUser.this, R.string.upload_media, Toast.LENGTH_LONG).show();
                        if (MediaUrl.contains(".mp4")) {
                        }
                        MediaFile mediaFile = new MediaFile();
                        String mediaId = String.valueOf(System.currentTimeMillis());
                        mediaFile.setMediaId(mediaId);
                        mEditorFragment.appendMediaFile(mediaFile, MediaUrl, null);
                        if (mEditorFragment instanceof EditorMediaUploadListener) {
                            simulateFileUpload(mediaId, MediaUrl.toString());
                        }
                    }
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
            CameraLayout();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Bitmap OverlayImage(Bitmap bitmap1, Bitmap bitmap2, Bitmap bitmap3) {

        Bitmap overlayBitmap = Bitmap.createBitmap(bitmap1.getWidth(), bitmap1.getHeight(), bitmap1.getConfig());
        Canvas canvas = new Canvas(overlayBitmap);
        canvas.drawBitmap(bitmap1, new Matrix(), null);
        canvas.drawBitmap(bitmap2, (int) (canvas.getWidth() - bitmap2.getWidth() * 1.6), (int) (canvas.getHeight() - bitmap2.getHeight() * 1.6), null);
        //  canvas.drawBitmap(bitmap3, canvas.getWidth() - (int) (bitmap3.getWidth() * 2.7), (int) (canvas.getHeight() - bitmap3.getHeight() * 2), null);
        return overlayBitmap;
    }

    public void LogoutDialog() {
        // TODO Auto-generated method stub
        final Dialog dialog = new Dialog(RegisterUser.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.CENTER;
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(getResources().getColor(R.color.colorTransperent)));
        dialog.setContentView(R.layout.dialog_logout);
        TextView txtv_message = (TextView) dialog.findViewById(R.id.txtv_message);
        txtv_message.setText("Are you sure you want to Logout?");
        Button btn_confirm = (Button) dialog.findViewById(R.id.btn_confirm);
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                SharedPreference.ClearAll();
                DataBaseHelper dataBaseHelper = new DataBaseHelper(RegisterUser.this);
                dataBaseHelper.CreateDatabase();
                dataBaseHelper.Open();
                dataBaseHelper.DeleteAllDataFromTableName("Authentication");
                RegisterUser.this.finishAffinity();
                startActivity(new Intent(RegisterUser.this, SplashActivity.class));
            }
        });
        Button btn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
