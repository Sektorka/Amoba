package amoba.end.hu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import amoba.end.hu.interfaces.PlayedGame;

public class ChatAdapter<T extends ChatMessage> extends ArrayAdapter<T>{
    protected LayoutInflater inflater;
    protected int resource;

    public ChatAdapter(Context context, int resource, List<T> items) {
        super(context, resource, items);

        this.resource = resource;
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = (convertView == null ? inflater.inflate(resource, parent, false) : convertView);

        ((TextView) view.findViewById(R.id.tvMsg)).setText(getItem(position).toString());

        return view;
    }
}
