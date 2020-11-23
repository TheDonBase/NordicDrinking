package com.thedonbase.nordicdrinking;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

public class MainActivity extends AppCompatActivity {

    ProgressBar progressBar;
    TextView textView;
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

        progressBar = findViewById(R.id.loading_bar);
        textView = findViewById(R.id.loading_text);

        progressBar.setMax(100);
        progressBar.setScaleY(3f);
        progressAnimation();
    }

    public void progressAnimation() {
        LoadingAnimation anim = new LoadingAnimation(this, progressBar, textView, 0f, 100f);
        anim.setDuration(8000);
        progressBar.setAnimation(anim);
    }
}