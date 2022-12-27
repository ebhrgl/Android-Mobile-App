package com.example.eda.myapplication;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class WallActivity extends AppCompatActivity {
    public RecyclerView myRecylerView ;
    public List<Wall> MessageList ;
    public WallAdapter chatBoxAdapter;
    //public  EditText messagetxt ;
    public  Button send ;
    public Button send2;
    //declare socket object
    private Socket socket;
    private static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;
    String lattitudenew,longitudenew;

    public String Nickname ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_box);

        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            getLocation();
        }
        try {
            socket = IO.socket("http://192.168.43.47:3000");
            socket.connect();
            socket.emit("join", Nickname);
        } catch (URISyntaxException e) {
            e.printStackTrace();

        }
        //setting up recyler
        MessageList = new ArrayList<>();
        chatBoxAdapter = new WallAdapter(MessageList);

        myRecylerView = (RecyclerView) findViewById(R.id.messagelist);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        myRecylerView.setLayoutManager(mLayoutManager);
        myRecylerView.setItemAnimator(new DefaultItemAnimator());


        //implementing socket listeners
        socket.on("userjoinedthechat", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String data = (String) args[0];

                        Toast.makeText(WallActivity.this,data,Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
        socket.on("userdisconnect", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String data = (String) args[0];

                        Toast.makeText(WallActivity.this,data,Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });

        String url = "http://192.168.43.47:3000/walls";

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new com.android.volley.Response.Listener<JSONArray>(){

                    @Override
                    public void onResponse(final JSONArray response) {
                runOnUiThread(new Runnable()
                {

                    @Override
                    public void run() {
                        for (int i=0; i<response.length(); i++) {
                            try {
                                JSONObject data = response.getJSONObject(i);
                                String wallname = data.getString("title");
                                double latitude = data.getDouble("latitude");
                                double longtitude = data.getDouble("longtitude");
                                int id = data.getInt("id");
                                // make instance of message

                                Wall m = new Wall(id,wallname,latitude,longtitude);
                                Log.d("LATITUDE", String.valueOf(lattitudenew));
                                Log.d("LATITUDE", String.valueOf(longitudenew));
                                float x = getDistance(Double.parseDouble(lattitudenew), Double.parseDouble(longitudenew),latitude, longtitude);
                                Log.d("DISTANCE", String.valueOf(x));

                                MessageList.add(m);

                                chatBoxAdapter.notifyDataSetChanged();

                                //set the adapter for the recycler view

                                myRecylerView.setAdapter(chatBoxAdapter);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        }
                    });
                    }
                }, new com.android.volley.Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error

                    }
                });

// Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);

    }
    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(WallActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (WallActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(WallActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location location2 = locationManager.getLastKnownLocation(LocationManager. PASSIVE_PROVIDER);

            if (location != null) {
                double latti = location.getLatitude();
                double longi = location.getLongitude();
                lattitudenew = String.valueOf(latti);
                longitudenew = String.valueOf(longi);

            } else  if (location1 != null) {
                double latti = location1.getLatitude();
                double longi = location1.getLongitude();
                lattitudenew = String.valueOf(latti);
                longitudenew = String.valueOf(longi);

            } else  if (location2 != null) {
                double latti = location2.getLatitude();
                double longi = location2.getLongitude();
                lattitudenew = String.valueOf(latti);
                longitudenew = String.valueOf(longi);

            }else{

                Toast.makeText(this,"Unble to Trace your location",Toast.LENGTH_SHORT).show();

            }
        }
    }

    protected void buildAlertMessageNoGps() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please Turn ON your GPS Connection")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private float getDistance(double lat1, double lon1, double lat2, double lon2) {
        float[] distance = new float[2];
        Location.distanceBetween(lat1, lon1, lat2, lon2, distance);
        return distance[0];
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        socket.disconnect();
    }
}

