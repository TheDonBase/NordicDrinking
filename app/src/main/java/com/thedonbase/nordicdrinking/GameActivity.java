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
import java.util.stream.IntStream;

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
    private int roundCnt = 0;
    private int minRoundCnt = 65;
    private int maxRoundCnt = 125;
    private int curMaxRoundCnt;
    private String lastChallenge = "";

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
        curMaxRoundCnt = new Random().nextInt((maxRoundCnt - minRoundCnt) + 1) + minRoundCnt;
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
        String newChallenge;
        String curPartner;
        try {
            curPlayer = players.getString(randomizer.nextInt(players.length()));
            curPartner = players.getString(randomizer.nextInt(players.length()));
            newChallenge = challenges.getString(randomizer.nextInt(challenges.length()));
            if(newChallenge.equals(lastChallenge))
            {
                newChallenge = challenges.getString(randomizer.nextInt(challenges.length()));
            }
            if(curPartner.equals(curPlayer))
            {
                curPartner = players.getString(randomizer.nextInt(players.length()));
            }
            if(newChallenge.contains("@partner"))
            {
                newChallenge = newChallenge.replace("@partner", curPartner);
            }
            if(newChallenge.contains("@player"))
            {
                newChallenge = newChallenge.replace("@player", curPlayer);
            }
            if(roundCnt >= curMaxRoundCnt)
            {
                displayChallenge.setText("Game over! Start new Round");
            } else {
                displayChallenge.setText(newChallenge);
                lastChallenge = newChallenge;
            }
            roundCnt++;
            Log.d("Debug", String.valueOf(curMaxRoundCnt));
            Log.d("Debug", String.valueOf(roundCnt));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void fillData() throws JSONException {
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
                        try {
                            fillData();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        builder.create();
        builder.show();
    }
}