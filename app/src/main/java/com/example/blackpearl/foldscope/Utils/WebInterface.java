package com.example.blackpearl.foldscope.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.util.Date;

public class WebInterface {

    public static ApiResponse doPost(String url, String jsonString)
            throws Exception {

        ApiResponse apiResponse = null;
        HttpEntity httpentity = null;
        HttpClient httpClient = null;
        HttpParams httpParameters = null;
        HttpPost httppost = null;
        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        trustStore.load(null, null);
        MySSLSocketFactory sf = new MySSLSocketFactory(trustStore);
        sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        HttpParams params = new BasicHttpParams();
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
        SchemeRegistry registry = new SchemeRegistry();
        registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        registry.register(new Scheme("https", sf, 443));
        ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);
        httpParameters = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParameters, Const.TIMEOUT_CONNECTION);
        HttpConnectionParams.setSoTimeout(httpParameters, Const.TIMEOUT_SOCKET);
        // set timeout
        httpParameters = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParameters, Const.TIMEOUT_CONNECTION);
        HttpConnectionParams.setSoTimeout(httpParameters, Const.TIMEOUT_SOCKET);
        httpClient = new DefaultHttpClient(ccm, params);
        httppost = new HttpPost(url);
        StringEntity entity = new StringEntity(jsonString, "UTF-8");
        httppost.setEntity(entity);
        httppost.setHeader("Content-Type", "application/x-www-form-urlencoded");
        HttpResponse httpResponse = httpClient.execute(httppost);
        apiResponse = new ApiResponse();
        apiResponse.code = httpResponse.getStatusLine().getStatusCode();
        if (apiResponse.code == HttpStatus.SC_OK) {
            httpentity = httpResponse.getEntity();
            apiResponse.response = new String(EntityUtils.toString(httpentity));
        }
        // release
        httpentity = null;
        httpResponse = null;
        httppost = null;
        httpClient = null;
        return apiResponse;
    }

    public static ApiResponse doGet(String url) throws Exception {

        ApiResponse apiResponse = null;
        HttpEntity httpentity = null;
        HttpGet httpGet = null;
        HttpClient httpclient = new DefaultHttpClient();
        url = url.replace(" ", "%20");
        httpGet = new HttpGet(url);
        HttpResponse httpResponse = httpclient.execute(httpGet);
        apiResponse = new ApiResponse();
        apiResponse.code = httpResponse.getStatusLine().getStatusCode();
        apiResponse.header = httpResponse.getLastHeader("X-WP-Total").toString().replaceAll("[^0-9]", "");
        if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            httpentity = httpResponse.getEntity();
            apiResponse.response = new String(EntityUtils.toString(httpentity));
        }
        // Release
        httpResponse = null;
        httpclient = null;
        httpGet = null;
        httpentity = null;
        return apiResponse;
    }

    public static ApiResponse doGetLogin(String url, String Token) throws Exception {

        ApiResponse apiResponse = null;
        HttpEntity httpentity = null;
        HttpGet httpGet = null;
        HttpClient httpclient = new DefaultHttpClient();
        url = url.replace(" ", "%20");
        httpGet = new HttpGet(url);
        httpGet.setHeader("Content-Type", "application/json");
        httpGet.setHeader("Authorization", "Bearer " + Token);
        HttpResponse httpResponse = httpclient.execute(httpGet);
        apiResponse = new ApiResponse();
        apiResponse.code = httpResponse.getStatusLine().getStatusCode();
        if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            httpentity = httpResponse.getEntity();
            apiResponse.response = new String(EntityUtils.toString(httpentity));
        }
        // Release
        httpResponse = null;
        httpclient = null;
        httpGet = null;
        httpentity = null;
        return apiResponse;
    }

    public static ApiResponse doPostForPostBlog(String url, String jsonString, String Token)
            throws Exception {

        ApiResponse apiResponse = null;
        HttpEntity httpentity = null;
        HttpClient httpClient = null;
        HttpParams httpParameters = null;
        HttpPost httppost = null;
        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        trustStore.load(null, null);
        MySSLSocketFactory sf = new MySSLSocketFactory(trustStore);
        sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        HttpParams params = new BasicHttpParams();
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
        SchemeRegistry registry = new SchemeRegistry();
        registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        registry.register(new Scheme("https", sf, 443));
        ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);
        httpParameters = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParameters, Const.TIMEOUT_CONNECTION);
        HttpConnectionParams.setSoTimeout(httpParameters, Const.TIMEOUT_SOCKET);
        // set timeout
        httpParameters = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParameters, Const.TIMEOUT_CONNECTION);
        HttpConnectionParams.setSoTimeout(httpParameters, Const.TIMEOUT_SOCKET);
        httpClient = new DefaultHttpClient(ccm, params);
        httppost = new HttpPost(url);
        StringEntity entity = new StringEntity(jsonString, "UTF-8");
        httppost.setEntity(entity);
        httppost.setHeader("Content-Type", "application/x-www-form-urlencoded");
        httppost.setHeader("Authorization", "Bearer " + Token);
        HttpResponse httpResponse = httpClient.execute(httppost);
        apiResponse = new ApiResponse();
        apiResponse.code = httpResponse.getStatusLine().getStatusCode();
