package com.example.eda.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public  class MainActivity extends AppCompatActivity {

    private Button btn;
    private EditText nickname;
    public static final String NICKNAME = "usernickname";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //call UI components  by id
        btn = (Button)findViewById(R.id.enterchat) ;
        nickname = (EditText) findViewById(R.id.nickname);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if the nickname is not empty go to chatbox activity and add the nickname to the intent extra


                if(!nickname.getText().toString().isEmpty()){

                    Intent i  = new Intent(MainActivity.this,WallActivity.class);
                    //retreive nickname from EditText and add it to intent extra
                    i.putExtra(NICKNAME,nickname.getText().toString());

                    startActivity(i);
                }
            }
        });


    }
}