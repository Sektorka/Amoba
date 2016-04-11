package amoba.end.hu;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.List;

import amoba.end.hu.exceptions.NotConnectedToMasterServerException;
import amoba.end.hu.interfaces.NotStartedMatch;
import amoba.end.hu.interfaces.NotStartedMatches;

public class NotStartedMatchesActivity extends Activity implements NotStartedMatches, View.OnClickListener {
    private ProgressDialog loading;
    private ListView listNotStartedMatches;
    public final static int LIST_NOT_STARTED_GAMES = 0x10001;
    public final static String NOT_STARTED_MATCH_OBJECT = "nsm";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_not_started_matches);

        findViewById(R.id.btnConnect).setOnClickListener(this);
        findViewById(R.id.btnCancel).setOnClickListener(this);

        listNotStartedMatches = (ListView) findViewById(R.id.listViewNotStartedMatches);

        loading = ProgressDialog.show(this, "",
                "Jelenlegi játszmák betöltése", true);
        try{
            Manager.instance().getNotStartedMatches(this);
        }
        catch(NotConnectedToMasterServerException e){
            Toast.makeText(this, R.string.unableToConnectMasterServer, Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        getMenuInflater().inflate(R.menu.not_started_matches, menu);
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
    public void onGotNotStartedMatchesList(List<NotStartedMatch> notStartedMatchList) {
        listNotStartedMatches.setAdapter(new NotStartedMatchesListAdapter(this,R.layout.not_started_matche_list_item,notStartedMatchList));
        ((NotStartedMatchesListAdapter)listNotStartedMatches.getAdapter()).notifyDataSetChanged();
    }

    @Override
    public void onMessage(JSONObject json) {
        loading.dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnConnect:

                NotStartedMatch nsm = (NotStartedMatch)listNotStartedMatches.getAdapter().getItem( NotStartedMatchesListAdapter.getCheckedRadioButtonIndex() );

                Intent returnIntent = new Intent();
                returnIntent.putExtra(NOT_STARTED_MATCH_OBJECT, nsm);

                setResult(RESULT_OK, returnIntent);

                System.out.println("NSM=" + nsm.name);

                finish();
                break;
            case R.id.btnCancel:
                setResult(RESULT_CANCELED, null);
                finish();
                break;
        }
    }
}
