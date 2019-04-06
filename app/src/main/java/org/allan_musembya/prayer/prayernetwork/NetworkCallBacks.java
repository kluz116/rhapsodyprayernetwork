package org.allan_musembya.prayer.prayernetwork;

import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;
import org.allan_musembya.prayer.endpoints.Endpoints;

import java.util.HashMap;
import java.util.Map;

public class NetworkCallBacks {
    public Context context;

    public NetworkCallBacks(Context context){
        this.context = context;

    }
    public void SubmitCommentDevotion(final String comment, final String user_id, final String user_name,final String img ,final EditText editText){
        String cancel_req_tag = "testimony";
        StringRequest strReq = new StringRequest(Request.Method.POST, Endpoints.URL_DEVOTION_COMMENT, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");


                    if (!error) {
                        String errorMsg = jObj.getString("response_msg");
                        Toast.makeText(context, ""+errorMsg, Toast.LENGTH_LONG).show();
                        editText.getText().clear();
                    } else {
                        String error_crap = jObj.getString("error_msg");
                        Toast.makeText(context, ""+error_crap, Toast.LENGTH_LONG).show();


                    }
                } catch (JSONException e) {

                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("comment",comment);
                params.put("user_id",user_id);
                params.put("user_name",user_name);
                params.put("img",img);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> param = new HashMap<String, String>();

                return param;
            }

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded";
            }
        };
        // Adding request to request queue
        AppSingleton.getInstance(context).addToRequestQueue(strReq, cancel_req_tag);
    }

    public void SubmitComment(final String comment, final String user_id, final String user_name, final String img, final EditText editText){
        String cancel_req_tag = "testimony";


        StringRequest strReq = new StringRequest(Request.Method.POST, Endpoints.URL_TESTIMONY_COMMENT, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");


                    if (!error) {
                        String errorMsg = jObj.getString("response_msg");
                        Toast.makeText(context, ""+errorMsg, Toast.LENGTH_LONG).show();
                        editText.getText().clear();
                    } else {
                        String error_crap = jObj.getString("error_msg");
                        Toast.makeText(context, ""+error_crap, Toast.LENGTH_LONG).show();


                    }
                } catch (JSONException e) {

                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("comment",comment);
                params.put("user_id",user_id);
                params.put("user_name",user_name);
                params.put("img",img);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> param = new HashMap<String, String>();

                return param;
            }

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded";
            }
        };
        // Adding request to request queue
        AppSingleton.getInstance(context).addToRequestQueue(strReq, cancel_req_tag);
    }

    //Call back fired to add individual user testimonies...
    public void SubmitCommentPrayer(final String userid,final String names, final String comment_text,final String img,final EditText editText){
        String cancel_req_tag = "testimony";


        StringRequest strReq = new StringRequest(Request.Method.POST, Endpoints.URL_FOR_COMMENT, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");


                    if (!error) {
                        String errorMsg = jObj.getString("response_msg");
                        Toast.makeText(context, ""+errorMsg, Toast.LENGTH_LONG).show();
                        editText.getText().clear();
                    } else {
                        String error_crap = jObj.getString("error_msg");
                        Toast.makeText(context, ""+error_crap, Toast.LENGTH_LONG).show();


                    }
                } catch (JSONException e) {

                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("userid",userid);
                params.put("names",names);
                params.put("comment_text",comment_text);
                params.put("img",img);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> param = new HashMap<String, String>();

                return param;
            }

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded";
            }
        };
        // Adding request to request queue
        AppSingleton.getInstance(context).addToRequestQueue(strReq, cancel_req_tag);
    }



}
