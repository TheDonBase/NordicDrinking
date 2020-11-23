package com.thedonbase.nordicdrinking;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

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
    ArrayList<EditText> etTextList = new ArrayList<EditText>();
    private Context context;
    LoadingDialog loadingDialog = new LoadingDialog(LandingActivity.this);
    boolean addedTextBoxes = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        myDb = new DatabaseHelper(this);

        final EditText playerAmount = (EditText) findViewById(R.id.playerAmtText);
        playerAmount.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(playerAmount.getText()))
                {
                    Toast.makeText(LandingActivity.this, "Please add the number of players!", Toast.LENGTH_SHORT).show();
                }
                if (addedTextBoxes == false) {
                    players = Integer.parseInt(playerAmount.getText().toString());
                    LinearLayout ll = (LinearLayout) findViewById(R.id.playerNamesLayout);
                    for (int i = 0; i < players; i++) {
                        EditText et = new EditText(getBaseContext());
                        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        et.setGravity(Gravity.CENTER);
                        et.setCompoundDrawablesWithIntrinsicBounds(R.drawable.person, 0, 0, 0);
                        et.setHint("Name" + i);
                        et.setId(View.generateViewId());
                        et.setSingleLine(true);
                        et.setLayoutParams(p);
                        et.setHintTextColor(getResources().getColor(R.color.light_blue_A200));
                        etTextList.add(et);
                        ll.addView(et);
                    }
                    addedTextBoxes = true;
                }
            }
        });

        final Button startGameBtn = (Button) findViewById(R.id.startGameBtn);
        startGameBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onClick(View v) {
                for(int i = 0; i < etTextList.size(); i++) {
                    boolean isInserted = myDb.insertData(etTextList.get(i).getText().toString());
                    if(isInserted)
                    {
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