package org.allan_musembya.prayer.prayernetwork;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.signature.ObjectKey;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.allan_musembya.prayer.endpoints.Endpoints;
import org.allan_musembya.prayer.models.UserImage;
import org.allan_musembya.prayer.utils.GlideApp;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class RemoteImages {

    public Context context;
    SharedPreferences sp;
    public String image_url;
    public static String kluz_url="";

    public RemoteImages(Context context){
        this.context = context.getApplicationContext();
    }
    public void getPrayerPointImages(final ImageView imgProfile){

        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest arrayreq = new StringRequest(Request.Method.POST, Endpoints.PRAYER_POINT,

                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray jsonarray = new JSONArray(response);

                            for (int i = 0; i<jsonarray.length(); i++){
                                JSONObject obj = jsonarray.getJSONObject(i);
                                Glide.with(context).load(obj.getString("actualpath")).into(imgProfile);


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                    }
                },

                new Response.ErrorListener() {
                    @Override
                    // Handles errors that occur due to Volley
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", "Error");

                    }
                })

        {


        };

        queue.add(arrayreq);


    }
    public void getRppnImages(final ImageView imgProfile){

        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest arrayreq = new StringRequest(Request.Method.POST, Endpoints.RPPN,

                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray jsonarray = new JSONArray(response);

                            for (int i = 0; i<jsonarray.length(); i++){
                                JSONObject obj = jsonarray.getJSONObject(i);
                                //GlideApp.
                                GlideApp.with(context).load(obj.getString("actualpath")).placeholder(R.drawable.logo).into(imgProfile);


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                    }
                },

                new Response.ErrorListener() {
                    @Override
                    // Handles errors that occur due to Volley
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", "Error");

                    }
                })

        {


        };

        queue.add(arrayreq);


    }

    public void getGiveImages(final ImageView imgProfile){

        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest arrayreq = new StringRequest(Request.Method.POST, Endpoints.RPPN,

                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray jsonarray = new JSONArray(response);

                            for (int i = 0; i<jsonarray.length(); i++){
                                JSONObject obj = jsonarray.getJSONObject(i);
                                GlideApp.with(context).load(obj.getString("actualpath")).placeholder(R.drawable.logo).into(imgProfile);


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                    }
                },

                new Response.ErrorListener() {
                    @Override
                    // Handles errors that occur due to Volley
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", "Error");

                    }
                })

        {


        };

        queue.add(arrayreq);


    }

    public void getDevotion(final ImageView imgProfile){

        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest arrayreq = new StringRequest(Request.Method.POST, Endpoints.DEVOTION,

                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray jsonarray = new JSONArray(response);

                            for (int i = 0; i<jsonarray.length(); i++){
                                JSONObject obj = jsonarray.getJSONObject(i);
                                GlideApp.with(context).load(obj.getString("actualpath")).placeholder(R.drawable.logo).into(imgProfile);


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                    }
                },

                new Response.ErrorListener() {
                    @Override
                    // Handles errors that occur due to Volley
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", "Error");

                    }
                })

        {


        };

        queue.add(arrayreq);


    }

    public void getPrayerRequest(final ImageView imgProfile){

        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest arrayreq = new StringRequest(Request.Method.POST, Endpoints.PRAYERREQUEST,

                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray jsonarray = new JSONArray(response);

                            for (int i = 0; i<jsonarray.length(); i++){
                                JSONObject obj = jsonarray.getJSONObject(i);
                                GlideApp.with(context).load(obj.getString("actualpath")).placeholder(R.drawable.logo).into(imgProfile);


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                    }
                },

                new Response.ErrorListener() {
                    @Override
                    // Handles errors that occur due to Volley
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", "Error");

                    }
                })

        {


        };

        queue.add(arrayreq);


    }

    public void getITesfy(final ImageView imgProfile){

        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest arrayreq = new StringRequest(Request.Method.POST, Endpoints.ITESTIFY,

                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray jsonarray = new JSONArray(response);

                            for (int i = 0; i<jsonarray.length(); i++){
                                JSONObject obj = jsonarray.getJSONObject(i);
                                GlideApp.with(context)
                                        .load(obj.getString("actualpath"))
                                        .placeholder(R.drawable.logo).into(imgProfile);


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                    }
                },

                new Response.ErrorListener() {
                    @Override
                    // Handles errors that occur due to Volley
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", "Error");

                    }
                })

        {


        };

        queue.add(arrayreq);


    }

    public void getDailyDevotionEng(final ImageView imgProfile){

        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest arrayreq = new StringRequest(Request.Method.POST, Endpoints.DEVOTION_IMG,

                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray jsonarray = new JSONArray(response);

                            for (int i = 0; i<jsonarray.length(); i++){
                                JSONObject obj = jsonarray.getJSONObject(i);
                                Glide.with(context).load(obj.getString("actualpath")).into(imgProfile);


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                    }
                },

                new Response.ErrorListener() {
                    @Override
                    // Handles errors that occur due to Volley
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", "Error");

                    }
                })

        {


        };

        queue.add(arrayreq);

    }
    public void getDailyDevotionFR(final ImageView imgProfile){

        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest arrayreq = new StringRequest(Request.Method.POST, Endpoints.DEVOTION_IMG,

                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray jsonarray = new JSONArray(response);

                            for (int i = 0; i<jsonarray.length(); i++){
                                JSONObject obj = jsonarray.getJSONObject(i);
                                Glide.with(context)
                                        .load(obj.getString("actualpath"))
                                        .into(imgProfile);


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                    }
                },

                new Response.ErrorListener() {
                    @Override
                    // Handles errors that occur due to Volley
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", "Error");

                    }
                })

        {


        };

        queue.add(arrayreq);


    }

    public void getMonthly(final ImageView imgProfile){

        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest arrayreq = new StringRequest(Request.Method.POST, Endpoints.MONTHLY,

                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray jsonarray = new JSONArray(response);

                            for (int i = 0; i<jsonarray.length(); i++){
                                JSONObject obj = jsonarray.getJSONObject(i);
                                GlideApp.with(context)
                                        .load(obj.getString("actualpath"))
                                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                                        .signature(new ObjectKey(String.valueOf(System.currentTimeMillis())))
                                        .skipMemoryCache(true)
                                        .placeholder(R.drawable.logo)
                                        .into(imgProfile);


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                    }
                },

                new Response.ErrorListener() {
                    @Override
                    // Handles errors that occur due to Volley
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", "Error");

                    }
                })

        {


        };

        queue.add(arrayreq);


    }
    public void getLoggedInUserPic (final String user_id,final ImageView imgProfile){

        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest arrayreq = new StringRequest(Request.Method.POST, Endpoints.PROFILE_IMG,

                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray jsonarray = new JSONArray(response);

                            for (int i = 0; i<jsonarray.length(); i++){
                                JSONObject obj = jsonarray.getJSONObject(i);
                                GlideApp.with(context)
                                        .load(obj.getString("image"))
                                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                                        .signature(new ObjectKey(String.valueOf(System.currentTimeMillis())))
                                        .skipMemoryCache(true)
                                        .placeholder(R.drawable.profile)
                                        .into(imgProfile);
                                sharedPreferences(obj.getString("image"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        }


                },

                new Response.ErrorListener() {
                    @Override
                    // Handles errors that occur due to Volley
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", "Error");

                    }
                })

        {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", user_id);
                return params;
            }

        };

        queue.add(arrayreq);


    }
    public void sharedPreferences(String image)
    {
        SharedPreferences shared = context.getSharedPreferences("login", MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();
        editor.putString("USER_PHOTO", image);
        editor.commit();
    }
    public String  retrivesharedPreferences()
    {
        SharedPreferences shared = context.getSharedPreferences("login", MODE_PRIVATE);
        if(shared.contains("USER_PHOTO"));
            String photo = shared.getString("USER_PHOTO", null);
            return photo;



    }

    public String getLoggedInUserImage (final String user_id){

        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest arrayreq = new StringRequest(Request.Method.POST, Endpoints.PROFILE_IMG,

                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray jsonarray = new JSONArray(response);

                            for (int i = 0; i<jsonarray.length(); i++){
                                JSONObject obj = jsonarray.getJSONObject(i);
                                //Glide.with(context).load(obj.getString("image")).placeholder(R.drawable.profile).dontAnimate().diskCacheStrategy(DiskCacheStrategy.ALL).into(imgProfile);

                                Toast.makeText(context, "Profile Image : "+obj.getString("image"), Toast.LENGTH_SHORT).show();
                                UserImage ui = new UserImage(obj.getString("image"));
                                ui.setImage(obj.getString("image"));


                                Toast.makeText(context, "Hmmmmm"+ui.getImage(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        }


                },

                new Response.ErrorListener() {
                    @Override
                    // Handles errors that occur due to Volley
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", "Error");

                    }
                })

        {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", user_id);
                return params;
            }

        };

        queue.add(arrayreq);

    return image_url;
    }



}
