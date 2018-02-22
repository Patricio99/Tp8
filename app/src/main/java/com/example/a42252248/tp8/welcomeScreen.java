package com.example.a42252248.tp8;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class welcomeScreen  extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_screen);
    }
    public void startGame(View v) {
        Intent intent = new Intent(this, MainActivity.class);

        startActivity(intent);
    }
}
