package amoba.end.hu;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AboutActivity extends Activity implements View.OnClickListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        ((Button)findViewById(R.id.btnClose)).setOnClickListener(this);

        try {
            ((TextView)findViewById(R.id.lblVersion)).setText(
                    getString(
                            R.string.aboutVersion,
                            getPackageManager().getPackageInfo(getPackageName(), 0).versionName
                    )
            );
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnClose:
                finish();
                break;
        }
    }

}
