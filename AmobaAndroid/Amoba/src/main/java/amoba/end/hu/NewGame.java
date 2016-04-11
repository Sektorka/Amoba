package amoba.end.hu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

public class NewGame extends Activity implements View.OnClickListener, android.widget.SeekBar.OnSeekBarChangeListener{
    private TextView lblWidth, lblHeight;
    private EditText tbServerName, tbPassword;
    private SeekBar sbWidth, sbHeight;

    private final static int SEEKBAR_MIN = 10;
    public final static int NEW_SERVER_REQUEST = 0x10000;

    public final static String SERVER_NAME = "serverName";
    public final static String SERVER_PASSWORD = "joinPassword";
    public final static String STAGE_WIDTH = "width";
    public final static String STAGE_HEIGHT = "height";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);

        sbWidth = (SeekBar)findViewById(R.id.sbWidth);
        sbHeight = (SeekBar)findViewById(R.id.sbHeight);

        tbServerName = (EditText) findViewById(R.id.tbServerName);
        tbPassword = (EditText) findViewById(R.id.tbPassword);

        lblWidth = (TextView) findViewById(R.id.lblWidth);
        lblHeight = (TextView) findViewById(R.id.lblHeight);

        lblWidth.setText(getString(R.string.lblWidth, sbWidth.getProgress() + SEEKBAR_MIN));
        lblHeight.setText(getString(R.string.lblHeight, sbHeight.getProgress() + SEEKBAR_MIN));

        sbWidth.setOnSeekBarChangeListener(this);
        sbHeight.setOnSeekBarChangeListener(this);

        findViewById(R.id.btnOk).setOnClickListener(this);
        findViewById(R.id.btnCancel).setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.new_game, menu);
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
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()){
            case R.id.sbWidth:
                lblWidth.setText(getString(R.string.lblWidth, progress + SEEKBAR_MIN));
                break;
            case R.id.sbHeight:
                lblHeight.setText(getString(R.string.lblHeight, progress + SEEKBAR_MIN));
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnOk:
                Intent returnIntent = new Intent();
                returnIntent.putExtra(SERVER_NAME, tbServerName.getText().toString());
                returnIntent.putExtra(SERVER_PASSWORD, tbPassword.getText().toString());
                returnIntent.putExtra(STAGE_WIDTH, sbWidth.getProgress() + SEEKBAR_MIN);
                returnIntent.putExtra(STAGE_HEIGHT, sbHeight.getProgress() + SEEKBAR_MIN);

                setResult(RESULT_OK, returnIntent);
                finish();
                break;
            case R.id.btnCancel:
                setResult(RESULT_CANCELED, null);
                finish();
                break;
        }
    }
}
