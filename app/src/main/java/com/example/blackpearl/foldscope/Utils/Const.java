package com.example.blackpearl.foldscope.Utils;

public class Const {

    // connection timeout is set to 20 seconds
    public static int TIMEOUT_CONNECTION = 90000;
    // SOCKET TIMEOUT IS SET TO 30 SECONDS
    public static int TIMEOUT_SOCKET = 90000;
    //public static ArrayList<ImageBean> imageList;

    // Api Response
    public static enum API_RESULT {
        SUCCESS, FAIL
    }

	/* Default Message */
    public static String PREF_PASSWORD = "PREF_PASSWORD";
    public static String PREF_USERID = "UserID";
    public static String PREF_USERNAME = "UserName";
    public static String PREF_PROFILE_PIC = "ProfilePictureRealPath";
    public static String PREF_USER_EMAIL = "PREF_EMAIL";
    public static String PREF_TOKEN = "PREF_Token";
    public static final String URL_POST = "http://svt.x10.bz/wp-json/wp/v2/posts";
    public static final String URL_POST_ALL = "http://svt.x10.bz/wp-json/wp/v2/posts?page=1&per_page=";
    public static final String URL_TOKEN = "http://svt.x10.bz/wp-json/jwt-auth/v1/token";
    public static final String URL_LOGIN = "http://svt.x10.bz/wp-json/wp/v2/users/me/";
    public static final String URL_CHECK_USERNAME = "http://svt.x10.bz/wp-json/wp/v2/users";
    public static final String URL_REGISTRATION = "http://svt.x10.bz/api/user/register/?";
    public static final String URL_GETCATEGOIES = "http://svt.x10.bz/wp-json/wp/v2/categories";
    public static final String URL_GETCATEGOIESWISEPOST = "http://svt.x10.bz/wp-json/wp/v2/posts?";
    public static final String URL_FORGOT_PASSWORD = "http://svt.x10.bz/api/user/retrieve_password/?user_login=";
    public static final String URL_SEARCH = "http://svt.x10.bz/wp-json/wp/v2/posts/?search=";
    public static final String URL_MY_EXPERIMENTS = "http://svt.x10.bz/wp-json/wp/v2/posts/?author=";
    public static final String URL_POST_BLOG = "http://svt.x10.bz/wp-json/wp/v2/posts";
    public static final String URL_UPLOAD_MEDIA = "http://svt.x10.bz/wp-json/wp/v2/media";
}