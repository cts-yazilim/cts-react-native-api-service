package com.ctsapiservice;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;

import android.text.TextUtils;
import android.util.Log;
import android.os.Environment;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.UnexpectedNativeTypeException;
import com.facebook.react.bridge.WritableMap;


import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.AlgorithmParameterGenerator;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RNAndroidCtsApiServiceModule extends ReactContextBaseJavaModule {

    public static ReactApplicationContext reactContext;

    public OkHttpClient client;
    public String Cookie ="";
    public   MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    public RNAndroidCtsApiServiceModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
        Log.d("SELCUK","Inited");
        try {
            InitHttpClient();
            
        } catch (Exception ex) {
Log.d("ERR",ex.toString());            //TODO: handle exception
        }
        Log.d("SELCUK","Inited Fin");
    }

    @Override
    public String getName() {
        return "RNAndroidCtsApiService";
    }

    @ReactMethod
    public void GetToken(String Host, String Location,String UserName, String Password, String CihazNo, Callback callback) throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException, UnrecoverableKeyException, KeyManagementException {
        WritableMap resp = Arguments.createMap();
        Response response;
        Request req;
        Test(Host);
        // RequestBody formBody = new FormBody.Builder()
        //         .add("grant_type", "password")
        //         .add("username", "admin")
        //         .add("Password", "12345")
        //         .add("cihazno", "16a5b862c4c62620")
        //         .build();
         RequestBody formBody = new FormBody.Builder()
         .add("grant_type", "password")
         .add("username", UserName)
         .add("Password", Password)
         .add("cihazno", CihazNo)
         .build();

        req = GetNewRequest(Host,Location,Cookie,formBody,"");

        try {
            response = client.newCall(req).execute();

            String bodyString =response.body().string();
            resp.putInt("status", response.code());
            Log.d("Succ","code succ");
            Log.d("Succ",bodyString);
            resp.putString("bodyString",bodyString );
            Log.d("Succ","callback");
            
            callback.invoke(resp, null);
        }

        catch (Exception ex) {
            Log.d("errr", ex.toString());
            WritableMap er = Arguments.createMap();
            er.putString("message", "The request timed out.");
            er.putInt("code", -1001);
            callback.invoke(null, er);
        }
    }

    @ReactMethod
    public void GraphGet(String Host, String Location,String Json, String Token, Callback callback) throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException, UnrecoverableKeyException, KeyManagementException {
        WritableMap resp = Arguments.createMap();
        Response response;
        Request req;
        Test(Host);

        // String json = "{" +
        //         "query :'query{" +
        //         "vadenakitalimlar{id, fiyat_Baslik_Id, aktif}" +
        //         "}'" +
        //         "}";
        Log.d("JSON",Json);

        RequestBody formBody = RequestBody.create(JSON,Json);

        req = GetNewRequest(Host,Location,Cookie,formBody,Token);

        try {
            response = client.newCall(req).execute();
            String bodyString =response.body().string();
            resp.putInt("status", response.code());
            Log.d("Succ","code succ");
            Log.d("Succ",bodyString);
            resp.putString("bodyString",bodyString );
            Log.d("Succ","callback");
            
            callback.invoke(resp, null);
        }
        catch (Exception ex) {
            Log.d("errr", ex.toString());
            WritableMap er = Arguments.createMap();
            er.putString("message", "The request timed out.");
            er.putInt("code", -1001);
            callback.invoke(null, er);

        }
    }
    
    @ReactMethod
    public void PostValue(String Host, String Location,String Json, String Token, Callback callback) throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException, UnrecoverableKeyException, KeyManagementException {
        WritableMap resp = Arguments.createMap();
        Response response;
        Request req;
        Test(Host);
        RequestBody formBody = RequestBody.create(JSON,Json);

        req = GetNewRequest(Host,Location,Cookie,formBody,Token);

        try {
            response = client.newCall(req).execute();
            String bodyString =response.body().string();
            resp.putInt("status", response.code());
            Log.d("Succ","code succ");
            Log.d("Succ",bodyString);
            resp.putString("bodyString",bodyString );
            Log.d("Succ","callback");
            
            callback.invoke(resp, null);
        }
        catch (Exception ex) {
            Log.d("errr", ex.toString());
            WritableMap er = Arguments.createMap();
            er.putString("message", "The request timed out.");
            er.putInt("code", -1001);
            callback.invoke(null, er);
        }
    }

    
    public void Test(String Host) throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException, UnrecoverableKeyException, KeyManagementException {
        Response response;
        Request req;
        String Location = "";

        req = GetNewRequest(Host,"",Cookie,null,"");
        try {
            response = client.newCall(req).execute();
            while(response.code() ==302)
            {

                Location = response.header("Location");
                Cookie = TextUtils.join( ";",response.headers("Set-Cookie"));

                if (Location.equals("/vdesk/hangup.php3"))
                {
                    Log.d("Err","Ulasılamadı");
                    break;
                }

                req = GetNewRequest(Host,Location,Cookie,null,"");
                response = client.newCall(req).execute();
            }
            Log.d("Succ",response.body().string());
        }
        catch (Exception ex) {
            Log.d("errr", ex.toString());
        }
    }



    public Request GetNewRequest(String Host, String targetUrl,String myCookie,RequestBody body ,String Token)
    {
        Request.Builder request = new Request.Builder()
                .url(Host +targetUrl)
                .addHeader("Cookie",myCookie);
        if(body != null)
        {

            request.addHeader("content-type", "application/x-www-form-urlencoded;charset=utf-8")
                    .post(body);
        }
        if(!Token.equals(""))
            request.addHeader("Authorization",  "Bearer " + Token);

        return  request.build();
    }


    public void InitHttpClient() throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException, UnrecoverableKeyException, KeyManagementException
    {
        Log.d("SELCUK","PfxFile");
        InputStream caFileInputStream = reactContext.getResources().getAssets().open("cts.pfx");
        Log.d("SELCUK","PfxFile");
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        keyStore.load(caFileInputStream, "TW9SxEpG7dduTg".toCharArray());
        Log.d("SELCUK","Keystore");

        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("X509");
        keyManagerFactory.init(keyStore, "TW9SxEpG7dduTg".toCharArray());

        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
                TrustManagerFactory.getDefaultAlgorithm());

        trustManagerFactory.init((KeyStore)null);
        TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
        if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
            throw new IllegalStateException("Unexpected default trust managers:"
                    + Arrays.toString(trustManagers));
        }
        X509TrustManager trustManager = (X509TrustManager) trustManagers[0];


        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(keyManagerFactory.getKeyManagers(), new TrustManager[]{trustManager}, new SecureRandom());


        client = new OkHttpClient().newBuilder()
                .readTimeout(0, TimeUnit.SECONDS)
                .connectTimeout(0, TimeUnit.SECONDS)
                .sslSocketFactory(sslContext.getSocketFactory(), trustManager)
                .followRedirects(false)
                .followSslRedirects(false)
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                }).build();


    }


}