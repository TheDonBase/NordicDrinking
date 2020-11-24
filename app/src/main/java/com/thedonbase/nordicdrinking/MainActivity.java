package com.thedonbase.nordicdrinking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDb = new DatabaseHelper(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        FetchData fetchData = new FetchData(getApplicationContext(), new OnEventListener<String>() {
            @Override
            public void onSuccess(String result) throws JSONException {
                myDb.fetchQuestions(result);
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(getApplicationContext(), "ERROR: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        fetchData.execute();
        Intent intent = new Intent(this, LandingActivity.class);
        Handler handler = new Handler();
        handler.postDelayed(() -> startActivity(intent), 3000);
    }

}