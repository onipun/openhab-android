package org.openhab.habdroid;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class RequestItems {
    private static RequestItems ourInstance;
    private RequestQueue mRequestQueue;
    private static Context mCtx;


    public static synchronized RequestItems getInstance(Context context)
    {
        if (ourInstance == null)
            ourInstance = new RequestItems(context);
        return ourInstance;
    }

    private RequestItems(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();
    }

    public RequestQueue getRequestQueue(){
        if (mRequestQueue == null){
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return  mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req){
        getRequestQueue().add(req);
    }
}


