package edu.calvin.cs262.lab06;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Reads from and shows name and email of monopoly players.
 *
 * for CS 262, Homework 02
 *
 * @author Shurjo Maitra
 * @version fall, 2016
 *
 */
public class MainActivity extends AppCompatActivity {

    private EditText playerText;
    private Button fetchButton;

    private List<Player> playerList = new ArrayList<>();
    private ListView itemsListView;

    /* This formater can be used as follows to format temperatures for display.
     *     numberFormat.format(SOME_DOUBLE_VALUE)
     */
    //private NumberFormat numberFormat = NumberFormat.getInstance();

    private static String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playerText = (EditText) findViewById(R.id.playerText);
        fetchButton = (Button) findViewById(R.id.fetchButton);
        itemsListView = (ListView) findViewById(R.id.playerListView);

        // See comments on this formatter above.
        //numberFormat.setMaximumFractionDigits(0);

        dismissKeyboard(playerText);
        new GetPlayerTask().execute(createURL(playerText.getText().toString()));

        fetchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissKeyboard(playerText);
                new GetPlayerTask().execute(createURL(playerText.getText().toString()));
            }
        });
    }
    /**
     * Formats a URL for the webservice specified in the string resources.
     *
     * @param playerText the id or lack of it
     * @return URL formatted for openweathermap.com
     */
    private URL createURL(String playerText) {
        try {
            String urlString;
            if (playerText.matches("")) {
                urlString = getString(R.string.calvin_player_server) + "players";
            } else {
                urlString = getString(R.string.calvin_player_server) + "player/" + playerText;
            }
            return new URL(urlString);
        } catch (Exception e) {
            if (playerText.matches("")) {
                Toast.makeText(this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
            }
        }

        return null;
    }

    /**
     * Deitel's method for programmatically dismissing the keyboard.
     *
     * @param view the TextView currently being edited
     */
    private void dismissKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * Inner class for receiving the current player data
     */
    private class GetPlayerTask extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... params) {
            HttpURLConnection connection = null;
            StringBuilder result = new StringBuilder();
            try {
                connection = (HttpURLConnection) params[0].openConnection();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(connection.getInputStream()));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    return result.toString();
                } else {
                    throw new Exception();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                connection.disconnect();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String players) {
            if (players != null) {
                //Log.d(TAG, weather.toString());
                makePlayers(players);
                MainActivity.this.updateDisplay();
            } else {
                Toast.makeText(MainActivity.this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Get JSON from server
     *
     * @param players
     */
    private void makePlayers(String players) {
        playerList.clear(); // clear old player data
        try {
            if (playerText.getText().toString().matches("")) {
                JSONArray list = new JSONArray(players);
                Log.v("x", players.toString());
                for (int i = 0; i < list.length(); i++) {
                    addToPlayerList(list.getJSONObject(i));
                }
            } else {
                try {
                    addToPlayerList(new JSONObject(players));
                } catch (JSONException e){
                    Toast.makeText(MainActivity.this, "No player found for ID", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /*
     * Adds to player list
     */
    private void addToPlayerList(JSONObject player) throws JSONException {
        String id, name, email;
        id = getPlayerString(player, "id");
        name = getPlayerString(player, "name");
        email = getPlayerString(player, "emailaddress");
        playerList.add(new Player(
                id,
                name,
                email));
    }

    /*
     * Gets player string. none if it doesn't exist
     */
    private String getPlayerString(JSONObject player, String str) {
        try {
            return player.getString(str);
        } catch (JSONException e) {
            return "none";
        }
    }

    /**
     * Refresh the player data by a simple adapter
     */
    private void updateDisplay() {
        if (playerList == null) {
            Toast.makeText(MainActivity.this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
        }
        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
        for (Player item : playerList) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("id", item.getId());
            map.put("name", item.getName());
            map.put("email", item.getEmail());
            data.add(map);
        }

        int resource = R.layout.player_item;
        String[] from = {"id", "name", "email"};
        int[] to = {R.id.playerId, R.id.playerName, R.id.playerEmail};

        SimpleAdapter adapter = new SimpleAdapter(this, data, resource, from, to);
        itemsListView.setAdapter(adapter);
    }

}
