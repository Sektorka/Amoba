package amoba.end.hu;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import org.json.JSONObject;

import java.util.List;

import amoba.end.hu.interfaces.PlayedGame;
import amoba.end.hu.interfaces.PlayedGames;

public class PlayedGamesActivity extends Activity implements PlayedGames, View.OnClickListener {
    private ProgressDialog loading;
    private ListView listPlayedGames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_played_games);

        findViewById(R.id.btnWatch).setOnClickListener(this);
        findViewById(R.id.btnCancel).setOnClickListener(this);

        listPlayedGames = (ListView) findViewById(R.id.listPlayedGames);

        loading = ProgressDialog.show(this, "",
                "Lejátszott játszmák", true);
        Manager.instance().getPlayedGames(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.played_games, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnWatch:
                finish();
                break;
            case R.id.btnCancel:
                finish();
                break;
        }
    }

    @Override
    public void onGotNotStartedMatchesList(List<PlayedGame> playedGames) {
        listPlayedGames.setAdapter(new PlayedGamesListAdapter(this, R.layout.played_games_list_item, playedGames));
        ((PlayedGamesListAdapter)listPlayedGames.getAdapter()).notifyDataSetChanged();
    }

    @Override
    public void onMessage(JSONObject json) {
        loading.dismiss();
    }
}
