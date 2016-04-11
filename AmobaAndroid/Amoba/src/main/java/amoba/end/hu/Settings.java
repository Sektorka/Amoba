package amoba.end.hu;

import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.TreeMap;

public class Settings {
    private static Settings inst = null;

    //private String appPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/VideoDownloader";
    //private String downloadPath = appPath + "/downloads";
    //private String pluginsDir = appPath + "/plugins";
    //private int attempToConnect = 5, downloadThreadsCount = 1;
    //private boolean askForQualitiesOnRedownload = true, autoDownload = true;

    private String nickName, clientId;
    private InetAddress masterServerHost;
    private int masterServerPort;

    public void load() throws IllegalAccessException, UnknownHostException {
        TreeMap<String, String> dbSettings = DbManager.getInstance().loadSettings();

        for(Map.Entry<String, String> pair : dbSettings.entrySet()){
            Field field = getFieldByName(pair.getKey());

            if(field != null &&
                    !Modifier.isStatic(field.getModifiers()) &&
                    !Modifier.isFinal(field.getModifiers())){

                if(field.getType().equals(String.class)){
                    field.set(this, pair.getValue());
                }
                else if(field.getType().equals(InetAddress.class)){
                    field.set(this, InetAddress.getByName(pair.getValue()));
                }
                else if(field.getType().equals(Integer.class) || field.getType().getName().equals("int")){
                    field.set(this, Integer.parseInt(pair.getValue()));
                }
                else if(field.getType().equals(Boolean.class) || field.getType().getName().equals("boolean")){
                    field.set(this, Boolean.parseBoolean(pair.getValue()));
                }
            }
        }
    }

    public void save() throws IllegalAccessException {
        TreeMap<String, String> data = new TreeMap<String, String>();

        for(Field field : this.getClass().getDeclaredFields()){
            if(!Modifier.isStatic(field.getModifiers()) &&
                    !Modifier.isFinal(field.getModifiers())){

                if(field.getType().equals(InetAddress.class)){
                    data.put(field.getName(), ((InetAddress) field.get(this)).getHostAddress());
                }
                else{
                    data.put(field.getName(), field.get(this).toString());
                }
            }
        }

        DbManager.getInstance().saveSettings(data);
    }

    private Field getFieldByName(String fieldName){
        for(Field field : this.getClass().getDeclaredFields()){
            if(field.getName().equals(fieldName)){
                return field;
            }
        }

        return null;
    }

    public static Settings instance(){
        if(inst == null){
            inst = new Settings();
        }

        return inst;
    }

    private Settings(){
        try{
            load();
        }
        catch(Exception e){
            e.printStackTrace();
            try{
                Toast.makeText(StartActivity.instance(), R.string.toastSettingsLoadFailed, Toast.LENGTH_LONG).show();
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
        }
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public InetAddress getMasterServerHost() {
        return masterServerHost;
    }

    public void setMasterServerHost(InetAddress masterServerHost) {
        this.masterServerHost = masterServerHost;
    }

    public int getMasterServerPort() {
        return masterServerPort;
    }

    public void setMasterServerPort(int masterServerPort) {
        this.masterServerPort = masterServerPort;
    }
}