//        if (apiResponse.code == HttpStatus.SC_OK) {
        httpentity = httpResponse.getEntity();
        apiResponse.response = new String(EntityUtils.toString(httpentity));
//        }
        // release
        httpentity = null;
        httpResponse = null;
        httppost = null;
        httpClient = null;
        return apiResponse;
    }

    public static ApiResponse doPostForUploadFile(String url, String Type, String Token, String FilePath)
            throws Exception {

        ApiResponse apiResponse = null;
        HttpEntity httpentity = null;
        HttpClient httpClient = null;
        HttpParams httpParameters = null;
        HttpPost httppost = null;
        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        trustStore.load(null, null);
        MySSLSocketFactory sf = new MySSLSocketFactory(trustStore);
        sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        HttpParams params = new BasicHttpParams();
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
        SchemeRegistry registry = new SchemeRegistry();
        registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        registry.register(new Scheme("https", sf, 443));
        ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);
        httpParameters = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParameters, Const.TIMEOUT_CONNECTION);
        HttpConnectionParams.setSoTimeout(httpParameters, Const.TIMEOUT_SOCKET);
        // set timeout
        httpParameters = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParameters, Const.TIMEOUT_CONNECTION);
        HttpConnectionParams.setSoTimeout(httpParameters, Const.TIMEOUT_SOCKET);
        String fileName = null;
        byte[] byteArray = null;
        if (Type.equals("image")) {
            fileName = String.format("FOLDSCOPE%d.jpeg", new Date().getTime());
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(FilePath, options);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
            byteArray = byteArrayOutputStream.toByteArray();
        } else if (Type.equals("video")) {
            fileName = String.format("FOLDSCOPE%d.mp4", new Date().getTime());
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            FileInputStream fis = new FileInputStream(new File(FilePath));
            byte[] buf = new byte[1024];
            int n;
            while (-1 != (n = fis.read(buf)))
                baos.write(buf, 0, n);
            byteArray = baos.toByteArray();
        }
        httpClient = new DefaultHttpClient(ccm, params);
        httppost = new HttpPost(url);
        httppost.setHeader("Content-Type", "multipart/form-data");
        httppost.setHeader("Authorization", "Bearer " + Token);
        httppost.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        httppost.setHeader("media_type", "file");
        httppost.setEntity(new ByteArrayEntity(byteArray));
        HttpResponse httpResponse = httpClient.execute(httppost);
        apiResponse = new ApiResponse();
        apiResponse.code = httpResponse.getStatusLine().getStatusCode();
        httpentity = httpResponse.getEntity();
        apiResponse.response = new String(EntityUtils.toString(httpentity));
        // release
        httpentity = null;
        httpResponse = null;
        httppost = null;
        httpClient = null;
        return apiResponse;
    }
}