package com.example.eda.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class MessageActivity extends AppCompatActivity {
    public RecyclerView myRecylerView ;
    public List<Message> MessageList ;
    public MessageAdapter chatBoxAdapter;
    public EditText messagetxt ;
    public Button send ;
    public Button send2;
    //declare socket object
    private Socket socket;

    public String wallname;
    public int wallid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);

        messagetxt = (EditText) findViewById(R.id.message) ;
        send = (Button)findViewById(R.id.send);
        wallname = (String)getIntent().getExtras().getString("wallname");
        wallid = (int)getIntent().getExtras().getInt("wallid");

        //connect you socket client to the server
        try {
            socket = IO.socket("http://192.168.43.47:3000");
            socket.connect();
           // socket.emit("join", Nickname);
        } catch (URISyntaxException e) {
            e.printStackTrace();

        }

        MessageList = new ArrayList<>();
        chatBoxAdapter = new MessageAdapter(MessageList);

        String url = "http://192.168.43.47:3000/walls/"+wallid;

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
                            String nickname = data.getString("nickname");
                            String message = data.getString("message");
                            double latitude = data.getDouble("latitude");
                            double longtitude = data.getDouble("longtitude");
                            int wallid = data.getInt("wallid");
                            // make instance of message

                            Message m = new Message(nickname,message,latitude,longtitude);


                            MessageList.add(i,m);
                            // add the new updated list to the dapter


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

        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);

        myRecylerView = (RecyclerView) findViewById(R.id.messagelist);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        myRecylerView.setLayoutManager(mLayoutManager);
        myRecylerView.setItemAnimator(new DefaultItemAnimator());

        // message send action
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //retrieve the nickname and the message content and fire the event messagedetection
                if(!messagetxt.getText().toString().isEmpty()){
                    socket.emit("messagedetection","Username : ",messagetxt.getText().toString(),10.0, 10.0);
                    messagetxt.setText(" ");
                }
            }
        });

        //implementing socket listeners
        socket.on("userjoinedthechat", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                String data = (String) args[0];
                Toast.makeText(MessageActivity.this,data,Toast.LENGTH_SHORT).show();

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
                Toast.makeText(MessageActivity.this,data,Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });

        socket.on(wallname, new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                JSONObject data = (JSONObject) args[0];
                try {
                    //extract data from fired event

                    String nickname = data.getString("nickname");
                    String message = data.getString("message");

                    // make instance of message

                    Message m = new Message(nickname,message,0,0);

                    MessageList.add(m);

                    chatBoxAdapter.notifyDataSetChanged();

                    //set the adapter for the recycler view

                    myRecylerView.setAdapter(chatBoxAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                }});
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        socket.disconnect();
    }
}