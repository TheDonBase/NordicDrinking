package com.thedonbase.nordicdrinking;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class LandingActivity extends AppCompatActivity {
    DatabaseHelper myDb;
    int players;
    ArrayList<EditText> etTextList = new ArrayList<>();
    private Context context;
    LoadingDialog loadingDialog = new LoadingDialog(LandingActivity.this);
    boolean addedTextBoxes = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        myDb = new DatabaseHelper(this);

        final EditText playerAmount = (EditText) findViewById(R.id.playerAmtText);
        playerAmount.setOnClickListener((View.OnClickListener) v -> {
            if(TextUtils.isEmpty(playerAmount.getText()))
            {
                Toast.makeText(LandingActivity.this, "Please add the number of players!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!addedTextBoxes) {
                try {
                    players = Integer.parseInt(playerAmount.getText().toString());
                } catch (NumberFormatException ex) {
                    Toast.makeText(LandingActivity.this, "There was an error processing the player amount.", Toast.LENGTH_SHORT).show();
                }
                    LinearLayout ll = (LinearLayout) findViewById(R.id.playerNamesLayout);
                    for (int i = 0; i < players; i++) {
                        EditText et = new EditText(getBaseContext());
                        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        et.setGravity(Gravity.CENTER);
                        et.setCompoundDrawablesWithIntrinsicBounds(R.drawable.person, 0, 0, 0);
                        et.setPadding(40, 40, 40, 40);
                        et.setHint("Player " + i);
                        et.setId(View.generateViewId());
                        et.setSingleLine(true);
                        et.setLayoutParams(p);
                        et.setHintTextColor(ContextCompat.getColor(this, R.color.light_blue_A200));
                        etTextList.add(et);
                        ll.addView(et);
                    }
                    addedTextBoxes = true;
            }
        });

        final Button startGameBtn = (Button) findViewById(R.id.startGameBtn);
        startGameBtn.setOnClickListener(v -> {
            for(int i = 0; i < etTextList.size(); i++) {
                if(etTextList.get(i).getText().toString().isEmpty())
                {
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