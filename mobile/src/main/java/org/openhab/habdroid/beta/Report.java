package org.openhab.habdroid.beta;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.openhab.habdroid.R;

import java.lang.reflect.Method;

public class Report extends AppCompatActivity {
    private WebView wInflux;
    private static final String TAG = Report.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        wInflux = findViewById(R.id.influxReport);
//        wInflux.setHorizontalScrollBarEnabled(false);
//        wInflux.setOnTouchListener(new View.OnTouchListener(){
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return (event.getAction() == MotionEvent.ACTION_MOVE);
//            }
//        });
        WebSettings ws = wInflux.getSettings();
        ws.setJavaScriptEnabled(true);
        ws.setAllowFileAccess(true);

        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.ECLAIR) {
            try {
                Log.d(TAG, "Enabling HTML5-Features");
                Method m1 = WebSettings.class.getMethod("setDomStorageEnabled", new Class[]{Boolean.TYPE});
                m1.invoke(ws, Boolean.TRUE);

                Method m2 = WebSettings.class.getMethod("setDatabaseEnabled", new Class[]{Boolean.TYPE});
                m2.invoke(ws, Boolean.TRUE);

                Method m3 = WebSettings.class.getMethod("setDatabasePath", new Class[]{String.class});
                m3.invoke(ws, "/data/data/" + getPackageName() + "/databases/");

                Method m4 = WebSettings.class.getMethod("setAppCacheMaxSize", new Class[]{Long.TYPE});
                m4.invoke(ws, 1024 * 1024 * 8);

                Method m5 = WebSettings.class.getMethod("setAppCachePath", new Class[]{String.class});
                m5.invoke(ws, "/data/data/" + getPackageName() + "/cache/");
            }catch(Exception e){

                e.printStackTrace();
            }
        }

                wInflux.loadUrl("http://192.168.43.201:3000/d/TUoPc2Wgk/led?orgId=1&refresh=5m&panelId=2&fullscreen&theme=light");
//        wInflux.setWebViewClient(new HelloWebViewClient());


    }

    private class HelloWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }


    }

}
