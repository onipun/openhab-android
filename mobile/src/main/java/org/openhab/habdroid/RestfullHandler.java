package org.openhab.habdroid;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class RestfullHandler {
    private static RestfullHandler ourInstance;
    private RequestQueue mRequestQueue;
    private static Context mCtx;
    private static final String TAG = RestfullHandler.class.getSimpleName();
    private String state;
    private String strUrl;


    public static synchronized RestfullHandler getInstance(Context context) {
        if (ourInstance != null){
            ourInstance = new RestfullHandler(context);
        }
        return ourInstance;
    }

    private RestfullHandler(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();

    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }

        Log.d(TAG, "getRequestQueue: entered");
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public void stringRequest(){


        StringRequest stringRequest = new StringRequest(Request.Method.POST, strUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //do something here
                Log.d(TAG, "onResponse: success return response");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: error on stringRequest ");
            }
        }){


            public byte[] getBody() throws AuthFailureError {
                //take a string and convert into toString().getbytes() and return;
                //if it was a success, to send 2 value will be fail since need to convert byte into a stream
                if (state != null) {
                    return state.getBytes();
                }
                return null;
            }
        };

        addToRequestQueue(stringRequest);
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setStrUrl(String strUrl) { this.strUrl = strUrl; }
}
