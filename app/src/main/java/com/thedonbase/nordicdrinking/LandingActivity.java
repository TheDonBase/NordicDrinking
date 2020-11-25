package com.thedonbase.nordicdrinking;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class LandingActivity extends AppCompatActivity {
    DatabaseHelper myDb;
    int players;
    int maxPlayers = 20;
    ArrayList<EditText> etTextList = new ArrayList<>();
    LoadingDialog loadingDialog = new LoadingDialog(LandingActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        myDb = new DatabaseHelper(this);

        final Spinner playerAmount = (Spinner) findViewById(R.id.playerAmtSpinner);
        playerAmount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    players = Integer.parseInt(playerAmount.getSelectedItem().toString());
                    if (players > maxPlayers) {
                        players = maxPlayers;
                        Toast.makeText(LandingActivity.this, "Max number of players is 20 players.", Toast.LENGTH_SHORT).show();
                    }
                } catch (NumberFormatException ex) {
                    Toast.makeText(LandingActivity.this, "There was an error processing the player amount.", Toast.LENGTH_SHORT).show();
                }
                LinearLayout ll = (LinearLayout) findViewById(R.id.playerNamesLayout);
                etTextList.clear();
                ll.removeAllViews();
                for (int i = 0; i < players; i++) {
                    EditText et = new EditText(getBaseContext());
                    LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    et.setGravity(Gravity.CENTER);
                    et.setCompoundDrawablesWithIntrinsicBounds(R.drawable.person, 0, 0, 0);
                    et.setPadding(40, 40, 40, 40);
                    et.setTextSize(24);
                    et.setTextColor(ContextCompat.getColor(LandingActivity.this, R.color.white));
                    et.setHint("Player " + (i + 1));
                    et.setId(View.generateViewId());
                    et.setContentDescription(getString(R.string.etDesc));
                    et.setSingleLine(true);
                    et.setLayoutParams(p);
                    et.setHintTextColor(ContextCompat.getColor(LandingActivity.this, R.color.light_blue_A200));
                    etTextList.add(et);
                    ll.addView(et);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(LandingActivity.this, "You need to specify how many players you are.", Toast.LENGTH_SHORT).show();
            }
        });


        final Button startGameBtn = (Button) findViewById(R.id.startGameBtn);
        startGameBtn.setOnClickListener(v -> {
            for (int i = 0; i < etTextList.size(); i++) {
                if (etTextList.get(i).getText().toString().isEmpty()) {
                    Toast.makeText(LandingActivity.this, "Please add the player names!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    boolean isInserted = myDb.insertData(etTextList.get(i).getText().toString());
                    if (isInserted) {
                        Intent intent = new Intent(LandingActivity.this, GameActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(LandingActivity.this, "Something went horribly wrong. Sorry!", Toast.LENGTH_SHORT).show();
                        loadingDialog.dismissDialog();
                    }
                }
            }
        });
    }
}