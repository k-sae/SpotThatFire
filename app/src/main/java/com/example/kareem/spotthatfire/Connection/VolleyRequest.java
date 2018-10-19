package com.example.kareem.spotthatfire.Connection;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Map;

/**
 * Created by kareem on 7/28/17.
 */

public abstract class VolleyRequest implements  Response.Listener<String>, Response.ErrorListener{
    private String url;
    private Map<String, String> params;
    private Context context;
    private StringRequest stringRequest;
    public VolleyRequest(String url, Map<String, String> params, Context context) {
        this.url = url;
        this.params = params;
        this.context = context;
    }

    public void start(){
        //Showing the progress dialog
         stringRequest = new StringRequest(Request.Method.GET, url,this,this){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String
                //Creating parameters

                //Adding parameter
                //returning parameters
                return params;
            }

        };

        RetryPolicy policy = new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        //Adding request to the queue
        requestQueue.add(stringRequest);
    }
    public StringRequest getStringRequest() {
        return stringRequest;
    }

}
