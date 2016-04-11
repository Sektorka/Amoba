package amoba.end.hu.interfaces;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class NotStartedMatch implements Serializable {
    public final String name;
    public final int port;
    public boolean password;

    public NotStartedMatch(String name, int port, boolean password){
        this.name = name;
        this.port = port;
        this.password = password;
    }

    @Override
    public String toString() {
        JSONObject json = new JSONObject();

        try {
            json.put("name",name);
            json.put("port",port);
            json.put("password",password);
        } catch (JSONException e) {}

        return json.toString();
    }
}
