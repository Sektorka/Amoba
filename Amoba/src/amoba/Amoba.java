package amoba;

import java.util.Date;
import javax.swing.JOptionPane;

public class Amoba {
    public static final boolean DEBUG = true;
    
    public static void main(String[] args) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            if(DEBUG){
                ex.printStackTrace();
            }
        } catch (InstantiationException ex) {
            if(DEBUG){
                ex.printStackTrace();
            }
        } catch (IllegalAccessException ex) {
            if(DEBUG){
                ex.printStackTrace();
            }
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            if(DEBUG){
                ex.printStackTrace();
            }
        }
        
        new Menu().setVisible(true);
    }
    
    public static String now(){
        Date now = new Date();
        
        return  (now.getHours() < 10 ? "0" + now.getHours() : now.getHours()) + ":" + 
                (now.getMinutes() < 10 ? "0" + now.getMinutes() : now.getMinutes()) + ":" + 
                (now.getSeconds() < 10 ? "0" + now.getSeconds() : now.getSeconds());
    }
}
