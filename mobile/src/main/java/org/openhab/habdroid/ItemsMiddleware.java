package org.openhab.habdroid;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.openhab.habdroid.beta.Item;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

public class ItemsMiddleware {
    private static final String TAG = ItemsMiddleware.class.getSimpleName();
    private static final MediaType MEDIA_TYPE_PLAINTEXT = MediaType
            .parse("text/plain");
    private static ItemsMiddleware ourInstance;
    private Context mCtx;
    private ArrayList<Item> arrayList = new ArrayList<Item>();

    public static synchronized ItemsMiddleware getInstance(Context context) {
        if (ourInstance == null){
            ourInstance = new ItemsMiddleware(context);
        }
        return ourInstance;
    }

    private ItemsMiddleware(Context context) {
        mCtx = context;
    }

    public void setItemState(String state, String item){

        OkHttpClient okHttpClient = new OkHttpClient();
        HttpUrl.Builder builder = HttpUrl.parse("http://192.168.43.201:8080/rest/items/" + item).newBuilder();
        String url = builder.build().toString();

        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url).post(RequestBody.create(MEDIA_TYPE_PLAINTEXT,state.getBytes())).build();

        // Get a handler that can be used to post to the main thread
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Log.d(TAG, "onFailure: failed");
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }

                Log.d(TAG, "onResponse: "+response.toString());
            }
        });
    }

    private void requestItems(){
        String strUrl = "http://192.168.43.201:8080/rest/items?recursive=false";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, strUrl, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, "onRespond: " + response.toString());
                        JSONObject jObj;
                        try {
                            for (int i = 0; i< response.length(); i++){
                                jObj = response.getJSONObject(i);
                                Item item = new Item();
                                item.setName(jObj.getString("name"));
                                item.setState(jObj.getString("state"));
                                item.setType(jObj.getString("type"));
                                arrayList.add(item);
                            }
                        }catch (JSONException e){
                            Log.d(TAG, "onResponse: " + e.toString());
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Log.d(TAG, "onErrorResponse: error " +error.toString());

                    }
                });


        RequestItems.getInstance(mCtx).addToRequestQueue(jsonArrayRequest);
    }

    public void fetchItems(){
        String name , state, type;
        requestItems();

        for (int i = 0; i<arrayList.size(); i++){
            name = arrayList.get(i).getName();
            state = arrayList.get(i).getState();
            type = arrayList.get(i).getType();

            Log.d(TAG, "fetchItems: " + name + " "+ state+ " " + type);
        }
    }

    public List<String> getItemName(){

        List<String> label = new ArrayList<String>();
        String name;

        Log.d(TAG, "getItemName: runned");
        if (arrayList != null) {

            for (int i = 0; i < arrayList.size(); i++) {
                if (arrayList.get(i).getType() == "Switch" ) {
                    name = arrayList.get(i).getName();
                    Log.d(TAG, "getItemName: " + name);
                    label.add(name);
                }
            }


            return label;
        }

        return null;
    }


}
