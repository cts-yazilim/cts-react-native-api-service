package com.ctsapiservice;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;

import android.text.TextUtils;
import android.util.Log;
import android.os.Environment;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;


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
    }

    @Override
    public String getName() {
        return "RNAndroidCtsApiService";
    }

    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@React METHOD
    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

    @ReactMethod
    public void Backup() {
        try {

            // String path = "/data/data/com.yascayalim/databases/";
            // Log.d("DBFILE", "Path: " + path);
            // File directory = new File(path);
            // File[] files = directory.listFiles();
            // Log.d("DBFILE", "Size: " + files.length);
            // for (int i = 0; i < files.length; i++) {
            // Log.d("DBFILE", "FileName:" + files[i].getName());
            // }

            final String inFileName = reactContext.getDatabasePath("dataDB").getPath();
            Log.d("DBFILE", inFileName);
            File dbFile = new File(inFileName);
            FileInputStream fis = new FileInputStream(dbFile);

            String outFileName = Environment.getExternalStorageDirectory() + "/database_copy.db";
            Log.d("DBFILE", outFileName);

            // Open the empty db as the output stream
            OutputStream output = new FileOutputStream(outFileName);

            // Transfer bytes from the inputfile to the outputfile
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }

            // Close the streams
            output.flush();
            output.close();
            fis.close();
            Log.d("DBFILE", "BAŞARILI");

        } catch (Exception ex) {
            Log.d("DBFILE", "DOSYA HATA" + ex.toString());
        }

    }

    public void Test() throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException, UnrecoverableKeyException, KeyManagementException {
        Response response;
        Request req;
        String Host = "https://yascay-api-test.jdecoffee.com/";
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
        InputStream caFileInputStream = reactContext.getResources().getAssets().open("cts.pfx");

        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        keyStore.load(caFileInputStream, "TW9SxEpG7dduTg".toCharArray());

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