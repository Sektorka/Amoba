package amoba;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.IllegalFormatException;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Settings {
    private static Settings settings;
    private static final String FILE = "Amoba.xml";
    private static final String ROOT_NODE = "Amoba";
    
    private String masterServerHost = "localhost", clientID = "", nickName = "";
    private int masterServerPort = 9100;

    public static Settings getInstance(){
        if(settings == null){
            settings = new Settings();
        }
        
        return settings;
    }
    
    private Settings(){
        masterServerHost = "localhost";
        masterServerPort = 9100;
        clientID = new PasswordGenerator(true, true, true, false, 30).GeneratePassword();
        nickName = System.getProperty("user.name");
        
        try{
            loadSettings();
        }
        catch(FileNotFoundException e){
            if(Amoba.DEBUG){
                e.printStackTrace();
            }
            try {
                saveSettings();
            } catch(Exception ex){
                if(Amoba.DEBUG){
                    ex.printStackTrace();
                }
                JOptionPane.showMessageDialog(null, ex.getMessage(),"Beállítás vezérlő",JOptionPane.ERROR_MESSAGE);
            }
            JOptionPane.showMessageDialog(null, "A \"" + FILE + "\" konfigurációs fájl nem létezik!\n" + e.getMessage() + "\nKonfigurációs fájl létrehozva az alap beállításokkal!","Beállítás vezérlő",JOptionPane.INFORMATION_MESSAGE);
            
        }
        catch(IOException e){
            JOptionPane.showMessageDialog(null, "Hiba lépett fel a " + FILE + " konfigurációs fájl beolvasása közben!\n" + e.getMessage(),"Beállítás vezérlő",JOptionPane.ERROR_MESSAGE);
        }
        catch(NumberFormatException e){
            JOptionPane.showMessageDialog(null, "A \"" + FILE + "\" konfigurációs fájl hibás értékeket tartalmaz!","Beállítás vezérlő",JOptionPane.ERROR_MESSAGE);
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, "Hiba történt a " + FILE + " konfigurációs fájl feldolgozása közben!","Beállítás vezérlő",JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private Field getFieldByName(String fieldName){
        for(Field field : this.getClass().getDeclaredFields()){
            if(field.getName().equals(fieldName)){
                return field;
            }
        }
        
        return null;
    }
    
    private void loadSettings() throws FileNotFoundException, IOException, 
            ParserConfigurationException, SAXException, IllegalFormatException,
            IllegalArgumentException, NumberFormatException, IllegalAccessException{
        
        Document d = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(FILE);
        
        NodeList rootnl = d.getElementsByTagName(ROOT_NODE);
        if(rootnl.getLength() != 1){
            throw new IllegalArgumentException("Hibás a beállításokat tartalmazó xml fájl!");
        }
        
        Element root = (Element) rootnl.item(0);
        
        NodeList fields = root.getChildNodes();
        
        for(int i = 0; i < fields.getLength(); i++){
            if(fields.item(i) != null){
                Field field = getFieldByName(fields.item(i).getNodeName());
                String nodeValue = "";
                
                if(fields.item(i).getFirstChild() != null){
                    nodeValue = fields.item(i).getFirstChild().getNodeValue();
                }
                
                if(field != null){
                    if(field.getType().getName().equals("java.lang.String")){
                        field.set(this, nodeValue);
                    }
                    else if(field.getType().getName().equals("int")){
                        field.set(this, Integer.parseInt(nodeValue));
                    }
                }
            }
            
        }
        
        if(masterServerPort <= 0 || masterServerPort > 65535){
            masterServerPort = 10000;
        }
        
        if(masterServerHost.equals("")){
            masterServerHost = "localhost";
        }
    }
    
    public void saveSettings() throws ParserConfigurationException, IllegalArgumentException, 
            IllegalAccessException, IOException{
        
        
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Element root = doc.createElement(ROOT_NODE);
        doc.appendChild(root);
        for(Field field : this.getClass().getDeclaredFields()){
            if(Modifier.isStatic(field.getModifiers())) continue;
            
            Element fieldElement = doc.createElement(field.getName());
            fieldElement.setTextContent(field.get(this).toString());
            root.appendChild(fieldElement);
        }
        
        OutputFormat format = new OutputFormat(doc);
        format.setLineWidth(65);
        format.setIndenting(true);
        format.setIndent(4);
        
        Writer out = new StringWriter();
        
        XMLSerializer serializer = new XMLSerializer(out, format);
        serializer.serialize(doc);
        
        FileWriter fwriter = new FileWriter(FILE);
        BufferedWriter bwriter = new BufferedWriter(fwriter);
        
        bwriter.write(out.toString());
        
        bwriter.close();
        fwriter.close();
        out.close();
    }

    public String getMasterServerHost() {
        return masterServerHost;
    }

    public void setMasterServerHost(String masterServerHost) {
        this.masterServerHost = masterServerHost;
    }

    public int getMasterServerPort() {
        return masterServerPort;
    }

    public void setMasterServerPort(int masterServerPort) {
        this.masterServerPort = masterServerPort;
    }

    public String getClientID(){
        return clientID;
    }

    public String getNickName(){
        return nickName;
    }

    public void setNickName(String nickName){
        this.nickName = nickName;
    }
}