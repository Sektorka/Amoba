package amoba.end.hu;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.net.InetAddress;

public class SettingsActivity extends Activity implements View.OnClickListener{
    private EditText tbMasterServerHost, tbMasterServerPort,
                    tbClientID, tbNickName;
    private Button btnSave, btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        tbMasterServerHost = (EditText) findViewById(R.id.tbMasterServerHost);
        tbMasterServerPort = (EditText) findViewById(R.id.tbMasterServerPort);
        tbClientID = (EditText) findViewById(R.id.tbClientID);
        tbNickName = (EditText) findViewById(R.id.tbNickName);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnCancel = (Button) findViewById(R.id.btnCancel);

        tbMasterServerHost.setText(Settings.instance().getMasterServerHost().getHostAddress());
        tbMasterServerPort.setText(Settings.instance().getMasterServerPort() + "");
        tbClientID.setText(Settings.instance().getClientId());
        tbNickName.setText(Settings.instance().getNickName());
        btnSave.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    private void saveSettings(){
        Settings s = Settings.instance();

        try{
            s.setMasterServerHost(InetAddress.getByName(tbMasterServerHost.getText().toString()));
            s.setMasterServerPort(Integer.parseInt(tbMasterServerPort.getText().toString()));
            s.setClientId(tbClientID.getText().toString());
            s.setNickName(tbNickName.getText().toString());

            s.save();
            finish();
        }
        catch(Exception e){
            Toast.makeText(this, R.string.toastSettingsSaveFailed, Toast.LENGTH_LONG).show();
        }
    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnSave:
                saveSettings();
                break;
            case R.id.btnCancel:
                finish();
                break;
        }
    }

}
