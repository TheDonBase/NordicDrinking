package com.thedonbase.nordicdrinking;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class GameActivity extends AppCompatActivity {
    DatabaseHelper myDb;
    public String category;
    public String[] categories;
    TextView displayChallenge;
    JSONArray players = new JSONArray();
    JSONArray challenges = new JSONArray();

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myDb = new DatabaseHelper(this);
        categories = this.getResources().getStringArray(R.array.Categories);
        setContentView(R.layout.activity_game);
        final FrameLayout nextQuestion = (FrameLayout) findViewById(R.id.Frame);
        displayChallenge = findViewById(R.id.gm_Challenge);
        nextQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayNewChallenge();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void displayNewChallenge() {
        Random randomizer = new Random();
        String curPlayer;
        String curChallenge;
        String curPartner;
        try {
            curPlayer = players.getString(randomizer.nextInt(players.length()));
            curChallenge = challenges.getString(randomizer.nextInt(challenges.length()));
            curPartner = players.getString(randomizer.nextInt(players.length()));
            if(curPartner.equals(curPlayer))
            {
                curPartner = players.getString(randomizer.nextInt(players.length()));
            }
            if(curChallenge.contains("@partner"))
            {
                curChallenge = curChallenge.replace("@partner", curPartner);
            }
            if(curChallenge.contains("@player"))
            {
                curChallenge = curChallenge.replace("@player", curPlayer);
            }
            displayChallenge.setText(curChallenge);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void fillData() {
        Cursor getPlayers = myDb.getAllPlayers();
        Cursor getQuestions = myDb.getAllQuestions(category);
        if(getPlayers.getCount() == 0)
        {
            Toast.makeText(this, "No players were found.", Toast.LENGTH_SHORT).show();
            return;
        }
        if(getQuestions.getCount() == 0)
        {
            Toast.makeText(this, "No Questions were found in this category.", Toast.LENGTH_SHORT).show();
            chooseCategory();
            return;
        }
        while(getPlayers.moveToNext()) {
            players.put(getPlayers.getString(1));
        }
        while(getQuestions.moveToNext()) {
            String JSONChallenge = getQuestions.getString(2);
            challenges.put(JSONChallenge);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        chooseCategory();
    }

    private void chooseCategory() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose a category!")
                .setSingleChoiceItems(R.array.Categories, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(categories[which].equals("Normal Category")) {
                            category = "Normal_Category";
                        } else if (categories[which].equals("Dirty Category")) {
                            category = "Dirty_Category";
                        } else if (categories[which].equals("Fun Category")) {
                            category = "Fun_Category";
                        } else if (categories[which].equals("Get To Know Category")) {
                            category = "GTK_Category";
                        } else if (categories[which].equals("Crazy Category")) {
                            category = "Crazy_Category";
                        } else if (categories[which].equals("Chaos Category")) {
                            category = "Chaos_Category";
                        }
                    }
                })
                .setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(category == null)
                        {
                            category = "Normal_Category";
                        }
                        Toast.makeText(GameActivity.this,"Choosen Category is: " + category, Toast.LENGTH_SHORT).show();
                        fillData();
                    }
                });
        builder.create();
        builder.show();
    }
}