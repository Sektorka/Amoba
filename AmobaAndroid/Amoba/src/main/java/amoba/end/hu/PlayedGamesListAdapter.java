package amoba.end.hu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import amoba.end.hu.interfaces.NotStartedMatch;
import amoba.end.hu.interfaces.PlayedGame;

public class PlayedGamesListAdapter extends ArrayAdapter<PlayedGame>{
    protected LayoutInflater inflater;
    protected int resource, textViewResourceId;

    public PlayedGamesListAdapter(Context context, int resource, List<PlayedGame> items) {
        super(context, resource, items);
        this.resource = resource;
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return createViewFromResource(position, convertView, parent);
    }

    protected View createViewFromResource(int position, View convertView, ViewGroup parent){
        View view = (convertView == null ? inflater.inflate(resource, parent, false) : convertView);

        PlayedGame item = getItem(position);

        ((TextView)view.findViewById(R.id.lblGame)).setText(
                view.getResources().getString(
                        R.string.playedGameLine,
                        item.header.formattedTimestamp("yyyy.MM.dd. HH:mm:ss"),
                        item.header.width,
                        item.header.height
                )
        );

        return view;
    }
}
