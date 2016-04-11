package amoba.end.hu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import amoba.end.hu.interfaces.NotStartedMatch;

public class NotStartedMatchesListAdapter extends ArrayAdapter<NotStartedMatch> {
    protected LayoutInflater inflater;
    protected int resource, textViewResourceId;
    protected NotStartedMatchesActivity activity;
    protected static RadioGroup group;
    private static ArrayList<RadioButton> radioButtons;

    public NotStartedMatchesListAdapter(Context context, int resource, List<NotStartedMatch> items) {
        super(context, resource, items);
        this.resource = resource;
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.activity = (NotStartedMatchesActivity) context;

        radioButtons = new ArrayList<RadioButton>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return createViewFromResource(position, convertView, parent);
    }

    protected View createViewFromResource(int position, View convertView, ViewGroup parent){
        View view = (convertView == null ? inflater.inflate(resource, parent, false) : convertView);


        TextView textViewResource = (TextView)view.findViewById(textViewResourceId);

        NotStartedMatch item = getItem(position);

        ((TextView)view.findViewById(R.id.lblName)).setText(item.name);
        //((TextView)view.findViewById(R.id.lblHost)).setText(Settings.instance().getMasterServerHost().getHostAddress());
        //((TextView)view.findViewById(R.id.lblPort)).setText(item.port + "");

        if(convertView == null){
            RadioButton rb = (RadioButton)view.findViewById(R.id.rbSelected);
            rb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    for(RadioButton rb: radioButtons){
                        rb.setChecked(false);
                    }

                    buttonView.setChecked(isChecked);
                }
            });


            if(radioButtons.size() == 0){
                rb.setChecked(true);
            }

            radioButtons.add(rb);
        }

        //((RadioButton)view.findViewById(R.id.rbSelected)).setOnCheckedChangeListener();
        ((TextView)view.findViewById(R.id.lblPassword)).setText(item.password ? "Igen" : "Nem");

        return view;
    }

    protected static int getCheckedRadioButtonIndex(){
        for(int i = 0; i < radioButtons.size(); i++){
            if(radioButtons.get(i).isChecked()){
                return i;
            }
        }

        return 0;
    }
}
